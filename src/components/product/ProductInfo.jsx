import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import image from "../../assets/images/firstbodyimage.png";
import { HiLocationMarker, HiCalendar, HiHeart } from "react-icons/hi";

const ProductInfo = () => {
  return (
    <div css={productInfoWrapper}>
      <div>
        <img src={image} />
      </div>
      <div>
        <div>
          <h3>맥북 에어 M1 공유해요</h3>
          {/* Link로 변경 */}
          <a>글 보러가기</a>
        </div>
        <span>
          <HiLocationMarker /> 진평동
        </span>
        <span>
          <HiCalendar /> 2023.01.23 - 2023.02.22
        </span>
        <span>
          <HiHeart /> 25
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
