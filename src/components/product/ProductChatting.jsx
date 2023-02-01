import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProductInfo from "./ProductInfo";
import MiddleWideButton from "../button/MiddleWideButton";
import StompRealTime from "../StompRealTime";
import MeetConfirmModal from "../modal/MeetConfirmModal";
import QuitChattingReal from "../modal/QuitChattingReal";
import OathMoal from "../modal/OathModal";
import ShareComplete from "../modal/ShareComplete";
import { useParams } from "react-router-dom";
import { getBoardIdByRoomId } from "../../api/chat";
import { getAskArticleDetailByBoardId } from "../../api/ask";
import { getShareArticleByBoardId } from "../../api/share";
import { getUserDetail } from "../../api/profile";

const ProductChatting = () => {
  const { roomId } = useParams();
  const loginUserId = localStorage.getItem("id");

  const [isConfirm, setIsConfirm] = useState(false);
  const [isOath, setIsOath] = useState(false);
  const [isQuit, setIsQuit] = useState(false); // 채팅 나가기 관련
  const [isComplete, setIsComplete] = useState(false);

  const [otherUserId, setOtherUserId] = useState(null);
  const [boardId, setBoardId] = useState(null);
  const [boardType, setBoardType] = useState(null);
  const [boardDetail, setBoardDetail] = useState({
    otherUserNickname: "",
    thumbnailImage: "",
    boardId: boardId,
    title: "",
    location: "",
    startDay: "",
    endDay: "",
    bookmarkCnt: "",
  });

  function onClickQuit() {
    setIsQuit(true);
  }

  function onClickConfirm() {
    setIsConfirm(!isConfirm);
  }

  useEffect(() => {
    // boardId 얻기
    getBoardIdByRoomId(roomId)
      .then((res) => {
        res = res[0];

        setBoardId(res.boardId);
        setBoardType(res.type);

        if (loginUserId == res.shareUserId) {
          setOtherUserId(res.notShareUserId);
        } else setOtherUserId(res.shareUserId);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  useEffect(() => {
    if (otherUserId) {
      // 상대방 nickname 얻기
      getUserDetail(otherUserId).then((res) => {
        setBoardDetail((prev) => {
          return {
            ...prev,
            otherUserNickname: res.nickName,
          };
        });
      });
    }
  }, [otherUserId]);

  useEffect(() => {
    if ((boardId, boardType)) {
      // 게시글의 상세정보 얻기
      boardType === 1
        ? // 요청글
          getAskArticleDetailByBoardId(boardId)
            .then((res) => {
              res = res[0];

              setBoardDetail((prev) => {
                return {
                  ...prev,
                  thumbnailImage: res.list[0],
                  title: res.title,
                  location: res.address,
                  startDay: res.startDay,
                  endDay: res.endDay,
                  bookmarkCnt: res.bookmarkCnt,
                };
              });
            })
            .catch((error) => {
              console.log(error);
            })
        : // 공유글
          getShareArticleByBoardId(boardId)
            .then((res) => {
              res = res[0];

              setBoardDetail((prev) => {
                return {
                  ...prev,
                  thumbnailImage: res.list[0],
                  title: res.title,
                  location: res.address,
                  startDay: res.startDay,
                  endDay: res.endDay,
                  bookmarkCnt: res.bookmarkCnt,
                };
              });
            })
            .catch((error) => {
              console.log(error);
            });
    }
  }, [boardId, boardType]);

  return (
    <div css={wrapper}>
      <div css={articleInfoWrapper}>
        <h2>{boardDetail.otherUserNickname} 님과의 대화</h2>
        <ProductInfo infos={boardDetail} boardId={boardId} boardType={boardType} />
      </div>
      <div css={mapAndChatWrapper}>
        {boardId && boardType && otherUserId && boardDetail.otherUserNickname && (
          <StompRealTime
            roomId={roomId}
            boardId={boardId}
            boardType={boardType}
            otherUserId={otherUserId}
            otherUserNickname={boardDetail.otherUserNickname}
          />
        )}
      </div>
      <div css={buttonWrapper}>
        <MiddleWideButton text={"채팅 나가기"} onclick={onClickQuit} />
        <MiddleWideButton text={"만남 확정하기"} onclick={onClickConfirm} />
      </div>
      <div>
        {isConfirm ? (
          <MeetConfirmModal
            close={setIsConfirm}
            openOath={setIsOath}
            otherUserNickname={boardDetail.otherUserNickname}
          />
        ) : null}
      </div>
      <div>{isQuit ? <QuitChattingReal close={setIsQuit} /> : null}</div>
      <div>{isOath ? <OathMoal close={setIsOath} openLastConfirm={setIsComplete} /> : null} </div>
      <div>{isComplete ? <ShareComplete /> : null}</div>
    </div>
  );
};

const wrapper = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
  position: relative;
`;

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

export default ProductChatting;
