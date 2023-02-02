import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import bookmark from "../../assets/images/bookmark.png";
import { Link, useLocation, useParams } from "react-router-dom";

const ProductDeatilHeader = ({ title, category, time, bookmarkCount }) => {
  const pathname = useLocation().pathname;
  const boardId = parseInt(useParams().boardId);
  const type = pathname.includes("share") ? 2 : 1;
  return (
    <div css={headerWrapper}>
      <div css={headerLeftSectionWrapper}>
        <span>{title}</span>
        <span>{category}</span>
        <small>{time}</small>
      </div>
      <div css={headerRightSectionWrapper}>
        {/* Link로 변경 */}
        {type === 1 ? (
          <div>
            <Link to={"/product/list/ask"}>
              <span>목록</span>
            </Link>
            <Link to={`/product/edit/ask/${boardId}`}>
              <span>수정</span>
            </Link>
          </div>
        ) : (
          <div>
            <Link to={"/product/list/share"}>
              <span>목록</span>
            </Link>
            <Link to={`/product/edit/share/${boardId}`}>
              <span>수정</span>
            </Link>
          </div>
        )}

        <div>
          <img src={bookmark} alt="bookmark" />
          <small>{bookmarkCount}</small>
        </div>
      </div>
    </div>
  );
};

const headerWrapper = css`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 5px;
`;

const headerLeftSectionWrapper = css`
  & > span:nth-of-type(1) {
    font-size: 30px;
    font-weight: bold;
    margin-right: 20px;
  }

  & > span:nth-of-type(2) {
    color: #66dd9c;
    font-weight: bold;
    margin-right: 20px;
  }

  & > small {
    color: #8a8a8a;
  }
`;

const headerRightSectionWrapper = css`
  display: flex;
  flex-direction: row;

  & > a {
    color: #8a8a8a;
    cursor: pointer;
  }

  & > div {
    display: flex;
    flex-direction: row;
    align-items: center;

    & img {
      margin-left: 20px;
      margin-right: 5px;
      width: 25px;
      height: 20px;
    }
  }
`;

export default ProductDeatilHeader;
