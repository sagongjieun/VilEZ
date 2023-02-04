import React, { useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const MyBoxHeader = () => {
  const userNickName = localStorage.getItem("nickName");
  useEffect(() => {}, []);
  return (
    <div css={headerWrapper}>
      <div>{userNickName}님 께서는</div>
      <div>
        <div>10번 공유를 하고</div>
        <div>10번 공유받았습니다.</div>
      </div>
    </div>
  );
};

const headerWrapper = css`
  width: 100%;
  height: 300px;
  border-radius: 10px;
  background-color: #66dd9c;
`;
export default MyBoxHeader;
