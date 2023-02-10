import React, { useEffect, useState } from "react";
import { Animated } from "react-animated-css";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";
import mainBackgroundImage from "../assets/images/mainBackgroundImage.png";
import mainarrow from "../assets/images/mainarrow.png";
import secondbodyimg from "../assets/images/secondbodyimg.png";
import thirdbodyimg from "../assets/images/thirdbodyimg.png";
import mapimg from "../assets/images/mapimg.png";
import chatimg from "../assets/images/chatimg.png";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import { locationState, mainSearchTextState } from "../recoil/atom";
import { getUserDetail } from "../api/user";

function MainBody() {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();
  const userId = localStorage.getItem("id");
  const setLocation = useSetRecoilState(locationState); // 어떤 변수가 상태를 담을 수 있는 그릇을 만들어 줌
  const setMainSearchText = useSetRecoilState(mainSearchTextState);

  const onChangeSearch = (e) => {
    setSearch(e.target.value);
  };

  const onKeyPresssearch = (e) => {
    if (e.keyCode == 13) {
      setMainSearchText(search);
      navigate("/product/list/share");
    }
  };

  useEffect(() => {
    if (userId) {
      getUserDetail(userId).then((res) => {
        if (res) {
          setLocation({ areaLat: res.areaLat, areaLng: res.areaLng });
        }
      });
    }
  }, []);

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
          <input
            type="text"
            placeholder="공유하고 싶은 물품을 검색해보세요."
            css={InputBox}
            value={search}
            onChange={onChangeSearch}
            onKeyDown={onKeyPresssearch}
          />
        </div>
        {/* 애니메이션 넣어야 함 */}
        {/* animate__slideInDown */}
        <div css={ArrowBox}>
          <a href="#movebottom">
            <img src={mainarrow} alt="" />
          </a>
        </div>
      </div>
      <a id="movebottom"></a>
      <div css={FirstWrap}>
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
      <div css={ExplainWrap}>
        <div css={[ExplainLeft, ImgHeight]}>
          <img css={ImgHeight} src={thirdbodyimg} alt="" />
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
          <img src={mapimg} alt="" />
        </div>
      </div>
      <div css={ExplainWrap}>
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
  width: 100%;
  height: 700px;
  background-image: url(${mainBackgroundImage});
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
  height: 85px;
  width: 750px;
  border-radius: 20px;
  line-height: 85px;
  padding: 0 30px;
  font-size: 25px;
  outline: none;
  border: 1px solid #e1e2e3;
  font-size: 20px;
`;

const floating = keyframes`
  {
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
`;

const ExplainLeft = css`
  height: 584px;
`;

const ExplainRight = css`
  width: 50%;
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

export default MainBody;
