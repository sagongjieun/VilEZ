import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
function ShareComplete() {
  return (
    <div css={completeWrap}>
      <h2>공유를 성공했어요 😀</h2>
      <button css={completeButton}>확인</button>
    </div>
  );
}
const completeWrap = css`
  width: 600px;
  height: 400px;
  text-align: center;
  line-height: 200px;
`;

const completeButton = css`
  width: 105px;
  background-color: #66dd9c;
  color: white;
  border: none;
  height: 45px;
  font-size: 14px;
  border-radius: 5px;
`;

export default ShareComplete;
