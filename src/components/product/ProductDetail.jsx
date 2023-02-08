import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import DivideLine from "../common/DivideLine";
import bookmark from "../../assets/images/bookmark.png";
import MiddleWideButton from "../button/MiddleWideButton";
import ProductDeatilHeader from "./ProductDeatilHeader";
import Map from "../common/Map";
import ImageSlide from "../common/ImageSlide";
import ProductDetailFooter from "./ProductDetailFooter";
// import ProductCardView from "./ProductCardView";
import { getShareArticleByBoardId, getBookmarkStateByUserId, postBookmark, deleteBookmark } from "../../api/share";
import elapsedTime from "./ProductElapsedTime";
import bookmarkCancel from "../../assets/images/bookmarkCancel.png";
import { getUserDetail } from "../../api/user";
import MannerPoint from "../common/MannerPoint";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { postChatRoom } from "../../api/chat";
import { getAskArticleDetailByBoardId } from "../../api/ask";
import { getCheckMyRoom } from "../../api/chat";
// import { getRelatedShareArticle } from "../../api/share";

const ProductDetail = () => {
  const navigate = useNavigate();
  const boardId = parseInt(useParams().boardId);
  const pathname = useLocation().pathname;
  const loginUserId = localStorage.getItem("id"); // 로그인유저 id

  const [writerId, setWriterId] = useState(""); // 공유자 id
  const [isRelated, setIsRelated] = useState(false); // 로그인유저가 현재 공유중인 공유자 or 피공유자인지 확인

  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("");
  const [content, setContent] = useState("");
  const [imageList, setImageList] = useState([]);
  const [date, setDate] = useState("");
  const [startDay, setStartDay] = useState("");
  const [endDay, setEndDay] = useState("");
  const [hopeAreaLat, setHopeAreaLat] = useState("");
  const [hopeAreaLng, setHopeAreaLng] = useState("");
  const [location, setLocation] = useState("");
  const [bookmarkCnt, setBookmarkCnt] = useState(0);
  const [state, setState] = useState(0); // 0 : 일반, 1 : 공유중
  const [writerProfile, setWriterProfile] = useState("");
  const [writerNickname, setWriterNickname] = useState("");
  // const [writerArea, setWriterArea] = useState("");
  const [writerManner, setWriterManner] = useState("");
  const [isBookmarked, setIsBookmarked] = useState(false);

  function onClickBookmark() {
    if (isBookmarked) {
      deleteBookmark(boardId, loginUserId);
      setIsBookmarked(false);
      setBookmarkCnt(bookmarkCnt - 1);
    } else {
      postBookmark(boardId, loginUserId);
      setIsBookmarked(true);
      setBookmarkCnt(bookmarkCnt + 1);
    }
  }

  function onClickMoveChat() {
    const type = pathname.includes("share") ? 2 : 1; // 요청글 = 1, 공유글 = 2

    getCheckMyRoom(boardId, type, loginUserId).then((res) => {
      // 채팅방이 이미 존재하면 해당 방으로 이동
      if (res) {
        navigate(`/product/chat/${res[0].id}`);
      }
      // 채팅방이 없으면 채팅방 생성
      else {
        // 요청글이면 공유자 = 나, 피공유자 = 상대방
        // 공유글이면 공유자 = 상대방, 피공유자 = 나
        type === 1
          ? postChatRoom({
              type: type,
              boardId: boardId,
              shareUserId: loginUserId,
              notShareUserId: writerId,
            }).then((res) => {
              navigate(`/product/chat/${res[0].id}`);
            })
          : postChatRoom({
              type: type,
              boardId: boardId,
              shareUserId: writerId,
              notShareUserId: loginUserId,
            }).then((res) => {
              navigate(`/product/chat/${res[0].id}`);
            });
      }
    });
  }

  function onClickReturnProduct() {
    // 반납완료 로직 구현
  }

  // 게시글 정보 얻어오기
  useEffect(() => {
    const type = pathname.includes("share") ? 2 : 1;

    type === 1
      ? getAskArticleDetailByBoardId(boardId).then((res) => {
          const data = res[0];

          setWriterId(data.userId);
          setTitle(data.title);
          setCategory(data.category);
          setDate(elapsedTime(data.date));
          setImageList(data.list);
          setContent(data.content);
          setStartDay(data.startDay);
          setEndDay(data.endDay);
          setHopeAreaLat(data.hopeAreaLat);
          setHopeAreaLng(data.hopeAreaLng);
          setBookmarkCnt(data.bookmarkCnt);
          setState(data.state);
          setLocation(data.address);
        })
      : getShareArticleByBoardId(boardId).then((res) => {
          const data = res[0];

          setWriterId(data.userId);
          setTitle(data.title);
          setCategory(data.category);
          setDate(elapsedTime(data.date));
          setImageList(data.list);
          setContent(data.content);
          setStartDay(data.startDay);
          setEndDay(data.endDay);
          setHopeAreaLat(data.hopeAreaLat);
          setHopeAreaLng(data.hopeAreaLng);
          setBookmarkCnt(data.bookmarkCnt);
          setState(data.state);
          setLocation(data.address);
        });
  }, []);

  // 작성자(공유자) 정보 얻어오기
  useEffect(() => {
    if (writerId) {
      getUserDetail(writerId)
        .then((res) => {
          const data = res[0];

          setWriterProfile(data.profile_img);
          setWriterNickname(data.nickName);
          // setWriterArea(data.area);
          setWriterManner(MannerPoint(data.manner));
        })
        .catch((error) => console.log(error));
    }
  }, [writerId]);

  // 내가 이 게시글을 북마크했는지 여부 확인
  useEffect(() => {
    if (boardId && loginUserId) {
      getBookmarkStateByUserId(boardId, loginUserId)
        .then((res) => {
          const data = res[0];

          if (!data) setIsBookmarked(false);
          else setIsBookmarked(true);
        })
        .catch((error) => console.log(error));
    }
  }, [boardId, loginUserId]);

  // 이 게시물이 이미 공유중일 때, 내가 공유자 or 피공유자인지 확인
  useEffect(() => {
    if (state === 1) {
      const type = pathname.includes("share") ? 2 : 1; // 요청글 = 1, 공유글 = 2

      getCheckMyRoom(boardId, type, loginUserId).then((res) => {
        if (!res) setIsRelated(false);
        else setIsRelated(true);
      });
    }
  }, [state]);

  return (
    <div css={wrapper}>
      <ProductDeatilHeader
        title={title}
        category={category}
        time={date}
        bookmarkCount={bookmarkCnt}
        // editt={setIsEdit}
      />

      <DivideLine />

      <div css={contentsWrapper}>
        <ImageSlide imageSlideList={imageList} />
        <div css={nickNameAndChatWrapper}>
          <div css={nickNameWrapper}>
            <img src={writerProfile} alt="writerProfileImage" />
            <span>{writerNickname}</span>
            <img src={writerManner} alt="writerMannerPoint" />
          </div>
          <div css={chatWrapper}>
            {isBookmarked ? (
              <img src={bookmark} alt="bookmark" onClick={onClickBookmark} />
            ) : (
              <img src={bookmarkCancel} alt="bookmarkCancel" onClick={onClickBookmark} />
            )}
            {state === 0 ? (
              loginUserId == writerId ? (
                <></>
              ) : (
                <MiddleWideButton text="채팅하기" onclick={onClickMoveChat} />
              )
            ) : loginUserId == writerId ? (
              <MiddleWideButton text="반납확정" onclick={onClickReturnProduct} />
            ) : isRelated ? (
              <MiddleWideButton text="채팅하기" onclick={onClickMoveChat} />
            ) : (
              <MiddleWideButton text="예약하기" onclick={onClickMoveChat} />
            )}
          </div>
        </div>
        <div css={contentWrapper}>
          <h3>설명</h3>
          <textarea readOnly value={content}></textarea>
        </div>
        <div css={hopeDateWrapper}>
          <h3>희망 공유 기간</h3>
          <div>
            <span>
              {startDay} - {endDay}
            </span>
          </div>
        </div>
        <div css={hopeAreaWrapper}>
          <div>
            <h3>희망 공유 장소</h3>
            <span>{location}</span>
          </div>
          <Map readOnly={true} selectedLat={hopeAreaLat} selectedLng={hopeAreaLng} />
        </div>
      </div>

      {/* <DivideLine />

      <div css={relatedProductWrapper}>
        <div>
          <h3>관련 인기 게시글</h3>
        </div>
        <div>
          <ProductCardView />
          <ProductCardView />
          <ProductCardView />
        </div>
      </div> */}

      <DivideLine />

      <ProductDetailFooter />
    </div>
  );
};

