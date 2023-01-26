import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProductInfo from "./ProductInfo";
import MiddleWideButton from "../button/MiddleWideButton";
import Map from "../common/Map";
import recommendLocationButton from "../../assets/images/recommendLocationButton.png";
import selectDateButton from "../../assets/images/selectDateButton.png";
import startWebRTCButton from "../../assets/images/startWebRTCButton.png";
import Chatting from "../Chatting";
import { useLocation } from "react-router-dom";

const ProductChatting = () => {
  const [location, setLocation] = useState("");
  const [hopeAreaLat, setHopeAreaLat] = useState(""); //eslint-disable-line no-unused-vars
  const [hopeAreaLng, setHopeAreaLng] = useState(""); //eslint-disable-line no-unused-vars

  const { state } = useLocation(); // detail로부터 받은 정보들

  function receiveLocation(location, lat, lng) {
    setLocation(location);
    setHopeAreaLat(lat);
    setHopeAreaLng(lng);
  }

  return (
    <div css={wrapper}>
      <div css={articleInfoWrapper}>
        <h2>{state.writerNickname} 님과의 대화</h2>
        <ProductInfo infos={state} />
      </div>
      <div css={mapAndChatWrapper}>
        <div css={mapWrapper}>
          <span>{location}</span>
          <div>
            <Map readOnly={false} sendLocation={receiveLocation} />
          </div>
        </div>
        <div>
          <div css={menusWrapper}>
            <img src={selectDateButton} />
            <img src={startWebRTCButton} />
            <img src={recommendLocationButton} />
          </div>
          <Chatting />
        </div>
      </div>
      <div css={buttonWrapper}>
        <MiddleWideButton text={"채팅 나가기"} />
        <MiddleWideButton text={"만남 확정하기"} />
      </div>
    </div>
  );
};

const wrapper = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
`;

const articleInfoWrapper = css`
  display: flex;
  flex-direction: column;
  margin-bottom: 60px;

  & > h2 {
    margin-bottom: 30px;
  }
`;

const mapAndChatWrapper = css`
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;

  & > div:nth-of-type(2) {
    display: flex;
    flex-direction: column;
    width: 30%;
  }
`;

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
  }
`;

const buttonWrapper = css`
  display: flex;
  flex-direction: row;
  margin-left: auto;
  margin-top: 80px;

  & > button {
    width: 250px;
  }

  & > button:nth-of-type(1) {
    margin-right: 40px;
    background-color: #c82333;
  }
`;

export default ProductChatting;
