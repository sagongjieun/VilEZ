import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { Routes, Route } from "react-router-dom";
import { Link } from "react-router-dom";
import ProfileInformation from "../components/profile/ProfileInformation";
import ProfileMainCalander from "../components/profile/ProfileMainCalander";
import ProfileMainProduct from "../components/profile/ProfileMainProduct";
import ProfileMainPoint from "../components/profile/ProfileMainPoint";
import EditProfile from "../components/modal/EdifProfile";

const Profile = () => {
  return (
    <div css={ProfileWrapper}>
      <ProfileInformation />
      <div css={linkWrapper}>
        <Link to={"/profile/calander"}>공유 캘린더</Link>
        <Link to={"/profile/product"}>공유 목록</Link>
        <Link to={"/profile/point"}>포인트 내역</Link>
      </div>

      <Routes>
        <Route path="/calander" element={<ProfileMainCalander />} />
        <Route path="/product" element={<ProfileMainProduct />} />
        <Route path="/point" element={<ProfileMainPoint />} />
      </Routes>

      <EditProfile />
    </div>
  );
};

const ProfileWrapper = css`
  margin: 60px 200px;
`;
const linkWrapper = css`
  display: flex;
  justify-content: space-between;
  width: calc(50%);
  min-width: 300px;
  margin: 30px auto;
  font-size: 18px;
  & > a {
    color: #000;
  }
`;
export default Profile;