const wrapper = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
`;

const contentsWrapper = css`
  display: flex;
  flex-direction: column;
  padding: 60px 20px;

  & > div {
    margin-bottom: 60px;
  }
`;

const nickNameAndChatWrapper = css`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const nickNameWrapper = css`
  display: flex;
  flex-direction: row;
  align-items: center;

  & > img:nth-of-type(1) {
    width: 90px;
    height: 90px;
    margin-right: 20px;
    border-radius: 100%;
  }

  & > span {
    font-size: 20px;
    font-weight: bold;
    margin-right: 10px;
  }

  & > img:nth-of-type(2) {
    width: 40px;
    height: 40px;
  }
`;

const chatWrapper = css`
  display: flex;
  flex-direction: row;
  align-items: center;

  & img {
    width: 35px;
    height: 30px;
    margin-right: 20px;
    cursor: pointer;
  }

  & button {
    margin-top: 0;
  }
`;

const contentWrapper = css`
  display: flex;
  flex-direction: column;

  & textarea {
    margin-top: 20px;
    max-width: 100%;
    height: 246px;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    padding: 30px;
    font-size: 18px;
    resize: none;
    outline: none;
    overflow-y: scroll;

    &::-webkit-scrollbar {
      width: 8px;
    }

    &::-webkit-scrollbar-thumb {
      height: 30%;
      background: #c4c4c4;
      border-radius: 10px;
    }

    &::-webkit-scrollbar-track {
      background: none;
    }
  }
`;

const hopeDateWrapper = css`
  & div {
    margin-top: 20px;
    width: 260px;
    height: 54px;
    background: #ffffff;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
`;

const hopeAreaWrapper = css`
  display: flex;
  flex-direction: column;

  & > div:nth-of-type(1) {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-between;
    margin-bottom: 20px;

    & span {
      color: #8a8a8a;
    }
  }

  & > div:nth-of-type(2) {
    width: 100%;
    height: 479px;
  }
`;

// const relatedProductWrapper = css`
//   margin: 50px 0;
//   display: flex;
//   flex-direction: column;

//   & > div {
//     display: flex;
//     flex-direction: row;
//   }

//   & > div:nth-of-type(1) {
//     margin-bottom: 30px;
//     justify-content: space-between;
//     align-items: flex-end;

//     & > a {
//       cursor: pointer;
//       font-size: 18px;
//     }
//   }
// `;

export default ProductDetail;
