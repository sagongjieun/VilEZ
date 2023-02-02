import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import elapsedTime from "./../product/ProductElapsedTime";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { modalOpenState } from "../../recoil/atom";

const ChattingModalItem = ({ chat }) => {
  const navigate = useNavigate();
  const [modalOpen, setModalOpen] = useRecoilState(modalOpenState);

  function onClickMoveChatRoom(roomId) {
    setModalOpen(!modalOpen);
    navigate(`/product/chat/${roomId}`);
  }

  return (
    <div css={ChatListWrap} onClick={() => onClickMoveChatRoom(chat.chatData.roomId)}>
      <img src={chat.profile} alt="profileImage" />
      {chat.noReadCount ? <div></div> : <div></div>}
      <div css={chatInfoWrap}>
        <div>
          <span>{chat.nickName}</span>
          {chat.area ? <small>{chat.area}</small> : <small>지역 미인증</small>}
          <small>{elapsedTime(chat.chatData.time)}</small>
        </div>
        <span>{chat.chatData.content}</span>
      </div>
    </div>
  );
};

const ChatListWrap = css`
  max-height: 70px;
  margin-right: 3px;
  margin-bottom: 10px;
  padding: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
  background: #ffffff;
  border: 1px solid #e1e2e3;
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  cursor: pointer;
  position: relative;

  & > img {
    width: 40px;
    height: 40px;
    object-fit: cover;
    border-radius: 100%;
    margin-right: 10px;
  }

  & > div:nth-of-type(1) {
    position: absolute;
    border-radius: 100%;
    border: 1px solid white;
    width: 10px;
    height: 10px;
    top: 12px;
    left: 40px;
    background-color: #fc0101;
  }
`;

const chatInfoWrap = css`
  display: flex;
  flex-direction: column;

  & > div:nth-of-type(1) {
    width: 210px;

    & > span {
      font-size: 14px;
      margin-right: 5px;
    }

    & > small {
      margin-right: 5px;
      font-size: 12px;
      color: #8a8a8a;
    }
  }

  & > span {
    font-size: 13px;
  }
`;

export default ChattingModalItem;
