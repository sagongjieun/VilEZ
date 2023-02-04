import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getUserShare } from "../../api/profile";
import ProductCardView from "../product/ProductCardView";

// const id = localStorage.getItem("id");
const userId = localStorage.getItem("id");
const ProfileMyBookMark = (props) => {
  const [myShareBoard, setMyShareBoard] = useState([]);
  useEffect(() => {
    getUserShare(userId).then((response) => {
      setMyShareBoard(response);
      props.setBookMarkDefaultPages(parseInt(response.length / 3) + 1);
    });
  }, []);
  return (
    <div css={cardWrapper(props.bookMarkPages)}>
      {myShareBoard.map((share) => (
        <ProductCardView key={share.id} />
      ))}
    </div>
  );
};

const cardWrapper = (pages) => {
  const cards = pages * 3;
  return css`
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    row-gap: 20px;
    column-gap: 20px;
    height: calc(6px + ${pages}* 274px);
    transition: all 0.5s;
    & > div {
      display: none;
      min-width: 300px;
    }
    & > div:nth-of-type(-n + ${cards}) {
      display: block;
      overflow: hidden;
    }
  `;
};

export default ProfileMyBookMark;
