import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import DivideLine from "../common/DivideLine";
import bookmark from "../../assets/images/bookmark.png";
import { HiChevronRight } from "react-icons/hi2";
import baseProfile from "../../assets/images/baseProfile.png";
import MiddleWideButton from "../button/MiddleWideButton";

const ProductDetail = () => {
  return (
    <div css={wrapper}>
      <div css={headerWrapper}>
        <div css={headerLeftSectionWrapper}>
          <span>맥북 에어 M1 공유해요</span>
          <span>전자기기</span>
          <small>1시간 전</small>
        </div>
        <div css={headerRightSectionWrapper}>
          {/* Link로 변경 */}
          <a>목록</a>
          <div>
            <img src={bookmark} alt="bookmark" />
            <small>25</small>
          </div>
        </div>
      </div>

      <DivideLine />

      <div css={contentsWrapper}>
        <div css={imageSlideWrapper}>이미지</div>
        <div css={nickNameAndChatWrapper}>
          <div css={nickNameWrapper}>
            <img src={baseProfile} alt="baseProfile" />
            <div>
              <span>닉네임</span>
              <span>구미시 진평동</span>
            </div>
            <span>😀</span>
          </div>
          <div css={chatWrapper}>
            <img src={bookmark} alt="bookmark" />
            <MiddleWideButton text="채팅하기" />
          </div>
        </div>
        <div css={contentWrapper}>
          <h3>설명</h3>
          <div>
            <span>이것은 설명입니다.</span>
          </div>
        </div>
        <div css={hopeDateWrapper}>
          <h3>희망 공유 기간</h3>
          <div>
            <span>2023.01.11 - 2023.02.20</span>
          </div>
        </div>
        <div css={hopeAreaWrapper}>
          <div>
            <h3>희망 공유 장소</h3>
            <span>경상북도 구미시 임수동 94-1</span>
          </div>
          <div id="map"></div>
        </div>
      </div>

      <DivideLine />

      <div>관련 게시글</div>

      <DivideLine />

      <div css={menusWrapper}>
        <div>
          <span>이 게시물 신고하기</span>
          <HiChevronRight size="22" />
        </div>
        <div>
          <span>이 게시물 공유하기</span>
          <HiChevronRight size="22" />
        </div>
        <div>
          <span>맨 위로 이동하기</span>
          <HiChevronRight size="22" />
        </div>
      </div>
    </div>
  );
};

const wrapper = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
`;

/* HeaderWrapper */

const headerWrapper = css`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 5px;
`;

const headerLeftSectionWrapper = css`
  & > span:nth-of-type(1) {
    font-size: 30px;
    font-weight: bold;
    margin-right: 20px;
  }

  & > span:nth-of-type(2) {
    color: #66dd9c;
    font-weight: bold;
    margin-right: 20px;
  }

  & > small {
    color: #8a8a8a;
  }
`;

const headerRightSectionWrapper = css`
  display: flex;
  flex-direction: row;

  & > a {
    color: #8a8a8a;
    cursor: pointer;
  }

  & > div {
    display: flex;
    flex-direction: row;
    align-items: center;

    & img {
      margin-left: 20px;
      margin-right: 5px;
      width: 25px;
      height: 20px;
    }
  }
`;

/* ContentsWrapper */

const contentsWrapper = css`
  display: flex;
  flex-direction: column;
  padding: 60px 20px;
`;

const imageSlideWrapper = css`
  margin-bottom: 60px;
`;

const nickNameAndChatWrapper = css`
  margin-bottom: 60px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const nickNameWrapper = css`
  display: flex;
  flex-direction: row;
  align-items: center;

  & img {
    width: 90px;
    height: 90px;
    margin-right: 20px;
  }

  & > div {
    display: flex;
    flex-direction: column;
    margin-right: 20px;
  }

  & > div > span:nth-of-type(1) {
    margin-bottom: 10px;
    font-weight: bold;
  }

  & > span {
    font-size: 25px;
  }
`;

const chatWrapper = css`
  display: flex;
  flex-direction: row;
  align-items: center;

  & img {
    width: 35px;
    height: 30px;
    margin-right: 20px;
    cursor: pointer;
  }

  & button {
    margin-top: 0;
  }
`;

const contentWrapper = css`
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;

  & div {
    margin-top: 20px;
    max-width: 100%;
    height: 246px;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    padding: 30px;
    overflow-y: scroll; // CSS 변경 필요
  }
`;

const hopeDateWrapper = css`
  margin-bottom: 60px;

  & div {
    margin-top: 20px;
    width: 260px;
    height: 54px;
    background: #ffffff;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
`;

const hopeAreaWrapper = css`
  display: flex;
  flex-direction: column;

  & > div:nth-of-type(1) {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-between;

    & span {
      color: #8a8a8a;
    }
  }
`;

/* MenusWrapper */

const menusWrapper = css`
  display: flex;
  flex-direction: column;

  & > div {
    width: 180px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    margin: 20px 0;
  }
`;

export default ProductDetail;
