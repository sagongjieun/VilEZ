import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import DivideLine from "../../components/common/DivideLine";
import InputBox from "../common/InputBox";
import ProductCategory from "./ProductCategory";
import { useState, useEffect } from "react";
import NoProductList from "./NoProductList";
import image from "../../assets/images/mainBackgroundImage.png";
import { getShareArticleList } from "../../api/share";
import { HiLocationMarker } from "react-icons/hi";
import { HiCalendar } from "react-icons/hi";
import { HiHeart } from "react-icons/hi";
import { useLocation } from "react-router-dom";
import { getAskArticleList } from "../../api/ask";

const ProductList = () => {
  const [category, setCategory] = useState("");
  const [isClick, setIsClick] = useState(false);
  const [search, setSearch] = useState("");
  const [getArticle, setArticles] = useState([]);
  const pathname = useLocation().pathname;
  useEffect(() => {
    const type = pathname.includes("share") ? 2 : 1;

    type === 1
      ? getAskArticleList(category, 0, 200, 0, "").then((res) => {
          const data = res;
          setArticles(data);
        })
      : getShareArticleList(category, 0, 200, 0, "").then((res) => {
          const data = res;
          // console.log(data);
          setArticles(data);
        });
  }, [category]);
  // props에서 받아온 값이 newCategory에 들어감
  // setCategory에 넘어온 값을 입력
  function receiveCategory(newCategory) {
    setCategory(newCategory);
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
      <div css={contentWrap}>
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

        <div css={relatedProductWrapper}>
          {getArticle.map((article, idx) => (
            <div key={idx}>
              <div css={thumbnailWrapper}>
                <img src={image} />
              </div>
              <div css={infoWrapper}>
                <div>
                  <span>{article.shareListDto.title}</span>
                  <small>1시간 전</small>
                </div>
                <div>
                  <small>
                    <HiLocationMarker />
                    {article.shareListDto.area}
                  </small>
                  <small>
                    <HiCalendar />
                    {article.shareListDto.startDay} ~ {article.shareListDto.endDay}
                  </small>
                  <small>
                    <HiHeart />
                    {article.listCnt}
                  </small>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
      <div css={NoProductWrap}>
        <NoProductList />
      </div>
    </div>
  );
};

const topWrap = css`
  padding-left: 200px;
  padding-right: 200px;
  margin-top: 70px;
  height: 100%;
`;
const contentWrap = css``;

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
`;

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
  font-size: 12px;
  background-color: #66dd9c;
  color: white;
`;

const relatedProductWrapper = css`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 50px;
  width: 100%;
  height: 100%;
  margin-right: 50px;
  border-radius: 10px;

  cursor: pointer;

  & > div {
    margin-bottom: 70px;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  }
`;

const thumbnailWrapper = css`
  height: 170px;
  background: #d9d9d9;
  border-radius: 10px 10px 0 0;

  & > img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    border-radius: 10px 10px 0 0;
  }
`;

const infoWrapper = css`
  max-height: 80px;
  padding: 10px;
  background: #ffffff;
  border-radius: 0 0 10px 10px;

  & small {
    color: #8a8a8a;
  }

  & > div:nth-of-type(1) {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 5px;
  }

  & > div:nth-of-type(2) {
    display: flex;
    flex-direction: row;

    & > small {
      margin-right: 10px;
      display: flex;
      flex-direction: row;
      align-items: center;

      & > svg {
        margin-right: 3px;
      }
    }
  }
`;

const NoProductWrap = css`
  height: 250px;
`;

export default ProductList;
