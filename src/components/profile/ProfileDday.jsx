import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const ProfileDday = () => {
  return (
    <div css={ddayWrapper}>
      <div>
        <h4>잊지 마세요</h4>
      </div>
    </div>
  );
};

const ddayWrapper = css`
  height: 50%;
  padding: 6px 0px;
  & > div {
    padding: 10px;
    & > h4 {
    }
  }
`;
export default ProfileDday;
