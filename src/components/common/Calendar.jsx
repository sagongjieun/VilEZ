import React, { useEffect, useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const Calendar = ({ sendStartDate, sendEndDate }) => {
  const [dateRange, setDateRange] = useState([null, null]);
  const [startDate, endDate] = dateRange;

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

  return (
    <div
      css={css`
        width: 250px;

        & .react-datepicker-popper {
          z-index: 9999;
        }
      `}
    >
      <DatePicker
        selectsRange={true}
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
  width: 200px;
`;

export default Calendar;
