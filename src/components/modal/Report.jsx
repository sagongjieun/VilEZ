import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useState } from "react";

function Report() {
  const [report, setReport] = useState("");
  const onChangeReport = (e) => {
    setReport(e.target.value);
  };
  return (
    <div css={reportWrap}>
      <div css={reportContentWrap}>
        <div>
          <h2>신고하기</h2>
          <textarea css={inputBox} value={report} placeholder="신고사유를 작성해주세요." onChange={onChangeReport} />
        </div>
        <div css={buttonWrap}>
          <button css={badbutton}>취소</button>
          <button css={goodbutton}>제출하기</button>
        </div>
      </div>
    </div>
  );
}
const reportWrap = css`
  width: 500px;
  height: 600px;
  box-shadow: 1px 1px 5px;
  border-radius: 10px;
  text-align: center;
`;
const reportContentWrap = css`
  display: flex;
  flex-direction: column;
  & > div:first-of-type {
    margin-top: 30px;
  }
  & input {
    margin-top: 70px;
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

const inputBox = css`
  resize: none;
  margin-top: 30px;
  padding-left: 10px;
  padding-top: 20px;
  width: 400px;
  height: 300px;
  border-radius: 20px;
  ::placeholder {
    color: 8A8A8A;
  }
`;
export default Report;
