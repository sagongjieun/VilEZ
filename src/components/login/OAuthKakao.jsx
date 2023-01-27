import React from "react";
import LargeWideButton from "../button/LargeWideButton";
import { requestKakaoLogin } from "../../api/oAuthLogin";
// import { useState } from "react";

const OAuthKakao = () => {
  const code = new URL(window.location.href).searchParams.get("code");
  const onKakaoLogin = () => {
    requestKakaoLogin(code).then((response) => {
      console.log(response);
    });
  };

  return (
    <div>
      <div>로그인 하세요</div>
      <LargeWideButton text={"로그인 고고"} onClick={onKakaoLogin} />
    </div>
  );
};

export default OAuthKakao;
