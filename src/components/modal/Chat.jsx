import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import closebutton from "../../assets/images/closebutton.png";
import luffy from "../../assets/images/luffy.png";
import onpiecethumb from "../../assets/images/onpiecethumb.jfif";
import jjangu from "../../assets/images/jjangu.png";
import sinhyeongman from "../../assets/images/sinhyeongman.jfif";

function ChatModal() {
  const [isOpen, setIsOpen] = useState(false);
  const onClickOpenChat = () => {
    setIsOpen(!isOpen);
  };
  const Chatts = [
    {
      profile: luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: onpiecethumb,
    },
    {
      profile: luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: onpiecethumb,
    },
    {
      profile: luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: onpiecethumb,
    },
    {
      profile: jjangu,
      nickname: "액션가면내놔",
      location: "테이블 속",
      time: "1시간 전",
      lastChat: "울랄라울랄라",
      thumbnail: sinhyeongman,
    },
    {
      profile: jjangu,
      nickname: "액션가면내놔",
      location: "테이블 속",
      time: "1시간 전",
      lastChat: "울랄라울랄라",
      thumbnail: sinhyeongman,
    },
  ];
  if (!isOpen) {
    return (
      <div css={chatWrap}>
        <div css={ChatTitleWrap}>
          채팅목록
          <img css={closeWrap} src={closebutton} alt="" onClick={onClickOpenChat} />
        </div>

        {/* 채팅body 시작 */}
        <div css={chatContentWrap}>
          {Chatts.length ? (
            Chatts.map((chat, idx) => (
              <div key={idx} css={ChatListWrap}>
                {/* 프로필 사진 */}
                <span>
                  <img src={chat.profile} alt="" css={chatListProfile} />
                </span>
                {/* 닉네임, 거주장소, 최근 채팅시간, 채팅내용 */}
                <div>
                  <div css={chatListInfoWrap}>
                    <div>
                      <span css={nicknameWrap}>
                        {/* 닉네임 */}
                        {chat.nickname}
                      </span>
                      <span css={locationWrap}>
                        {/* 동네 */}
                        {chat.location}
                      </span>
                      <span css={timeWrap}>
                        {/* 최근 채팅 시간 */}
                        {chat.time}
                      </span>
                    </div>
                  </div>
                  <div css={lastChatWrap}>
                    {/* 마지막 채팅 문장 */}
                    {chat.lastChat}
                  </div>
                </div>
                {/* 물품썸네일 */}
                <span css={ChatListThumb}>{/* <img src={chat.thumbnail} alt="" css={ChatListThumb} /> */}</span>
              </div>
            ))
          ) : (
            <div css={NochatWrap}>
              <div>채팅목록이 없어요</div>
            </div>
          )}
        </div>
      </div>
    );
  } else {
    return null;
  }
}
const chatWrap = css`
  width: 400px;
  height: 500px;
  border: 1px solid gray;
  border-radius: 30px;
  overflow: auto;
  position: fixed;
  bottom: 80px;
  right: 20px;
  background-color: #f5f6f7;
`;

const ChatTitleWrap = css`
  font-size: 22px;
  display: flex;
  justify-content: space-between;
  padding: 30px;
`;

const closeWrap = css`
  cursor: pointer;
`;

const NochatWrap = css`
  font-size: 25px;
  justify-content: center;
  display: flex;
  padding-top: 50%;
`;
const chatContentWrap = css`
  padding: 15px;
`;

const ChatListWrap = css`
  width: 97%;
  display: flex;
  justify-content: space-between;
  height: 70px;
  border: 1px solid #e1e2e3;
  border-radius: 10px;
  margin-bottom: 20px;
  align-items: center;
  overflow: hidden;
`;
const chatListProfile = css`
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 100%;
`;

const chatListInfoWrap = css`
  width: 190px;
  margin-right: 2px;
`;

const nicknameWrap = css`
  padding: 15px;
`;

const locationWrap = css`
  font-size: 12px;
  color: #8a8a8a;
  padding-right: 5px;
`;

const timeWrap = css`
  font-size: 12px;
  color: #8a8a8a;
`;

const lastChatWrap = css`
  font-size: 14px;
  padding-top: 10px;
`;

const ChatListThumb = css`
  width: 60px;
  height: 100%;
  background-image: url(${sinhyeongman});
  background-size: cover;
`;
export default ChatModal;
