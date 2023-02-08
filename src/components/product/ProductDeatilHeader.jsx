import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import bookmark from "../../assets/images/bookmark.png";
import { Link, useLocation, useParams, useNavigate } from "react-router-dom";
import { deleteShareArticleByBoardId } from "../../api/share";
import { deleteAskArticleByBoardId } from "../../api/ask";

const ProductDeatilHeader = ({ title, category, time, bookmarkCount }) => {
  const pathname = useLocation().pathname;
  const boardId = parseInt(useParams().boardId);
  const type = pathname.includes("share") ? 2 : 1;
  const navigate = useNavigate();
  // console.log(boardId);

  function onClickDelete() {
    type === 2
      ? deleteShareArticleByBoardId(boardId).then(() => {
          navigate(`/product/list/share`);
        })
      : deleteAskArticleByBoardId(boardId).then(() => {
          navigate(`/product/list/ask`);
        });
  }
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
              <span css={optionWrap}>목록</span>
            </Link>
            <Link to={`/product/edit/ask/${boardId}`}>
              <span css={optionWrap}>수정</span>
            </Link>
            <span css={optionWrap} onClick={onClickDelete}>
              삭제
            </span>
          </div>
        ) : (
          <div>
            <Link to={"/product/list/share"}>
              <span css={optionWrap}>목록</span>
            </Link>
            <Link to={`/product/edit/share/${boardId}`}>
              <span css={optionWrap}>수정</span>
            </Link>
            <span css={optionWrap} onClick={onClickDelete}>
              삭제
            </span>
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
  & > span {
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

const optionWrap = css`
  margin-right: 10px;
  color: black;
  cursor: pointer;
`;

export default ProductDeatilHeader;
