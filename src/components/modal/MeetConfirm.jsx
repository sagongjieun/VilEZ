import React from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

function MeetConfirm({ close, openOath }) {
  // function MeetConfirm({ 전달할 key이름을 넣는 것, 태그에서 사용할 때 <MeetConfirm (MeetConfirm에서 사용하는 키 이름) : (작성중인 컴포넌트에서 사용하는 이름)})
  const user = { nickname: "회먹고싶다요" };
  const startdate = "2023.01.19";
  const enddate = "2023.01.23";

  function closeModal() {
    close(false);
  }
  function onClickOpenOath() {
    openOath(true);
    close(false);
  }
  return (
    <div>
      {close && (
        <div css={modalTop}>
          <div css={ModalWrap}>
            <strong>{user.nickname}님과</strong>
            <div>
              <strong>
                {startdate} ~ {enddate}
              </strong>
            </div>
            <div>기간동안</div>
            <div>물품을 공유하시겠어요?</div>
            <div css={buttonWrap}>
              <button css={badbutton} onClick={closeModal}>
                취소
              </button>
              <button css={goodbutton} onClick={onClickOpenOath}>
                제출하기
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
const modalTop = css`
  position: fixed;
  width: 100%;
  height: 100%;
  left: 0px;
  top: 0px;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const ModalWrap = css`
  position: absolute;
  font-size: 20px;
  margin: auto;
  margin-bottom: 100px;
  width: 600px;
  height: 450px;
  box-shadow: 1px 1px 5px;
  border-radius: 10px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  > div {
    padding: 10px;
  }
  background-color: white;
`;
const buttonWrap = css`
  margin-top: 50px;
`;

const goodbutton = css`
  width: 105px;
  background-color: #66dd9c;
  color: white;
  border: none;
  height: 45px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
`;
const badbutton = css`
  width: 105px;
  background-color: #aeaeae;
  color: white;
  border: none;
  height: 45px;
  font-size: 14px;
  border-radius: 5px;
  margin-right: 30px;
  cursor: pointer;
`;
export default MeetConfirm;
