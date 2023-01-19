import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

function EditProfile() {
  return (
    <div css={EditProfileBox}>
      <div css={firstWrap}>
        <div>닉네임</div>
        <div>8자 이상 16자 이하 영소문자와 숫자로만 작성해주세요.</div>
        <div>
          <input type="text" placeholder="닉네임을 입력해주세요." />
          <button>중복확인</button>
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
  flex-direction: column;
  height: 1000px;
  width: 600px;
  box-shadow: 1px 1xp 5px;
`;

const firstWrap = css`
  display: flex;
  flex-direction: column;
`;
export default EditProfile;
