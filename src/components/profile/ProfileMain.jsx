import React from "react";
import { css } from "@emotion/react";
import { Routes, Route } from "react-router-dom";
import ProfileInformation from "./ProfileInformation";
import ProfileMainCalander from "./ProfileMainCalander";
import ProfileMainProduct from "./ProfileMainProduct";
import ProfileMainPoint from "./ProfileMainPoint";

const ProfileMain = () => {
  return (
    <div css={ProfileWrapper}>
      <ProfileInformation />
      <Routes>
        <Route path="/calander" element={<ProfileMainCalander />} />
        <Route path="/product" element={<ProfileMainProduct />} />
        <Route path="/point" element={<ProfileMainPoint />} />
      </Routes>
    </div>
  );
};

const ProfileWrapper = css`
  padding: 20px 200px 60px;
`;

export default ProfileMain;
