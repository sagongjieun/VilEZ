import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { useRecoilValue } from "recoil";
import { shareDataState } from "../../recoil/atom";
import { getUserDetail } from "../../api/profile";

function OathGetModal() {
  const shareData = useRecoilValue(shareDataState);

  const [giver, setGiver] = useState("");
  const [receiver, setReceiver] = useState("");

  useEffect(() => {
    getUserDetail(shareData.shareUserId).then((res) => {
      setGiver(res.nickName);
    });
    getUserDetail(shareData.notShareUserId).then((res) => {
      setReceiver(res.nickName);
    });
  }, []);

  return (
    <div css={topWrap}>
      <div css={oathWrap}>
        <h3>{receiver}님의 서약서</h3>
        <div>
          <span>
            피공유자 <strong>{receiver}</strong>는 공유자 <strong>{giver}</strong>에게 물품을
          </span>
          <span>빌리며, {receiver}는 분실, 도난 기타 등의 이유로</span>
          <span>물품의 원래 형태로 복구가 불가능할 경우</span>
          <span>
            <b>민사, 형사상의 책임을 질 수 있음</b>을 확인합니다.
          </span>
        </div>
      </div>
    </div>
  );
}

const topWrap = css`
  position: fixed;
  left: 0px;
  top: 0px;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
`;

const oathWrap = css`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  width: 500px;
  height: 625px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px, rgba(0, 0, 0, 0.3) 0px 1px 3px -1px;
  border-radius: 10px;
  padding: 20px;
  background-color: white;
`;

export default OathGetModal;
