import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { HiChevronRight } from "react-icons/hi2";
import { useLocation } from "react-router-dom";
// import Report from "../modal/Report";

const ProductDetailFooter = () => {
  const location = useLocation();
  // const [showReport, setShowReport] = useState(false);

  // function onClickReportArticle() {
  //   /** 모달 띄우기 */
  //   alert("정말 신고하시겠습니까?");
  //   setShowReport(!showReport);
  // }

  async function onClickShareArticle(url) {
    try {
      await navigator.clipboard.writeText(url);

      alert("링크를 클립보드에 복사했습니다.");
    } catch (error) {
      console.log("URL 복사 실패 : ", error);
    }
  }

  function onClickMoveToTop() {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  return (
    <div css={menusWrapper}>
      {/* <div onClick={onClickReportArticle}>
        <span>이 게시물 신고하기</span>
        <HiChevronRight size="22" />
      </div> */}
      <div onClick={() => onClickShareArticle(`https://i8d111.p.ssafy.io${location.pathname}`)}>
        <span>이 게시물 공유하기</span>
        <HiChevronRight size="22" />
      </div>
      <div onClick={onClickMoveToTop}>
        <span>맨 위로 이동하기</span>
        <HiChevronRight size="22" />
      </div>
      {/* <span>{showReport ? <Report close={setShowReport} /> : null}</span> */}
    </div>
  );
};

const menusWrapper = css`
  display: flex;
  flex-direction: column;
  margin-top: 30px;

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

export default ProductDetailFooter;
