import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { Routes, Route } from "react-router-dom";
import ProfileInformation from "../components/profile/ProfileInformation";
import ProfileMainCalander from "../components/profile/ProfileMainCalander";
import ProfileMainProduct from "../components/profile/ProfileMainProduct";
import ProfileMainPoint from "../components/profile/ProfileMainPoint";

const Profile = () => {
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
  margin: 60px 200px;
`;

export default Profile;
