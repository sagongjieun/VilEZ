import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getUserShare } from "../../api/profile";
import ProfileCardView from "../profile/ProfileCardView";

// const id = localStorage.getItem("id");
const ProfileMyWriting = (props) => {
  const [myShareBoard, setMyShareBoard] = useState([]);
  const [pages, setPages] = useState(1);
  function onClickMore() {
    setPages((prev) => prev + 1);
    props.setSection("myWriting");
  }
  useEffect(() => {
    if (props.section !== "myWriting") {
      setPages(1);
    }
  }, [props.section]);
  useEffect(() => {
    getUserShare(28).then((response) => {
      setMyShareBoard(response);
    });
  }, []);
  return (
    <div>
      {myShareBoard.map((share) => {
        <div key={share.id}>{share.id}</div>;
      })}
      <div css={cardWrapper(pages)}>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
        <div>
          <ProfileCardView />
        </div>
      </div>
      <button onClick={onClickMore} css={moreWrapper}>
        더보기 {pages} / 4
      </button>
    </div>
  );
};

const cardWrapper = (pages) => {
  const cards = pages * 3;
  return css`
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    & > div {
      display: none;
      width: 32%;
      min-width: 300px;
      padding-top: 30px;
    }
    & > div:nth-of-type(-n + ${cards}) {
      display: block;
    }
  `;
};

const moreWrapper = css`
  cursor: pointer;
  background-color: #fff;
  border: 1px solid #c4c4c4;
  border-radius: 5px;
  line-height: 40px;
  width: 100%;
  margin-top: 30px;
`;

export default ProfileMyWriting;
