import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const InputBox = ({ placeholder, onChangeValue }) => {
  return <input css={inputBox} placeholder={placeholder} onChange={(e) => onChangeValue(e.target.value)} />;
};

const inputBox = css`
  box-sizing: border-box;
  display: block;
  width: 100%;
  height: 55px;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  font-size: 18px;
  background-color: #ffffff;
  outline: none;
  padding: 0 20px;
  & ::placeholder {
    color: #c4c4c4;
  }
`;

export default InputBox;
