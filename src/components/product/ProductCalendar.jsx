import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
// import { useLocation, useParams } from "react-router-dom";
// import { getShareArticleByBoardId } from "../../api/share";

const ProductCalendar = ({ sendStartDate, sendEndDate }) => {
  const [dateRange, setDateRange] = useState([null, null]);
  const [startDate, endDate] = dateRange;
  // const pathname = useLocation().pathname;
  // const boardId = parseInt(useParams().boardId);
  // const type = pathname.includes("share") ? 2 : 1;

  useEffect(() => {
    if (startDate) {
      sendStartDate(startDate);
    }
  }, [startDate]);

  useEffect(() => {
    if (endDate) {
      sendEndDate(endDate);
    }
  }, [endDate]);
  // console.log("@@@@@", startDate, endDate);
  // useEffect(() => {
  //   type === 2
  //     ? getShareArticleByBoardId(boardId).then((res) => {
  //         const data = res[0].list;
  //         // console.log("@@@@@@", data);
  //         // const tempimage = data.map((d) => d);
  //         // const filename = tempimage.map((obj) => obj.fileName);
  //         setImageList(data);
  //       })
  //     : null;
  // }, []);
  return (
    <div
      css={css`
        & .react-datepicker-popper {
          z-index: 9999;
        }
      `}
    >
      <DatePicker
        selectsRange={true}
        minDate={new Date()} // 과거 날짜 disable
        showPopperArrow={false}
        startDate={startDate}
        endDate={endDate}
        onChange={(update) => {
          setDateRange(update);
        }}
        dateFormat="yyyy.MM.dd" // 시간 포맷 변경
        css={calendar}
        placeholderText="날짜를 설정해주세요."
      />
    </div>
  );
};

const calendar = css`
  padding: 14px 18px;
  font-size: 18px;
  background: #ffffff;
  border: 1px solid #e1e2e3;
  border-radius: 5px;
  width: 210px;
`;

export default ProductCalendar;
