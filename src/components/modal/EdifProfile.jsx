import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useState } from "react";
// import bazzie from "../../assets/images/bazzie.jfif";
import ProfileImageSelect from "../profile/ProfileImageSelect";

function EditProfile() {
  const userId = localStorage.getItem("id");
  const [nickName, setNickName] = useState("");
  const [nickNameCheck, setNickNameCheck] = useState("");
  const [isNickNameAvailable, setIsNickNameAvailable] = useState(false);
  const [password, setPassword] = useState("");
  const [password2, setPassword2] = useState("");
  const [imageList, setImageList] = useState([]);
  function onChangeNickName(event) {
    setNickName(event.target.value);
  }
  function onChangePassword(event) {
    setPassword(event.target.value);
  }
  function onChangePassword2(event) {
    setPassword2(event.target.value);
  }
  // function onClickImageChange(event) {
  //   event.preventDefault();
  //   const imgInput = document.getElementById("imgInput");
  //   imgInput.click();
  // }
  function receiveImageList(imageList) {
    setImageList(imageList);
  }
  function onSubmit() {
    // console.log(errors, handleChange, handleSubmit);
    console.log(nickName, nickNameCheck, isNickNameAvailable, password, password2, imageList, userId);
    setNickNameCheck("");
    setIsNickNameAvailable("");
  }
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
          />
          <button css={duplicateCheck}>중복확인</button>
        </div>
        <div css={possibleNickWrap}>사용가능한 닉네임입니다.</div>
      </div>

      {/* 비밀번호 파트 */}
      <div css={secondWrap}>
        <div css={subTitleWrap}>
          <strong>비밀번호</strong>
        </div>
        <div css={condition}>8자 이상 16자 이하 영소문자와 숫자로만 작성해주세요</div>
        <div>
          <input
            name="password"
            type="text"
            placeholder="비밀번호를 입력해주세요"
            css={inputBox}
            onChange={onChangePassword}
          />
        </div>
        <div>
          <input
            name="password2"
            type="text"
            placeholder="비밀번호를 재입력해주세요"
            css={inputBox}
            onChange={onChangePassword2}
          />
        </div>
      </div>

      {/* 프로필 사진 파트 */}
      <div css={thirdWrap}>
        <div css={subTitleWrap}>
          <strong>프로필 사진</strong>
        </div>
        {/* <div css={imgContentWrap}>
          <div css={FirstImgContent}>
            <img src={bazzie} alt="" css={profileImgWrap} />
          </div>
          <div css={modifyButtonWrap}>
            <input type="file" id="imgInput" accept=".jpg,.jpeg,.png" css={inputWrap} />
            <button css={modifyButton} onClick={onClickImageChange}>
              변경
            </button>
            <button css={deleteButton}>삭제</button>
          </div>
        </div> */}
        <ProfileImageSelect sendImageList={receiveImageList} />
      </div>
      {/* 취소 / 완료 버튼 */}
      <div css={commitButtonWrapper}>
        <button>취소</button>
        <button onClick={onSubmit}>완료</button>
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
  align-items: center;
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
  margin: 5px 0;
  & ::placeholder {
    color: #c4c4c4;
  }
`;
const duplicateCheck = css`
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

const possibleNickWrap = css`
  font-size: 13px;
  color: #66dd9c;
  margin-top: 4px;
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

// const imgContentWrap = css`
//   display: flex;
// `;

// const FirstImgContent = css`
//   width: 135px;
// `;

// const profileImgWrap = css`
//   border-radius: 100%;
//   width: 65%;
//   margin-top: 10px;
// `;
// const inputWrap = css`
//   display: none;
// `;

// const modifyButtonWrap = css`
//   display: flex;
//   align-items: flex-end;
// `;

// const modifyButton = css`
//   width: 80px;
//   height: 35px;
//   font-size: 14px;
//   color: white;
//   background-color: #66dd9c;
//   border-radius: 5px;
//   border: none;
//   margin-right: 20px;
//   cursor: pointer;
// `;

// const deleteButton = css`
//   width: 80px;
//   height: 35px;
//   font-size: 14px;
//   color: white;
//   background-color: #c82333;
//   border-radius: 5px;
//   border: none;
// `;

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
