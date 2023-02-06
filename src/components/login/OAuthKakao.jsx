import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { requestKakaoLogin } from "../../api/oAuthLogin";
import LoginLoadingModal from "../modal/LoginLoadingModal";
import { useSetRecoilState } from "recoil";
import { loginUserState } from "../../recoil/atom";

const OAuthKakao = () => {
  const setLoginUser = useSetRecoilState(loginUserState);
  const navigate = useNavigate();
  const [code, setCode] = useState("");
  function onKakaoLogin() {
    requestKakaoLogin(code).then((response) => {
      const data = response.data[0];
      console.log("***************");
      localStorage.setItem("accessToken", data.accessToken);
      localStorage.setItem("refreshToken", data.refreshToken);
      localStorage.setItem("id", data.id);
      localStorage.setItem("nickName", data.nickName);
      setLoginUser((prev) => {
        return {
          ...prev,
          id: data.id,
          nickName: data.nickName,
          manner: data.manner,
          point: data.point,
          profileImg: data.profileImg,
        };
      });
    });
  }
  useEffect(() => {
    setCode(new URL(window.location.href).searchParams.get("code"));
  }, []);
  useEffect(() => {
    onKakaoLogin();
  }, [code]);
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
export default OAuthKakao;
