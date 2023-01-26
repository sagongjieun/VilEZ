import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { postChatRoom } from "../api/chat"; //eslint-disable-line no-unused-vars
import baseProfile from "../assets/images/baseProfile.png";

let client;

const Chatting = () => {
  const [chatRoomId, setChatRoomId] = useState(10); //eslint-disable-line no-unused-vars
  const [chatMessage, setChatMessage] = useState(""); // 클라이언트가 입력하는 메시지
  const [showingMessage, setShowingMessage] = useState([]); // 서버로부터 받는 메시지
  // 임시 데이터
  const [myUserId, setMyUserId] = useState(28); //eslint-disable-line no-unused-vars

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

    const sendMessage = {
      roomId: chatRoomId,
      boardId: 55,
      type: 2,
      fromUserId: 28,
      toUserId: 29,
      content: chatMessage,
      time: new Date().getTime(),
    };

    client.send("/recvchat", {}, JSON.stringify(sendMessage));

    setChatMessage("");
  }

  // test
  useEffect(() => {
    console.log("보여주는 메시지 : ", showingMessage);
  }, [showingMessage]);

  useEffect(() => {
    const sockJS = new SockJS(`${process.env.REACT_APP_API_BASE_URL}/chat`);
    client = Stomp.over(sockJS);

    client.connect({}, () => {
      client.debug("connected To Stomp");

      // subscribe
      // url, callback, header(option)
      // 내아이디는 임시 데이터
      client.subscribe(`/sendchat/${chatRoomId}/${myUserId}`, (data) => {
        setShowingMessage((prev) => [...prev, JSON.parse(data.body)]);
      });

      client.subscribe(`/sendmy/${chatRoomId}/${myUserId}`, (data) => {
        setShowingMessage((prev) => [...prev, JSON.parse(data.body)]);
      });

      client.activate();
    });
  }, []);

  // useEffect(() => {
  //   /** 방이 계속 만들어지니까 일단 주석처리하고 roomId 10번으로 쓰기 */
  //   // 공유자와 피공유자 사이에 연결되는 채팅방 id 받기
  //   // body값 임시 데이터
  //   postChatRoom({
  //     type: 2, // 요청글 1 공유글 2
  //     boardId: 55,
  //     shareUserId: 28,
  //     notShareUserId: 29,
  //   }).then((res) => {
  //     setChatRoomId(res[0].id);
  //   });
  // }, []);

  return (
    <div css={chatWrapper}>
      <div>
        {showingMessage.map((message, index) => {
          if (message.fromUserId === myUserId) {
            return (
              <div key={index} css={myMessageWrapper}>
                <span>{message.content}</span>
              </div>
            );
          } else {
            return (
              <div key={index} css={yourMessageWrapper}>
                <img src={baseProfile} />
                <div>
                  <small>{message.fromUserId}</small>
                  <span>{message.content}</span>
                </div>
              </div>
            );
          }
        })}
      </div>
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
    margin-bottom: 20px;
    overflow-y: scroll;

    &::-webkit-scrollbar {
      width: 8px;
    }

    &::-webkit-scrollbar-thumb {
      height: 30%;
      background: #c4c4c4;
      border-radius: 10px;
    }

    &::-webkit-scrollbar-track {
      background: none;
    }
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
      color: #66dd9c;
      cursor: pointer;
    }
  }
`;

const myMessageWrapper = css`
  text-align: right;
  margin-bottom: 10px;
  margin-right: 10px;

  & > span {
    font-size: 16px;
  }
`;

const yourMessageWrapper = css`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 10px;

  & > img {
    width: 50px;
    height: 50px;
    margin-right: 10px;
  }

  & > div {
    display: flex;
    flex-direction: column;

    & > span {
      font-size: 16px;
    }
  }
`;

export default Chatting;
