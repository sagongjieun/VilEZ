import React from "react";
import { Routes, Route } from "react-router-dom";
import MyBoxMain from "../components/mybox/MyBoxMain";

const MyBox = () => {
  return (
    <div>
      <Routes>
        <Route path="/mybox" element={<MyBoxMain />} />
      </Routes>
    </div>
  );
};

export default MyBox;
