import React from "react";
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

function App() {
  return (
    <BrowserRouter>
      <MainNavBar />
      <ChatOpenIcon />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/product/*" element={<Product />} />
        <Route path="/profile/*" element={<Profile />} />
        <Route path="/password" element={<FindPassword />} />
        <Route path="/mybox" element={<MyBox />} />
        <Route path="/oauth/kakao/callback" element={<OAuthKakao />} />
        <Route path="/oauth/naver/callback" element={<OAuthNaver />} />
        {/* <Route path="*" element={<NotFound />}></Route> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
