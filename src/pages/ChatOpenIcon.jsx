import React, { useState } from "react";
import { BsChatSquare } from "react-icons/bs";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import Chat from "../components/modal/Chat";
function ChatOpenIcon() {
  const [isOpen, setIsOpen] = useState(false);
  const onClickOpenChat = () => {
    setIsOpen(!isOpen);
  };
  return (
    <div>
      <div css={IconBox} onClick={onClickOpenChat}>
        <BsChatSquare />
      </div>
      {isOpen ? <Chat /> : null}
    </div>
  );
}
const IconBox = css`
  bottom: 30px;
  right: 30px;
  width: 40px;
  height: 40px;
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
