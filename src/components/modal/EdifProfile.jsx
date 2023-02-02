import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useState, useEffect } from "react";
import { AiOutlineEyeInvisible, AiOutlineEye, AiOutlineExclamationCircle } from "react-icons/ai";
import { BsCheck2Circle } from "react-icons/bs";
import { IoIosCloseCircle } from "react-icons/io";
import bazzie from "../../assets/images/bazzie.jfif";
import ProfileImageSelect from "../profile/ProfileImageSelect";
import { checkNickName } from "../../api/signup";
import { getUserDetail } from "../../api/user";
// import DefaultProfile from "../../assets/default_profile.png"

function EditProfile() {
  const userId = localStorage.getItem("id");
  const [userNickName, setUserNickName] = useState("");
  const [userProfileImage, setUserProfileImage] = useState("");
  const [nickName, setNickName] = useState("");
  const [nickNameCheck, setNickNameCheck] = useState("");
  const [nickNameError, setNickNameError] = useState("");
  const [isNickNameAvailable, setIsNickNameAvailable] = useState(false);
  const [password, setPassword] = useState("");
  const [password2, setPassword2] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [password2Error, setPassword2Error] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [isDeleted, setIsDeleted] = useState(true);
  const [isPasswordConfirmed, setIsPasswordConfirmed] = useState("");
  const [imageList, setImageList] = useState([]);
  const image = bazzie;
  function onKeyDown(event) {
    if (event.key === "Enter") {
      event.preventDefault();
    }
  }
  function onChangeNickName(event) {
    setNickName(event.target.value);
  }
  function onChangePassword(event) {
    setPassword(event.target.value);
  }
  function onChangePassword2(event) {
    setPassword2(event.target.value);
  }
  function onClickNickNameCheck() {
    checkNickName(nickName).then((response) => {
      setNickNameCheck(response.text);
      setIsNickNameAvailable(response.isNickNameAvailable);
    });
  }
  function onClickVisible() {
    setIsVisible((prev) => !prev);
  }
  function onClickDelete() {
    setIsDeleted(true);
    setPassword("");
    setPassword2("");
    console.log("here");
  }
  function receiveImageList(imageList) {
    setImageList(imageList);
  }
  function onSubmit() {
    const formData = new FormData();
    formData.append("image", image);
  }
  useEffect(() => {
    getUserDetail(userId).then((response) => {
      setUserNickName(response[0].nickName);
      setUserProfileImage(response[0].profile_img);
    });
  }, []);
  useEffect(() => {
    if (nickName === userNickName) {
      setNickNameError(`${nickName}"은(는) 현재 닉네임과 동일합니다.`);
    } else if (nickName && nickName.length > 6) {
      setNickNameError("닉네임은 최대 6자까지 설정할 수 있어요.");
    } else {
      setNickNameError("");
    }
  }, [nickName]);
  useEffect(() => {
    if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/i.test(password) && password) {
      setPasswordError("영어 소문자, 숫자 조합 8~16자리로 입력해주세요.");
    } else {
      setPasswordError("");
    }
  }, [password]);
  useEffect(() => {
    if (password !== password2 && password2) {
      setPassword2Error("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
    } else {
      setPassword2Error("");
    }
    if (password || password2) {
      setIsDeleted(false);
    } else {
      setIsDeleted(true);
    }
  }, [password, password2]);
  useEffect(() => {
    if (password && password2 && !passwordError && !password2Error) {
      setIsPasswordConfirmed("비밀번호가 일치합니다.");
    } else {
      setIsPasswordConfirmed("");
    }
  }, [passwordError, password2Error]);
  useEffect(() => {
    console.log(imageList);
  }, [imageList]);
  return (
    <form css={EditProfileBox}>
      <h3>프로필 수정</h3>

      {/* 닉네임 파트 */}
      <div css={firstWrap}>
        <div css={subTitleWrap}>
          <strong>닉네임</strong>
        </div>
        <div css={condition}>2자 이상 6자 이하 영소문자와 숫자로만 작성해주세요</div>
        <div css={doubleCheckBox}>
          <input
            name="nickName"
            type="text"
            placeholder="닉네임을 입력해주세요."
            css={inputBox}
            onChange={onChangeNickName}
            onKeyDown={onKeyDown}
          />
          <button
            css={duplicateCheck}
            type="button"
            onClick={() => {
              onClickNickNameCheck();
            }}
          >
            중복확인
          </button>
        </div>
        {nickNameError ? (
          <small css={errorWrapper({ color: "#fc0101" })}>
            <AiOutlineExclamationCircle size={12} />
            {nickNameError}
          </small>
        ) : null}
        {nickNameCheck ? (
          <small css={errorWrapper({ color: isNickNameAvailable ? "#66dd9c" : "#fc0101" })}>{nickNameCheck}</small>
        ) : null}
      </div>

      {/* 비밀번호 파트 */}
      <div css={secondWrap}>
        <div css={subTitleWrap}>
          <strong>비밀번호</strong>
        </div>
        <div css={condition}>8자 이상 16자 이하 영소문자와 숫자로만 작성해주세요</div>
        <div css={passwordWrapper}>
          <input
            name="password"
            type={isVisible ? "text" : "password"}
            placeholder="비밀번호를 입력해주세요"
            css={inputBox}
            onChange={onChangePassword}
            value={password}
          />
          {passwordError ? (
            <small css={errorWrapper({ color: "#fc0101" })}>
              <AiOutlineExclamationCircle size={12} />
              {passwordError}
            </small>
          ) : null}
          <div
            onClick={() => {
              onClickVisible();
            }}
          >
            {isVisible ? (
              <AiOutlineEye size="28" color="#66dd9c" />
            ) : (
              <AiOutlineEyeInvisible size="28" color="#66dd9c" />
            )}
          </div>
        </div>
        <div css={passwordWrapper}>
          <input
            name="password2"
            type={isVisible ? "text" : "password"}
            placeholder="비밀번호를 재입력해주세요"
            css={inputBox}
            onChange={onChangePassword2}
            value={password2}
          />
          {password2Error ? (
            <small css={errorWrapper({ color: "#fc0101" })}>
              <AiOutlineExclamationCircle size={12} />
              {password2Error}
            </small>
          ) : null}
          {isPasswordConfirmed ? (
            <small css={errorWrapper({ color: "#66dd9c" })}>
              <BsCheck2Circle />
              {isPasswordConfirmed}
            </small>
          ) : null}
          <div
            onClick={() => {
              onClickDelete();
            }}
          >
            {isDeleted ? null : <IoIosCloseCircle />}
          </div>
        </div>
      </div>

      {/* 프로필 사진 파트 */}
      <div css={thirdWrap}>
        <div css={subTitleWrap}>
          <strong>프로필 사진</strong>
        </div>
        <ProfileImageSelect sendImageList={receiveImageList} userProfileImage={userProfileImage} />
      </div>
      {/* 취소 / 완료 버튼 */}
      <div css={commitButtonWrapper}>
        <button type="button">취소</button>
        <button type="button" onClick={onSubmit}>
          완료
        </button>
      </div>
      {/* flex 3개 마지막 div */}
    </form>
  );
}

