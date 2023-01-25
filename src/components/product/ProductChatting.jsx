import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProductInfo from "./ProductInfo";
import MiddleWideButton from "../button/MiddleWideButton";
import Map from "../common/Map";
import recommendLocationButton from "../../assets/images/recommendLocationButton.png";
import selectDateButton from "../../assets/images/selectDateButton.png";
import startWebRTCButton from "../../assets/images/startWebRTCButton.png";

const ProductChatting = () => {
  const [location, setLocation] = useState("");
  const [hopeAreaLat, setHopeAreaLat] = useState(""); //eslint-disable-line no-unused-vars
  const [hopeAreaLng, setHopeAreaLng] = useState(""); //eslint-disable-line no-unused-vars

  function receiveLocation(location, lat, lng) {
    setLocation(location);
    setHopeAreaLat(lat);
    setHopeAreaLng(lng);
  }

  return (
    <div css={wrapper}>
      <div css={articleInfoWrapper}>
        <h2>닉네임 님과의 대화</h2>
        <ProductInfo />
      </div>
      <div css={mapAndChatWrapper}>
        <div css={mapWrapper}>
          <div>
            <span>{location}</span>
            <small>지도 제어권 넘기기</small>
          </div>
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
          <div css={chatWrapper}>
            <div></div>
            <div>
              <input placeholder="메시지를 입력하세요." />
              <small>전송</small>
            </div>
          </div>
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

  & > div:nth-of-type(1) {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 10px;

    & > small {
      color: #66dd9c;
      font-weight: bold;
      cursor: pointer;
    }
  }

  & > div:nth-of-type(2) {
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
