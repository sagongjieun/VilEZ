import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { shareDataState } from "../../recoil/atom";
import MiddleWideButton from "../button/MiddleWideButton";

function ShareCompleteModal() {
  const navigate = useNavigate();
  const shareData = useRecoilValue(shareDataState);

  function onClickMovedetail() {
    shareData.boardType == 1
      ? navigate(`/product/detail/ask/${shareData.boardId}`)
      : navigate(`/product/detail/share/${shareData.boardId}`);
  }

  return (
    <div css={topWrap}>
      <div css={completeWrap}>
        <h3>공유를 성공했어요 😀</h3>
        <div>
          <MiddleWideButton text={"확인"} onclick={onClickMovedetail} />
        </div>
      </div>
    </div>
  );
}

const topWrap = css`
  position: fixed;
  left: 0px;
  top: 0px;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
`;

const completeWrap = css`
  background-color: white;
  width: 300px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px, rgba(0, 0, 0, 0.3) 0px 1px 3px -1px;
  border-radius: 10px;
  padding: 20px;

  & > div {
    width: 105px;
    margin-top: 30px;
  }
`;

export default ShareCompleteModal;
