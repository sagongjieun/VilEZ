import React from "react";
import { css } from "@emotion/react";
import MiddleWideButton from "./../button/MiddleWideButton";

const RealDeleteModal = () => {
  return (
    <div css={DeleteWrap}>
      <div>정말 삭제하시겠어요?</div>
      <div css={buttonWrap}>
        <MiddleWideButton text="아니오" cancel={true} />
        <MiddleWideButton text="네" />
      </div>
    </div>
  );
};

const DeleteWrap = css`
  position: fixed;
  width: 500px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.25);
  z-index: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const buttonWrap = css`
  display: flex;
  font-size: 300px;
  justify-content: space-between;

  & > button {
    width: 150px;
  }
`;
export default RealDeleteModal;
