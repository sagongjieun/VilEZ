import React, { useState } from "react";
import { BsChatSquare } from "react-icons/bs";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import ChattingModal from "../components/modal/ChattingModal";

function ChatOpenIcon() {
  const [isOpen, setIsOpen] = useState(false);

  const onClickOpenChat = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <div css={IconBox} onClick={onClickOpenChat}>
        <BsChatSquare size="25" />
      </div>
      {isOpen ? <ChattingModal setIsOpen={setIsOpen} /> : null}
    </>
  );
}

const IconBox = css`
  bottom: 30px;
  right: 30px;
  width: 60px;
  height: 60px;
  border-radius: 100%;
  color: white;
  border: 1px solid white;
  background-color: #66dd9c;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: fixed;
  box-shadow: 5px 3px 10px rgba(0, 0, 0, 0.35);
  z-index: 100;
`;

export default ChatOpenIcon;
