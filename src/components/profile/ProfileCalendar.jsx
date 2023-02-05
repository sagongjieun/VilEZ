import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { format, addMonths, subMonths } from "date-fns";
import { startOfMonth, endOfMonth, startOfWeek, endOfWeek } from "date-fns";
// import { isSameMonth, isSameDay, addDays, parse } from "date-fns";
import { isSameMonth, isSameDay, addDays } from "date-fns";

const RenderHeader = ({ currentMonth, prevMonth, nextMonth }) => {
  return (
    <div css={headerWrapper}>
      <button onClick={prevMonth}>이전</button>
      <div>
        <span>{format(currentMonth, "yyyy")}</span>.<span>{format(currentMonth, "M").padStart(2, "0")}</span>
      </div>
      <button onClick={nextMonth}>다음</button>
    </div>
  );
};

const RenderDays = () => {
  const days = [];
  const date = ["Sun", "Mon", "Thu", "Wed", "Thrs", "Fri", "Sat"];

  for (let i = 0; i < 7; i++) {
    days.push(<div key={i}>{date[i]}</div>);
  }

  return <div css={daysWrapper}>{days}</div>;
};

// const RenderCells = ({ currentMonth, selectedDate, onDateClick }) => {
const RenderCells = ({ currentMonth, selectedDate }) => {
  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(monthStart);
  const startDate = startOfWeek(monthStart);
  const endDate = endOfWeek(monthEnd);

  const rows = [];
  let days = [];
  let day = startDate;
  let formattedDate = "";

  // appointment 데이터 삽입

  const [appointList, setAppointList] = useState([]);
  function appointmentsStartForDay(day) {
    const filteredAppointments = appointList.filter(
      (appoint) =>
        day.getFullYear() + "-0" + (day.getMonth() + 1) + "-" + day.getDate() === appoint.appointmentStart ||
        day.getFullYear() + "-0" + (day.getMonth() + 1) + "-0" + day.getDate() === appoint.appointmentStart ||
        day.getFullYear() + "-" + (day.getMonth() + 1) + "-" + day.getDate() === appoint.appointmentStart ||
        day.getFullYear() + "-" + (day.getMonth() + 1) + "-0" + day.getDate() === appoint.appointmentStart
    );
    return filteredAppointments;
  }
  function appointmentsEndForDay(day) {
    const filteredAppointments = appointList.filter(
      (appoint) =>
        day.getFullYear() + "-0" + (day.getMonth() + 1) + "-" + day.getDate() === appoint.appointmentEnd ||
        day.getFullYear() + "-0" + (day.getMonth() + 1) + "-0" + day.getDate() === appoint.appointmentEnd ||
        day.getFullYear() + "-" + (day.getMonth() + 1) + "-" + day.getDate() === appoint.appointmentEnd ||
        day.getFullYear() + "-" + (day.getMonth() + 1) + "-0" + day.getDate() === appoint.appointmentEnd
    );
    return filteredAppointments;
  }
  useEffect(() => {
    const dummyData = [
      {
        appointmentId: 1,
        appointmentStart: "2023-02-05",
        appointmentEnd: "2023-02-07",
        state: 1,
        title: "내가 공유함",
      },
      {
        appointmentId: 1,
        appointmentStart: "2023-02-06",
        appointmentEnd: "2023-02-10",
        state: 1,
        title: "내가 공유함 전동드리힐힐힐",
      },
      {
        appointmentId: 1,
        appointmentStart: "2023-02-12",
        appointmentEnd: "2023-02-14",
        state: 2,
        title: "내가 대여함",
      },
      {
        appointmentId: 1,
        appointmentStart: "2023-02-15",
        appointmentEnd: "2023-02-17",
        state: 2,
        title: "내가 대여함",
      },
    ];
    setAppointList(dummyData);
  }, []);

  while (day <= endDate) {
    for (let i = 0; i < 7; i++) {
      formattedDate = format(day, "d");
      // const cloneDay = day;
      days.push(
        <div
          css={
            !isSameMonth(day, monthStart)
              ? disabled
              : isSameDay(day, selectedDate)
              ? selected
              : format(currentMonth, "M") !== format(day, "M")
              ? notValid
              : valid
          }
          key={day}
          // onClick={() => onDateClick(parse(cloneDay))}
        >
          <span css={format(currentMonth, "M") !== format(day, "M") ? notValid : null}>{formattedDate}</span>
          {appointmentsStartForDay(day).map((appoint, idx) => (
            <div key={idx} css={appoint.state === 1 ? shareStartDay : rentStartDay}>
              {appoint.title}
            </div>
          ))}
          {appointmentsEndForDay(day).map((appoint, idx) => (
            <div key={idx} css={appoint.state === 1 ? shareEndDay : rentEndDay}>
              {appoint.title}
            </div>
          ))}
        </div>
      );
      day = addDays(day, 1);
    }
    rows.push(<div key={day}>{days}</div>);
    days = [];
  }
  return <div css={bodyWrapper}>{rows}</div>;
};

const ProfileCalender2 = () => {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const selectedDate = new Date();

  const prevMonth = () => {
    setCurrentMonth(subMonths(currentMonth, 1));
  };
  const nextMonth = () => {
    setCurrentMonth(addMonths(currentMonth, 1));
  };
  // const onDateClick = (day) => {
  //   setSelectedDate(day);
  // };
  return (
    <div css={calendarContainer}>
      <RenderHeader currentMonth={currentMonth} prevMonth={prevMonth} nextMonth={nextMonth} />
      <RenderDays />
      {/* <RenderCells currentMonth={currentMonth} selectedDate={selectedDate} onDateClick={onDateClick} /> */}
      <RenderCells currentMonth={currentMonth} selectedDate={selectedDate} />
    </div>
  );
};
const calendarContainer = css`
  background-color: aliceblue;
  width: calc(100% - 100px);
  margin: 0 auto;
`;
const headerWrapper = css`
  background-color: red;
  display: flex;
  justify-content: center;
`;
const daysWrapper = css`
  background-color: yellow;
  display: flex;
  > div {
    width: calc(100% / 7);
  }
`;
const bodyWrapper = css`
  background-color: blue;
  > div {
    display: flex;
    > div {
      // 각 날짜
      width: calc(100% / 7);
      height: 110px;
      border: 1px solid #fff;
      cursor: pointer;
      > div {
        // 각 일정
        font-size: 14px;
        height: 20px;
        line-height: 20px;
        overflow: hidden;
      }
    }
  }
`;
// 해당 월의 날짜가 아님
const notValid = css``;
// 해당 월의 날짜가 아님
const disabled = css`
  font-weight: bold;
  background-color: antiquewhite;
`;
// 오늘 날짜
const selected = css`
  color: red;
  background-color: aqua;
`;
const valid = css`
  background-color: beige;
`;

const shareStartDay = css`
  background-color: aliceblue;
`;
const shareEndDay = css`
  background-color: #76aedf;
`;
const rentStartDay = css`
  background-color: #487092;
`;
const rentEndDay = css`
  background-color: #a8c1d6;
`;

export default ProfileCalender2;
