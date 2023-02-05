import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
// import { Routes, Route } from "react-router-dom";

const MyBoxRent = () => {
  // const userId = localStorage.getItem("id");
  const [rentNow, setRentNow] = useState(true);

  useEffect(() => {
    setRentNow(true);
    console.log(rentNow);
  }, []);
  return <div css={mainWrapper}></div>;
};
const mainWrapper = css``;
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

export default MyBoxRent;
