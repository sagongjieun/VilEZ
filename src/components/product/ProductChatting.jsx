import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProductInfo from "./ProductInfo";
import MiddleWideButton from "../button/MiddleWideButton";
import MapAndChatting from "../Chatting";
import { useLocation } from "react-router-dom";

const ProductChatting = () => {
  const { state } = useLocation(); // detail로부터 받은 정보들
  // state: {
  //   writerNickname: writerNickname,
  //   thumbnailImage: imageList[0],
  //   boardId: boardId,
  //   title: title,
  //   location: location,
  //   startDay: startDay,
  //   endDay: endDay,
  //   bookmarkCnt: bookmarkCnt,
  // },

  return (
    <div css={wrapper}>
      <div css={articleInfoWrapper}>
        <h2>{state.writerNickname} 님과의 대화</h2>
        <ProductInfo infos={state} />
      </div>
      <div css={mapAndChatWrapper}>
        <MapAndChatting writerNickname={state.writerNickname} />
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
