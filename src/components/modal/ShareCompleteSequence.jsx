import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const ShareCompleteSequence = () => {
  return (
    <div css={topWrap}>
      <div></div>
    </div>
  );
};

const topWrap = css`
  position: fixed;
  left: 0px;
  top: 0px;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
`;
export default ShareCompleteSequence;
