import React, { useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import ChatOpenIcon from "./pages/ChatOpenIcon";
import MainNavBar from "./components/common/MainNavBar";
import Product from "./pages/Product";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile";
import FindPassword from "./components/signup/FindPassword";
import OAuthKakao from "./components/login/OAuthKakao";
import OAuthNaver from "./components/login/OAuthNaver";
import MyBox from "./pages/MyBox";
import ScrollToTop from "./components/common/ScrollToTop";
import SocialNickName from "./pages/SocialNickName";
import { getCheckValidToken } from "./api/user";
import { useSetRecoilState, useRecoilState } from "recoil";
import { loginUserState, isLoginState } from "./recoil/atom";

function App() {
  const setLoginUser = useSetRecoilState(loginUserState);
  const [isLogin, setIsLogin] = useRecoilState(isLoginState);

  // 로그인 유지
  useEffect(() => {
    const accessToken = window.localStorage.getItem("accessToken");

    if (!accessToken) {
      window.localStorage.clear();
    } else {
      getCheckValidToken().then((res) => {
        // 유효한 토큰이면 유저 정보를 recoil에 담기
        if (res) {
          setLoginUser((prev) => {
            return {
              ...prev,
              id: res.id,
              nickName: res.nickName,
              manner: res.manner,
              point: res.point,
              profileImg: res.profileImg,
              areaLng: res.areaLng,
              areaLat: res.areaLat,
            };
          });

          setIsLogin(true);
        }
      });
    }

    // if (accessToken !== "undefined" && accessToken !== "null" && accessToken) {
    //   getCheckValidToken().then((res) => {
    //     // 유효한 토큰이면 유저 정보를 recoil에 담기
    //     if (res) {
    //       setLoginUser((prev) => {
    //         return {
    //           ...prev,
    //           id: res.id,
    //           nickName: res.nickName,
    //           manner: res.manner,
    //           point: res.point,
    //           profileImg: res.profileImg,
    //           areaLng: res.areaLng,
    //           areaLat: res.areaLat,
    //         };
    //       });

    //       setIsLogin(true);
    //     }
    //   });
    // } else {
    //   window.localStorage.clear();
    // }
  }, []);

  return (
    <BrowserRouter>
      <ScrollToTop />
      <MainNavBar />
      {isLogin && <ChatOpenIcon />}
      {/* {isModal ? <div>빌리지는 로그인 후에 이용하실 수 있어요</div> : null} */}
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/product/*" element={<Product />} />
        <Route path="/profile/*" element={<Profile />} />
        <Route path="/password" element={<FindPassword />} />
        <Route path="/mybox/*" element={<MyBox />} />
        <Route path="/oauth/kakao/callback" element={<OAuthKakao />} />
        <Route path="/oauth/naver/callback" element={<OAuthNaver />} />
        <Route path="/socialnickname" element={<SocialNickName />} />
        {/* <Route path="*" element={<NotFound />}></Route> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
