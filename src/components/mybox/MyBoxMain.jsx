import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import MyBoxHeader from "./MyBoxHeader";
// import { Routes, Route } from "react-router-dom";
import { getAppointmentsByUserId, getShareListByUserId, getNotShareListByUserId } from "../../api/appointment";
import MyBoxBody from "./MyBoxBody";

const MyBoxMain = () => {
  const userId = localStorage.getItem("id");
  const [myShareList, setMyShareList] = useState([]);
  const [myRentList, setMyRentList] = useState([]);
  const [myAppointList, setMyAppointList] = useState([]);
  console.log(myShareList, myRentList, myAppointList);

  useEffect(() => {
    getAppointmentsByUserId(userId).then((response) => {
      setMyAppointList([]);
      console.log(response);
    });
    getShareListByUserId(userId).then((response) => {
      console.log(response);
      setMyShareList([]);
    });
    getNotShareListByUserId(userId).then((response) => {
      console.log(response);
      setMyRentList([]);
    });
  }, []);
  return (
    <div css={mainWrapper}>
      <div>
        <h2>나의 공유박스</h2>
      </div>
      <MyBoxHeader />
      <MyBoxBody />
    </div>
  );
};
const mainWrapper = css`
  padding: 30px 0;
  > div:nth-of-type(1) {
    display: flex;
    justify-content: space-between;
    align-items: center;
    & > h2 {
      padding-bottom: 10px;
    }
    & > div {
      width: 140px;
      display: flex;
      align-items: center;
    }
  }
`;
// const basicButton = css`
//   cursor: pointer;
//   height: 35px;
//   background-color: #66dd9c;
//   width: 100%;
//   font-size: 14px;
//   border: 1px solid #66dd9c;
//   color: #fff;
// `;
// const borderedButton = css`
//   cursor: pointer;
//   height: 35px;
//   background-color: #fff;
//   width: 100%;
//   font-size: 14px;
//   border: 1px solid #66dd9c;
//   color: #66dd9c;
//   :hover {
//     background-color: #acf0cb32;
//   }
// `;
// const squareButton = css`
//   border-radius: 5px;
// `;
// const roundButton = css`
//   border-radius: 20px;
// `;

// const appointContainer = css``;

export default MyBoxMain;
