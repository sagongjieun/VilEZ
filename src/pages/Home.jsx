import React, { useState, useEffect, useRef } from "react";
// import { Animated } from "react-animated-css";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";
import mainarrow from "../assets/images/mainarrow.png";
import secondbodyimg from "../assets/images/secondbodyimg.png";
import thirdbodyimg from "../assets/images/thirdbodyimg.png";
import mapimg from "../assets/images/mapimg.png";
import chatimg from "../assets/images/chatimg.png";
import homeBackground from "../assets/images/home_background.jpg";
import mockUpImage from "../assets/images/mockup3.png";
import backGradient from "../assets/images/back_gradient.png";
import backScroll from "../assets/images/back_scroll.png";

function MainBody() {
  const mainBox = useRef();
  const firstBox = useRef();
  const secondBox = useRef();
  const thirdBox = useRef();
  const [mainHeight, setMainHeight] = useState(0);
  const [firstHeight, setFirstHeight] = useState(0);
  const [secondHeight, setSecondHeight] = useState(0);
  const [thirdHeight, setThirdHeight] = useState(0);
  const [forthHeight, setForthHeight] = useState(0);

  function toNumber(styleText) {
    return Number(styleText.height.slice(0, styleText.height.length - 2));
  }

  useEffect(() => {
    const mainStyle = window.getComputedStyle(mainBox.current);
    const firstStyle = window.getComputedStyle(firstBox.current);
    const secondStyle = window.getComputedStyle(secondBox.current);
    const thirdStyle = window.getComputedStyle(thirdBox.current);
    setMainHeight(toNumber(mainStyle) + 80);
    setFirstHeight(toNumber(mainStyle) + 5000 - 200);
    setSecondHeight(toNumber(mainStyle) + toNumber(firstStyle) + 5000 - 200);
    setThirdHeight(toNumber(mainStyle) + toNumber(firstStyle) + toNumber(secondStyle) + 5000 - 200);
    setForthHeight(
      toNumber(mainStyle) + toNumber(firstStyle) + toNumber(secondStyle) + toNumber(thirdStyle) + 5000 - 200
    );
  }, []);

  const [scrollPosition, setScrollPosition] = useState(0);
  const updateScroll = () => {
    setScrollPosition(window.scrollY || document.documentElement.scrollTop);
  };

  useEffect(() => {
    window.addEventListener("scroll", updateScroll);
  }, []);

  return (
    <div>
      <div css={FirstBodyWrap} ref={mainBox}>
        <div css={slogan}>
          {/* <div>내 손 안의 작은 선행</div> */}
          <div>쉽게 빌리고 쉽게 빌려주는</div>
          <div>내 손 안의 작은 선행</div>
          <div>
            VilEZ <span>[빌리지]</span>
          </div>
          <div></div>
        </div>
        <div css={mockUp}>
          <img src={mockUpImage} alt="" />
        </div>
        <div css={ArrowBox}>
          <a href="#movebottom">
            <img src={mainarrow} alt="" />
          </a>
        </div>
      </div>
      <a id="movebottom"></a>
      <div css={blanks}>
        <div
          css={[
            test(scrollPosition),
            scrollPosition > mainHeight - 200 && scrollPosition < firstHeight + 80
              ? fixedStyle
              : scrollPosition > firstHeight + 80 || scrollPosition < mainHeight - 200
              ? hiddenStyle
              : relativeStyle(scrollPosition - mainHeight),
          ]}
        >
          <div>
            <div>잠깐 필요한 물품이 없어 곤란했던 경험.</div>
            <div>당장 쓰지 않는 물건을 빌려줘서</div>
            <div>이웃이 기뻐하는 모습.</div>
          </div>
          <div>
            <div>이 모습을 경험한다면 어떻게 될까요?</div>
            <div>이웃끼리 따뜻함도 공유되지 않을까요?</div>
          </div>
          <div>
            <div>빌리지의 이웃들과 함께</div>
            <div>공유하기를 통해 물품을 빌려주고,</div>
            <div>공유하기를 통해 물품을 빌려보세요.</div>
          </div>
        </div>
      </div>
      <div css={[FirstWrap, scrollPosition > firstHeight ? visibleBox : hiddenBox]} ref={firstBox}>
        <div css={ExplainLeft}>
          <div css={ExplainTitle}>따뜻해지는 공유 문화</div>
          <div css={ExplainContent}>필요하지만 구매하기엔 부담스럽고, </div>
          <div css={ExplainContent}>있지만 당장은 쓰지 않는 물품,</div>
          <div css={ExplainContent}>지역 사람들과 공유해봐요.</div>
        </div>
        <div css={ExplainRight}>
          <img css={[ImgHeight, firstImgMargin]} src={secondbodyimg} alt="" />
        </div>
      </div>
      <div css={[ExplainWrap, scrollPosition > secondHeight ? visibleBox : hiddenBox]} ref={secondBox}>
        <div css={[ExplainLeft, ImgHeight]}>
          <img css={ImgHeight} src={thirdbodyimg} alt="" />
        </div>
        <div css={[ExplainRight, ExplainRightCenter]}>
          <div css={ExplainTitle}>안전한 공유방식</div>
          <div css={ExplainContent}>이웃간의 약속을 통해</div>
          <div css={ExplainContent}>안전하게 물건을 빌리고, 빌려줄 수 있어요.</div>
        </div>
      </div>
      <div css={[thirdWrap, scrollPosition > thirdHeight ? visibleBox : hiddenBox]} ref={thirdBox}>
        <div css={ExplainLeft}>
          <div css={ExplainTitle}>실시간 장소선정</div>
          <div css={ExplainContent}>장소를 선정할 때 어려웠던 경험,</div>
          <div css={ExplainContent}>실시간으로 장소를 같이</div>
          <div css={ExplainContent}>선택할 수 있게 빌리지가 도와줄게요.</div>
        </div>
        <div css={ExplainRight}>
          <img src={mapimg} alt="" />
        </div>
      </div>
      <div css={[ExplainWrap, scrollPosition > forthHeight ? visibleBox : hiddenBox]}>
        <div css={ExplainLeft}>
          <img css={ImgHeight} src={chatimg} alt="" />
        </div>
        <div css={[ExplainRight, ExplainRightCenter]}>
          <div css={ExplainTitle}>다른 곳 갈 필요 없어요</div>
          <div css={ExplainContent}>빌리지 안에서 약속을 잡고,</div>
          <div css={ExplainContent}>화상으로 물품 상태를 확인해서</div>
          <div css={ExplainContent}>귀찮음을 없애드려요</div>
        </div>
      </div>
    </div>
  );
}

