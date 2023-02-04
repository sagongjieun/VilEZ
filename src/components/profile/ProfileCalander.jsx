import React, { useCallback, useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getAppointmentsByUserId } from "../../api/appointment";

const Calendar = () => {
  const today = {
    year: new Date().getFullYear(), //오늘 연도
    month: new Date().getMonth() + 1, //오늘 월
    date: new Date().getDate(), //오늘 날짜
    day: new Date().getDay(), //오늘 요일
  };
  const week = ["일", "월", "화", "수", "목", "금", "토"]; //일주일
  const [selectedYear, setSelectedYear] = useState(today.year); //현재 선택된 연도
  const [selectedMonth, setSelectedMonth] = useState(today.month); //현재 선택된 달
  const dateTotalCount = new Date(selectedYear, selectedMonth, 0).getDate(); //선택된 연도, 달의 마지막 날짜
  const [appointments, setAppointments] = useState([]);
  const [nowStartAppointments, setNowStartAppointments] = useState([]);
  const [nowEndAppointments, setNowEndAppointments] = useState([]);
  useEffect(() => {
    getAppointmentsByUserId(29).then((response) => {
      setAppointments(response[0]);
    });
  }, []);
  useEffect(() => {
    const start = appointments?.filter((appoint) => {
      appoint.appointmentStart.slice(0, 4) == String(selectedYear) &&
        appoint.appointmentStart.slice(5, 7) == ("0" + String(selectedMonth)).slice(-2);
    });
    const end = appointments?.filter((appoint) => {
      appoint.appointmentEnd.slice(0, 4) == String(selectedYear) &&
        appoint.appointmentEnd.slice(5, 7) == ("0" + String(selectedMonth)).slice(-2);
    });
    setTimeout(() => {
      console.log(start);
    }, 2000);
    setNowStartAppointments(start);
    setNowEndAppointments(end);
  }, [appointments]);
  const prevMonth = useCallback(() => {
    //이전 달 보기 버튼
    if (selectedMonth === 1) {
      setSelectedMonth(12);
      setSelectedYear((prev) => prev - 1);
    } else {
      setSelectedMonth((prev) => prev - 1);
    }
  }, [selectedMonth]);

  const nextMonth = useCallback(() => {
    //다음 달 보기 버튼
    if (selectedMonth === 12) {
      setSelectedMonth(1);
      setSelectedYear(selectedYear + 1);
    } else {
      setSelectedMonth(selectedMonth + 1);
    }
  }, [selectedMonth]);

  const monthControl = useCallback(() => {
    //달 선택박스에서 고르기
    let monthArr = [];
    for (let i = 0; i < 12; i++) {
      monthArr.push(
        <option key={i + 1} value={i + 1}>
          {i + 1 > 9 ? i + 1 : `0${i + 1}`}
        </option>
      );
    }
    return (
      <select onChange={changeSelectMonth} value={selectedMonth}>
        {monthArr}
      </select>
    );
  }, [selectedMonth]);

  const yearControl = useCallback(() => {
    //연도 선택박스에서 고르기
    let yearArr = [];
    const startYear = today.year - 10; //현재 년도부터 10년전 까지만
    const endYear = today.year + 10; //현재 년도부터 10년후 까지만
    for (let i = startYear; i < endYear + 1; i++) {
      yearArr.push(
        <option key={i} value={i}>
          {i}
        </option>
      );
    }
    return (
      <select onChange={changeSelectYear} value={selectedYear}>
        {yearArr}
      </select>
    );
  }, [selectedYear]);

  const changeSelectMonth = (e) => {
    setSelectedMonth(Number(e.target.value));
  };
  const changeSelectYear = (e) => {
    setSelectedYear(Number(e.target.value));
  };

  const returnWeek = useCallback(() => {
    //요일 반환 함수
    let weekArr = [];
    week.forEach((v) => {
      weekArr.push(<div key={v}>{v}</div>);
    });
    return weekArr;
  }, []);

  const returnDay = useCallback(() => {
    //선택된 달의 날짜들 반환 함수
    let dayArr = [];
    for (const nowDay of week) {
      const day = new Date(selectedYear, selectedMonth - 1, 1).getDay();
      if (week[day] === nowDay) {
        for (let i = 0; i < dateTotalCount; i++) {
          dayArr.push(
            <div key={i + 1}>
              <div css={date}>{i + 1}</div>
              <div css={toDo}>
                {nowStartAppointments?.map((appoint) => (
                  // const startDate = appoint.appointmentStart.slice(8, 10);
                  // startDate == String(date) ? <div key={appoint.appointmentId}>{appoint.title}</div> : null;
                  <div key={appoint.id}>{appoint.id}</div>
                ))}
              </div>
            </div>
          );
        }
      } else {
        dayArr.push(<div css={blankDay}></div>);
      }
    }

    return dayArr;
  }, [selectedYear, selectedMonth, dateTotalCount, appointments, nowStartAppointments, nowEndAppointments]);

  return (
    <div css={container}>
      <div>
        <button onClick={prevMonth}>◀︎</button>
        <div>
          {yearControl()}
          <span>.</span>
          {monthControl()}
        </div>
        <button onClick={nextMonth}>▶︎</button>
      </div>
      <div>{returnWeek()}</div>
      <div>{returnDay()}</div>
    </div>
  );
};

const container = css`
  width: 1000px;
  height: 800px;
  margin: auto;
  padding: 20px 20px;
  /* border: 1px solid rgba(128, 128, 128, 0.267); */
  box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  & * {
    box-sizing: border-box;
    text-align: left;
  }
  & span {
    font-size: 20px;
  }
  & select {
    border: none;
    font-size: 20px;
    -webkit-appearance: none;
  }
  & > div:nth-of-type(1) {
    display: flex;
    justify-content: center;
    font-size: 16px;
    font-weight: bold;
    & > div {
      width: 100px;
      display: flex;
      justify-content: center;
    }
    & button {
      cursor: pointer;
      border: none;
      padding: 0 10px;
      align-self: center;
      background: none;
    }
  }
  & > div:nth-of-type(2) {
    //week
    display: flex;
    font-size: 16px;
    margin-top: 20px;
    & > div {
      width: calc(100% / 7);
      text-align: left;
      padding: 0 4px;
      /* background-color: aqua; */
    }
  }
  & > div:nth-of-type(3) {
    // date
    margin-top: 10px;
    font-size: 16px;
    & > div {
      width: calc(100% / 7);
      float: left;
      padding-top: 4px;
      text-align: center;
      height: 120px;
      border-bottom: 1px solid #efefef;
    }
  }
`;

const blankDay = css`
  border: none !important;
`;

const date = css``;

const toDo = css`
  margin-top: 5px;
  & > div {
    color: #000;
    background-color: aliceblue;
    border: 1px solid #fafafa;
    font-size: 14px;
    text-align: left;
    padding: 0 4px;
  }
`;

export default Calendar;
