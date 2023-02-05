import React, { useState, useEffect, useRef } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import baseProfile from "../assets/images/baseProfile.png";
import Map from "./common/Map";
import recommendLocationButton from "../assets/images/recommendLocationButton.png";
import selectDateButton from "../assets/images/selectDateButton.png";
import openOathButton from "../assets/images/openOathButton.png";
import { getLatestMapLocation, getChatHistory } from "../api/chat";
import CalendarModal from "./modal/CalendarModal";
import { getOath } from "../api/oath";
import OathModal from "./modal/OathModal";

let client;

const StompRealTime = ({
  roomId,
  boardId,
  boardType,
  otherUserId,
  otherUserNickname,
  shareUserId,
  // notShareUserId,
  shareState,
}) => {
  const scrollRef = useRef();
  const myUserId = localStorage.getItem("id");
  const chatRoomId = roomId;

  const [chatMessage, setChatMessage] = useState(""); // 클라이언트가 입력하는 메시지
  const [showingMessage, setShowingMessage] = useState([]); // 서버로부터 받는 메시지
  const [hopeLocation, setHopeLocation] = useState("");
  const [movedLat, setMovedLat] = useState("");
  const [movedLng, setMovedLng] = useState("");
  const [movedZoomLevel, setMovedZoomLevel] = useState(0);
  const [movedMarker, setMovedMarker] = useState(false);
  const [calendarModalOpen, setCalendarModalOpen] = useState(false);
  const [isOathModalOpen, setIsOathModalOpen] = useState(false);
  const [oathSign, setOathSign] = useState("");

  function onKeyDownSendMessage(e) {
    if (e.keyCode === 13) {
      onClickSendMessage();
    }
  }

  function onChangeChatMessage(message) {
    setChatMessage(message);
  }

  function scrollToBottom() {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }

  // 채팅 메시지 서버에 전송
  function onClickSendMessage() {
    if (chatMessage === "") return;

    const sendMessage = {
      roomId: chatRoomId,
      boardId: boardId,
      type: boardType,
      fromUserId: myUserId,
      toUserId: otherUserId,
      content: chatMessage,
      time: new Date().getTime(),
    };

    setShowingMessage((prev) => [...prev, sendMessage]);

    client.send("/recvchat", {}, JSON.stringify(sendMessage));

    setChatMessage("");
  }

  // Map에서 받은 데이터로 서버에 전송
  function receiveLocation(location, lat, lng, zoomLevel, isMarker) {
    setHopeLocation(location);

    /** 마커찍을때만 내가 받은 걸 다시 send하게 돼서 문제 생김 */

    if (lat && lng && zoomLevel) {
      const sendMapData = {
        roomId: chatRoomId,
        toUserId: otherUserId,
        lat: lat,
        lng: lng,
        zoomLevel: zoomLevel,
        isMarker: isMarker,
      };

      client.send("/recvmap", {}, JSON.stringify(sendMapData));
    }
  }

  function onClickOpenCalendarModal() {
    // 공유자만 클릭 가능
    /** 임시 alert, 피공유자에게는 disable된 달력 띄우기 */
    if (myUserId == shareUserId) setCalendarModalOpen(true);
    else alert("공유자만 공유 기간 확정을 할 수 있어요 😀");
  }

  function onClickOpenOath() {
    getOath(roomId).then((res) => {
      if (res) {
        // 모달로 oath 열어서 보여주기
        setOathSign(res.sign);
        setIsOathModalOpen(!isOathModalOpen);
      } else {
        alert("작성된 서약서가 없습니다.");
      }
    });
  }

  function onClickRecommendLocation() {
    alert("추천 장소 가져오기");
  }

  useEffect(() => {
    if (chatRoomId) {
      /** 채팅방의 마지막 공유지도 장소 받기 */
      getLatestMapLocation(chatRoomId).then((res) => {
        // 마지막 장소가 있다면
        if (res) {
          res = res[0];

          setMovedLat(res.lat);
          setMovedLng(res.lng);
          setMovedZoomLevel(res.zoomLevel);
          setMovedMarker(res.isMarker);
        }
        // 마지막 장소가 없다면
        else {
          // 서울시청 좌표
          setMovedLat(37.56682870560737);
          setMovedLng(126.9786409384806);
          setMovedZoomLevel(3);
        }
      });

      /** 소켓에 연결되면 채팅 내역 보여주기 */
      getChatHistory(chatRoomId).then((res) => {
        setShowingMessage(res);
      });

      // const sockJS = new SockJS(`${process.env.REACT_APP_API_BASE_URL}/chat`); // STOMP 서버가 구현돼있는 url
      client = Stomp.over(function () {
        return new SockJS(`${process.env.REACT_APP_API_BASE_URL}/chat`);
      }); // 웹소켓 클라이언트 생성

      // 웹소켓과 연결됐을 때 동작하는 콜백함수들
      client.connect({}, () => {
        // 다른 유저의 채팅을 구독
        client.subscribe(`/sendchat/${chatRoomId}/${myUserId}`, (data) => {
          setShowingMessage((prev) => [...prev, JSON.parse(data.body)]);
        });

        // 공유지도를 구독
        client.subscribe(`/sendmap/${chatRoomId}/${myUserId}`, (data) => {
          data = JSON.parse(data.body);

          // 다른 유저가 움직인 지도의 데이터들
          setMovedLat(data.lat);
          setMovedLng(data.lng);
          setMovedZoomLevel(data.zoomLevel);
          data.isMarker ? setMovedMarker(true) : setMovedMarker(false);
        });
      });
    }
  }, [chatRoomId]);

  useEffect(() => {
    scrollToBottom();
  }, [showingMessage]);

  useEffect(() => {
    /* state : 0 예약 후, -1 반납 후, -2 예약 후(예약 취소 : 확장), -3 예약 전 */
    // test로 0, 원래는 -1
    if (shareState === -1) {
      // 소켓 끊기
      // client.disconnect();

      // 채팅방 막기
      const messageInput = document.getElementById("messageInput");
      messageInput.disabled = true;
      messageInput.placeholder = "채팅이 불가능합니다.";

      const messageSendButton = document.getElementById("messageSendButton");
      messageSendButton.hidden = true;

      // 공유지도 막기
    }
  }, [shareState]);

  return (
    <>
      <div css={mapWrapper}>
        <span>{hopeLocation}</span>
        <div>
          <Map
            readOnly={false}
            sendLocation={receiveLocation}
            movedLat={movedLat}
            movedLng={movedLng}
            movedZoomLevel={movedZoomLevel}
            movedMarker={movedMarker}
            shareState={shareState}
          />
        </div>
      </div>
      <div>
        <div css={menusWrapper}>
          <img src={selectDateButton} onClick={onClickOpenCalendarModal} />
          {calendarModalOpen && <CalendarModal setCalendarModalOpen={setCalendarModalOpen} boardId={boardId} />}
          <img src={openOathButton} onClick={onClickOpenOath} />
          <img src={recommendLocationButton} onClick={onClickRecommendLocation} />
        </div>
        <div css={chatWrapper}>
          <div ref={scrollRef}>
            {showingMessage.map((message, index) => {
              if (message.fromUserId == myUserId) {
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
                      <small>{otherUserNickname}</small>
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
              id="messageInput"
            />
            <small onClick={onClickSendMessage} id="messageSendButton">
              전송
            </small>
          </div>
        </div>
      </div>
      {isOathModalOpen ? (
        <OathModal close={setIsOathModalOpen} roomId={roomId} readOnly={true} oathSign={oathSign} />
      ) : null}
    </>
  );
};

const mapWrapper = css`
  display: flex;
  flex-direction: column;
  width: 65%;

  & > div {
    margin-top: 10px;
    width: 100%;
    height: 600px;
  }
`;

const menusWrapper = css`
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;
  margin-bottom: 10px;

  & > img {
    cursor: pointer;
    width: 60px;
    height: 60px;
    position: relative;
  }
`;

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
    overflow-x: hidden;
  }

  & > div:nth-of-type(2) {
    max-width: 100%;
    height: 40px;
    padding: 0 20px;
    background-color: #ffffff;
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

      &:disabled {
        background-color: #ffffff;
      }
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
  justify-content: flex-start;

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

export default StompRealTime;