const appear = keyframes`
  0% {
    opacity: 0;
    transform: translateY(15px);
  }
  100% {
    opacity: 1;
    transform: translateY(0px);
  }
`;

const FirstBodyWrap = css`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: calc(100vh);
  /* background-image: linear-gradient(90deg, #66dd9c50, #66dd9c50, #66dd9c90); */
  background-image: url(${backGradient});
  overflow-x: hidden;
  background-size: cover;
  background-position: center center;
`;

const slogan = css`
  box-sizing: border-box;
  width: 50%;
  font-size: 50px;
  color: #000;
  padding-left: 120px;
  & > div {
    opacity: 0;
    font-family: "GmarketSansMedium";
    animation-name: ${appear};
    animation-duration: 1.5s;
    animation-fill-mode: forwards;
  }
  & > div:nth-of-type(1) {
    font-size: 24px;
  }
  & > div:nth-of-type(2) {
    font-size: 42px;
    letter-spacing: -3px;
    animation-delay: 0.4s;
  }
  & > div:nth-of-type(3) {
    font-family: Pretendard-Regular;
    font-weight: 900;
    font-size: 160px;
    color: #66dd9c;
    animation-delay: 0.9s;
    & > span {
      display: inline-block;
      transform: translateX(-20px);
      width: fit-content;
      color: #888;
      font-size: 24px;
      font-family: "GmarketSansMedium";
    }
  }
  & > div:nth-of-type(4) {
    font-family: Pretendard-Regular;
  }
`;

