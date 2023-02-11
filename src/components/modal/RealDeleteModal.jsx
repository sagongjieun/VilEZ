import React from "react";
import { css } from "@emotion/react";
import MiddleWideButton from "./../button/MiddleWideButton";

const RealDeleteModal = () => {
  return (
    <div css={DeleteWrap}>
      <div>정말 삭제하시겠어요?</div>
      <MiddleWideButton text="아니오" cancel={true} />
    </div>
  );
};
const DeleteWrap = css`
  position: fixed;
  left: 0px;
  top: 0px;
  bottom: 0;
  right: 0;
  color: black;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;

  left: 150px;
`;
export default RealDeleteModal;
