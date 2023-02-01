import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useNavigate } from "react-router-dom";
function ShareComplete() {
  const navigate = useNavigate();
  function onClickMovedetail() {
    navigate(`/product/detail/55`);
  }
  return (
    <div css={topWrap}>
      <div css={completeWrap}>
        <h2>공유를 성공했어요 😀</h2>
        <button css={completeButton} onClick={onClickMovedetail}>
          확인
        </button>
      </div>
    </div>
  );
}
const topWrap = css`
  position: fixed;
  width: 100%;
  height: 100%;
  left: 0px;
  top: 0px;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const completeWrap = css`
  background-color: white;
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
