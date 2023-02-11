import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ProductInfo from "./ProductInfo";
import MiddleWideButton from "../button/MiddleWideButton";
import StompRealTime from "../StompRealTime";
import MeetConfirmModal from "../modal/MeetConfirmModal";
import QuitChattingModal from "../modal/QuitChattingModal";
import OathModal from "../modal/OathModal";
import { useParams, useNavigate } from "react-router-dom";
import { deleteChatRoom, getBoardIdByRoomId } from "../../api/appointment";
import { getChattingRoomState } from "../../api/back";
import { getAskArticleDetailByBoardId } from "../../api/ask";
import { getShareArticleByBoardId } from "../../api/share";
import { getUserDetail } from "../../api/user";
import { useSetRecoilState } from "recoil";
import { shareDataState } from "../../recoil/atom";
import { getShareDate } from "../../api/appointment";
import DateFormat from "../common/DateFormat";
import { getShareReturnState } from "../../api/back";
import ProductReturnModal from "../modal/ProductReturnModal";
import ShareCompleteModal from "../modal/ShareCompleteModal";
import ShareCancelAskModal from "../modal/ShareCancelAskModal";
import ShareCancelModal from "../modal/ShareCancelModal";

const ProductChatting = () => {
  const { roomId } = useParams();
  const loginUserId = localStorage.getItem("id");
  const navigate = useNavigate();

  const setShareData = useSetRecoilState(shareDataState);

  const [isConfirm, setIsConfirm] = useState(false);
  const [isOath, setIsOath] = useState(false);
  const [isQuit, setIsQuit] = useState(false);
  const [isProductReturn, setIsProductReturn] = useState(false);
  const [isShareComplete, setIsShareComplete] = useState(false);
  const [isShareCancel, setIsShareCancel] = useState(false);
  const [isShareCancelAsk, setIsShareCancelAsk] = useState(false);

  const [otherUserId, setOtherUserId] = useState(null);
  const [shareUserId, setShareUserId] = useState(null);
  const [notShareUserId, setNotShareUserId] = useState(null);
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
  const [confirmedStartDate, setConfirmedStartDate] = useState("");
  const [confirmedEndDate, setConfirmedEndDate] = useState("");
  const [shareState, setShareState] = useState(0);
  const [roomState, setRoomState] = useState(0);
  const [isChatEnd, setIsChatEnd] = useState(false);
  const [isOtherLeave, setIsOtherLeave] = useState(false);

  // 채팅 나가기
  function onClickQuit() {
    // 상대방이 이미 나갔다면 그냥 나가기
    if (isOtherLeave) {
      deleteChatRoom(roomId, loginUserId).then((res) => {
        if (res) {
          navigate(`/product/list/share`);
        }
      });
    }
    // 내가 먼저 나가는거면 stomp로
    else {
      setIsQuit(true);
    }
  }

  function receiveOtherLeave(flag) {
    // 상대방이 나갔다면
    if (flag) setIsOtherLeave(true);
  }

  // 예약(약속) 확정
  function onClickConfirm() {
    getShareDate(boardId, notShareUserId, shareUserId, boardType).then((res) => {
      res = res[0];

      // 공유자가 기간을 확정했다면
      if (res) {
        res.startDay = DateFormat(new Date(res.startDay));
        res.endDay = DateFormat(new Date(res.endDay));
        setConfirmedStartDate(res.startDay);
        setConfirmedEndDate(res.endDay);

        // recoil에 현재 예약하려는 데이터 담기
        setShareData((prev) => {
          return {
            ...prev,
            appointmentStart: res.startDay,
            appointmentEnd: res.endDay,
          };
        });

        setIsConfirm(!isConfirm);
      } else {
        alert("공유자가 아직 기간을 확정하지 않았습니다. 😥");
      }
    });
  }

  // 예약취소 요청 (피공유자에 의해)
  function onClickAskCancelShare() {
    getShareReturnState(roomId).then((res) => {
      // 반납 확인을 안 눌렀을 때만 예약 취소요청이 가능하게
      if (res != "true") {
        setIsShareCancelAsk(!isShareCancelAsk);
      } else {
        alert("예약 취소 요청이 불가능합니다.");
      }
    });
  }

  // 예약 취소 (공유자에 의해)
  function onClickCancelShare() {
    getShareReturnState(roomId).then((res) => {
      // 반납 확인을 안 눌렀을 때만 예약 취소가 가능하게
      if (res != "true") {
        setIsShareCancel(!isShareCancel);
      } else {
        alert("예약 취소가 불가능합니다.");
      }
    });
  }

  // 반납 확인 (공유자에 의해)
  function onClickCheckReturn() {
    setIsProductReturn(!isProductReturn);
  }

  // 공유 종료 (피공유자에 의해)
  function onClickEndShare() {
    // 공유자가 반납 확인을 눌렀는지 확인
    getShareReturnState(roomId).then((res) => {
      if (res == "true") {
        // 모달로 공유가 끝났다는 것 알리기
        setIsShareComplete(!isShareComplete);
        setIsChatEnd(true);
      } else {
        alert("공유자가 물품에 대해 확인중입니다. 공유자에게 반납 확인 요청을 해주세요. 🙂");
      }
    });
  }

  // StompREalTime.jsx에서 변경되는 state값 받기
  function receiveShareState(state) {
    setShareState(state);
  }
  const [isAuthorized, setIsAuthorized] = useState(false);
  useEffect(() => {
    // boardId 얻기
    getBoardIdByRoomId(roomId)
      .then((res) => {
        res = res[0];

        setBoardId(res.boardId);
        setBoardType(res.type);

        if (res.state == -1) {
          console.log("대화가 종료된 채팅방입니다.");
          setRoomState(res.state);
        }

        // 로그인유저가 공유자면
        if (loginUserId == res.shareUserId) {
          setOtherUserId(res.notShareUserId);
          setShareUserId(loginUserId);
          setNotShareUserId(parseInt(res.notShareUserId));
        }
        // 로그인유저가 피공유자면
        else if (loginUserId == res.notShareUserId) {
          setOtherUserId(res.shareUserId);
          setShareUserId(res.shareUserId);
          setNotShareUserId(parseInt(loginUserId));
        } else {
          alert("채팅방에 입장할 수 없습니다.");
          navigate(`/`);
          return null;
        }
        // Add a state variable to keep track of the authorization status

        // Update the authorization status based on the result of the API call
        if (loginUserId == res.shareUserId || loginUserId == res.notShareUserId) {
          setIsAuthorized(true);
        }
      })
      .catch((error) => {
        console.log(error);
      });

    // 이 채팅방의 예약 상태 얻기
    getChattingRoomState(parseInt(roomId)).then((res) => {
      if (res) {
        res = res[0];

        // 공유 전 상태
        if (res == null) {
          setShareState(-3);
        } else if (res.status == 0) {
          setShareState(0);
        } else if (res.status == -1) {
          setShareState(-1);
        } else if (res.status == -2) {
          setShareState(-2);
        }
      }
    });
  }, [roomId]);

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

  useEffect(() => {
    if (boardId && boardType && shareUserId && notShareUserId) {
      // recoil에 현재 예약하려는 데이터 담기
      setShareData((prev) => {
        return {
          ...prev,
          boardId: boardId,
          boardType: boardType,
          shareUserId: shareUserId,
          notShareUserId: notShareUserId,
        };
      });
    }
  }, [boardId, boardType, shareUserId, notShareUserId]);

  return (
    <div css={wrapper}>
      {isAuthorized ? (
        <div>
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
                shareUserId={shareUserId}
                notShareUserId={notShareUserId}
                shareState={shareState}
                roomState={roomState}
                sendShareState={receiveShareState}
                isChatEnd={isChatEnd}
                sendOtherLeave={receiveOtherLeave}
              />
            )}
          </div>
          <div css={buttonWrapper}>
            {/* state : 0 예약 후, -1 반납 후, -2 예약 취소 후, -3 예약 전 */}
            {shareState == 0 && (
              <>
                {loginUserId == notShareUserId ? (
                  <>
                    <MiddleWideButton text={"예약 취소"} onclick={onClickAskCancelShare} />
                    <MiddleWideButton text={"공유 종료"} onclick={onClickEndShare} />
                  </>
                ) : (
                  <>
                    <MiddleWideButton text={"예약 취소"} onclick={onClickCancelShare} />
                    <MiddleWideButton text={"반납 확인"} onclick={onClickCheckReturn} />
                  </>
                )}
              </>
            )}
            {shareState == -1 && (
              <>
                <MiddleWideButton text={"채팅 나가기"} onclick={onClickQuit} />
              </>
            )}
            {shareState == -2 && (
              <>
                <MiddleWideButton text={"채팅 나가기"} onclick={onClickQuit} />
              </>
            )}
            {shareState == -3 && (
              <>
                <MiddleWideButton text={"채팅 나가기"} onclick={onClickQuit} />
                {loginUserId == notShareUserId ? (
                  <MiddleWideButton text={"예약 확정"} onclick={onClickConfirm} />
                ) : (
                  <></>
                )}
              </>
            )}
          </div>

          {isConfirm ? (
            <MeetConfirmModal
              close={setIsConfirm}
              openOath={setIsOath}
              otherUserNickname={boardDetail.otherUserNickname}
              confirmedStartDate={confirmedStartDate}
              confirmedEndDate={confirmedEndDate}
            />
          ) : null}
          {isQuit ? <QuitChattingModal close={setIsQuit} roomId={roomId} /> : null}
          {isOath ? <OathModal close={setIsOath} roomId={roomId} readOnly={false} /> : null}
          {isProductReturn ? (
            <ProductReturnModal
              close={setIsProductReturn}
              otherUserNickname={boardDetail.otherUserNickname}
              otherUserId={otherUserId}
              roomId={roomId}
            />
          ) : null}
          {isShareComplete ? (
            <ShareCompleteModal otherUserNickname={boardDetail.otherUserNickname} close={setIsShareComplete} />
          ) : null}
          {isShareCancel ? (
            <ShareCancelModal
              close={setIsShareCancel}
              otherUserNickname={boardDetail.otherUserNickname}
              roomId={roomId}
            />
          ) : null}
          {isShareCancelAsk ? (
            <ShareCancelAskModal
              close={setIsShareCancelAsk}
              otherUserNickname={boardDetail.otherUserNickname}
              roomId={roomId}
            />
          ) : null}
        </div>
      ) : null}
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
    background-color: #c82333;
  }

  & > button:nth-of-type(2) {
    margin-left: 40px;
  }
`;

export default ProductChatting;
