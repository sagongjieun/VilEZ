import React from "react";
/** @jsxImportSource @emotion/react */
import { Global, css } from "@emotion/react";

const GlobalStyle = () => {
  return <Global styles={style} />;
};

const style = css`
  * {
    margin: 0;
    padding: 0;
    font-family: "Noto Sans KR", sans-serif;
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

export default GlobalStyle;
