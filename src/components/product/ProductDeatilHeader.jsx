import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import bookmark from "../../assets/images/bookmark.png";
import { Link, useLocation, useParams, useNavigate } from "react-router-dom";
import { deleteShareArticleByBoardId, getShareArticleByBoardId } from "../../api/share";
import { deleteAskArticleByBoardId } from "../../api/ask";

const ProductDeatilHeader = ({ title, category, time, bookmarkCount }) => {
  const userId = localStorage.getItem("id");
  const [ThisboardUserId, setThisboardUserId] = useState(null);
  const pathname = useLocation().pathname;
  const boardId = parseInt(useParams().boardId);
  const type = pathname.includes("share") ? 2 : 1;
  const navigate = useNavigate();
  useEffect(() => {
    type === 2
      ? getShareArticleByBoardId(boardId).then((res) => {
          setThisboardUserId(res[0].userId);
          console.log(res[0].userId);
        })
      : null;
  }, []);
  console.log(userId === ThisboardUserId);
  function onClickDelete() {
    type === 2
      ? deleteShareArticleByBoardId(boardId).then(() => {
          navigate(`/product/list/share`);
        })
      : deleteAskArticleByBoardId(boardId).then(() => {
          navigate(`/product/list/ask`);
        });
  }
  console.log(userId, ThisboardUserId, parseInt(userId) === parseInt(ThisboardUserId));

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
          parseInt(userId) === parseInt(ThisboardUserId) ? (
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
            <div>하이요</div>
          )
        ) : parseInt(userId) === parseInt(ThisboardUserId) ? (
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
        ) : (
          <Link to={"/product/list/share"}>
            <span css={optionWrap}>목록</span>
          </Link>
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
