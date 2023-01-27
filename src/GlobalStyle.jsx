import React from "react";
/** @jsxImportSource @emotion/react */
import { Global, css } from "@emotion/react";

const GlobalStyle = () => {
  return <Global styles={[style, ModalWrap]} />;
};

const style = css`
  html {
    scroll-behavior: smooth;
  }

  * {
    margin: 0;
    padding: 0;
    /* font-family: "Noto Sans KR", sans-serif; */
    font-family: "Spoqa Han Sans Neo", "sans-serif";
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
