import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import ChatOpenIcon from "./pages/ChatOpenIcon";
import MainNavBar from "./components/common/MainNavBar";
import Product from "./pages/Product";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile";
// import EditProfile from "./components/modal/EdifProfile";

// import Qrcode from "./components/modal/Qrcode";
// import Oath from "./components/modal/Oath";

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
      </Routes>
      {/* <Oath /> */}
      {/* <Qrcode /> */}
      {/* <EditProfile /> */}
    </BrowserRouter>
  );
}

export default App;
