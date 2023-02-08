import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";
// import { getUserShare, getUserAsk } from "../../api/profile";
import ProfileCardView from "./MyBoxCardView";
import { getMyShareAppointments } from "../../api/appointment";

// const id = localStorage.getItem("id");
const MyBoxShare = (props) => {
  const userId = localStorage.getItem("id");
  const [myBoard, setMyBoard] = useState([]);
  const [mySharingBoard, setMySharingBoard] = useState([]);
  const [myToBeSharedBoard, setMyToBeSharedBoard] = useState([]);
  useEffect(() => {
    getMyShareAppointments(userId).then((response) => {
      setMyBoard(response.filter((res) => new Date(res.myAppointListVO.startDay) < new Date()));
      setMySharingBoard(response.filter((res) => new Date(res.myAppointListVO.startDay) < new Date()));
      setMyToBeSharedBoard(response.filter((res) => new Date(res.myAppointListVO.startDay) >= new Date()));
      console.log(response[0].myAppointListVO.startDay);
    });
  }, []);
  useEffect(() => {
    props.setSharePages(1);
    if (props.myShareType === 1) {
      setMyBoard(mySharingBoard);
    } else {
      setMyBoard(myToBeSharedBoard);
    }
  }, [props.myShareType]);
  useEffect(() => {
    props.setShareDefaultPages(parseInt((myBoard?.length - 1) / 3) + 1);
  }, [myBoard]);
  return (
    <div css={cardWrapper(props.sharePages)}>
      {myBoard?.length > 0 ? (
        myBoard.map((share, idx) => (
          <div key={idx}>
            <ProfileCardView
              title={share.myAppointListVO.title}
              endDay={share.myAppointListVO.endDay}
              startDay={share.myAppointListVO.startDay}
              date={share.myAppointListVO.date}
              thumbnail={share.imgPathList[0]?.path}
              boardType={1}
              boardId={share.imgPathList[0].boradId}
              // dDay={share.}
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

export default MyBoxShare;
