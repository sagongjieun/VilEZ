import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { Link } from "react-router-dom";
import { FiSearch } from "react-icons/fi";
import { HiArchiveBox } from "react-icons/hi2";
// import { CgProfile } from "react-icons/cg";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { loginUserState } from "../../recoil/atom";
import { postLogout } from "../../api/user";

function MainNavBar() {
  const navigate = useNavigate();

  const nickName = localStorage.getItem("nickName");
  const profileImg = localStorage.getItem("profileImg");
  const [loginUser, setLoginUser] = useRecoilState(loginUserState);

  const menus = [
    { name: "물품 공유 목록", path: "/product/list/share" },
    { name: "물품 요청 목록", path: "/product/list/ask" },
    { name: "글 등록하기", path: "/product/regist" },
  ];

  const userId = localStorage.getItem("id");

  const [isMenu, setIsMenu] = useState(false);
  const [isLogin, setIsLogin] = useState(false);

  function onClickShowMenu() {
    setIsMenu(!isMenu);
  }

  function onClickMoveLogin() {
    navigate("/login");
  }

  function onClickMoveMyBox() {
    navigate("/mybox");
  }

  function onClickMoveMyPage() {
    navigate("/profile/product");
  }

  function onClickMoveSearchPage() {
    navigate("/product/list/share");
  }

  async function onClickLogout() {
    const response = postLogout({ id: userId });

    if (response) {
      localStorage.clear();
      setLoginUser(null);
      navigate("/");
    }
  }

  /** 로그인 유지 변경하기 */
  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      setIsLogin(true);
    } else {
      setIsLogin(false);
    }
  }, [loginUser]);

  return (
    <>
      {isLogin ? (
        <nav css={navWrap}>
          <div css={navStyle}>
            <div>
              <Link to={"/"}>VilEZ</Link>
            </div>
            <div css={MidWrap}>
              <div>
                <div css={navMid} onClick={onClickShowMenu}>
                  마을 글보기
                </div>
                {isMenu ? (
                  <div css={MenuWrap}>
                    {menus.map((menu, idx) => (
                      <Link to={menu.path} key={idx} onClick={onClickShowMenu}>
                        <div css={navMenu}>{menu.name}</div>
                      </Link>
                    ))}
                  </div>
                ) : (
                  <></>
                )}
              </div>
              <div>
                <div css={midrightWrap}>
                  <FiSearch onClick={onClickMoveSearchPage} />
                  <HiArchiveBox onClick={onClickMoveMyBox} />
                  <div onClick={onClickMoveMyPage}>
                    <div>
                      <img src={profileImg} />
                    </div>
                    <div>
                      <div>
                        <img src={profileImg} />
                      </div>
                      <div>{nickName}</div>
                    </div>
                  </div>
                  {/* <CgProfile onClick={onClickMoveMyPage} /> */}
                </div>
                <span onClick={onClickLogout}>로그아웃</span>
              </div>
            </div>
          </div>
        </nav>
      ) : (
        // false(not login 상태)
        <nav css={navWrap}>
          <div css={navStyle}>
            <div>
              <Link to={"/"}>VilEZ</Link>
            </div>
            <div css={MidWrap}>
              <div>
                <div css={navMid} onClick={onClickShowMenu}>
                  마을 글보기
                </div>
                {isMenu ? (
                  <div css={MenuWrap}>
                    {menus.map((menu, idx) => (
                      <Link to={menu.path} key={idx} onClick={onClickShowMenu}>
                        <div css={navMenu}>{menu.name}</div>
                      </Link>
                    ))}
                  </div>
                ) : (
                  <></>
                )}
              </div>
              <div>
                <div css={midrightWrap}>
                  <FiSearch onClick={onClickMoveSearchPage} />
                </div>
                <span onClick={onClickMoveLogin}>마을 입장</span>
              </div>
            </div>
          </div>
        </nav>
      )}
    </>
  );
}
const navWrap = css`
  position: sticky;
  top: 0px;
  z-index: 100;
`;

const navStyle = css`
  display: flex;
  height: 70px;
  background-color: white;
  border-bottom: 1px solid #e2e2e2;
  flex-direction: row;
  align-items: center;
  padding: 0 200px 0 0;

  & > div:nth-of-type(1) {
    width: 200px;
    display: flex;
    align-items: center;
    justify-content: center;

    & > a {
      color: #66dd9c;
      font-weight: Bold;
      font-size: 36px;
    }
  }
`;

const MidWrap = css`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;

  & > div:nth-of-type(2) {
    display: flex;
    flex-direction: row;

    & > span {
      color: black;
      cursor: pointer;
    }
  }
`;

const navMid = css`
  background-color: #66dd9c;
  color: white;
  border-radius: 30px;
  padding: 10px 40px;
  text-align: center;
  cursor: pointer;
`;

const MenuWrap = css`
  position: absolute;
`;

// box-shadox : x(왼->우), y(아->위), 흐린정도, 그림자크기,색상
const navMenu = css`
  background-color: white;
  color: #66dd9c;
  border: 0.1px solid #e2e2e2;
  box-shadow: 5px 5px 5px gray;
  border-radius: 30px;
  width: 125px;
  height: 35px;
  padding: 5px 15px;
  font-size: 17px;
  text-align: center;
  line-height: 35px;
  cursor: pointer;
  margin-top: 5px;
`;

const midrightWrap = css`
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-right: 20px;

  & > svg {
    font-size: 25px;
    margin-right: 15px;
    color: #66dd9c;
    cursor: pointer;
  }

  // 프로필 사진
  & > div {
    position: relative;
    cursor: pointer;
    width: 35px;
    height: 35px;

    // 프로필 사진 동그라미
    & > div:nth-of-type(1) {
      box-sizing: border-box;
      width: 100%;
      height: 100%;
      border-radius: 100%;
      overflow: hidden;
      border: 2px solid #fff;
      transition: all 0.3s;
      :hover {
        border: 2px solid #66dd9c;
      }
      & > img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    :hover > div:nth-of-type(2) {
      visibility: visible;
      opacity: 1;
    }

    // hover시 나오는 닉네임 + 프로필 사진
    & > div:nth-of-type(2) {
      visibility: hidden;
      opacity: 0;
      position: absolute;
      box-sizing: border-box;
      top: 36px;
      left: -25px;
      width: 80px;
      padding: 10px;
      font-size: 14px;
      background-color: #fff;
      border-radius: 5px;
      box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.1);
      transition: all 0.3s;
      & > div:nth-of-type(1) {
        width: 50px;
        height: 50px;
        margin: 0 auto;
        border-radius: 50%;
        overflow: hidden;
        & > img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
      & > div:nth-of-type(2) {
        text-align: center;
        margin-top: 10px;
        font-size: 12px;
      }
    }
  }
`;

export default MainNavBar;
