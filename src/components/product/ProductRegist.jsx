import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronDown, HiChevronRight, HiChevronUp, HiChevronLeft } from "react-icons/hi2";
import { postShareArticle } from "../../api/share";
import { postAskArticle } from "../../api/ask";
import { AiOutlineClose } from "react-icons/ai";
import DivideLine from "../common/DivideLine";
import InputBox from "../common/InputBox";
import Calendar from "../common/Calendar";
import MiddleWideButton from "../button/MiddleWideButton";

const { kakao } = window;

const ProductRegist = () => {
  const [openRegistType, setOpenRegistType] = useState(false);
  const [openCategory, setOpenCategory] = useState(false);
  const [registType, setRegistType] = useState("선택해주세요.");
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("카테고리");
  const categoryType = [
    "화장품/미용",
    "생활/건강",
    "식품",
    "스포츠/레저",
    "가구/인테리어",
    "디지털/가전",
    "출산/육아",
    "패션잡화",
    "여가/생활편의",
    "패션의류",
    "도서",
  ];
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

  function onClickOpenCategory() {
    if (openCategory) {
      setOpenCategory(false);
    } else {
      setOpenCategory(true);
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

  function onClickCategoryType(type) {
    setCategory(type);
    setOpenCategory(false);
  }

  function onClickFileUpload() {
    const fileInput = document.getElementById("file-input");
    fileInput.click();
  }

  function onChangeImage(e) {
    if (!e.target.files) {
      e.preventDefault();
      return;
    }

    if (imageList.length + e.target.files.length > 8) {
      alert("사진은 최대 8개 등록 가능합니다.");
      return;
    }

    setImageList([...imageList, ...e.target.files]);
  }

  function onClickDeleteImage(deletedImage) {
    setImageList(imageList.filter((image) => image !== deletedImage));
  }

  function sendStartDate(startDate) {
    const utcStartDate = new Date(Date.UTC(startDate.getFullYear(), startDate.getMonth(), startDate.getDate()));
    const startDateResult = JSON.stringify(utcStartDate);

    setStartDay(startDateResult.substring(1, 11));
    setEndDay(""); // 시작일만 설정했을 경우의 이슈를 막기 위해 시작일이 바뀔 때마다 종료일 리셋
  }

  function sendEndDate(endDate) {
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
    <div css={container}>
      <div css={typeWrapper}>
        <h2
          css={css`
            margin-right: 10px;
          `}
        >
          {registType}
        </h2>
        <button css={typeButton} onClick={onClickOpenRegistType}>
          {openRegistType ? <HiChevronUp size="18" /> : <HiChevronDown size="18" />}
        </button>
        {openRegistType ? (
          <div css={registTypeWrapper}>
            <span css={registTypeSpan} onClick={() => onClickRegistType(1)}>
              물품 공유 등록
            </span>
            <DivideLine />
            <span css={registTypeSpan} onClick={() => onClickRegistType(2)}>
              물품 요청 등록
            </span>
          </div>
        ) : (
          <></>
        )}
      </div>
      <DivideLine />
      <div css={subtitleWrapper}>
        <h3
          css={css`
            margin-bottom: 15px;
          `}
        >
          제목{" "}
          <b
            css={css`
              color: red;
            `}
          >
            *
          </b>
        </h3>
        <InputBox placeholder="제목을 입력해주세요." onChangeValue={onChangeTitle} />
      </div>
      <div css={subtitleWrapper}>
        <h3
          css={css`
            margin-bottom: 15px;
          `}
        >
          카테고리{" "}
          <b
            css={css`
              color: red;
            `}
          >
            *
          </b>
        </h3>
        <div css={categoryWrapper}>
          <span
            css={css`
              font-weight: bold;
              color: #66dd9c;
              margin-right: 20px;
            `}
          >
            {category}
          </span>
          <button css={typeButton} onClick={onClickOpenCategory}>
            {openCategory ? <HiChevronLeft size="18" /> : <HiChevronRight size="18" />}
          </button>
          {openCategory ? (
            <div css={categoryTypeWrapper}>
              {categoryType.map((category, index) => (
                <span key={index} css={categoryTypeSpan} onClick={() => onClickCategoryType(category)}>
                  {category}
                </span>
              ))}
            </div>
          ) : (
            <></>
          )}
        </div>
      </div>
      <div css={subtitleWrapper}>
        <h3
          css={css`
            margin-bottom: 15px;
          `}
        >
          설명{" "}
          <b
            css={css`
              color: red;
            `}
          >
            *
          </b>
          <small>(최대 300자)</small>
        </h3>
        <textarea
          css={textarea}
          placeholder="물품에 대한 상세한 설명을 해주면 좋아요."
          onChange={(e) => setContent(e.target.value)}
          id="textarea"
        ></textarea>
      </div>
      <div css={subtitleWrapper}>
        <h3
          css={css`
            margin-bottom: 15px;
          `}
        >
          물품 사진{" "}
          <b
            css={css`
              color: red;
            `}
          >
            *
          </b>
          <small>(최대 8개)</small>
        </h3>
        <small
          css={css`
            color: #847a7a;
          `}
        >
          물품에 대한 사진을 보여주면, 찾는 사람이 정확하게 볼 수 있어요.
        </small>
        <input
          css={css`
            display: none;
          `}
          type="file"
          id="file-input"
          accept=".jpg,.jpeg,.png"
          onChange={onChangeImage}
        />
        <div
          css={css`
            width: 165px;
          `}
        >
          <MiddleWideButton text="사진 찾기" onclick={onClickFileUpload} />
        </div>
        <div css={fileNameWrapper}>
          {imageList.map((image, index) => (
            <small css={fileNameSpan} key={index}>
              {image.name}
              <AiOutlineClose css={closeButton} onClick={() => onClickDeleteImage(image)} />
            </small>
          ))}
        </div>
      </div>
      <div css={subtitleWrapper}>
        <h3
          css={css`
            margin-bottom: 15px;
          `}
        >
          희망 공유 기간{" "}
          <b
            css={css`
              color: red;
            `}
          >
            *
          </b>
        </h3>
        <small
          css={css`
            color: #847a7a;
            margin-bottom: 15px;
          `}
        >
          희망 공유기간을 적어주세요. 기간은 대화를 통해 수정할 수 있어요.
        </small>
        <Calendar sendStartDate={sendStartDate} sendEndDate={sendEndDate} />
      </div>
      <div css={subtitleWrapper}>
        <div css={locationSpan}>
          <h3>
            희망 공유 장소{" "}
            <b
              css={css`
                color: red;
              `}
            >
              *
            </b>
          </h3>
          <span
            css={css`
              color: #8a8a8a;
            `}
          >
            {location}
          </span>
        </div>
        <div
          id="map"
          css={css`
            width: 100%;
            height: 479px;
            border-radius: 5px;
          `}
        ></div>
      </div>
      <div
        css={css`
          width: 100%;
          margin-top: 90px;
          display: flex;
          justify-content: center;
        `}
      >
        <div
          css={css`
            width: 165px;
          `}
        >
          <MiddleWideButton text="등록하기" onclick={onClickRegistButton} />
        </div>
      </div>
    </div>
  );
};

const fileNameSpan = css`
  color: #8a8a8a;
  margin-right: 20px;
  display: flex;
  align-items: center;
`;

const closeButton = css`
  margin-left: 5px;
  cursor: pointer;
`;

const container = css`
  padding: 90px 200px;
  display: flex;
  flex-direction: column;
`;

const typeWrapper = css`
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 5px;
  position: relative;
`;

const typeButton = css`
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
`;

const registTypeWrapper = css`
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
`;

const registTypeSpan = css`
  margin: 14px 0;
  cursor: pointer;

  &:hover {
    color: #66dd9c;
    font-weight: bold;
  }
`;

const categoryTypeSpan = css`
  display: block;
  width: 100%;
  height: 50px;
  line-height: 50px;
  cursor: pointer;
  text-align: center;

  &:hover {
    color: #66dd9c;
    font-weight: bold;
  }
`;

const subtitleWrapper = css`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-top: 60px;
`;

const categoryWrapper = css`
  display: flex;
  flex-direction: row;
  border: 1px solid #ededed;
  border-radius: 5px;
  width: 200px;
  height: 55px;
  align-items: center;
  justify-content: center;
  background: #ffffff;
  position: relative;
`;

const categoryTypeWrapper = css`
  position: absolute;
  width: 158px;
  height: 300px;
  left: 210px;
  top: 0px;
  background: #ffffff;
  border: 1px solid #ededed;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 5px;
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
`;

const textarea = css`
  max-width: 100%;
  height: 176px;
  background: #ffffff;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  outline: none;
  resize: none;
  font-size: 18px;
  padding: 20px;
`;

const fileNameWrapper = css`
  max-width: 100%;
  height: 55px;
  background: #ffffff;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  margin-top: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 25px;
`;

const locationSpan = css`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
`;

export default ProductRegist;
