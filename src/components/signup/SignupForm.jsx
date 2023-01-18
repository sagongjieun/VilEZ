import React from "react";
import { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { AiOutlineEyeInvisible, AiOutlineEye, AiOutlineExclamationCircle } from "react-icons/ai";
import LargeWideButton from "../../components/button/LargeWideButton";
import ConfirmButton from "./ConfirmButton";
import SignupInputBox from "./SignupInputBox";
import Validation from "./SignupValidation";
import useForm from "../../hooks/useForm";

const SignupForm = () => {
  const onSubmit = () => {};
  const onEmailSubmit = () => {};
  const onEmailCodeSubmit = () => {};
  const onNicknameSubmit = () => {};
  const [visible, setVisible] = useState(false);
  const [emailCodeVisible, setEmailCodeVisible] = useState(false);
  const onClickVisible = (event) => {
    event.preventDefault();
    setVisible((prev) => !prev);
  };
  const onClickEmailCodeVisible = () => {
    setEmailCodeVisible((prev) => !prev);
  };
  const { errors, handleChange, handleSubmit } = useForm({
    initialValues: {
      password: "",
      password2: "",
      email: "",
      nickname: "",
      emailCode: "",
    },
    onSubmit: onSubmit,
    Validation,
  });
  let {
    errors: emailError,
    handleChange: emailChange,
    handleClick: emailSubmit,
  } = useForm({
    initialValues: {
      email: "",
    },
    onSubmit: onEmailSubmit,
    Validation,
  });
  let {
    errors: emailCodeError,
    handleChange: emailCodeChange,
    handleClick: emailCodeSubmit,
  } = useForm({
    initialValues: {
      emailCode: "",
    },
    onSubmit: onEmailCodeSubmit,
    Validation,
  });
  let {
    errors: nicknameError,
    handleChange: nicknameChange,
    handleClick: nicknameSubmit,
  } = useForm({
    initialValues: {
      nickname: "",
    },
    onSubmit: onNicknameSubmit,
    Validation,
  });
  const errorsInitialize = () => {
    emailError.email = "";
    emailCodeError.emailCode = "";
    nicknameError.nickname = "";
  };
  return (
    <form
      onSubmit={(event) => {
        handleSubmit(event);
        errorsInitialize();
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
                disabled={emailCodeVisible}
                onChange={(event) => {
                  handleChange(event);
                  emailChange(event);
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
                onClick={(event) => {
                  emailSubmit(event);
                  onClickEmailCodeVisible();
                }}
              />
            </div>
          </div>
          {emailError.email ? (
            <small css={alertWrapper}>
              <small css={alert}>
                <AiOutlineExclamationCircle size="14" />
              </small>
              <small
                css={css`
                  line-height: 22px;
                `}
              >
                {emailError.email}
              </small>
            </small>
          ) : null}
        </div>
        {/* email 인증 보내기 */}
        <div
          css={
            emailCodeVisible && !errors.email && !emailError.email ? [emailCodeWrapper] : [emailCodeWrapper, hideBox]
          }
        >
          <div
            css={css`
              display: flex;
              justify-content: space-between;
            `}
          >
            <div
              css={css`
                width: calc(100% - 90px);
              `}
            >
              <SignupInputBox
                name="emailCode"
                type="text"
                placeholder="인증 코드를 입력해주세요."
                onChange={(event) => {
                  handleChange(event);
                  emailCodeChange(event);
                }}
              />
            </div>
            <div
              css={css`
                width: 80px;
              `}
            >
              <ConfirmButton text="확인" onClick={emailCodeSubmit} />
            </div>
          </div>

          <small>이메일을 받지 못했나요?</small>
          {emailCodeError.emailCode ? (
            <small css={alertWrapper}>
              <small css={alert}>
                <AiOutlineExclamationCircle size="14" />
              </small>
              <small
                css={css`
                  line-height: 22px;
                `}
              >
                {emailCodeError.emailCode}
              </small>
            </small>
          ) : null}
        </div>
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
            type={visible ? "text" : "password"}
            placeholder="비밀번호를 입력해주세요."
            onChange={handleChange}
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
            type={visible ? "text" : "password"}
            placeholder="비밀번호를 다시 한 번 입력해주세요."
            onChange={handleChange}
          />
          <span onClick={onClickVisible} css={visibleButton}>
            {visible ? <AiOutlineEye size="28" color="#66dd9c" /> : <AiOutlineEyeInvisible size="28" color="#66dd9c" />}
          </span>
        </div>
        <small css={alertWrapper}>
          <small css={alert}>{errors.password2 ? <AiOutlineExclamationCircle size="14" /> : null}</small>
          <small
            css={css`
              line-height: 22px;
            `}
          >
            {errors.password2 ? errors.password2 : null}
          </small>
        </small>
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
              name="nickname"
              type="text"
              placeholder="닉네임을 입력하세요."
              onChange={(event) => {
                handleChange(event);
                nicknameChange(event);
              }}
            />
          </div>
          <div
            css={css`
              width: 100px;
            `}
          >
            <ConfirmButton outline={true} text="중복확인" onClick={nicknameSubmit} />
          </div>
        </div>
        {nicknameError.nickname || errors.nickname ? null : (
          <small
            css={css`
              color: #8a8a8a;
            `}
          >
            최대 6자까지 설정할 수 있어요.
          </small>
        )}
        {errors.nickname ? (
          <small css={alertWrapper}>
            <small css={alert}>
              <AiOutlineExclamationCircle size="14" />
            </small>
            <small
              css={css`
                line-height: 22px;
              `}
            >
              {errors.nickname}
            </small>
          </small>
        ) : null}
        {nicknameError.nickname ? (
          <small css={alertWrapper}>
            <small css={alert}>
              <AiOutlineExclamationCircle size="14" />
            </small>
            <small
              css={css`
                line-height: 22px;
              `}
            >
              {nicknameError.nickname}
            </small>
          </small>
        ) : null}
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
const visibleButton = css`
  position: absolute;
  right: 24px;
  top: calc(55px / 2 - 14px);
  cursor: pointer;
  border: none;
  background-color: rgba(0, 0, 0, 0);
`;
const alertWrapper = css`
  color: red;
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
  visibility: visible;
  opacity: 1;
  transition: all 0.5s;
`;
const hideBox = css`
  display: none;
  visibility: hidden;
  opacity: 0;
  transition: all 0.5s;
`;

export default SignupForm;
