import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

function QuitChattingReal() {
  return (
    <div css={ModalWrap}>
      <div>정말 채팅을 종료하시겠어요?</div>
      <div css={buttonWrap}>
        <button css={badbutton}>취소</button>
        <button css={goodbutton}>종료하기</button>
      </div>
    </div>
  );
}
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
const buttonWrap = css`
  margin-top: 50px;
`;

const goodbutton = css`
  width: 105px;
  background-color: #66dd9c;
  color: white;
  border: none;
  height: 45px;
  font-size: 14px;
  border-radius: 5px;
`;
const badbutton = css`
  width: 105px;
  background-color: #aeaeae;
  color: white;
  border: none;
  height: 45px;
  font-size: 14px;
  border-radius: 5px;
  margin-right: 30px;
`;
export default QuitChattingReal;
