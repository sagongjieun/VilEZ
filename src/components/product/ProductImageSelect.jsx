import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { AiOutlineClose } from "react-icons/ai";
import MiddleWideButton from "../button/MiddleWideButton";
import { useLocation, useParams } from "react-router-dom";
import { getShareArticleByBoardId } from "../../api/share";

const ProductImageSelect = ({ sendImageList }) => {
  const pathname = useLocation().pathname;
  const boardId = parseInt(useParams().boardId);
  const type = pathname.includes("share") ? 2 : pathname.includes("regist") ? null : 1;
  // console.log(type);
  const [imageList, setImageList] = useState([]);

  function onClickFileUpload() {
    const fileInput = document.getElementById("file-input");
    fileInput.click();
  }

  function onChangeImage(e) {
    if (!e.target.files) {
      e.preventDefault();
      return;
    }

    if (imageList.length + e.target.files.length > 4) {
      alert("사진은 최대 4개 등록 가능합니다.");
      return;
    }

    setImageList([...imageList, ...e.target.files]);
  }

  function onClickDeleteImage(deletedImage) {
    setImageList(imageList.filter((image) => image !== deletedImage));
  }

  useEffect(() => {
    sendImageList(imageList);
  }, [imageList]);
  useEffect(() => {
    type === 2
      ? getShareArticleByBoardId(boardId).then((res) => {
          const data = res[0].list;
          // console.log("@@@@@@", data);
          // const tempimage = data.map((d) => d);
          // const filename = tempimage.map((obj) => obj.fileName);
          setImageList(data);
        })
      : null;
  }, []);

  return (
    <div css={imageWrapper}>
      <input type="file" id="file-input" accept=".jpg,.jpeg,.png" onChange={onChangeImage} />
      <div>
        <MiddleWideButton text="사진 찾기" onclick={onClickFileUpload} />
      </div>
      <div>
        {imageList.map((image, index) => (
          <small key={index}>
            {type ? image.fileName : image.name}

            <AiOutlineClose onClick={() => onClickDeleteImage(image)} />
          </small>
        ))}
      </div>
    </div>
  );
};

const imageWrapper = css`
  & > input {
    display: none;
  }

  & > div:nth-of-type(1) {
    width: 165px;
  }

  & > div:nth-of-type(2) {
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

    & small {
      color: #8a8a8a;
      margin-right: 20px;
      display: flex;
      align-items: center;

      & svg {
        margin-left: 5px;
        cursor: pointer;
      }
    }
  }
`;

export default ProductImageSelect;
