import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

import DivideLine from "../../components/common/DivideLine";
import ProductCardView from "../../components/product/ProductCardView";
const ProductList = () => {
  return (
    <div css={topWrap}>
      <h2>물품 공유 목록</h2>
      <div css={filterWrap}>
        <div>전체</div>
        <div>
          <span>
            <input type="text" placeholder="필요한 물품을 검색해보세요." />
          </span>
          <span>최신순</span>
        </div>
      </div>
      <DivideLine />
      <div css={buttonWrap}>
        <button>물품등록</button>
      </div>
      <div>
        <ProductCardView />
      </div>
    </div>
  );
};
const topWrap = css`
  padding-left: 200px;
  padding-right: 200px;
`;
const filterWrap = css`
  display: flex;
  justify-content: space-between;
`;

const buttonWrap = css`
  width: 100%;
  position: relative;
`;
export default ProductList;
