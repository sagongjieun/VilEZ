import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProfileCalander from "./ProfileCalander";

const ProfileMainCalander = () => {
  return (
    <div
      css={css`
        padding-top: 30px;
      `}
    >
      <ProfileCalander />
    </div>
  );
};

export default ProfileMainCalander;
