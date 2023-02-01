import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useNavigate } from "react-router-dom";

function QuitChattingReal({ close }) {
  const navigate = useNavigate();
  // const params = useParams();
  // const boardId = params.boardId;
  function onClickCancel() {
    close(false);
  }
  function onClickNavigate() {
    // navigate(`/product/detail/${boardId}`, {});
    navigate(`/product/detail/55`, {});
  }
  return (
    <div css={topWrap}>
      <div css={ModalWrap}>
        <div>정말 채팅을 종료하시겠어요?</div>
        <div css={buttonWrap}>
          <button css={badbutton} onClick={onClickCancel}>
            취소
          </button>
          <button css={goodbutton} onClick={onClickNavigate}>
            종료하기
          </button>
        </div>
      </div>
    </div>
  );
}

const topWrap = css`
  position: absolute;
  top: 0px;
  left: 0px;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
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
  background-color: white;
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
