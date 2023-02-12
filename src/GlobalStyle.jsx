import React from "react";
/** @jsxImportSource @emotion/react */
import { Global, css } from "@emotion/react";

const GlobalStyle = () => {
  return <Global styles={[style, ModalWrap]} />;
};

const style = css`
  @font-face {
    font-family: "GmarketSansMedium";
    src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff") format("woff");
    font-weight: normal;
    font-style: normal;
  }
  @font-face {
    font-family: "LINESeedKR-Bd";
    src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_11-01@1.0/LINESeedKR-Bd.woff2") format("woff2");
    font-weight: 700;
    font-style: normal;
  }
  html {
    scroll-behavior: smooth;
  }

  * {
    margin: 0;
    padding: 0;
    /* font-family: "Noto Sans KR", sans-serif; */
    /* font-family: "Spoqa Han Sans Neo", "sans-serif"; */
    font-family: "LINESeedKR-Bd";

    text-decoration: none;
  }

  body {
    box-sizing: border-box;
  }

  h2 {
    font-size: 30px;
    font-weight: bold;
  }

  h3 {
    font-size: 26px;
    font-weight: bold;
  }

  span {
    font-size: 18px;
    font-weight: normal;
  }

  small {
    font-size: 14px;
    font-weight: normal;
  }

  ::-webkit-scrollbar {
    width: 8px;
    height: 10px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.3);
    border-radius: 10px;
  }

  ::-webkit-scrollbar-track {
    background-color: initial;
    border-radius: 10px;
  }
`;

const ModalWrap = css`
  font-size: 20px;
  margin: auto;
  margin-bottom: 100px;
  width: 600px;
  height: 450px;
  box-shadow: 1px 1px 5px;
  border-radius: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  > div {
    padding: 10px;
  }
`;

export default GlobalStyle;
