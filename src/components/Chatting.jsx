import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { postChatRoom } from "../api/chat"; //eslint-disable-line no-unused-vars

let client;

const Chatting = () => {
  const [chatRoomId, setChatRoomId] = useState(10); //eslint-disable-line no-unused-vars
  const [chatMessage, setChatMessage] = useState(""); // 사용자가 입력하는 메시지
  const [sendMessage, setSendMessage] = useState({
    boardId: 55,
    type: 2,
    fromUserId: 28,
    toUserId: 29,
    content: "",
  }); // 임시 데이터

  function onChangeChatMessage(message) {
    setChatMessage(message);
  }

  function onKeyDownSendMessage(e) {
    if (e.keyCode === 13) {
      onClickSendMessage();
    }
  }

  function onClickSendMessage() {
    if (chatMessage === "") return;

    setSendMessage({ ...sendMessage, content: chatMessage });

    client.send("/recvchat", JSON.stringify(sendMessage));

    setChatMessage("");
  }

  useEffect(() => {
    /** 방이 계속 만들어지니까 일단 주석처리하고 roomId 10번으로 쓰기 */
    // 공유자와 피공유자 사이에 연결되는 채팅방 id 받기
    // body값 임시 데이터
    // postChatRoom({
    //   type: 2, // 요청글 1 공유글 2
    //   boardId: 55,
    //   shareUserId: 28,
    //   notShareUserId: 29,
    // }).then((res) => {
    //   setChatRoomId(res[0].id);
    // });
  }, []);

  useEffect(() => {
    const sockJS = new SockJS(`${process.env.REACT_APP_API_BASE_URL}/chat`);
    client = Stomp.over(sockJS);

    client.connect({}, () => {
      client.debug("connected To Stomp");

      // subscribe
      // url, callback, header(option)
      // 내아이디는 임시 데이터
      client.subscribe(`/sendchat/${chatRoomId}/${28}`, (data) => {
        console.log("subscribe data : ", data);
      });

      // send
      // url, header(option), body(option)
      client.send("/recvchat", {}, JSON.stringify(sendMessage));
    });
  }, [chatRoomId]);

  return (
    <div css={chatWrapper}>
      <div></div>
      <div>
        <input
          placeholder="메시지를 입력하세요."
          onChange={(e) => onChangeChatMessage(e.target.value)}
          onKeyDown={(e) => onKeyDownSendMessage(e)}
          value={chatMessage}
        />
        <small onClick={onClickSendMessage}>전송</small>
      </div>
    </div>
  );
};

const chatWrapper = css`
  max-width: 100%;
  max-height: 550px;
  border: 1px solid #e1e2e3;
  border-radius: 10px;
  padding: 20px;

  & > div:nth-of-type(1) {
    width: 100%;
    height: 462px;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    margin-bottom: 20px;
  }

  & > div:nth-of-type(2) {
    max-width: 100%;
    height: 40px;
    padding: 0 20px;
    background: #ffffff;
    border: 1px solid #e1e2e3;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 10px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;

    & > input {
      outline: none;
      border: none;
      width: 85%;
    }

    & > small {
      cursor: poitner;
      color: #66dd9c;
    }
  }
`;

export default Chatting;
