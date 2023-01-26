import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiLocationMarker, HiCalendar, HiHeart } from "react-icons/hi";
import { Link } from "react-router-dom";

const ProductInfo = ({ infos }) => {
  return (
    <div css={productInfoWrapper}>
      <div>
        <img src={infos.thumbnailImage.path} alt="물품 대표사진" />
      </div>
      <div>
        <div>
          <h3>{infos.title}</h3>
          <Link to={`/product/detail/${infos.boardId}`}>글 보러가기</Link>
        </div>
        <span>
          <HiLocationMarker /> {infos.location}
        </span>
        <span>
          <HiCalendar /> {infos.startDay} - {infos.endDay}
        </span>
        <span>
          <HiHeart /> {infos.bookmarkCnt}
        </span>
      </div>
    </div>
  );
};

const productInfoWrapper = css`
  display: flex;
  flex-direction: row;

  & > div:nth-of-type(1) {
    width: 275px;
    height: 165px;
    margin-right: 25px;
    border: 1px solid #e1e2e3;
    border-radius: 15px;

    & > img {
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
  }

  & > div:nth-of-type(2) {
    display: flex;
    flex-direction: column;

    & > div {
      display: flex;
      flex-direction: row;
      align-items: flex-end;
      margin-bottom: 20px;

      & > h3 {
        margin-right: 15px;
      }

      & > a {
        color: #8a8a8a;
        cursor: pointer;
      }
    }

    & > span {
      margin-bottom: 15px;
      color: #aeaeae;
      display: flex;
      flex-direction: row;
      align-items: center;

      & > svg {
        margin-right: 8px;
      }
    }
  }
`;

export default ProductInfo;
