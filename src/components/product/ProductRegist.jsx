import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronDown, HiChevronUp } from "react-icons/hi2";
import { postShareArticle } from "../../api/share";
import { postAskArticle } from "../../api/ask";
import DivideLine from "../common/DivideLine";
import InputBox from "../common/InputBox";
import Calendar from "../common/Calendar";
import MiddleWideButton from "../button/MiddleWideButton";
import ProductCategory from "./ProductCategory";
import ProductImageSelect from "./ProductImageSelect";

const { kakao } = window;

const ProductRegist = () => {
  const [openRegistType, setOpenRegistType] = useState(false);
  const [registType, setRegistType] = useState("선택해주세요.");
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("");
  const [content, setContent] = useState("");
  const [startDay, setStartDay] = useState("");
  const [endDay, setEndDay] = useState("");
  const [location, setLocation] = useState("");
  const [hopeAreaLat, setHopeAreaLat] = useState("");
  const [hopeAreaLng, setHopeAreaLng] = useState("");
  const [imageList, setImageList] = useState([]);

  function onClickOpenRegistType() {
    if (openRegistType) {
      setOpenRegistType(false);
    } else {
      setOpenRegistType(true);
    }
  }

  function onClickRegistType(type) {
    switch (type) {
      case 1:
        setRegistType("물품 공유 등록");
        break;
      case 2:
        setRegistType("물품 요청 등록");
        break;
    }

    setOpenRegistType(false);
  }

  function onChangeTitle(value) {
    setTitle(value);
  }

  function receiveCategory(category) {
    setCategory(category);
  }

  function receiveImageList(imageList) {
    setImageList(imageList);
  }

  function receiveStartDate(startDate) {
    const utcStartDate = new Date(Date.UTC(startDate.getFullYear(), startDate.getMonth(), startDate.getDate()));
    const startDateResult = JSON.stringify(utcStartDate);

    setStartDay(startDateResult.substring(1, 11));
    setEndDay(""); // 시작일만 설정했을 경우의 이슈를 막기 위해 시작일이 바뀔 때마다 종료일 리셋
  }

  function receiveEndDate(endDate) {
    const utcEndDate = new Date(Date.UTC(endDate.getFullYear(), endDate.getMonth(), endDate.getDate()));
    const endDateResult = JSON.stringify(utcEndDate);

    setEndDay(endDateResult.substring(1, 11));
  }

  function onClickRegistButton() {
    // 유효성 검사
    if (registType === "선택해주세요.") {
      alert("글 등록 타입을 선택해주세요.");
      return;
    }

    if (
      category === "카테고리" ||
      !category ||
      !content ||
      !endDay ||
      !hopeAreaLat ||
      !hopeAreaLng ||
      !startDay ||
      !title
    ) {
      alert("빈 칸을 모두 채워주세요.");
      return;
    }

    if (content.length > 300) {
      alert("물품에 대한 설명은 최대 300자 입력 가능합니다.");
      return;
    }

    setContent(content.replaceAll("<br>", "\r\n")); // 개행 처리

    const formData = new FormData();

    imageList.forEach((image) => {
      formData.append("image", image);
    });

    formData.append(
      "board",
      new Blob(
        [
          JSON.stringify({
            category: category,
            content: content,
            endDay: endDay,
            hopeAreaLat: hopeAreaLat,
            hopeAreaLng: hopeAreaLng,
            startDay: startDay,
            title: title,
            userId: 1, // 임시 데이터
          }),
        ],
        { type: "application/json" }
      )
    );

    if (registType === "물품 공유 등록") {
      postShareArticle(formData);
    } else if (registType === "물품 요청 등록") {
      postAskArticle(formData);
    }
  }

  useEffect(() => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 4,
    };
    const map = new kakao.maps.Map(container, options);

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function (position) {
        const lat = position.coords.latitude, // 위도
          lon = position.coords.longitude; // 경도

        const locPosition = new kakao.maps.LatLng(lat, lon);
        map.setCenter(locPosition);
      });
    } else {
      const locPosition = new kakao.maps.LatLng(33.450701, 126.570667);
      map.setCenter(locPosition);
    }

    const marker = new kakao.maps.Marker();
    const geocoder = new kakao.maps.services.Geocoder();

    kakao.maps.event.addListener(map, "click", function (mouseEvent) {
      const latlng = mouseEvent.latLng;

      marker.setPosition(latlng);
      setHopeAreaLat(latlng.getLat());
      setHopeAreaLng(latlng.getLng());

      searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
          setLocation(result[0].address.address_name);
          marker.setMap(map);
        }
      });
    });

    function searchDetailAddrFromCoords(coords, callback) {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    }
  }, []);

  return (
    <div css={wrapper}>
      <div css={registTypeWrapper}>
        <h2>{registType}</h2>
        <button onClick={onClickOpenRegistType}>
          {openRegistType ? <HiChevronUp size="18" /> : <HiChevronDown size="18" />}
        </button>
        {openRegistType ? (
          <div>
            <span onClick={() => onClickRegistType(1)}>물품 공유 등록</span>
            <DivideLine />
            <span onClick={() => onClickRegistType(2)}>물품 요청 등록</span>
          </div>
        ) : (
          <></>
        )}
      </div>

      <DivideLine />

      <div css={titleWrapper}>
        <h3>
          제목 <b>*</b>
        </h3>
        <InputBox placeholder="제목을 입력해주세요." onChangeValue={onChangeTitle} />
      </div>

      <div css={categoryWrapper}>
        <h3>
          카테고리 <b>*</b>
        </h3>
        <ProductCategory sendCategory={receiveCategory} />
      </div>

      <div css={contentWrapper}>
        <h3>
          설명 <b>*</b>
          <small>(최대 300자)</small>
        </h3>
        <textarea
          placeholder="물품에 대한 상세한 설명을 해주면 좋아요."
          onChange={(e) => setContent(e.target.value)}
          id="textarea"
        ></textarea>
      </div>

      <div css={imageWrapper}>
        <h3>
          물품 사진 <b>*</b>
          <small>(최대 8개)</small>
        </h3>
        <small>물품에 대한 사진을 보여주면, 찾는 사람이 정확하게 볼 수 있어요.</small>
        <ProductImageSelect sendImageList={receiveImageList} />
      </div>

      <div css={hopeDateWrapper}>
        <h3>
          희망 공유 기간 <b>*</b>
        </h3>
        <small>희망 공유기간을 적어주세요. 기간은 대화를 통해 수정할 수 있어요.</small>
        <Calendar sendStartDate={receiveStartDate} sendEndDate={receiveEndDate} />
      </div>

      <div css={hopeAreaWrapper}>
        <div css={hopeAreaHeaderWrapper}>
          <h3>
            희망 공유 장소 <b>*</b>
          </h3>
          <span>{location}</span>
        </div>
        <div id="map"></div>
      </div>

      <div css={registButtonWrapper}>
        <div>
          <MiddleWideButton text="등록하기" onclick={onClickRegistButton} />
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

const registTypeWrapper = css`
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 5px;
  position: relative;

  & > h2 {
    margin-right: 10px;
  }

  & > button {
    width: 30px;
    height: 30px;
    border-radius: 100px;
    background: #ffffff;
    box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.25);
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  & > div {
    position: absolute;
    width: 185px;
    height: 120px;
    top: 52px;
    background: #ffffff;
    border: 1px solid #ededed;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    & span {
      margin: 14px 0;
      cursor: pointer;

      &:hover {
        color: #66dd9c;
        font-weight: bold;
      }
    }
  }
`;

const titleWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;

  & > h3 {
    margin-bottom: 15px;

    & b {
      color: red;
    }
  }
`;

const categoryWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;

  & > h3 {
    margin-bottom: 15px;

    & b {
      color: red;
    }
  }
`;

const contentWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;

  & > h3 {
    margin-bottom: 15px;

    & b {
      color: red;
    }
  }

  & > textarea {
    max-width: 100%;
    height: 176px;
    background: #ffffff;
    border: 1px solid #e1e2e3;
    border-radius: 5px;
    outline: none;
    resize: none;
    font-size: 18px;
    padding: 20px;
  }
`;

const imageWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;

  & > h3 {
    margin-bottom: 15px;

    & b {
      color: red;
    }
  }

  & > small {
    color: #847a7a;
  }
`;

const hopeDateWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;

  & > h3 {
    margin-bottom: 15px;

    & b {
      color: red;
    }
  }

  & > small {
    color: #847a7a;
    margin-bottom: 15px;
  }
`;

const hopeAreaWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;

  & #map {
    width: 100%;
    height: 479px;
    border-radius: 5px;
  }
`;

const hopeAreaHeaderWrapper = css`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;

  & > h3 b {
    color: red;
  }

  & > span {
    color: #8a8a8a;
  }
`;

const registButtonWrapper = css`
  width: 100%;
  margin-top: 90px;
  display: flex;
  justify-content: center;

  & > div {
    width: 165px;
  }
`;

export default ProductRegist;
