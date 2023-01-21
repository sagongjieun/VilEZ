import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import jjangu from "../../assets/images/jjangu.png";

const ProfileSummary = () => {
  return (
    <div css={summaryWrapper}>
      <div css={summaryBox}>
        <div css={profilePicture}>프사요</div>
        <h3>이름이요</h3>
        <div css={mannerWrapper}>
          <div>Lv.</div>
          <div>매너지수요</div>
        </div>
        <div>게이지요</div>
      </div>
    </div>
  );
};

const summaryWrapper = css`
  width: 28%;
  height: 100%;
  border-right: 1px solid #d8d8d8;
`;
const summaryBox = css`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
`;

const profilePicture = css`
  width: 120px;
  height: 120px;
  background-image: url(${jjangu});
  background-size: cover;
  background-position: center center;
  border-radius: 50%;
`;
const mannerWrapper = css``;
export default ProfileSummary;
