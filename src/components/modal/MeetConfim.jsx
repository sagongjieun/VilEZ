import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

function MeetConfirm() {
  const user = { nickname: "회먹고싶다요" };
  const startdate = "2023.01.19";
  const enddate = "2023.01.23";
  return (
    <div>
      <div css={ModalWrap}>
        <strong>{user.nickname}님과</strong>
        <div>
          <strong>
            {startdate} ~ {enddate}
          </strong>
        </div>
        <div>기간동안</div>
        <div>물품을 공유하시겠어요?</div>
        <div css={buttonWrap}>
          <button css={badbutton}>취소</button>
          <button css={goodbutton}>제출하기</button>
        </div>
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
export default MeetConfirm;
