import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight, HiChevronLeft } from "react-icons/hi2";
import firstbodyimage from "../../assets/images/firstbodyimage.png";
import secondbodyimg from "../../assets/images/secondbodyimg.png";

const ImageSlide = () => {
  const imageSlideList = [firstbodyimage, secondbodyimg];
  const COUNT = imageSlideList.length;
  const [currentSlide, setCurrentSlide] = useState(0);
  const [moveSlide, setMoveSlide] = useState(0);

  function onClickPrevSlide() {
    setCurrentSlide(currentSlide - 1);
    setMoveSlide(-(currentSlide * 100) / COUNT);
  }

  function onClickNextSlide() {
    setCurrentSlide(currentSlide + 1);
    setMoveSlide((currentSlide * 100) / COUNT);
  }

  return (
    <div css={imageSlideWrapper}>
      <div
        css={css`
          display: flex;
          width: calc(100% * ${COUNT});
          height: 100%;
          transition: all 0.5s;
          transform: translateX(-${moveSlide}%);
          & > div {
            width: calc(100% / ${COUNT});
            height: 100%;
          }
        `}
      >
        {imageSlideList.map((image, index) => (
          <div
            key={index}
            css={css`
              background-image: url(${image});
              background-size: cover;
            `}
          ></div>
        ))}
      </div>
      <button onClick={onClickPrevSlide}>
        <HiChevronLeft size="18" />
      </button>
      <button onClick={onClickNextSlide}>
        <HiChevronRight size="18" />
      </button>
    </div>
  );
};

const imageSlideWrapper = css`
  width: 100%;
  height: 450px;
  position: relative;
  margin: auto;
  overflow: hidden;
  border: 1px solid #e1e2e3;
  border-radius: 15px;

  /* & > div {
    width: 100%;
    height: 100%;
    text-align: center;
    display: none;

    & > div {
      display: table-cell;
      vertical-align: middle;

      & > img {
        object-fit: contain;
        width: 80%;
        height: 80%;
      }
    }
  } */

  & > button:nth-of-type(1) {
    all: unset;
    border: 1px solid #66dd9c;
    padding: 0.5rem 2em;
    color: #66dd9c;
    border-radius: 100px;
    cursor: pointer;
    position: absolute;
    top: 225px;
    left: 20px;
    width: 20px;
    height: 20px;

    &:hover {
      transition: all 0.3s ease-in-out;
      background-color: #66dd9c;
      color: #ffffff;
    }
  }

  & > button:nth-of-type(2) {
    all: unset;
    border: 1px solid #66dd9c;
    padding: 0.5rem 2em;
    color: #66dd9c;
    border-radius: 100px;
    cursor: pointer;
    position: absolute;
    width: 20px;
    height: 20px;
    top: 225px;
    right: 20px;

    &:hover {
      transition: all 0.3s ease-in-out;
      background-color: #66dd9c;
      color: #ffffff;
    }
  }
`;

export default ImageSlide;
