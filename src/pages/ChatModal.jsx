import React from "react";
import Close from "../assets/images/Close.png";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import Luffy from "../assets/images/Luffy.png";
import OnePieceThumb from "../assets/images/OnePieceThumb.jfif";
function ChatModal() {
  const Chatts = [
    {
      profile: Luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: OnePieceThumb,
    },
    {
      profile: Luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: OnePieceThumb,
    },
    {
      profile: Luffy,
      nickname: "해적왕",
      location: "신세계",
      time: "1시간 전",
      lastChat: "해적왕은 나야",
      thumbnail: OnePieceThumb,
    },
  ];

  return (
    <div css={chatWrap}>
      <h2 css={ChatTitleWrap}>
        채팅목록
        <img src={Close} alt="" />
      </h2>

      {/* 채팅body 시작 */}
      <div
        css={css`
          padding: 15px;
        `}
      >
        {Chatts.length ? (
          Chatts.map((chat, idx) => (
            <div key={idx} css={ChatListWrap}>
              {/* 프로필 사진 */}
              <div>
                <img src={chat.profile} alt="" css={chatListProfile} />
              </div>
              {/* 닉네임, 거주장소, 최근 채팅시간, 채팅내용 */}
              <div>
                <div css={chatListInfoWrap}>
                  <div
                    css={css`
                      padding-top: 10px;
                    `}
                  >
                    <span
                      css={css`
                        font-size: 20px;
                        padding-right: 5px;
                      `}
                    >
                      {chat.nickname}
                    </span>
                    <span
                      css={css`
                        font-size: 15px;
                        color: #8a8a8a;
                        padding-right: 5px;
                      `}
                    >
                      {chat.location}
                    </span>
                    <span
                      css={css`
                        font-size: 15px;
                        color: #8a8a8a;
                      `}
                    >
                      {chat.time}
                    </span>
                  </div>
                  <div
                    css={css`
                      font-size: 20px;
                      padding-top: 10px;
                    `}
                  >
                    {chat.lastChat}
                  </div>
                </div>
              </div>
              {/* 물품썸네일 */}
              <div>
                <img src={chat.thumbnail} alt="" css={ChatListThumb} />
              </div>
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
}
const chatWrap = css`
  width: 655px;
  height: 200px;
  border: 1px solid gray;
  border-radius: 30px;
  float: right;
  overflow: auto;
  position: absolute;
`;

const ChatTitleWrap = css`
  display: flex;
  justify-content: space-between;
  padding: 30px;
`;

const NochatWrap = css`
  font-size: 25px;
  justify-content: center;
  display: flex;
  padding-top: 50%;
`;
const ChatListWrap = css`
  width: 97%;
  display: flex;
  justify-content: space-between;
  height: 95px;
  border: 1px solid #e1e2e3;
  border-radius: 30px;
  margin-bottom: 20px;
`;
const chatListProfile = css`
  width: 80px;
  height: 95px;
  object-fit: cover;
  border-radius: 100%;
`;

const chatListInfoWrap = css`
  width: 400px;
  height: 95px;
`;

const ChatListThumb = css`
  width: 120px;
  height: 95px;
  object-fit: cover;
  border-radius: 6%;
`;
export default ChatModal;
