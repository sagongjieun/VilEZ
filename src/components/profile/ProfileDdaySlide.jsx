import React, { useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight, HiChevronLeft } from "react-icons/hi2";

const ProfileDdaySlide = ({ ddaySlideList }) => {
  const SIZE = ddaySlideList.length;

  let slideIndex = 0;

  function onClickPrevSlide() {
    showSlides((slideIndex -= 1));
  }

  function onClickNextSlide() {
    showSlides((slideIndex += 1));
  }

  function showSlides(current) {
    // 무한 캐러셀을 위해 양 끝 인덱스 처리
    if (current < 0) slideIndex = SIZE - 1;
    if (current === SIZE) slideIndex = 0;

    let slides = document.querySelectorAll(".slide");

    if (slides) {
      for (let i = 0; i < SIZE; i++) {
        if (i === slideIndex) slides[slideIndex].style.display = "block";
        else slides[i].style.display = "none";
      }
    }
  }

  useEffect(() => {
    showSlides(slideIndex);
  }, [ddaySlideList]);

  console.log(ddaySlideList);

  return (
    <div css={imageSlideWrapper}>
      {ddaySlideList.map((appoint, index) => (
        <div key={index} css={imageWrapper} className="slide fade">
          <div></div>
          <div css={ddayExp}>
            <div>{appoint.appointmentDto.title}</div>
            <div>
              <span>2</span>일 남았습니다.
            </div>
          </div>
          <div css={imgBack(appoint.imgPath[0].path)}></div>
        </div>
      ))}
      <div css={buttonsWrapper}>
        <button onClick={onClickPrevSlide}>
          <HiChevronLeft size="22" />
        </button>
        <button onClick={onClickNextSlide}>
          <HiChevronRight size="22" />
        </button>
      </div>
    </div>
  );
};

const imageSlideWrapper = css`
  box-sizing: border-box;
  padding-top: 7px;
  position: absolute;
  top: 0;
  width: 100%;
  height: 60%;
  position: relative;
  margin: auto;
  /* border: 1px solid #e1e2e3; */
  border-radius: 15px;

  & .fade {
    animation-name: fade;
    animation-duration: 1s;
  }

  @keyframes fade {
    from {
      opacity: 0.3;
    }
    to {
      opacity: 1;
    }
  }
`;

const imageWrapper = css`
  width: 100%;
  /* background-color: aliceblue; */
  height: 60%;
  display: flex !important;
  justify-content: space-between;
  align-items: center;

  & > div:nth-of-type(1) {
    display: none;
  }

  & > div:nth-of-type(2) {
    box-sizing: border-box;
    width: 68%;
    height: 180px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding-top: 40px;
  }
`;

const imgBack = (props) => {
  console.log(props);
  return css`
    width: 120px;
    height: 120px;
    overflow: hidden;
    border-radius: 5px;
    background-image: url(${props});
    background-size: cover;
    background-position: center center;
  `;
};

const buttonsWrapper = css`
  position: absolute;
  bottom: 24px;
  left: 0px;
  width: 110px;
  display: flex;
  justify-content: space-between;

  & > button {
    cursor: pointer;
    width: 30px;
    height: 30px;
    color: white;
    transition: 0.5s ease;
    user-select: none;
    border: none;
    opacity: 0.8;
    /* background-color: #e5e5e5; */
    border-radius: 50%;
    margin-top: -25px;
    display: flex;
    justify-content: center;
    align-items: center;

    &:hover {
      background-color: #66dd9c;
    }
  }

  & > button:nth-of-type(1) {
    left: 0;
    margin-left: 20px;
  }

  & > button:nth-of-type(2) {
    right: 0;
    margin-right: 20px;
  }
`;

const ddayExp = css`
  font-size: 24px;
  display: flex;
  /* background-color: aqua; */
  flex-direction: column;
  justify-content: flex-end;
  & > div {
    text-align: left;
    width: 100%;
    padding-left: 30px;
  }
  & > div:nth-of-type(1) {
    font-size: 18px;
    /* background-color: aliceblue; */
  }
  & > div:nth-of-type(2) {
    padding-top: 6px;
    & > span {
      font-size: 26px;
    }
  }
`;

export default ProfileDdaySlide;
