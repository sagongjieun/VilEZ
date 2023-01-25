import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import receiptSide from "../../assets/images/receipt_side.png";

const ProfilePointDetails = () => {
  const [pointList, setPointList] = useState([]);
  useEffect(() => {
    setPointList([
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
      { date: "2023.01.25", type: "적립", title: "충전기 빌려주세d요", pointNum: "50" },
    ]);
  }, []);
  // const pointListTag = pointList.map((data, index) => {
  //   <div key={index}>{data.title}</div>;
  // });
  return (
    <div css={pointDetailsWrapper}>
      <div css={receiptBox}>
        <div css={receiptSideBox}></div>
        <h3>
          <div>VilEZ</div>공유 영수증
        </h3>
        <div css={pointListWrapper}>
          {pointList.map(function (data, index) {
            return (
              <div key={index} css={pointItem}>
                <div>
                  <div>{data.date}</div>
                  <div>{data.type}</div>
                </div>
                <div>
                  <div>{data.title}</div>
                  <div
                    css={css`
                      font-size: 40px;
                      font-weight: 200;
                    `}
                  >
                    {data.pointNum}p
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
const pointDetailsWrapper = css`
  width: 100%;
  margin-top: 50px;
`;
const receiptBox = css`
  position: relative;
  width: 60%;
  height: 800px;
  margin: 0 auto;
  box-shadow: 0px 10px 5px rgba(0, 0, 0, 0.2);
  & > h3 {
    display: flex;
    justify-content: center;
    padding-top: 40px;
    & > div {
      padding: 0 10px;
    }
  }
`;
const receiptSideBox = css`
  position: absolute;
  right: -50px;
  width: 50px;
  height: 50px;
  background-image: url(${receiptSide});
  background-size: contain;
  background-repeat: no-repeat;
`;
const pointListWrapper = css`
  height: 600px;
  width: calc(100% - 80px);
  margin: 20px auto 0;
  overflow-y: scroll;
  & ::-webkit-scrollbar-thumb {
    height: 30%; /* 스크롤바의 길이 */
    background: #217af4; /* 스크롤바의 색상 */

    border-radius: 10px;
  }
`;
const pointItem = css`
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  border-bottom: 1px solid #d8d8d8;

  & > div {
    display: flex;
    justify-content: space-between;
    align-items: end;
    padding: 0 20px;
  }
`;
export default ProfilePointDetails;
