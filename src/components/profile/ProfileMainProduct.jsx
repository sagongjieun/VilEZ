import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProfileMyWriting from "./ProfileMyWriting";
import ProfileMyProduct from "./ProfileMyProduct";
import { useEffect } from "react";

// const id = localStorage.getItem("id");
const ProfileMainProduct = () => {
  const [section, setSection] = useState("");
  const [writingPages, setWritingPages] = useState(1);
  const [productPages, setProductPages] = useState(1);
  function onClickWritingMore() {
    setWritingPages((prev) => prev + 1);
    setSection("writing");
  }
  function onClickProductMore() {
    setProductPages((prev) => prev + 1);
    setSection("product");
  }
  useEffect(() => {
    if (section === "writing") {
      setProductPages(1);
    } else if (section === "product") {
      setWritingPages(1);
    }
  }, [section]);
  return (
    <div>
      <div css={sectionWrapper}>
        <h3>나의 작성글</h3>
        <ProfileMyWriting section={section} writingPages={writingPages} />
        <button onClick={onClickWritingMore} css={moreWrapper}>
          더보기 {writingPages} / 4
        </button>
      </div>
      <div css={sectionWrapper}>
        <h3>나의 공유 물품</h3>
        <ProfileMyProduct section={section} productPages={productPages} />
        <button onClick={onClickProductMore} css={moreWrapper}>
          더보기 {productPages} / 4
        </button>
      </div>
    </div>
  );
};

const sectionWrapper = css`
  padding: 50px 0;
  & > h3 {
    height: 50px;
  }
`;

const moreWrapper = css`
  cursor: pointer;
  background-color: #fff;
  border: 1px solid #c4c4c4;
  border-radius: 5px;
  line-height: 40px;
  width: 100%;
  margin-top: 30px;
`;

export default ProfileMainProduct;
