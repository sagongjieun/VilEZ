import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import MyBoxHeader from "./MyBoxHeader";
// import { Routes, Route } from "react-router-dom";
import { getAppointmentsByUserId, getShareListByUserId, getNotShareListByUserId } from "../../api/appointment";

const MyBoxMain = () => {
  const userId = localStorage.getItem("id");
  const [myShareList, setMyShareList] = useState([]);
  const [myNotShareList, setMyNotShareList] = useState([]);
  const [myAppointList, setMyAppointList] = useState([]);
  const [isHeaderOpen, setIsHeaderOpen] = useState(true);
  console.log(myShareList, myNotShareList, myAppointList);
  function onClickHeaderButton() {
    setIsHeaderOpen(!isHeaderOpen);
  }
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
      setMyNotShareList([]);
    });
  }, []);
  return (
    <div css={mainWrapper}>
      <div>
        <h2>나의 공유박스</h2>
        <div onClick={onClickHeaderButton}>
          {isHeaderOpen ? (
            <button css={[borderedButton, squareButton]}>접기</button>
          ) : (
            <button css={[basicButton, squareButton]}>보기</button>
          )}
        </div>
      </div>
      <MyBoxHeader />
    </div>
  );
};
const mainWrapper = css`
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
const basicButton = css`
  cursor: pointer;
  height: 35px;
  background-color: #66dd9c;
  width: 100%;
  font-size: 14px;
  border: 1px solid #66dd9c;
  color: #fff;
`;
const borderedButton = css`
  cursor: pointer;
  height: 35px;
  background-color: #fff;
  width: 100%;
  font-size: 14px;
  border: 1px solid #66dd9c;
  color: #66dd9c;
  :hover {
    background-color: #acf0cb32;
  }
`;
const squareButton = css`
  border-radius: 5px;
`;
// const roundButton = css`
//   border-radius: 20px;
// `;

export default MyBoxMain;
