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
import IndividualValidation from "./IndividualValidation";
import useForm from "../../hooks/useForm";
import useIndividualForm from "../../hooks/useIndividualForm";
import { SHA256 } from "./EmailCodeHashFunction";
import { confirmEmail } from "../../api/signup";
// import { Link } from "react-router-dom";
// import { useEffect } from "react";

const SignupForm = () => {
  const [hashedCode, setHashedCode] = useState("");
  const [userInputCode, setUserInputCode] = useState("");
  const [visible, setVisible] = useState(false);
  const [emailCodeVisible, setEmailCodeVisible] = useState(false);
  const [codeConfirmed, setCodeConfirmed] = useState(false);
  const [emailConfirmText, setemailConfirmText] = useState("");
  const onSubmit = () => {};
  const onEmailSubmit = (value) => {
    confirmEmail(value).then((response) => {
      setHashedCode(response[0]);
    });
    setEmailCodeVisible(true);
  };
  const onEmailCodeSubmit = () => {
    if (SHA256(userInputCode) === hashedCode) {
      setCodeConfirmed(true);
      setemailConfirmText("이메일 인증이 완료되었습니다. 회원가입을 계속 진행해주세요.");
    } else {
      emailCodeError.emailCode = "인증 코드가 일치하지 않습니다. 이메일을 다시 확인해주세요.";
    }
  };
  const onNickNameSubmit = () => {};
  const onClickVisible = (event) => {
    event.preventDefault();
    setVisible((prev) => !prev);
  };
  const onUserCodeInput = (event) => {
    setUserInputCode(event.target.value);
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
  const {
    error: emailError,
    handleChange: emailChange,
    handleClick: emailSubmit,
  } = useIndividualForm({
    initialValue: {
      email: "",
    },
    onSubmit: onEmailSubmit,
    Validation: IndividualValidation,
  });
  const {
    error: emailCodeError,
    handleChange: emailCodeChange,
    handleClick: emailCodeSubmit,
  } = useIndividualForm({
    initialValue: {
      emailCode: "",
    },
    onSubmit: onEmailCodeSubmit,
    Validation: IndividualValidation,
  });
  const {
    error: nickNameError,
    handleChange: nickNameChange,
    handleClick: nickNameSubmit,
  } = useIndividualForm({
    initialValue: {
      nickName: "",
    },
    onSubmit: onNickNameSubmit,
    Validation: IndividualValidation,
  });
  const errorsInitialize = () => {
    emailError.email = "";
    emailCodeError.emailCode = "";
    nickNameError.nickName = "";
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
        <div css={emailCodeVisible ? [emailCodeWrapper] : [emailCodeWrapper, hideBox]}>
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
                  onUserCodeInput(event);
                }}
                disabled={codeConfirmed}
              />
              <EmailCodeTimer setEmailConfirmText={setemailConfirmText} />
            </div>
            <div
              css={css`
                width: 80px;
              `}
            >
              <ConfirmButton text="확인" onClick={emailCodeSubmit} />
            </div>
          </div>
          <small>{emailConfirmText}</small>
          {emailConfirmText ? null : (
            <small>
              이메일을 받지 못했나요? <button>인증코드 재요청</button>
            </small>
          )}
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
              name="nickName"
              type="text"
              placeholder="닉네임을 입력하세요."
              onChange={(event) => {
                handleChange(event);
                nickNameChange(event);
              }}
            />
          </div>
          <div
            css={css`
              width: 100px;
            `}
          >
            <ConfirmButton outline={true} text="중복확인" onClick={nickNameSubmit} />
          </div>
        </div>
        {nickNameError.nickName || errors.nickName ? null : (
          <small
            css={css`
              color: #8a8a8a;
            `}
          >
            최대 6자까지 설정할 수 있어요.
          </small>
        )}
        {errors.nickName ? (
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
        {nickNameError.nickName ? (
          <small css={alertWrapper}>
            <small css={alert}>
              <AiOutlineExclamationCircle size="14" />
            </small>
            <small
              css={css`
                line-height: 22px;
              `}
            >
              {nickNameError.nickName}
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
