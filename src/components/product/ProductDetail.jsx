import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import DivideLine from "../common/DivideLine";
import bookmark from "../../assets/images/bookmark.png";
import baseProfile from "../../assets/images/baseProfile.png";
import MiddleWideButton from "../button/MiddleWideButton";
import ProductDeatilHeader from "./ProductDeatilHeader";
import Map from "../common/Map";
import ImageSlide from "../common/ImageSlide";
import ProductDetailFooter from "./ProductDetailFooter";
import ProductCardView from "./ProductCardView";
import { getShareArticleByBoardId, getBookmarkStateByUserId, postBookmark, deleteBookmark } from "../../api/share";
import elapsedTime from "./ProductElapsedTime";
import bookmarkCancel from "../../assets/images/bookmarkCancel.png";
import { getUserDetail } from "../../api/user";
import MannerPoint from "../common/MannerPoint";

const { kakao } = window;

const ProductDetail = () => {
  const [userId, setUserId] = useState("");
  const [boardId, setBoardId] = useState("");
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
  const [state, setState] = useState(0); //eslint-disable-line no-unused-vars
  // 0 : 일반, 1 : 공유중
  const [writerProfile, setWriterProfile] = useState(""); //eslint-disable-line no-unused-vars
  const [writerNickname, setWriterNickname] = useState(""); //eslint-disable-line no-unused-vars
  const [writerArea, setWriterArea] = useState(""); //eslint-disable-line no-unused-vars
  const [writerManner, setWriterManner] = useState(""); //eslint-disable-line no-unused-vars

  const [isBookmarked, setIsBookmarked] = useState(false);

  function onClickBookmark() {
    if (isBookmarked) {
      deleteBookmark(boardId, userId);
      setIsBookmarked(false);
    } else {
      postBookmark(boardId, userId);
      setIsBookmarked(true);
    }
  }

  // 위경도를 통한 주소 얻어오기
  useEffect(() => {
    const geocoder = new kakao.maps.services.Geocoder();
    const latlng = new kakao.maps.LatLng(hopeAreaLat, hopeAreaLng);

    searchDetailAddrFromCoords(latlng, function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        setLocation(result[0].address.address_name);
      }
    });

    function searchDetailAddrFromCoords(coords, callback) {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    }
  }, [hopeAreaLat, hopeAreaLng]);

  useEffect(() => {
    // boardId 임시 데이터
    getShareArticleByBoardId(55).then((res) => {
      const data = res[0];

      setUserId(data.userId);
      setBoardId(data.id);
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
    });
  }, []);

  useEffect(() => {
    if (userId) {
      getUserDetail(userId)
        .then((res) => {
          const data = res[0];

          /** 작성자 프로필이미지 받기 필요 */
          // setWriterProfile(data.profile);
          setWriterNickname(data.nickName);
          setWriterArea(data.area);
          setWriterManner(MannerPoint(data.manner));
        })
        .catch((error) => console.log(error));
    }
  }, [userId]);

  useEffect(() => {
    /** userId가 아니라 recoil에서 현재 로그인한 유저의 id를 파라미터로 넣어야 함. 테스트를 위해 임시로 userId로 넣음. */
    // 내(현재 로그인 한 유저)가 이 게시글을 북마크했는지 여부 확인
    if (boardId && userId) {
      getBookmarkStateByUserId(boardId, userId)
        .then((res) => {
          const data = res[0];

          if (!data) setIsBookmarked(false);
          else setIsBookmarked(true);
        })
        .catch((error) => console.log(error));
    }
  }, [boardId, userId]);

  return (
    <div css={wrapper}>
      <ProductDeatilHeader title={title} category={category} time={date} bookmarkCount={bookmarkCnt} />

      <DivideLine />

      <div css={contentsWrapper}>
        <ImageSlide imageSlideList={imageList} />
        <div css={nickNameAndChatWrapper}>
          <div css={nickNameWrapper}>
            <img src={baseProfile} alt="baseProfile" />
            <div>
              <span>{writerNickname}</span>
              <span>{writerArea}</span>
            </div>
            <span>{writerManner}</span>
          </div>
          <div css={chatWrapper}>
            {isBookmarked ? (
              <img src={bookmark} alt="bookmark" onClick={onClickBookmark} />
            ) : (
              <img src={bookmarkCancel} alt="bookmarkCancel" onClick={onClickBookmark} />
            )}
            {/* 유저 구분 필요 */}
            {state === 0 ? <MiddleWideButton text="채팅하기" /> : <MiddleWideButton text="예약하기" />}
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

      <DivideLine />

      <div css={relatedProductWrapper}>
        <div>
          <h3>관련 게시글</h3>
          <a>더 보기</a>
        </div>
        <div>
          <ProductCardView />
          <ProductCardView />
          <ProductCardView />
        </div>
      </div>

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

  & img {
    width: 90px;
    height: 90px;
    margin-right: 20px;
  }

  & > div {
    display: flex;
    flex-direction: column;
    margin-right: 20px;
  }

  & > div > span:nth-of-type(1) {
    margin-bottom: 10px;
    font-weight: bold;
  }

  & > span {
    font-size: 25px;
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

const relatedProductWrapper = css`
  margin: 60px 0;
  display: flex;
  flex-direction: column;

  & > div {
    display: flex;
    flex-direction: row;
  }

  & > div:nth-of-type(1) {
    margin-bottom: 30px;
    justify-content: space-between;
    align-items: flex-end;

    & > a {
      cursor: pointer;
      font-size: 18px;
    }
  }
`;

export default ProductDetail;
