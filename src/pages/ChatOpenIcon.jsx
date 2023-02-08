import React, { useEffect, useState } from "react";
import { BsChatSquare } from "react-icons/bs";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ChattingModal from "../components/modal/ChattingModal";
import { modalOpenState, enterChatRoomState } from "../recoil/atom";
import { useRecoilState } from "recoil";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

let client;

function ChatOpenIcon() {
  const loginUserId = localStorage.getItem("id");
  const [modalOpen, setModalOpen] = useRecoilState(modalOpenState);
  const [isNewMessage, setIsNewMessage] = useState(false);
  const [enterChatRoom, setEnterChatRoom] = useRecoilState(enterChatRoomState);
  const [isSocketConnected, setIsSocketConnected] = useState(false);

  const onClickOpenChat = () => {
    if (!modalOpen) setIsNewMessage(false); // 새로운메시지를 확인했다면 알림 지우기
    setModalOpen(!modalOpen);
  };

  useEffect(() => {
    if (loginUserId) {
      client = Stomp.over(function () {
        return new SockJS(`${process.env.REACT_APP_API_BASE_URL}/chat`); // STOMP 서버가 구현돼있는 url
      }); // 웹소켓 클라이언트 생성

      // 웹소켓과 연결됐을 때 동작하는 콜백함수들
      client.connect({}, () => {
        client.subscribe(`/sendlist/${loginUserId}`, (data) => {
          data = JSON.parse(data.body);
          // 상대방이 메시지 보낼 때만 새로운 메시지 알림
          if (data.fromUserId != loginUserId) {
            setIsNewMessage(true);
          }
        });

        setIsSocketConnected(true);
      });
    }
  }, []);

  useEffect(() => {
    if (enterChatRoom && loginUserId && isSocketConnected) {
      // 해당 방으로 들어갔다는 소켓 send
      const data = {
        roomId: enterChatRoom,
        userId: loginUserId,
      };

      console.log("여기 문제??????????????");
      client.send("/room_enter", {}, JSON.stringify(data));
      setEnterChatRoom(null);
    }
  }, [enterChatRoom, loginUserId, isSocketConnected]);

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
