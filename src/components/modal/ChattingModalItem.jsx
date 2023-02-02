import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const ChattingModalItem = ({ chat }) => {
  return (
    <div css={ChatListWrap}>
      <img src={chat.profile} alt="profileImage" />
      <div css={chatInfoWrap}>
        <div>
          <span>{chat.nickname}</span>
          <small>{chat.location}</small>
          <small>{chat.time}</small>
        </div>
        <span>{chat.lastChat}</span>
      </div>
      <img src={chat.thumbnail} alt="productImage" />
    </div>
  );
};

const ChatListWrap = css`
  max-height: 70px;
  margin-right: 3px;
  margin-bottom: 10px;
  padding: 10px 5px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  background: #ffffff;
  border: 1px solid #e1e2e3;
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  cursor: pointer;

  & > img {
    width: 40px;
    height: 40px;
    object-fit: cover;
    border-radius: 100%;
  }
`;

const chatInfoWrap = css`
  display: flex;
  flex-direction: column;

  & > div:nth-of-type(1) {
    width: 210px;

    & > span {
      margin-right: 5px;
    }

    & > small {
      margin-right: 5px;
      font-size: 12px;
      color: #8a8a8a;
    }
  }

  & span {
    font-size: 15px;
  }
`;

export default ChattingModalItem;
