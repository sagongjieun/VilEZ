import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const ProfileDday = (props) => {
  return (
    <div css={ddayWrapper}>
      <div>
        <h4>내 보유 포인트</h4>
        <div css={myPointWrapper}>
          <div> {props.point}</div>
          <span>p</span>
        </div>
      </div>
    </div>
  );
};

const ddayWrapper = css`
  height: 50%;
  border-bottom: 1px solid #d8d8d8;
  & > div {
    padding: 10px;
  }
`;
const myPointWrapper = css`
  display: flex;
  width: 100%;
  height: 80px;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: bold;
  color: #66dd9c;
  & > span {
    font-size: 34px;
    color: #8a8a8a;
    font-weight: bold;
  }
`;
export default ProfileDday;
