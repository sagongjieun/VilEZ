import React, { useEffect, useState } from "react";
import { BsChatSquare } from "react-icons/bs";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ChattingModal from "../components/modal/ChattingModal";
import { modalOpenState } from "../recoil/atom";
import { useRecoilState } from "recoil";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

let client;

function ChatOpenIcon() {
  const loginUserId = localStorage.getItem("id");
  const [modalOpen, setModalOpen] = useRecoilState(modalOpenState);
  const [isNewMessage, setIsNewMessage] = useState(false);

  const onClickOpenChat = () => {
    if (!modalOpen) setIsNewMessage(false); // 새로운메시지를 확인했다면 알림 지우기
    setModalOpen(!modalOpen);
  };

  // map.put("nickName",user.getNickName());
  // map.put("area", user.getArea());
  // map.put("content", chatVO.getContent());
  // map.put("roomId",chatVO.getRoomId());
  // map.put("fromUserId",chatVO.getFromUserId());
  // sendingOperations.convertAndSend("/sendlist/"+chatVO.getToUserId(),map);

  useEffect(() => {
    if (loginUserId) {
      const sockJS = new SockJS(`${process.env.REACT_APP_API_BASE_URL}/chat`); // STOMP 서버가 구현돼있는 url
      client = Stomp.over(sockJS); // 웹소켓 클라이언트 생성

      // 웹소켓과 연결됐을 때 동작하는 콜백함수들
      client.connect({}, () => {
        client.subscribe(`/sendlist/${loginUserId}`, (data) => {
          data = JSON.parse(data.body);
          // 상대방이 메시지 보낼 때만 새로운 메시지 알림
          if (data.fromUserId != loginUserId) {
            setIsNewMessage(true);
          }
        });
      });
    }
  });

  return (
    <>
      <div css={IconBox} onClick={onClickOpenChat}>
        <BsChatSquare size="25" />
        {isNewMessage ? <div css={newMessageAlarm}></div> : <></>}
      </div>

      {modalOpen ? <ChattingModal /> : null}
    </>
  );
}

const IconBox = css`
  bottom: 30px;
  right: 30px;
  width: 60px;
  height: 60px;
  border-radius: 100%;
  color: white;
  border: 1px solid white;
  background-color: #66dd9c;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: fixed;
  box-shadow: 5px 3px 10px rgba(0, 0, 0, 0.35);
  z-index: 100;

  & > svg {
    position: absolute;
    top: calc(50%-12.5px);
  }
`;

const newMessageAlarm = css`
  position: absolute;
  border-radius: 100%;
  border: 1px solid white;
  width: 18px;
  height: 18px;
  top: 0;
  left: 0;
  background-color: #fc0101;
`;

export default ChatOpenIcon;
