import React, { useState, useEffect } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { getAppointmentsWithinAWeek } from "../../api/appointment";
import ProfileDdaySlide from "./ProfileDdaySlide";

const ProfileDday = () => {
  const userId = localStorage.getItem("id");
  const [appointmentsWithinAWeek, setAppointmentsWithinAWeek] = useState([]);
  useEffect(() => {
    getAppointmentsWithinAWeek(userId).then((response) => {
      setAppointmentsWithinAWeek(response);
      // console.log(response);
      // console.log(appointmentsWithinAWeek);
    });
  }, []);
  return (
    <div css={ddayWrapper}>
      <div>
        <ProfileDdaySlide ddaySlideList={appointmentsWithinAWeek} />
      </div>
    </div>
  );
};

const ddayWrapper = css`
  position: relative;
  height: 60%;
`;
export default ProfileDday;
