import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import bazzie from "../../assets/images/bazzie.jfif";
import { useState } from "react";
function EditProfile() {
  const [userId, setUserId] = useState("");
  const [userPassWord, setUserPassWord] = useState("");
  const onChangeId = (e) => {
    setUserId(e.target.value);
  };
  const onChangePassWord = (e) => {
    setUserPassWord(e.target.value);
  };
  const onClickImageChange = (e) => {
    e.preventDefault();
    const imgInput = document.getElementById("imgInput");
    imgInput.click();
  };
  return (
    <form css={EditProfileBox}>
      <h2>프로필 수정</h2>

      {/* 닉네임 파트 */}
      <div css={firstWrap}>
        <div css={nickNameWrap}>
          <strong>닉네임</strong>
        </div>
        <div css={nickNameCondition}>(2자 이상 6자 이하 영소문자와 숫자로만 작성해주세요)</div>
        <div css={doubleCheckBox}>
          <input
            type="text"
            placeholder="닉네임을 입력해주세요."
            css={editProfileInputBox}
            onChange={onChangeId}
            value={userId}
          />
          <button css={duplicateCheck}>중복확인</button>
        </div>
        <div css={possibleNickWrap}>사용가능한 닉네임입니다.</div>
      </div>

      {/* 비밀번호 파트 */}
      <div css={secondWrap}>
        <div css={passwordWrap}>
          <strong>비밀번호</strong>
        </div>
        <div css={passwordCondition}>8자 이상 16자 이하 영소문자와 숫자로만 작성해주세요</div>
        <div>
          <input
            type="text"
            placeholder="비밀번호를 입력해주세요"
            css={passwordBox}
            onChange={onChangePassWord}
            value={userPassWord}
          />
        </div>
        <div>
          <input type="text" placeholder="비밀번호를 재입력해주세요" css={passwordBox} />
        </div>
      </div>

      {/* 프로필 사진 파트 */}
      <div css={thirdWrap}>
        <div css={imgWrap}>
          <strong>프로필 사진</strong>
        </div>
        <div css={imgContentWrap}>
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
        </div>
      </div>
      {/* 취소 / 완료 버튼 */}
      <div>
        <button css={cancelButton}>취소</button>
        <button css={completeButton}>완료</button>
      </div>
      {/* flex 3개 마지막 div */}
    </form>
  );
}
const EditProfileBox = css`
  display: flex;
  padding-left: 5px;
  padding-top: 5px;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 800px;
  width: 500px;
  border: 1px solid gray;
  border-radius: 20px;
  box-shadow: 1px 1px 2px;
`;

const firstWrap = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-left: 15px;
`;
const nickNameWrap = css`
  font-size: 25px;
`;

const nickNameCondition = css`
  color: #c4c4c4;
  font-size: 10px;
`;
const doubleCheckBox = css`
  width: 100%;
  display: flex;
`;

const editProfileInputBox = css`
  box-sizing: border-box;
  display: block;
  width: 60%;
  height: 35px;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  font-size: 13px;
  background-color: #ffffff;
  outline: none;
  padding: 0 10px;
  & ::placeholder {
    color: #c4c4c4;
  }
`;
const duplicateCheck = css`
  cursor: pointer;
  width: 110px;
  justify-content: center;
  align-items: center;
  height: 30px;
  border: 1px solid #66dd9c;
  border-radius: 5px;
  background-color: #fff;
  border: 1px solid #66dd9c;
  color: #66dd9c;
  margin-left: 20px;
  margin-top: 4px;
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
  margin-left: 15px;
`;
const passwordWrap = css`
  font-size: 25px;
`;
const passwordCondition = css`
  color: #c4c4c4;
  font-size: 10px;
`;
const passwordBox = css`
  box-sizing: border-box;
  display: block;
  width: 60%;
  height: 35px;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  font-size: 13px;
  background-color: #ffffff;
  outline: none;
  padding: 0 10px;
  margin-top: 10px;
  & ::placeholder {
    color: #c4c4c4;
  }
`;

const thirdWrap = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-left: 15px;
`;

const imgWrap = css`
  font-size: 25px;
`;

const imgContentWrap = css`
  display: flex;
`;

const FirstImgContent = css`
  width: 135px;
`;

const profileImgWrap = css`
  margin-top: 30px;
  border-radius: 100%;
  width: 65%;
`;
const inputWrap = css`
  display: none;
`;

const modifyButtonWrap = css`
  display: flex;
  align-items: flex-end;
`;

const modifyButton = css`
  width: 80px;
  height: 35px;
  font-size: 14px;
  color: white;
  background-color: #66dd9c;
  border-radius: 5px;
  border: none;
  margin-right: 20px;
  cursor: pointer;
`;

const deleteButton = css`
  width: 80px;
  height: 35px;
  font-size: 14px;
  color: white;
  background-color: #c82333;
  border-radius: 5px;
  border: none;
`;

const cancelButton = css`
  font-size: 14px;
  width: 127px;
  height: 45px;
  background-color: #d7d9dc;
  color: white;
  border: none;
  border-radius: 5px;
  margin-right: 50px;
`;

const completeButton = css`
  font-size: 14px;
  width: 127px;
  height: 45px;
  background-color: #66dd9c;
  color: white;
  border: none;
  border-radius: 5px;
  margin-left: 50px;
`;
export default EditProfile;
