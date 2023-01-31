import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import SmallWideButton from "../button/SmallWideButton";
import { GrClose } from "react-icons/gr";
import { ko } from "date-fns/locale";

const CalendarModal = ({ setCalendarModalOpen }) => {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  const [selectedStartDay, setSelectedStartDay] = useState("");
  const [selectedEndDay, setSelectedEndDay] = useState("");
  // eslint-disable-next-line no-unused-vars
  const [blockDate, setBlockDate] = useState([
    // 임시 데이터
    { start: new Date(2023, 1, 5), end: new Date(2023, 1, 15) },
    { start: new Date(2023, 2, 5), end: new Date(2023, 2, 15) },
  ]);

  const onChange = (dates) => {
    setSelectedEndDay("");
    const [start, end] = dates;
    setStartDate(start);
    setEndDate(end);
  };

  function onClickCloseModal() {
    setCalendarModalOpen(false);
  }

  useEffect(() => {
    if (startDate && endDate) {
      let syear = startDate.getFullYear();
      let smonth = startDate.getMonth() + 1;
      let sday = startDate.getDate();
      setSelectedStartDay(syear + "년 " + smonth + "월 " + sday + "일");

      let eyear = endDate.getFullYear();
      let emonth = endDate.getMonth() + 1;
      let eday = endDate.getDate();
      setSelectedEndDay(eyear + "년 " + emonth + "월 " + eday + "일");
    }
  }, [startDate, endDate]);

  return (
    <div css={modalWrapper}>
      <div css={calendarModalWrapper}>
        <GrClose onClick={onClickCloseModal} size="20" />
        <DatePicker
          locale={ko}
          minDate={new Date()} // 과거 날짜 disable
          onChange={onChange}
          startDate={startDate}
          endDate={endDate}
          excludeDateIntervals={blockDate}
          selectsRange
          selectsDisabledDaysInRange
          inline
        />
        {selectedStartDay && selectedEndDay ? (
          <span>
            {selectedStartDay} ~ {selectedEndDay}
          </span>
        ) : (
          <span>공유 기간을 설정해주세요!</span>
        )}
        <div>
          <SmallWideButton text={"물건 공유 기간 확정"} />
        </div>
      </div>
    </div>
  );
};

const modalWrapper = css`
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
`;

const calendarModalWrapper = css`
  width: 380px;
  height: 450px;
  padding: 20px;
  border: none;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #ffffff;
  border-radius: 30px;
  box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px, rgba(0, 0, 0, 0.3) 0px 1px 3px -1px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  & > svg {
    position: absolute;
    top: 20px;
    right: 20px;
    cursor: pointer;
  }

  & > span {
    margin-top: 20px;
  }

  & > div:nth-of-type(1) {
    & > div {
      & > div:nth-of-type(2) {
        & > div:nth-of-type(1) {
          background-color: #66dd9c;

          & > div:nth-of-type(1) {
            color: white;
          }

          & > div:nth-of-type(3) {
            & > div {
              color: white;
            }
          }
        }
      }
    }

    .react-datepicker__day--keyboard-selected,
    .react-datepicker__day--in-range,
    .react-datepicker__day--selected:hover,
    .react-datepicker__day--in-selecting-range:hover,
    .react-datepicker__day--in-range:hover,
    .react-datepicker__month-text--selected:hover,
    .react-datepicker__month-text--in-selecting-range:hover,
    .react-datepicker__month-text--in-range:hover,
    .react-datepicker__quarter-text--selected:hover,
    .react-datepicker__quarter-text--in-selecting-range:hover,
    .react-datepicker__quarter-text--in-range:hover,
    .react-datepicker__year-text--selected:hover,
    .react-datepicker__year-text--in-selecting-range:hover,
    .react-datepicker__year-text--in-range:hover {
      background-color: #66dd9c;
    }
  }

  & > div:nth-of-type(2) {
    width: 180px;
    margin-top: 10px;
  }
`;

export default CalendarModal;
