import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight } from "react-icons/hi2";

const ProductDetailFooter = () => {
  return (
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
  );
};

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

export default ProductDetailFooter;
