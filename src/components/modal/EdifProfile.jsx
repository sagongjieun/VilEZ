import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

function EditProfile() {
  return (
    <div css={EditProfileBox}>
      <h2>프로필 수정</h2>
      <div css={firstWrap}>
        <div css={nickNameWrap}>
          <strong>닉네임</strong>
        </div>
        <div css={nickNameCondition}>(8자 이상 16자 이하 영소문자와 숫자로만 작성해주세요)</div>
        <div>
          <input type="text" placeholder="닉네임을 입력해주세요." css={doubleCheckBox} />
          <button css={duplicateCheck}>중복확인</button>
        </div>
        <div>사용가능한 닉네임입니다.</div>
      </div>

      <div>
        <div>비밀번호</div>
        <div>8자 이상 16자 이하 영소문자와 숫자로만 작성해주세요</div>
        <div>
          <input type="text" placeholder="비밀번호를 입력해주세요." />
        </div>
        <div>
          <input type="text" placeholder="비밀번호를 재입력해주세요." />
        </div>
      </div>

      <div>
        <div>프로필 사진</div>
        <div>
          <button>변경</button>
          <button>삭제</button>
        </div>
      </div>

      <div>
        <button>취소</button>
        <button>완료</button>
      </div>
      {/* flex 3개 마지막 div */}
    </div>
  );
}
const EditProfileBox = css`
  display: flex;
  padding-left: 5px;
  padding-top: 5px;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 600px;
  width: 500px;
  border: 1px solid gray;
  border-radius: 20px;
  box-shadow: 1px 1px 2px;
`;

const firstWrap = css`
  display: flex;
  flex-direction: column;
`;
const nickNameWrap = css`
  font-size: 25px;
`;
const duplicateCheck = css`
  cursor: pointer;
  height: 20px;
  border: 1px solid #66dd9c;
  border-radius: 5px;
  background-color: #fff;
  padding: 0 20px;
  border: 1px solid #66dd9c;
  color: #66dd9c;
  margin-left: 20px;
`;

const nickNameCondition = css`
  color: #c4c4c4;
  font-size: 10px;
`;
const doubleCheckBox = css`
  width: 250px;
  height: 30px;
`;
export default EditProfile;
