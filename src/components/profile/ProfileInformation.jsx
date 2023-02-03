import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProfileEditButton from "./ProfileEditButton";
import ProfileSummary from "./ProfileSummary";
import ProfileLocation from "./ProfileLocation";
import ProfilePoint from "./ProfilePoint";
import { getUserDetail } from "../../api/profile";
// import { set } from "date-fns";

const ProfileInformation = ({ setIsQrCodeOpen, setIsEditProfileOpen }) => {
  const id = localStorage.getItem("id");
  // const id = 28;
  const [area, setArea] = useState("동네를 설정해주세요.");
  const [profileImage, setProfileImage] = useState("");
  const [nickName, setNickName] = useState("");
  const [manner, setManner] = useState(0);
  const [point, setPoint] = useState(0);
  function onClickEditProfileOpen() {
    setIsEditProfileOpen(true);
    console.log("true true");
  }
  useEffect(() => {
    getUserDetail(id).then((response) => {
      setArea(response.area);
      setProfileImage(response.profile_img);
      setNickName(response.nickName);
      setManner(response.manner);
      setPoint(response.point);
    });
  }, []);
  useEffect(() => {
    if (!area) {
      setArea("동네를 설정해주세요.");
    }
  }, [area]);
  return (
    <div css={profileWrapper}>
      <ProfileEditButton text="프로필 수정하기" onClick={onClickEditProfileOpen} />
      <ProfileSummary profileImage={profileImage} manner={manner} nickName={nickName} />
      <ProfileLocation area={area} setIsQrCodeOpen={setIsQrCodeOpen} />
      <div
        css={css`
          width: 44%;
          padding-left: 20px;
        `}
      >
        <ProfilePoint point={point} />
        {/* <ProfileDday /> */}
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
  & h4 {
    font-size: 22px;
  }
  // 좌우 padding 2%이므로 내부 width 합은 96%
`;

export default ProfileInformation;
