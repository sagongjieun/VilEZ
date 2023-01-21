import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const ProfileLocation = () => {
  return <div css={locationWrapper}></div>;
};

const locationWrapper = css`
  width: 28%;
  height: 100%;
  border-right: 1px solid #d8d8d8;
`;

export default ProfileLocation;
