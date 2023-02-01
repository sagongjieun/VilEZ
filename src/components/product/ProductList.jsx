import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

import DivideLine from "../../components/common/DivideLine";
import ProductCardView from "../../components/product/ProductCardView";
import InputBox from "../common/InputBox";

import ProductCategory from "./ProductCategory";
import { useState } from "react";

const ProductList = () => {
  const [category, setCategory] = useState("");
  const [isClick, setIsClick] = useState(false);
  const [search, setSearch] = useState("");
  function receiveCategory() {
    setCategory(category);
  }
  function onClickSeePossible() {
    setIsClick(!isClick);
  }
  function onChangeSearch(e) {
    setSearch(e.target.value);
    console.log(search);
  }
  // function onKeyDownSearch(e) {
  //   if (e.key === "Enter") {
  //     console.log("드가자");
  //   }
  // }
  return (
    <div css={topWrap}>
      <h2>물품 공유 목록</h2>
      <div css={filterWrap}>
        <div css={filterLeftWrap}>
          <ProductCategory isMain={false} sendCategory={receiveCategory} />
        </div>
        <div css={filterRighWrap}>
          <div css={searchWrap}>
            <InputBox
              useMainList={true}
              onChangeValue={(e) => onChangeSearch(e)}
              // value={search}
              // type="text"
              placeholder="필요한 물품을 검색해보세요."
              // onKeyDown={onKeyDownSearch}
            />
            <img src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png" />
            <button>검색</button>
          </div>
          <div onClick={onClickSeePossible} css={isClick ? possibleWrap : unPossibleWrap}>
            공유가능한 물품만 보기
          </div>
        </div>
      </div>
      <DivideLine />

      <div css={buttonDiv}>
        <button css={buttonWrap}>물품 등록</button>
      </div>
      <div css={contentWrap}>
        <div>
          <ProductCardView />
        </div>
        <div>
          <ProductCardView />
        </div>
        <div>
          <ProductCardView />
        </div>
        <div>
          <ProductCardView />
        </div>
        <div>
          <ProductCardView />
        </div>
      </div>
    </div>
  );
};
const topWrap = css`
  padding-left: 200px;
  padding-right: 200px;
  margin-top: 70px;
`;
const filterWrap = css`
  display: flex;
  justify-content: space-between;
  /* padding-bottom: 30px; */
  margin-top: 35px;
  position: relative;
`;

const filterLeftWrap = css`
  font-size: 25px;
  display: flex;
`;

const filterRighWrap = css`
  display: flex;
  width: 60%;
  justify-content: right;
  align-items: center;
`;

const searchWrap = css`
  width: 50%;
  position: relative;
  & > img {
    position: absolute;
    width: 15px;
    top: 11px;
    left: 3px;
  }
  & > button {
    position: absolute;
    width: 30px;
    top: 9px;
    right: 10px;
    border: none;
    background-color: white;
    color: #66dd9c;
  }
`;

const possibleWrap = css`
  margin-left: 15px;
  cursor: pointer;
`;

const unPossibleWrap = css`
  margin-left: 15px;
  color: #66dd9c;
  cursor: pointer;
  font-weight: bold;
  box-sizing: border-box;
  background-color: white;
  /* border: 1px solid gray;
  border-radius: 10px; */
`;

// const arrayWrap = css`
//   box-sizing: border-box;
//   display: block;
//   width: 200px;
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   height: 55px;

//   border-radius: 5px;
//   font-size: 15px;
//   background-color: #ffffff;
//   outline: none;
// `;

const buttonDiv = css`
  width: 100%;
  /* display: inline-block; */
  display: flex;
  justify-content: flex-end;
`;

const buttonWrap = css`
  width: 10%;
  position: relative;
  margin-top: 25px;
  margin-bottom: 40px;
  cursor: pointer;
  display: block;
  height: 35px;
  border: none;
  border-radius: 5px;
  font-size: 14px;
  background-color: #66dd9c;
  color: white;
`;

const contentWrap = css`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 10px;
  & > div {
    margin-bottom: 70px;
  }
`;
export default ProductList;
