import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getUserShare } from "../../api/profile";
import ProductCardView from "../product/ProductCardView";

// const id = localStorage.getItem("id");
const ProfileMyWriting = (props) => {
  const [myShareBoard, setMyShareBoard] = useState([]);
  useEffect(() => {
    getUserShare(28).then((response) => {
      setMyShareBoard(response);
    });
  }, []);
  return (
    <div css={cardWrapper(props.writingPages)}>
      {myShareBoard.map((share) => (
        <div
          key={share.id}
          css={css`
            margin-top: 20px;
          `}
        >
          <ProductCardView />
        </div>
      ))}
    </div>
  );
};

const cardWrapper = (pages) => {
  const cards = pages * 3;
  return css`
    overflow: hidden;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    height: calc(6px + ${pages}* 274px);
    justify-content: space-between;
    transition: all 0.5s;
    & > div {
      display: none;
      width: 32%;
      min-width: 300px;
    }
    & > div:nth-of-type(-n + ${cards}) {
      display: block;
    }
  `;
};

export default ProfileMyWriting;