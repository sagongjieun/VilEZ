import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProductInfo from "./ProductInfo";
import MiddleWideButton from "../button/MiddleWideButton";
import MapAndChatting from "../Chatting";
import { useParams } from "react-router-dom";
import MeetConfirm from "../modal/MeetConfirm";
import { getBoardIdByRoomId } from "../../api/chat";
import { getShareArticleByBoardId } from "../../api/share";
import { getAskArticleDetailByBoardId } from "../../api/ask";

const ProductChatting = () => {
  const roomId = useParams().roomId;

  const [isConfirm, setIsConfirm] = useState(false);
  const [boardId, setBoardId] = useState(null);
  const [boardType, setBoardType] = useState(null);
  const [boardDetail, setBoardDetail] = useState({
    writerNickname: null,
    thumbnailImage: null,
    boardId: boardId,
    title: null,
    location: null,
    startDay: null,
    endDay: null,
    bookmarkCnt: null,
  });

  function onClickConfirm() {
    // console.log(isConfirm);
    setIsConfirm(!isConfirm);
  }

  useEffect(() => {
    // boardId 얻기
    getBoardIdByRoomId(roomId)
      .then((res) => {
        res = res[0];

        setBoardId(res.id);
        setBoardType(res.type);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  useEffect(() => {
    // 게시글의 상세정보 얻기
    boardType === 1
      ? getAskArticleDetailByBoardId(boardId)
          .then((res) => {
            res = res[0];

            setBoardDetail((prev) => {
              return {
                ...prev,
                writerNickname: "임시",
                thumbnailImage: res.list[0],
                boardId: boardId,
                title: res.title,
                location: "임시",
                startDay: res.startDay,
                endDay: res.endDay,
                bookmarkCnt: res.bookmarkCnt,
              };
            });
          })
          .catch((error) => {
            console.log(error);
          })
      : getShareArticleByBoardId(boardId)
          .then((res) => {
            console.log(res);
          })
          .catch((error) => {
            console.log(error);
          });
  }, [boardId, boardType]);

  return (
    <div css={wrapper}>
      {/* div만든 이유 ? 가리개 만들기 위함 */}
      {/* <div css={modalWrap({ isConfirm })}>{isConfirm ? <MeetConfirm /> : null}</div> */}
      <div>{isConfirm ? <MeetConfirm close={setIsConfirm} /> : null}</div>
      <div css={articleInfoWrapper}>
        <h2>{boardDetail.writerNickname} 님과의 대화</h2>
        <ProductInfo infos={boardDetail} />
      </div>
      <div css={mapAndChatWrapper}>
        <MapAndChatting writerNickname={boardDetail.writerNickname} />
      </div>
      <div css={buttonWrapper}>
        <MiddleWideButton text={"채팅 나가기"} />

        <MiddleWideButton text={"만남 확정하기"} css={meetconfirmWrap} onclick={onClickConfirm} />
      </div>
    </div>
  );
};

const wrapper = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
  position: relative;
`;

// const modalWrap = (props) => css`
//   position: absolute;
//   width: 100%;
//   height: 100%;
//   /* background-color: gray; */
//   left: 0px;
//   top: 0px;
//   backdrop-filter: ${props.isConfirm ? "null" : "blur(4px"};
//   z-index: 1000;
// `;

const articleInfoWrapper = css`
  display: flex;
  flex-direction: column;
  margin-bottom: 60px;

  & > h2 {
    margin-bottom: 30px;
  }
`;

const mapAndChatWrapper = css`
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;

  & > div:nth-of-type(2) {
    display: flex;
    flex-direction: column;
    width: 30%;
  }
`;

const buttonWrapper = css`
  display: flex;
  flex-direction: row;
  margin-left: auto;
  margin-top: 80px;
  & > button {
    width: 250px;
  }

  & > button:nth-of-type(1) {
    margin-right: 40px;
    background-color: #c82333;
  }
`;
const meetconfirmWrap = css``;
export default ProductChatting;
