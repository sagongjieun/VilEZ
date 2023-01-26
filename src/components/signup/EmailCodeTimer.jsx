import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const EmailCodeTimer = (setEmailConfirmText) => {
  const [time, setTime] = useState(180);
  const [min, setMin] = useState("");
  const [sec, setSec] = useState("");
  useEffect(() => {
    const checkTimer = setInterval(() => {
      setMin(parseInt(time / 60));
      setSec(time % 60);
      setTime((prev) => prev - 1);
      if (time < 0) {
        clearInterval(checkTimer); //setInterval() 실행을 끝냄
        setEmailConfirmText("인증 시간이 만료되었습니다. 이메일 인증을 다시 시작해주세요.");
      }
    }, 1000);
  }, [time]);

  return (
    <div css={timerWrapper}>
      <div>
        {min}:{sec}
      </div>
    </div>
  );
};

const timerWrapper = css``;

export default EmailCodeTimer;
