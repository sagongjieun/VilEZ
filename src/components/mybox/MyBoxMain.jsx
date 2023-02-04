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
  console.log(myShareList, myNotShareList, myAppointList);
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
      <h2>나의 공유박스</h2>
      <MyBoxHeader />
    </div>
  );
};
const mainWrapper = css`
  > h2 {
    padding-bottom: 10px;
  }
`;

export default MyBoxMain;
