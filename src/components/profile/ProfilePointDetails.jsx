import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import receiptSide from "../../assets/images/receipt_side.png";
// import { getPointListByUserId } from "../../api/appointment";
import ProfilePointCategory from "./ProfilePointCategory";

const ProfilePointDetails = () => {
  // const userId = localStorage.getItem("id");
  // const userId = 42;
  const [pointList, setPointList] = useState([]);
  const [filteredPointList, setFilteredPointList] = useState([]);
  const [category, setCategory] = useState("");
  // console.log(pointList, category);
  useEffect(() => {
    // getPointListByUserId(userId).then((response) => {
    // setPointList([...response[0], { type: 1, increase: true, date: "2023-02-04", title: "가입 축하 포인트" }]);
    // setFilteredPointList([
    //   ...response[0],
    //   { type: 1, increase: true, date: "2023-02-04", title: "가입 축하 포인트" },
    // ]);
    // });
    const dummyData = [
      { boardId: 1, userId: 1, type: 1, date: "2023-02-06", reason: 0, title: "공유했어용 플러습니더" },
      { boardId: 1, userId: 1, type: 1, date: "2023-02-06", reason: 0, title: "공유했어용 플러습니더" },
      { boardId: 1, userId: 1, type: 1, date: "2023-02-05", reason: 1, title: "취소했어용 플러습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-02-04", reason: 2, title: "패널티예용 마이너습니더" },
      { boardId: 1, userId: 1, type: 2, date: "2023-02-03", reason: 0, title: "공유했어용 마이너습니더" },
      { boardId: 1, userId: 1, type: 2, date: "2023-02-02", reason: 0, title: "공유했어용 마이너습니더" },
      { boardId: 1, userId: 1, type: 1, date: "2023-02-01", reason: 1, title: "취소했어용 플러습니더" },
      { boardId: 1, userId: 1, type: 1, date: "2023-02-01", reason: 1, title: "취소했어용 플러습니더" },
      { boardId: 1, userId: 1, type: 2, date: "2023-01-30", reason: 1, title: "취소했어용 마이너습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-01-29", reason: 2, title: "패널티예용 마이너습니더" },
      { boardId: 1, userId: 1, type: 2, date: "2023-01-29", reason: 1, title: "취소했어용 마이너습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-01-27", reason: 2, title: "패널티예용 마이너습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-01-26", reason: 2, title: "패널티예용 마이너습니더" },
      { boardId: 1, userId: 1, type: 1, date: "2023-01-26", reason: 1, title: "취소했어용 플러습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-01-25", reason: 2, title: "패널티예용 마이너습니더" },
      { boardId: 1, userId: 1, type: 2, date: "2023-01-25", reason: 1, title: "취소했어용 마이너습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-01-24", reason: 2, title: "패널티예용 마이너습니더" },
      // { boardId: 1, userId: 1, type: 2, date: "2023-01-23", reason: 2, title: "패널티예용 마이너습니더" },
    ];
    setPointList(dummyData);
    setFilteredPointList(dummyData);
  }, []);
  useEffect(() => {
    console.log(category);
    if (category === "전체") {
      setFilteredPointList(pointList);
    } else if (category === "적립") {
      setFilteredPointList(pointList.filter((data) => data.type === 1));
    } else {
      setFilteredPointList(pointList.filter((data) => data.type === 2));
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
          <ProfilePointCategory setCategory={setCategory} category={category} />
        </div>
        <div css={pointListWrapper}>
          {filteredPointList.map((data, index) => (
            <div key={index} css={pointItem}>
              <div>
                <div>
                  {data.date.slice(0, 4)}.{data.date.slice(5, 7)}.{data.date.slice(8, 10)}
                </div>
                <div css={data.type === 1 ? greenText : data.reason === 1 ? redText : grayText}>
                  {data.reason === 1 ? "예약 취소" : data.type === 1 ? "공유" : "대여"}
                </div>
              </div>
              <div>
                <div>{data.title}</div>
                <div css={[pointWrapper, data.type === 1 ? greenText : data.reason === 1 ? redText : grayText]}>
                  {data.type === 1 ? "+30" : "-30"}p
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
  width: 50%;
  height: 600px;
  margin: 0 auto;
  box-shadow: 0px 10px 5px rgba(0, 0, 0, 0.2);
  & > h3 {
    display: flex;
    justify-content: center;
    padding-top: 30px;
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
  height: 400px;
  width: calc(100% - 80px);
  margin: 20px auto 0;
  overflow-y: scroll;
`;
const pointItem = css`
  box-sizing: border-box;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-bottom: 1px solid #d8d8d8;
  padding: 10px 0;

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
  font-size: 36px;
  font-weight: 200;
`;
export default ProfilePointDetails;
