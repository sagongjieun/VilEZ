import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { Link } from "react-router-dom";
import { FiSearch, FiBookmark } from "react-icons/fi";
import { CgProfile } from "react-icons/cg";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import { loginUserState } from "../../recoil/atom";

function MainNavBar() {
  const navigate = useNavigate();
  const setLoginUser = useSetRecoilState(loginUserState);

  const menus = [
    { name: "물품 공유 목록", path: "/product" }, // 임시 path
    { name: "물품 요청 목록", path: "/product" }, // 임시 path
    { name: "글 등록하기", path: "/product/regist" },
  ];

  const [isMenu, setIsMenu] = useState(false);
  const [isLogin, setIsLogin] = useState(false);

  function onClickShowMenu() {
    setIsMenu(!isMenu);
  }

  function onClickMoveLogin() {
    navigate("/login");
  }

  function onClickMoveMyBookmark() {
    // 내가 북마크하고 에약한 페이지로 이동
  }

  function onClickMoveMyPage() {
    navigate("/profile");
  }

  function onClickMoveSearchPage() {
    navigate("/product");
  }

  function onClickLogout() {
    localStorage.clear();

    setIsLogin(false);
    setLoginUser(null);
  }

  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      setIsLogin(true);
    } else {
      setIsLogin(false);
    }
  }, []);

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
                  <FiBookmark onClick={onClickMoveMyBookmark} />
                  <CgProfile onClick={onClickMoveMyPage} />
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
  margin-right: 10px;

  & > svg {
    font-size: 25px;
    margin-right: 15px;
    color: #66dd9c;
    cursor: pointer;
  }
`;

export default MainNavBar;
