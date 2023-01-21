import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProfileEditButton from "./ProfileEditButton";
import ProfileSummary from "./ProfileSummary";
import ProfileLocation from "./ProfileLocation";
import ProfilePoint from "./ProfilePoint";
import ProfileDday from "./ProfileDday";

const ProfileInformation = () => {
  return (
    <div css={profileWrapper}>
      <ProfileEditButton text="프로필 수정하기" />
      <ProfileSummary />
      <ProfileLocation />
      <div
        css={css`
          width: 44%;
        `}
      >
        <ProfilePoint />
        <ProfileDday />
      </div>
    </div>
  );
};

const profileWrapper = css`
  position: relative;
  display: flex;
  justify-content: space-between;
  height: 300px;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  padding: 20px 2%;
  // 좌우 padding 2%이므로 내부 width 합은 96%
`;

export default ProfileInformation;
