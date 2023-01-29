import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProfileMyWriting from "./ProfileMyWriting";
import ProfileMyProduct from "./ProfileMyProduct";

// const id = localStorage.getItem("id");
const ProfileMainProduct = () => {
  const [section, setSection] = useState("");
  return (
    <div>
      <div css={sectionWrapper}>
        <h3>나의 작성글</h3>
        <ProfileMyWriting
          setSection={() => {
            setSection();
          }}
          section={section}
        />
      </div>
      <div css={sectionWrapper}>
        <h3>나의 공유 물품</h3>
        <ProfileMyProduct
          setSection={() => {
            setSection();
          }}
          section={section}
        />
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

export default ProfileMainProduct;
