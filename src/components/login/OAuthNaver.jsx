import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { requestNaverLogin } from "../../api/oAuthLogin";
import LoginLoadingModal from "../modal/LoginLoadingModal";
import { useSetRecoilState } from "recoil";
import { loginUserState } from "../../recoil/atom";

const OAuthNaver = () => {
  const setLoginUser = useSetRecoilState(loginUserState);
  const navigate = useNavigate();
  const code = new URL(window.location.href).searchParams.get("code");
  function onNaverLogin(code) {
    requestNaverLogin(code).then((response) => {
      const resData = response[0];
      console.log("***************");
      localStorage.setItem("accessToken", resData.accessToken);
      localStorage.setItem("refreshToken", resData.refreshToken);
      localStorage.setItem("id", resData.id);
      localStorage.setItem("nickName", resData.nickName);
      setLoginUser((prev) => {
        return {
          ...prev,
          id: resData.id,
          nickName: resData.nickName,
          manner: resData.manner,
          point: resData.point,
          profileImg: resData.profileImg,
        };
      });
    });
  }
  useEffect(() => {
    // setCode(new URL(window.location.href).searchParams.get("code"));
    onNaverLogin(code);
  }, []);
  // useEffect(() => {
  //   onNaverLogin();
  // }, [code]);
  setTimeout(() => {
    navigate("/");
  }, 1500);

  return (
    <div css={container}>
      <LoginLoadingModal />
    </div>
  );
};
const container = css`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;
`;
export default OAuthNaver;
