import React, { useState, useEffect, useRef } from "react";
import { Animated } from "react-animated-css";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";
import mainBackgroundImage from "../assets/images/mainBackgroundImage.png";
import mainarrow from "../assets/images/mainarrow.png";
import secondbodyimg from "../assets/images/secondbodyimg.png";
import thirdbodyimg from "../assets/images/thirdbodyimg.png";
import mapimg from "../assets/images/mapimg.png";
import chatimg from "../assets/images/chatimg.png";
import homeBackground from "../assets/images/home_background.jpg";
import messageGreen from "../assets/images/back.png";
import TypingText from "../components/common/Typing";

function MainBody() {
  const vilEZ = useRef();
  const mainBox = useRef();
  const firstBox = useRef();
  const secondBox = useRef();
  const thirdBox = useRef();
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
    setFirstHeight(toNumber(mainStyle) - 200);
    setSecondHeight(toNumber(mainStyle) + toNumber(firstStyle) - 200);
    setThirdHeight(toNumber(mainStyle) + toNumber(firstStyle) + toNumber(secondStyle) - 200);
    setForthHeight(toNumber(mainStyle) + toNumber(firstStyle) + toNumber(secondStyle) + toNumber(thirdStyle) - 200);
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
        <div>
          <Animated animationIn="animate__fadeIn" animationInDuration={2700}>
            <div css={FirstBody}>
              <Animated animationIn="animate__fadeIn" animationInDuration={2200}>
                <div css={FirstBodyDiv}>쉽게 빌리고</div>
              </Animated>
              <Animated animationIn="animate__fadeIn" animationInDuration={3200}>
                <div css={FirstBodyDiv}>쉽게 빌려주는,</div>
              </Animated>
              <Animated animationIn="animate__fadeIn" animationInDuration={4200}>
                <div css={FirstBodyDiv} ref={vilEZ}>
                  공유마을 빌리지.
                  <TypingText />
                </div>
              </Animated>
            </div>
          </Animated>

          <div css={ArrowBox}>
            <a href="#movebottom">
              <img src={mainarrow} alt="" />
            </a>
          </div>
        </div>
      </div>
      <a id="movebottom"></a>
      {/* <div css={blanks}></div>
      <div
        css={[
          test(scrollPosition),
          scrollPosition > firstHeight + 280 && scrollPosition < secondHeight + 1200
            ? fixedStyle
            : relativeStyle(scrollPosition - secondHeight),
        ]}
      >
        <div>4</div>
        <div>3</div>
        <div>2</div>
        <div>1</div>
      </div> */}
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

const FirstWrap = css`
  display: flex;
  margin-top: 186px;
  padding-left: 200px;
  padding-right: 200px;
  justify-content: space-between;
  height: 700px;
`;

const FirstBodyWrap = css`
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-end;
  width: 100%;
  height: calc(100vh - 70px);
  /* height: 100vh; */
  /* background-image: url(${mainBackgroundImage}); */
  /* background-image: url(${homeBackground}); */
  background-image: url(${messageGreen});
  background-size: 40%;
  background-repeat: no-repeat;
  background-position: 200px center;
  & > div {
    display: flex;
    width: 44%;
    flex-direction: column;
    justify-content: center;
    /* width: 100%; */
    height: 100%;
    /* background: linear-gradient(180deg, rgba(255, 255, 255, 0.8) 0%, rgba(102, 221, 156, 0.8) 100%); */
    background-color: #fff;
  }
`;

const FirstBody = css`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  font-size: 60px;
  /* color: white; */
  color: #000;
  text-align: left;
  text-align: center;
  font-family: "GmarketSansMedium";
`;

const FirstBodyDiv = css`
  padding: 10px;
  font-family: "GmarketSansMedium";
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
  left: calc(50% - 64px);
  bottom: 0;
  text-align: center;
  margin-top: 100px;
  cursor: pointer;
  animation: slideInDown;
  animation: ${floating} 2s ease infinite;
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

// const blanks = css`
//   height: 1000px;
// `;

// const test = (scroll) => {
//   return css`
//     position: relative;
//     height: 1000px;
//     background-color: aquamarine;
//     > div {
//       position: absolute;
//       width: 100%;
//       height: 100vh;
//     }
//     > div:nth-of-type(1) {
//       background-color: red;
//       opacity: ${scroll - 300}%;
//     }
//     > div:nth-of-type(2) {
//       background-color: green;
//       opacity: ${scroll - 250}%;
//     }
//     > div:nth-of-type(3) {
//       background-color: #ffffff;
//       opacity: ${(scroll - 400) / 100};
//     }
//     > div:nth-of-type(4) {
//       background-color: #800000;
//       opacity: ${(scroll - 400) / 100};
//     }
//   `;
// };

// const fixedStyle = css`
//   position: fixed;
//   top: 0;
// `;
// const relativeStyle = (scroll) => {
//   return css`
//     position: fixed;
//     bottom: ${scroll - 1200};
//   `;
// };

export default MainBody;
