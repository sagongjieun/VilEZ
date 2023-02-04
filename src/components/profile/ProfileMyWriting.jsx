import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getUserShare, getUserAsk } from "../../api/profile";
import ProfileCardView from "./ProfileCardView";

// const id = localStorage.getItem("id");
const ProfileMyWriting = (props) => {
  const userId = localStorage.getItem("id");
  const [myBoard, setMyBoard] = useState([]);
  const [myShareBoard, setMyShareBoard] = useState([]);
  const [myAskBoard, setMyAskBoard] = useState([]);
  useEffect(() => {
    getUserShare(userId).then((response) => {
      setMyBoard(response);
      setMyShareBoard(response);
    });
    getUserAsk(userId).then((response) => {
      setMyAskBoard(response);
    });
  }, []);
  useEffect(() => {
    if (props.myWritingType === 1) {
      setMyBoard(myShareBoard);
    } else {
      setMyBoard(myAskBoard);
      console.log(myAskBoard);
    }
  }, [props.myWritingType]);
  useEffect(() => {
    props.setWritingDefaultPages(parseInt(myBoard.length / 3) + 1);
  }, [myBoard]);
  return (
    <div css={cardWrapper(props.writingPages)}>
      {myBoard?.map((share) => (
        <div key={share.id}>
          <ProfileCardView
            title={share.title}
            endDay={share.endDay}
            startDay={share.startDay}
            date={share.date}
            thumbnail={share.list[0].path}
          />
        </div>
      ))}
    </div>
  );
};

const cardWrapper = (pages) => {
  const cards = pages * 3;
  return css`
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    row-gap: 20px;
    column-gap: 20px;
    height: calc(6px + ${pages}* 274px);
    transition: all 0.5s;
    & > div {
      display: none;
      min-width: 300px;
    }
    & > div:nth-of-type(-n + ${cards}) {
      display: block;
      overflow: hidden;
    }
  `;
};

export default ProfileMyWriting;
