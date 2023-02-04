import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import receiptSide from "../../assets/images/receipt_side.png";
import { getPointListByUserId } from "../../api/appointment";
import ProfilePointCategory from "./ProfilePointCategory";

const ProfilePointDetails = () => {
  // const userId = localStorage.getItem("id");
  const userId = 42;
  const [pointList, setPointList] = useState([]);
  const [filteredPointList, setFilteredPointList] = useState([]);
  const [category, setCategory] = useState("전체");
  // console.log(pointList, category);
  useEffect(() => {
    getPointListByUserId(userId).then((response) => {
      setPointList([...response[0], { type: 1, increase: true, date: "2023-02-04", title: "가입 축하 포인트" }]);
      setFilteredPointList([
        ...response[0],
        { type: 1, increase: true, date: "2023-02-04", title: "가입 축하 포인트" },
      ]);
    });
  }, []);
  useEffect(() => {
    if (category === "전체") {
      setFilteredPointList(pointList);
    } else if (category === "적립") {
      setFilteredPointList(
        pointList.filter((data) => {
          data.increase === true;
        })
      );
    } else {
      setFilteredPointList(
        pointList.filter((data) => {
          data.increase === false;
        })
      );
    }
  }, [category]);

  return (
    <div css={pointDetailsWrapper}>
      <div css={receiptBox}>
        <div css={receiptSideBox}></div>
        <h3>
          <div>VilEZ</div>공유 영수증
        </h3>
        <div css={dropDownWrapper}>
          <ProfilePointCategory sendCategory={setCategory} category={category} />
        </div>
        <div css={pointListWrapper}>
          {filteredPointList.map((data, index) => (
            <div key={index} css={pointItem}>
              <div>
                <div>
                  {data.date.slice(0, 4)}.{data.date.slice(5, 7)}.{data.date.slice(8, 10)}
                </div>
                <div css={data.type === 2 ? redText : data.increase === true ? greenText : grayText}>
                  {data.type === 2 ? "미반납 페널티" : data.increase === true ? "공유" : "반납"}
                </div>
              </div>
              <div>
                <div>{data.title}</div>
                <div css={[pointWrapper, data.type === 2 ? redText : data.increase === true ? greenText : grayText]}>
                  {data.increase === true ? "+10" : "-10"}p
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
const dropDownWrapper = css`
  display: flex;
  width: calc(100% - 40px);
  justify-content: flex-end;
  margin-top: 10px;
`;
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
const redText = css`
  color: #fc0101;
`;
const greenText = css`
  color: #66dd9c;
`;
const grayText = css`
  color: #8a8a8a;
`;
const pointWrapper = css`
  font-size: 40px;
  font-weight: 200;
`;
export default ProfilePointDetails;
