import React, { useRef, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import SignatureCanvas from "react-signature-canvas";
import { jsonInstance } from "../../api/instance";
import { postCanvas } from "../../api/oath";
// https://stackblitz.com/edit/react-signature-canvas-demo?file=index.js
function Oath() {
  const jsonAxios = jsonInstance();
  const canvasRef = useRef(null);
  const onClickCanvas = async () => {
    const canvas = canvasRef.current.getTrimmedCanvas().toDataURL("image/png");
    // console.log(canvas);
    // const dataURL = canvas.toDataURL();
    const information = {
      boardId: 1,
      userId: 52,
      notUserId: 55,
      shareSign: canvas,
      notShareSign: canvas,
    };
    postCanvas(information);
    // console.log(information);
  };

  const [base64Img, setBase64Img] = useState("");

  const getOath = async () => {
    const completeinformation = {
      boardId: 1,
      notUserId: 55,
      shareUserId: 52,
    };
    try {
      const { data } = await jsonAxios.get(
        `/signs/?boardId=${completeinformation.boardId}&notUserId=${completeinformation.notUserId}&shareUserId=${completeinformation.shareUserId}`
      );
      // console.log(data.data[0].shareSign);
      const base64Encoded = data.data[0].shareSign;
      console.log(base64Encoded);
      setBase64Img(data.data[0].shareSign);
    } catch (error) {
      console.log(error);
    }
  };

  const giver = "공유자";
  const reciever = "피공유자";
  const stuff = "노트북";

  const [isSign, setIsSign] = useState(false);

  // 확정 버튼 누르면 서버로 전송할 api
  return (
    <div css={oathWrap}>
      <div css={oathTitle}>서약서</div>
      <div css={oathContentWrap}>
        <div>
          {giver}는 피공유자 {reciever}에게 {stuff}을(를)
        </div>
        <div>공유하며 {reciever}는 분실, 도난 기타 등의 이유로</div>
        <div>물품의 원래 형태로 복구가 불가능할 경우</div>
        <div>민사, 형사상의 책임을 질 수 있음을 확인합니다. </div>
      </div>
      <div css={signWrap}>
        {!isSign && <div css={signContentWrap}>여기에 서명을 해주세요</div>}
        <div>
          <SignatureCanvas
            ref={canvasRef}
            backgroundColor="#E8E8E8"
            canvasProps={{ width: 200, height: 100 }}
            onBegin={() => {
              setIsSign(true);
            }}
          />
        </div>
        <div>
          <button
            css={oathButton}
            onClick={() => {
              canvasRef.current.clear();
              setIsSign(false);
            }}
          >
            다시쓰기
          </button>
        </div>
      </div>
      <div>상기 내용을 모두 이해하고, 동의하시면 확정을 눌러주세요</div>
      <div>
        <button css={cancelButton}>아니오</button>
        <button css={completeButton} onClick={onClickCanvas}>
          확정
        </button>
      </div>
      <button onClick={getOath}>서명보기</button>
      <img src={base64Img} alt="" />
    </div>
  );
}
const oathWrap = css`
  display: flex;
  padding-left: 5px;
  padding-top: 5px;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 800px;
  width: 500px;
  border: 1px solid gray;
  border-radius: 20px;
  box-shadow: 1px 1px 2px;
`;

const oathTitle = css`
  margin-top: 50px;
  font-size: 25px;
  font-weight: bold;
`;

const signWrap = css`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const signContentWrap = css`
  position: absolute;
  display: flex;
  align-items: center;
  margin-bottom: 80px;
  font-size: 12px;
`;

const oathButton = css`
  width: 140px;
  height: 80px;
  border-radius: 5px;
  border: gray;
  background-color: white;
  cursor: pointer;
`;

const cancelButton = css`
  font-size: 14px;
  width: 127px;
  height: 45px;
  background-color: #d7d9dc;
  color: white;
  border: none;
  border-radius: 5px;
  margin-right: 50px;
`;

const completeButton = css`
  font-size: 14px;
  width: 127px;
  height: 45px;
  background-color: #66dd9c;
  color: white;
  border: none;
  border-radius: 5px;
  margin-left: 50px;
`;

const oathContentWrap = css`
  width: 100%;
  text-align: center;
  & > div {
    padding: 10px;
  }
`;
export default Oath;
