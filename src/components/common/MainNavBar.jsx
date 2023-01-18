import React, { useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
import { Link } from "react-router-dom";
// import { FaSearch } from "react-icons/fa";

function MainNavBar() {
  const menus = [
    { name: "물품 공유 목록", path: "" },
    { name: "물품 요청 목록", path: "" },
    { name: "글 등록하기", path: "/product/regist" },
  ];
  const [isMenu, setIsMenu] = useState(false);
  const onClickShowMenu = () => {
    setIsMenu(!isMenu);
    // console.log(isMenu);
  };
  return (
    <nav css={navWrap}>
      <div css={navStyle}>
        <Link to={"/"}>
          <p css={navLeft}>VilEZ</p>
        </Link>
        <div css={MidWrap}>
          <div
            css={css`
              position: relative;
            `}
          >
            <p css={navMid} onClick={onClickShowMenu}>
              마을 글보기
            </p>
            {isMenu ? (
              <div css={MenuWrap}>
                {menus.map((menu, idx) => (
                  <Link to={menu.path} key={idx} onClick={onClickShowMenu}>
                    <div css={navMenu}>{menu.name}</div>
                  </Link>
                ))}
              </div>
            ) : null}
          </div>
          <input type="text" placeholder="물품을 검색해보세요" css={[navInput, navInputPlaceHolder]} />
        </div>
        <div css={navRight}>
          <Link to={"/login"}>
            <p
              css={css`
                color: black;
              `}
            >
              마을 입장
            </p>
          </Link>
        </div>
      </div>
    </nav>
  );
}
const navWrap = css`
  position: sticky;
  top: 0px;
  z-index: 100;
`;

const navStyle = css`
  display: flex;
  width: device-width;
  height: 70px;
  background-color: white;
  border: 0.1px solid gray;
`;

const navLeft = css`
  width: 200px;
  color: #66dd9c;
  font-weight: Bold;
  font-size: 36px;
  text-align: center;
  line-height: 70px;
`;

const navMid = css`
  width: 125px;
  boxsizing: border-box;
  background-color: #66dd9c;
  color: white;
  font-color: white;
  border: solid;
  border-radius: 30px;
  height: 35px;
  padding: 5px 15px;
  text-align: center;
  line-height: 35px;
  cursor: pointer;
`;

const MidWrap = css`
  width: calc(100% - 400px);
  display: flex;
  justify-content: space-between;
  text-align: center;
  align-items: center;
  margin-top: 10px;
`;

const navInput = css`
  border: 1px solid #e1e2e3;
  fontsize: 18px;
  height: 35px;
  border-radius: 30px;
  padding: 5px 15px;
  margin-right: 10px;
`;
const navInputPlaceHolder = css`
  ::-webkit-input-placeholder {
    background-image: url(https://cdn1.iconfinder.com/data/icons/hawcons/32/698627-icon-111-search-256.png);
    background-size: contain;
    background-position: 1px center;
    background-repeat: no-repeat;
    text-align: center;
    text-indent: 0;
  }
`;

const navRight = css`
  width: 200px;
  justify-content: center;
  margin-top: 30px;
`;
const MenuWrap = css`
  position: absolute;
`;

const navMenu = css`
  boxsizing: border-box;
  background-color: white;
  color: #66dd9c;
  border: solid;
  border-radius: 30px;
  width: 125px;
  height: 35px;
  padding: 5px 15px;
  text-align: center;
  line-height: 35px;
  cursor: pointer;
  margin-top: 2px;
`;
export default MainNavBar;