const mockUp = css`
  width: 40%;
  > img {
    width: 150%;
    height: 100%;
    object-fit: contain;
  }
`;

const floating = keyframes`
  0% {
      transform: translateY(0);
  }
  50% {
      transform: translateY(-15px);
  }
  100% {
      transform: translateY(0);
  }
`;

const ArrowBox = css`
  position: absolute;
  left: calc(50% - 40px);
  bottom: 0;
  width: 80px;
  text-align: center;
  margin-top: 100px;
  cursor: pointer;
  animation: slideInDown;
  animation: ${floating} 2s ease infinite;
  & img {
    width: 100%;
    object-fit: contain;
  }
`;

const FirstWrap = css`
  display: flex;
  margin-top: 186px;
  padding-left: 200px;
  padding-right: 200px;
  justify-content: space-between;
  height: 700px;
`;

// SecondBody

const ExplainWrap = css`
  display: flex;
  padding-left: 200px;
  padding-right: 200px;
  justify-content: space-between;
  height: 800px;
  font-family: "GmarketSansMedium";
`;

const ExplainLeft = css`
  height: 584px;
  font-family: "GmarketSansMedium";
`;

const ExplainRight = css`
  width: 50%;
  height: 584px;
  font-family: "GmarketSansMedium";
`;

const ExplainRightCenter = css`
  display: flex;
  justify-content: center;
  flex-direction: column;
  text-align: right;
  font-family: "GmarketSansMedium";
`;

const ExplainTitle = css`
  font-size: 40px;
  color: #66dd9c;
  margin-bottom: 20px;
  font-family: "GmarketSansMedium";
`;

const ExplainContent = css`
  font-size: 28px;
  padding-bottom: 20px;
  font-family: "GmarketSansMedium";
`;

const ImgHeight = css`
  height: 584px;
`;

const firstImgMargin = css`
  width: 580px;
`;

const thirdWrap = css`
  display: flex;
  padding-left: 200px;
  padding-right: 200px;
  justify-content: space-between;
  height: 600px;
`;

const hiddenBox = css`
  opacity: 0;
  transition: all 1s;
  transform: translateY(40px);
`;
const visibleBox = css`
  opacity: 1;
  transition: all 1s;
  transform: translateY(0px);
`;

const blanks = css`
  height: 5000px;
  background-image: url(${backScroll});
  background-size: 100% 100%;
`;

const test = (scroll) => {
  return css`
    /* position: relative; */
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100vh;
    background-image: url(${homeBackground});
    background-size: cover;
    background-color: bisque;
    opacity: ${(scroll / 5 - 130) / 100};
    > div {
      position: relative;
      width: 100%;
      height: calc(100vh / 5);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 30px;
      font-weight: bold;
    }
    ::before {
      position: absolute;
      display: block;
      content: "";
      background-color: rgba(0, 0, 0, 0.8);
      opacity: ${(scroll / 5 - 300) / 100};
      width: 100%;
      height: 100vh;
    }
    ::after {
      position: absolute;
      display: block;
      content: "";
      background-color: #fff;
      opacity: ${(scroll / 5 - 900) / 100};
      width: 100%;
      height: 100vh;
    }
    > div:nth-of-type(1) {
      opacity: ${(scroll / 5 - 400) / 100};
    }
    > div:nth-of-type(2) {
      opacity: ${(scroll / 5 - 550) / 100};
    }
    > div:nth-of-type(3) {
      opacity: ${(scroll / 5 - 700) / 100};
    }
  `;
};

const fixedStyle = css`
  position: sticky;
  top: 0;
`;
const relativeStyle = (scroll) => {
  return css`
    position: fixed;
    bottom: ${scroll - 1200};
    bottom: 0;
  `;
};

const hiddenStyle = css`
  visibility: hidden;
`;

export default MainBody;
