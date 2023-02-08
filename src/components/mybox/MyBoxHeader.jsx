import React, { useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";
import villageLandscape from "../../assets/images/village_landscape.png";
import { useRecoilValue } from "recoil";
import { loginUserState } from "../../recoil/atom";

const MyBoxHeader = () => {
  const loginUser = useRecoilValue(loginUserState);
  // const userNickName = loginUser.nickName;
  console.log(loginUser);
  const userNickName = "임시용";
  // const userNickName = useRecoilValue("nickName");
  // const imgTest = useRecoilValue("profileImg");
  // console.log(imgTest);
  const shareCnt = 10;
  const rentCnt = 5;
  useEffect(() => {}, []);
  return (
    <div css={headerWrapper}>
      <div>
        <b>{userNickName}</b> 님은
      </div>
      <div>
        <div>
          <div>{shareCnt}회</div> 공유를 하고 <div>{rentCnt}회</div> 공유를 받았습니다.
        </div>
      </div>
      <img src={villageLandscape} />
    </div>
  );
};
const moveUpDown = keyframes`
	0% {
		transform: translateY(0px);
	}
	100% {
		transform: translateY(-12px);
	}
`;
const headerWrapper = css`
  box-sizing: border-box;
  position: relative;
  height: 300px;
  padding: 50px 60px;
  margin-top: 10px;
  border-radius: 10px;
  background-color: #66dd9c;
  background-image: linear-gradient(120deg, #66dd9c 0%, #8fd3f4 100%);
  > div:nth-of-type(1) {
    color: #fff;
    font-size: 26px;
  }
  > div:nth-of-type(2) {
    height: 40px;
    line-height: 40px;
    > div {
      font-size: 18px;
      padding-top: 20px;
      height: 40px;
      line-height: 40px;
      > div {
        display: inline;
        font-size: 26px;
        font-weight: bold;
        color: #fff;
      }
    }
  }
  > img {
    position: absolute;
    right: -20px;
    bottom: -20px;
    animation: ${moveUpDown} 1.4s ease-in-out infinite alternate;
  }
`;
export default MyBoxHeader;
