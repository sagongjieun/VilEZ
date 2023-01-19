import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight, HiChevronLeft } from "react-icons/hi2";
import baseProfile from "../../assets/images/baseProfile.png";

const ImageSlide = () => {
  const imageSlideList = [baseProfile];
  const [currentSlide, setCurrentSlide] = useState(1);

  function onClickPrevSlide() {
    setCurrentSlide(currentSlide - 1);
  }

  function onClickNextSlide() {
    setCurrentSlide(currentSlide + 1);
  }

  function showSlides(currentSlide) {
    const slides = document.getElementsByClassName("slideWrapper");

    if (currentSlide > slides.length) setCurrentSlide(1);
    if (currentSlide < 1) setCurrentSlide(slides.length);

    for (let i = 0; i < slides.length; i++) {
      slides[i].style.display = "none";
    }

    slides[currentSlide - 1].style.display = "table";
  }

  useEffect(() => {
    showSlides(currentSlide);
  }, []);

  return (
    <div css={imageSlideWrapper}>
      {imageSlideList.map((image, index) => (
        <div key={index} className="slideWrapper fade">
          <div>
            <img src={image} />
          </div>
        </div>
      ))}
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
  border: 1px solid #e1e2e3;
  border-radius: 15px;

  & > div {
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
  }

  .fade {
    animation-name: fade;
    animation-duration: 1.5s;
  }

  @keyframes fade {
    from {
      opacity: 0.4;
    }
    to {
      opacity: 1;
    }
  }

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
