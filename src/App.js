import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import ChatOpenIcon from "./pages/ChatOpenIcon";
import MainNavBar from "./components/modal/MainNavBar";
import Product from "./pages/Product";
import Signup from "./pages/Signup";
import EditProfile from "./components/modal/EdifProfile";
function App() {
  return (
    <BrowserRouter>
      <div>
        <MainNavBar />
        <ChatOpenIcon />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/product/*" element={<Product />} />
        </Routes>
        <EditProfile />
      </div>
    </BrowserRouter>
  );
}

export default App;
