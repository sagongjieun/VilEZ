import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { requestKakaoLogin } from "../../api/oAuthLogin";
import LoginLoadingModal from "../modal/LoginLoadingModal";
import { useSetRecoilState } from "recoil";
import { isLoginState, loginUserState } from "../../recoil/atom";

const OAuthKakao = () => {
  const navigate = useNavigate();
  const code = new URL(window.location.href).searchParams.get("code");
  const setLoginUser = useSetRecoilState(loginUserState);
  const setIsLogin = useSetRecoilState(isLoginState);
  const [nickname, setNickname] = useState("");

  function onKakaoLogin(code) {
    requestKakaoLogin(code).then((response) => {
      const resData = response[0];

      localStorage.setItem("accessToken", resData.accessToken);
      localStorage.setItem("refreshToken", resData.refreshToken);
      localStorage.setItem("id", resData.id);
      localStorage.setItem("nickName", resData.nickName);
      localStorage.setItem("profileImg", resData.profileImg);
      localStorage.setItem("areaLat", resData.areaLat);
      localStorage.setItem("areaLng", resData.areaLng);
      localStorage.setItem("oauth", resData.oauth);

      console.log(response[0]);

      setNickname(resData.nickName);

      setLoginUser({
        id: resData.id,
        nickName: resData.nickName,
        manner: resData.manner,
        point: resData.point,
        profileImg: resData.profileImg,
        areaLng: resData.areaLng,
        areaLat: resData.areaLat,
        oauth: resData.oauth,
      });

      setIsLogin(true);
    });
  }

  useEffect(() => {
    onKakaoLogin(code);
  }, []);

  useEffect(() => {
    if (nickname !== "") {
      setTimeout(() => {
        if (nickname.includes("#")) {
          navigate("/socialnickname", { state: { url: "/" } });
        } else navigate("/");
      }, 1500);
    }
  }, [nickname]);

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
