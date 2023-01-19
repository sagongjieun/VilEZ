import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import DivideLine from "../common/DivideLine";
import bookmark from "../../assets/images/bookmark.png";
import { HiChevronRight } from "react-icons/hi2";
import baseProfile from "../../assets/images/baseProfile.png";
import MiddleWideButton from "../button/MiddleWideButton";
import ProductDeatilHeader from "./ProductDeatilHeader";
import Map from "../common/Map";
import ImageSlide from "../common/ImageSlide";

const { kakao } = window;

const ProductDetail = () => {
  /* 임시 데이터 */
  const selectedLat = 37.39495141898642;
  const selectedLng = 127.1112037330217;
  const [location, setLocation] = useState("");

  // 위경도를 통한 주소 얻어오기
  useEffect(() => {
    const geocoder = new kakao.maps.services.Geocoder();
    const latlng = new kakao.maps.LatLng(selectedLat, selectedLng);

    searchDetailAddrFromCoords(latlng, function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        setLocation(result[0].address.address_name);
      }
    });

    function searchDetailAddrFromCoords(coords, callback) {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    }
  }, []);

  return (
    <div css={wrapper}>
      <ProductDeatilHeader
        title={"맥북에어 M1 공유합니다."}
        category={"전자기기"}
        time={"1시간"}
        bookmarkCount={"25"}
      />
      <DivideLine />
      <div css={contentsWrapper}>
        <ImageSlide />
        <div css={nickNameAndChatWrapper}>
          <div css={nickNameWrapper}>
            <img src={baseProfile} alt="baseProfile" />
            <div>
              <span>닉네임</span>
              <span>구미시 진평동</span>
            </div>
            <span>😀</span>
          </div>
          <div css={chatWrapper}>
            <img src={bookmark} alt="bookmark" />
            <MiddleWideButton text="채팅하기" />
          </div>
        </div>
        <div css={contentWrapper}>
          <h3>설명</h3>
          <div>
            <span>이것은 설명입니다.</span>
          </div>
        </div>
        <div css={hopeDateWrapper}>
          <h3>희망 공유 기간</h3>
          <div>
            <span>2023.01.11 - 2023.02.20</span>
          </div>
        </div>
        <div css={hopeAreaWrapper}>
          <div>
            <h3>희망 공유 장소</h3>
            <span>{location}</span>
          </div>
          <Map readOnly={true} selectedLat={selectedLat} selectedLng={selectedLng} />
        </div>
      </div>

      <DivideLine />

      <div>관련 게시글</div>

      <DivideLine />

      <div css={menusWrapper}>
        <div>
          <span>이 게시물 신고하기</span>
          <HiChevronRight size="22" />
        </div>
        <div>
          <span>이 게시물 공유하기</span>
          <HiChevronRight size="22" />
        </div>
        <div>
          <span>맨 위로 이동하기</span>
          <HiChevronRight size="22" />
        </div>
      </div>
    </div>
  );
};

const wrapper = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
`;

/* ContentsWrapper */

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

  & div {
    margin-top: 20px;
    max-width: 100%;
    height: 246px;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    padding: 30px;
    overflow-y: scroll; // CSS 변경 필요
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
`;

/* MenusWrapper */

const menusWrapper = css`
  display: flex;
  flex-direction: column;

  & > div {
    width: 180px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    margin: 20px 0;
  }
`;

export default ProductDetail;
