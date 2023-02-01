import React from "react";
import { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { AiOutlineEyeInvisible, AiOutlineEye, AiOutlineExclamationCircle } from "react-icons/ai";
import LargeWideButton from "../../components/button/LargeWideButton";
import ConfirmButton from "./ConfirmButton";
import SignupInputBox from "./SignupInputBox";
import EmailCodeTimer from "./EmailCodeTimer";
import Validation from "./SignupValidation";
import useForm from "../../hooks/useForm";
import { SHA256 } from "./EmailCodeHashFunction";
import { confirmEmail, checkNickName, postUserInformation } from "../../api/signup";
import { useNavigate } from "react-router-dom";

const SignupForm2 = () => {
  const navigate = useNavigate();
  const [isVisible, setIsVisible] = useState(false);
  const [email, setEmail] = useState("");
  const [emailCode, setEmailCode] = useState("");
  const [hashedCode, setHashedCode] = useState("");
  const [password, setPassword] = useState("");
  const [password2, setPassword2] = useState("");
  const [nickName, setNickName] = useState("");
  const [nickNameCheck, setNickNameCheck] = useState("");
  const [isNickNameAvailable, setIsNickNameAvailable] = useState(false);
  const [isEmailCodeVisible, setIsEmailCodeVisible] = useState(false);
  const [isTimeOut, setIsTimeOut] = useState(true);
  const [isCodeConfirmed, setIsCodeConfirmed] = useState(false);
  const [emailConfirmText, setEmailConfirmText] = useState("");
  function onChangeEmail(event) {
    setEmail(event.target.value);
    setIsEmailCodeVisible(false);
  }
  function onChangeEmailCode(event) {
    setEmailCode(event.target.value);
  }
  function onChangePassword(event) {
    setPassword(event.target.value);
    errors.password = "";
  }
  function onChangePassword2(event) {
    setPassword2(event.target.value);
    errors.password2 = "";
  }
  function onChangeNickName(event) {
    setNickName(event.target.value);
    errors.nickName = "";
    setNickNameCheck("");
    setIsNickNameAvailable(false);
  }
  function onSubmit() {
    const userInformation = {
      email,
      password,
      nickName,
    };
    if (isNickNameAvailable && isCodeConfirmed) {
      postUserInformation(userInformation).then((response) => {
        if (response) {
          navigate("/login");
        }
      });
    }
    console.log("회원가입 고");
  }
  function onSubmitEmail() {
    setIsCodeConfirmed(false);
    confirmEmail(email).then((response) => {
      if (response) {
        setHashedCode(response[0]);
        setIsEmailCodeVisible(false);
        setTimeout(() => {
          setIsEmailCodeVisible(true);
        }, 100);
        setIsTimeOut(false);
      }
    });
  }
  function onSubmitEmailCode() {
    if (SHA256(emailCode) === hashedCode) {
      setIsCodeConfirmed(true);
      setEmailConfirmText("이메일 인증이 완료되었습니다. 회원가입을 계속 진행해주세요.");
    } else {
      setEmailConfirmText("인증 코드가 일치하지 않습니다. 이메일을 다시 확인해주세요.");
    }
  }
  function onSubmitNickName() {
    if (nickName.length > 0 && nickName.length < 7) {
      checkNickName(nickName).then((response) => {
        setNickNameCheck(response.text);
        setIsNickNameAvailable(response.isNickNameAvailable);
      });
    } else if (!nickName) {
      setNickNameCheck("닉네임을 입력해주세요.");
      // errors.nickName = "닉네임을 입력해주세요.";
      setIsNickNameAvailable(false);
    } else if (nickName.length > 6) {
      setNickNameCheck("닉네임은 최대 6자까지 설정할 수 있어요.");
      // errors.nickName = "닉네임은 최대 6자까지 설정할 수 있어요.";
      setIsNickNameAvailable(false);
    }
  }
  const onClickVisible = (event) => {
    event.preventDefault();
    setIsVisible((prev) => !prev);
  };
  const { errors, handleChange, handleSubmit } = useForm({
    initialValues: {
      password: "",
      password2: "",
      email: "",
      nickName: "",
      emailCode: "",
    },
    onSubmit: onSubmit,
    Validation,
  });
  return (
    <form
      onSubmit={(event) => {
        handleSubmit(event);
        // errorsInitialize();
      }}
    >
      <div css={inputContainer}>
        <label css={loginLabelFont} htmlFor="email">
          이메일
        </label>
        <div>
          <div css={inputButtonWrapper}>
            <div
              css={css`
                width: calc(100% - 130px);
              `}
            >
              <SignupInputBox
                name="email"
                type="text"
                placeholder="vilez@villypeople.com"
                disabled={isCodeConfirmed}
                onChange={(event) => {
                  handleChange(event);
                  onChangeEmail(event);
                }}
              />
            </div>
            <div
              css={css`
                width: 120px;
              `}
            >
              <ConfirmButton
                outline={true}
                text="이메일 인증"
                onClick={() => {
                  onSubmitEmail();
                }}
              />
            </div>
          </div>
          {errors.email ? (
            <small css={alertWrapper}>
              <small css={alert}>
                <AiOutlineExclamationCircle size="14" />
              </small>
              <small
                css={css`
                  line-height: 22px;
                `}
              >
                {errors.email}
              </small>
            </small>
          ) : null}
        </div>
        {/* email 인증 보내기 */}
        {isEmailCodeVisible ? (
          <div css={emailCodeWrapper}>
            <div
              css={css`
                display: flex;
                justify-content: space-between;
              `}
            >
              <div css={codeAndTimer}>
                <SignupInputBox
                  name="emailCode"
                  type="text"
                  placeholder="인증 코드를 입력해주세요."
                  onChange={(event) => {
                    handleChange(event);
                    onChangeEmailCode(event);
                  }}
                  disabled={isCodeConfirmed}
                />
                {!isCodeConfirmed ? (
                  <EmailCodeTimer
                    setIsTimeOut={setIsTimeOut}
                    setEmailConfirmText={() => {
                      setEmailConfirmText();
                    }}
                    setHashedCode={() => {
                      setHashedCode();
                    }}
                  />
                ) : null}
              </div>
              <div
                css={css`
                  width: 80px;
                `}
              >
                <ConfirmButton text="확인" onClick={() => onSubmitEmailCode()} />
              </div>
            </div>
            {emailConfirmText && !isTimeOut ? <small css={alertWrapper}>{emailConfirmText}</small> : null}
            {isTimeOut ? (
              <small css={alertWrapper}>인증 시간이 초과되었습니다. 이메일 인증을 다시 해주세요.</small>
            ) : null}
            <small>
              이메일을 받지 못했나요?{" "}
              <button
                onClick={() => {
                  onSubmitEmail();
                }}
              >
                인증코드 재요청
              </button>
            </small>
            {/* {emailCode !== hashedCode ? (
              <small css={alertWrapper}>
                <small css={alert}>
                  <AiOutlineExclamationCircle size="14" />
                </small>
                <small
                  css={css`
                    line-height: 22px;
                  `}
                >
                  인증 코드가 일치하지 않습니다 .다시 확인해주세요.
                </small>
              </small>
            ) : null} */}
          </div>
        ) : null}
        {/* email 인증 확인하기 */}
      </div>
      {/* email */}

      <div css={inputContainer}>
        <label css={loginLabelFont} htmlFor="password">
          비밀번호
        </label>
        <div
          css={css`
            position: relative;
            padding-bottom: 6px;
          `}
        >
          <SignupInputBox
            name="password"
            type={isVisible ? "text" : "password"}
            placeholder="비밀번호를 입력해주세요."
            onChange={(event) => {
              handleChange(event);
              onChangePassword(event);
            }}
          />
          {errors.password ? null : (
            <small
              css={css`
                color: #8a8a8a;
              `}
            >
              영어 소문자, 숫자 조합 8~16자리로 입력해주세요.
            </small>
          )}
          {errors.password ? (
            <small css={alertWrapper}>
              <small css={alert}>
                <AiOutlineExclamationCircle size="14" />
              </small>
              <small
                css={css`
                  line-height: 22px;
                `}
              >
                {errors.password}
              </small>
            </small>
          ) : null}
        </div>
        <div
          css={css`
            position: relative;
          `}
        >
          <SignupInputBox
            name="password2"
            type={isVisible ? "text" : "password"}
            placeholder="비밀번호를 다시 한 번 입력해주세요."
            onChange={(event) => {
              handleChange(event);
              onChangePassword2(event);
            }}
          />
          <span onClick={onClickVisible} css={isVisibleButton}>
            {isVisible ? (
              <AiOutlineEye size="28" color="#66dd9c" />
            ) : (
              <AiOutlineEyeInvisible size="28" color="#66dd9c" />
            )}
          </span>
        </div>
        {errors.password2 || (password !== password2 && password2) ? (
          <small css={alertWrapper}>
            <small css={alert}>
              <AiOutlineExclamationCircle size="14" />
            </small>
            <small
              css={css`
                line-height: 22px;
              `}
            >
              {password !== password2 && password2
                ? "비밀번호가 일치하지 않습니다. 다시 입력해주세요."
                : errors.password2}
            </small>
          </small>
        ) : null}
      </div>
      {/* password */}

      <div css={inputContainer}>
        <label css={loginLabelFont} htmlFor="email">
          닉네임 설정하기
        </label>
        <div css={inputButtonWrapper}>
          <div
            css={css`
              width: calc(100% - 110px);
            `}
          >
            <SignupInputBox
              name="nickName"
              type="text"
              placeholder="닉네임을 입력하세요."
              onChange={(event) => {
                handleChange(event);
                onChangeNickName(event);
              }}
            />
          </div>
          <div
            css={css`
              width: 100px;
            `}
          >
            <ConfirmButton outline={true} text="중복확인" onClick={() => onSubmitNickName()} />
          </div>
        </div>
        {errors.nickName && !nickNameCheck ? (
          <small css={alertWrapper}>
            <small css={alert}>
              <AiOutlineExclamationCircle size="14" />
            </small>
            <small
              css={css`
                line-height: 22px;
              `}
            >
              {errors.nickName}
            </small>
          </small>
        ) : null}
        {!errors.nickName && nickNameCheck ? <small css={alertNickNameWrapper}>{nickNameCheck}</small> : null}
      </div>
      <br />
      <LargeWideButton text="가입하기" />
    </form>
  );
};

const loginLabelFont = css`
  display: block;
  font-size: 18px;
  font-weight: Bold;
  margin-bottom: 10px;
`;
const inputContainer = css`
  padding-top: 20px;
`;
const inputButtonWrapper = css`
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: start;
`;
const isVisibleButton = css`
  position: absolute;
  right: 24px;
  top: calc(55px / 2 - 14px);
  cursor: pointer;
  border: none;
  background-color: rgba(0, 0, 0, 0);
`;
const codeAndTimer = css`
  width: calc(100% - 90px);
  position: relative;
  & > div {
    position: absolute;
    height: 55px;
    line-height: 55px;
    top: 0px;
    right: 14px;
  }
`;
const alertWrapper = css`
  color: red;
  display: flex;
  align-items: center;
  padding-top: 3px;
  height: 21px;
`;
const alertNickNameWrapper = css`
  display: flex;
  align-items: center;
  padding-top: 3px;
  height: 21px;
`;
const alert = css`
  margin-right: 3px;
  display: flex;
  align-items: center;
  height: 21px;
`;
const emailCodeWrapper = css`
  display: block;
  margin-top: 16px;
  border-radius: 5px;
  background-color: #acf0cb;
  padding: 20px;
  visibility: isVisible;
  opacity: 1;
  transition: all 0.5s;
`;

export default SignupForm2;
