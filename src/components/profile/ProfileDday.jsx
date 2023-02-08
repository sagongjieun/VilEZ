import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getAppointmentsWithinAWeek } from "../../api/appointment";

const ProfileDday = () => {
  const userId = localStorage.getItem("id");
  const [appointmentsWithinAWeek, setAppointmentsWithinAWeek] = useState([]);
  useEffect(() => {
    getAppointmentsWithinAWeek(userId).then((response) => {
      setAppointmentsWithinAWeek(response);
      console.log(response);
      console.log(appointmentsWithinAWeek);
    });
  }, []);
  return (
    <div css={ddayWrapper}>
      <div>
        <h4>잊지 마세요</h4>
      </div>
    </div>
  );
};

const ddayWrapper = css`
  height: 50%;
  padding: 6px 0px;
  & > div {
    padding: 10px;
    & > h4 {
    }
  }
`;
export default ProfileDday;
