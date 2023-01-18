import React from "react";
/** @jsxImportSource @emotion/react */
// import { css } from "@emotion/react";

function Report() {
  return (
    <div>
      <h2>신고하기</h2>
      <input type="text" placeholder="신고사유를 작성해주세요." />
      <button>취소</button>
      <button>제출하기</button>
    </div>
  );
}
export default Report;
