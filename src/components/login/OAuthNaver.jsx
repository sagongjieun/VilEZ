import React from "react";
import LargeWideButton from "../button/LargeWideButton";
import { requestNaverLogin } from "../../api/oAuthLogin";
// import { useState } from "react";

const OAuthNaver = () => {
  const code = new URL(window.location.href).searchParams.get("code");
  // const state = new URL(window.location.href).searchParams.get("state");
  const onNaverLogin = () => {
    requestNaverLogin(code).then((response) => {
      console.log(response);
    });
  };

  return (
    <div>
      <div>로그인 하세요</div>
      <LargeWideButton text={"로그인 고고"} onClick={onNaverLogin} />
    </div>
  );
};

export default OAuthNaver;
