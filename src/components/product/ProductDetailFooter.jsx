import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight } from "react-icons/hi2";

const ProductDetailFooter = () => {
  function onClickReportArticle() {
    /** 모달 띄우기 */
    alert("정말 신고하시겠습니까?");
  }
  function onClickShareArticle() {
    // 클립보드에 url 복사하기
    alert("공유하시겠습니까?");
  }

  function onClickMoveToTop() {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  return (
    <div css={menusWrapper}>
      <div onClick={onClickReportArticle}>
        <span>이 게시물 신고하기</span>
        <HiChevronRight size="22" />
      </div>
      <div onClick={onClickShareArticle}>
        <span>이 게시물 공유하기</span>
        <HiChevronRight size="22" />
      </div>
      <div onClick={onClickMoveToTop}>
        <span>맨 위로 이동하기</span>
        <HiChevronRight size="22" />
      </div>
    </div>
  );
};

const menusWrapper = css`
  display: flex;
  flex-direction: column;
  margin-top: 30px;

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

export default ProductDetailFooter;
