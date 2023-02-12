import React, { useState, useEffect, useRef } from "react";
/** @jsxImportSource @emotion/react */
import { css, keyframes } from "@emotion/react";

const TypingText = () => {
  const text = useRef();
  // const [count, setCount] = useState(3);
  // const [limit, setLimit] = useState(4);
  // const [idx, setIdx] = useState(0);
  // const [flag, setFlag] = useState(false);
  // const textVilEZ = ["빌리지", "VilEZ"];

  // setInterval(() => {
  //   if (flag) {
  //     setCount((prev) => prev + 1);
  //   } else {
  //     setCount((prev) => prev - 1);
  //   }
  // }, 1000);
  // useEffect(()=>{
  // 	text.current.innerText = textVilEZ[idx].slice(0, textVilEZ[idx])
  // },[count])
  // // const letters = ["빌리지", "VilEZ"];
  // return (
  //   <div ref={text} css={textWrapper}>
  //     빌리지
  //   </div>
  // );
};

const cursor = keyframes`
	from { border-right: 2px solid #222; }
	to { border-right: 2px solid #777; }
`;

const textWrapper = css`
  ::after {
    content: "";
    margin-left: 0.4rem;
    border-right: 2px solid #777;
    animation: ${cursor} 0.9s infinite steps(2);
  }
`;

export default TypingText;
