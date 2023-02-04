import React from "react";
import { Routes, Route } from "react-router-dom";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import MyBoxMain from "../components/mybox/MyBoxMain.jsx";

const MyBox = () => {
  return (
    <div css={innerBox}>
      <Routes>
        <Route path="/" element={<MyBoxMain />} />
      </Routes>
    </div>
  );
};

const innerBox = css`
  width: calc(100% - 400px);
  height: 600px;
  margin: 40px auto;
`;
export default MyBox;
