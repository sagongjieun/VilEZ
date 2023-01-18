import React from "react";
import { Animated } from "react-animated-css";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import BodyImage1 from "../assets/images/BodyImage1.png";
import MainArrow from "../assets/images/MainArrow.png";
import SecondBodyImage from "../assets/images/SecondBodyImage.png";
import ThirdBodyImage from "../assets/images/ThirdBodyImage.png";
import MapBodyImg from "../assets/images/MapBodyImg.png";
import ChatImg from "../assets/images/ChatImg.png";
function MainBody() {
  return (
    <div>
      <div css={FirstBodyWrap}>
        <Animated animationIn="animate__jackInTheBox" animationInDuration={2000}>
          <div css={FirstBody}>
            <div css={FirstBodyDiv}>쉽게 빌리고</div>
            <div css={FirstBodyDiv}>쉽게 빌려주는,</div>
            <div css={FirstBodyDiv}>공유마을 빌리지.</div>
          </div>
        </Animated>
        <div css={InputWrap}>
          <input type="text" placeholder="공유하고 싶은 물품을 검색해보세요." css={[InputBox]} />
        </div>
        {/* 애니메이션 넣어야 함 */}
        <div css={ArrowBox}>
          <img src={MainArrow} alt="" />
        </div>
      </div>
      <div css={FirstWrap}>
        <div css={ExplainLeft}>
          <div css={ExplainTitle}>따뜻해지는 공유 문화</div>
          <div css={ExplainContent}>필요하지만 구매하기엔 부담스럽고, </div>
          <div css={ExplainContent}>있지만 당장은 쓰지 않는 물품,</div>
          <div css={ExplainContent}>지역 사람들과 공유해봐요.</div>
        </div>
        <div css={ExplainRight}>
          <img css={ImgHeight} src={SecondBodyImage} alt="" />
        </div>
      </div>
      <div css={ExplainWrap}>
        <div css={[ExplainLeft, ImgHeight]}>
          <img css={ImgHeight} src={ThirdBodyImage} alt="" />
        </div>
        <div css={[ExplainRight, ExplainRightCenter]}>
          <div css={ExplainTitle}>안전한 공유방식</div>
          <div css={ExplainContent}>이웃간의 약속을 통해</div>
          <div css={ExplainContent}>안전하게 물건을 빌리고, 빌려줄 수 있어요.</div>
        </div>
      </div>
      <div css={thirdWrap}>
        <div css={ExplainLeft}>
          <div css={ExplainTitle}>실시간 장소선정</div>
          <div css={ExplainContent}>장소를 선정할 때 어려웠던 경험,</div>
          <div css={ExplainContent}>실시간으로 장소를 같이</div>
          <div css={ExplainContent}>선택할 수 있게 빌리지가 도와줄게요.</div>
        </div>
        <div css={ExplainRight}>
          <img src={MapBodyImg} alt="" />
        </div>
      </div>
      <div css={ExplainWrap}>
        <div css={ExplainLeft}>
          <img css={ImgHeight} src={ChatImg} alt="" />
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
  width: 100%;
  height: 700px;
  background-image: url(${BodyImage1});
  background-size: cover;
`;
const FirstBody = css`
  padding-top: 100px;
  font-size: 40px;
  color: white;
  text-align: center;
`;

const FirstBodyDiv = css`
  padding: 10px;
`;

const InputWrap = css`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 90px;
`;

const InputBox = css`
  display: block;
  height: 95px;
  width: 755px;
  border-radius: 20px;
  line-height: 95px;
  padding-left: 20px;
  font-size: 25px;
`;
// const PlaceHolder = css`
//   ::placeholder {
//     text-align: left;
//     text-indent: 20px;
//     font-size: 25px;
//   }
// `;
const ArrowBox = css`
  text-align: center;
  margin-top: 100px;
`;

// SecondBody

const ExplainWrap = css`
  display: flex;
  padding-left: 200px;
  padding-right: 200px;
  justify-content: space-between;
  height: 800px;
`;

const ExplainLeft = css`
  height: 584px;
`;

const ExplainRight = css`
  height: 584px;
`;
const ExplainRightCenter = css`
  display: flex;
  justify-content: center;
  flex-direction: column;
  text-align: right;
`;

const ExplainTitle = css`
  font-size: 40px;
  color: #66dd9c;
  margin-bottom: 20px;
`;
const ExplainContent = css`
  font-size: 28px;
  padding-bottom: 20px;
`;
const ImgHeight = css`
  height: 584px;
`;

const thirdWrap = css`
  display: flex;
  padding-left: 200px;
  padding-right: 200px;
  justify-content: space-between;
  height: 600px;
`;
export default MainBody;
