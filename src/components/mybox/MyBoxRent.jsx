import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";
import ProfileCardView from "./MyBoxCardView";
import { getMyRentAppointments } from "../../api/appointment";

// const id = localStorage.getItem("id");
const MyBoxRent = (props) => {
  const userId = localStorage.getItem("id");
  const [myBoard, setMyBoard] = useState([]);
  const [myBeingRentedBoard, setMyBeingRentedBoard] = useState([]);
  const [myToBeRentedBoard, setMyToBeRentedBoard] = useState([]);
  useEffect(() => {
    getMyRentAppointments(userId).then((response) => {
      setMyBoard(response.filter((res) => new Date(res.myAppointListVO.startDay) < new Date()));
      setMyBeingRentedBoard(response.filter((res) => new Date(res.myAppointListVO.startDay) < new Date()));
      setMyToBeRentedBoard(response.filter((res) => new Date(res.myAppointListVO.startDay) >= new Date()));
    });
  }, []);
  useEffect(() => {
    props.setRentPages(1);
    if (props.myRentType === 1) {
      setMyBoard(myBeingRentedBoard);
    } else {
      setMyBoard(myToBeRentedBoard);
    }
  }, [props.myRentType]);
  useEffect(() => {
    props.setRentDefaultPages(parseInt((myBoard?.length - 1) / 3) + 1);
  }, [myBoard]);
  return (
    <div css={cardWrapper(props.rentPages)}>
      {myBoard?.length > 0 ? (
        myBoard.map((rent, idx) => (
          <div key={idx}>
            <ProfileCardView
              title={rent.myAppointListVO.title}
              endDay={rent.myAppointListVO.endDay}
              startDay={rent.myAppointListVO.startDay}
              date={rent.myAppointListVO.date}
              thumbnail={rent.imgPathList[0]?.path}
              boardType={2}
              boardId={rent.imgPathList[0].boradId}
            />
          </div>
        ))
      ) : (
        <div css={noCards}>작성한 글이 없습니다.</div>
      )}
    </div>
  );
};

const appear = keyframes`
  0% {
    display: none;
    opacity: 0;
  }
  100% {
    display: block;
    opacity: 1;
  }
`;
const cardWrapper = (pages) => {
  const cards = pages * 3;
  return css`
    position: relative;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    row-gap: 20px;
    column-gap: 20px;
    height: calc(6px + ${pages}* 274px);
    transition: all 0.5s;
    & > div {
      display: none;
      opacity: 1;
      min-width: 300px;
      transition: all 0.3s;
    }
    & > div:nth-of-type(-n + ${cards}) {
      display: block;
      animation-name: ${appear};
      animation-duration: 0.3s;
      transition: all 0.3s;
    }
  `;
};

const noCards = css`
  position: absolute;
  line-height: 200px;
  height: 200px;
  width: 100%;
  text-align: center;
`;

export default MyBoxRent;
