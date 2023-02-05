import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import MiddleWideButton from "../button/MiddleWideButton";
import { useLocation } from "react-router-dom";

function AppointmentCompleteModal({ close }) {
  const pathname = useLocation().pathname;

  function onClickMovedetail() {
    close(false);
    window.location.replace(pathname);
  }

  return (
    <div css={topWrap}>
      <div css={completeWrap}>
        <span>약속이 성사됐어요 😀</span>
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
  height: 180px;
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

  & > span {
    font-size: 20px;
  }

  & > div {
    width: 105px;
    margin-top: 30px;
  }
`;

export default AppointmentCompleteModal;