const EditProfileBox = css`
  padding: 30px 40px;
  width: 440px;
  border: 1px solid gray;
  border-radius: 10px;
  box-shadow: 1px 1px 2px;
  & > h3 {
    text-align: center;
    padding-bottom: 10px;
  }
`;

const firstWrap = css`
  display: flex;
  flex-direction: column;
  width: 100%;
`;
const subTitleWrap = css`
  font-size: 18px;
  padding-top: 20px;
`;

const condition = css`
  color: #c4c4c4;
  font-size: 12px;
`;
const doubleCheckBox = css`
  /* width: 100%; */
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
`;

const inputBox = css`
  box-sizing: border-box;
  display: block;
  width: calc(100% - 130px);
  height: 45px;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  font-size: 14px;
  background-color: #ffffff;
  outline: none;
  padding: 0 10px;
  margin: 6px 0 0;
  & ::placeholder {
    color: #c4c4c4;
  }
`;
const duplicateCheck = css`
  position: relative;
  cursor: pointer;
  width: 110px;
  font-size: 14px;
  height: 45px;
  border: 1px solid #66dd9c;
  border-radius: 5px;
  background-color: #fff;
  border: 1px solid #66dd9c;
  color: #66dd9c;
`;

const secondWrap = css`
  display: flex;
  flex-direction: column;
  width: 100%;
`;

const thirdWrap = css`
  display: flex;
  flex-direction: column;
  width: 100%;
`;

const errorWrapper = ({ color }) => css`
  font-size: 12px;
  color: ${color};
  display: flex;
  align-items: center;
  line-height: 20px;
`;

const passwordWrapper = css`
  position: relative;
  cursor: pointer;
  & > div {
    position: absolute;
    display: flex;
    align-items: center;
    right: 144px;
    height: 44px;
    top: 6px;
  }
`;

const commitButtonWrapper = css`
  display: flex;
  justify-content: center;
  padding-top: 20px;
  & > button {
    font-size: 14px;
    width: 127px;
    height: 45px;
    color: white;
    border: none;
    border-radius: 5px;
    margin: 0 10px;
  }
  & > button:nth-child(1) {
    background-color: #d7d9dc;
  }
  & > button:nth-child(2) {
    background-color: #66dd9c;
  }
`;
export default EditProfile;
