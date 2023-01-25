import React from "react";
import { Routes, Route } from "react-router-dom";
import ProductRegist from "../components/product/ProductRegist";
import ProductDetail from "../components/product/ProductDetail";
import ProductChatting from "../components/product/ProductChatting";

const Product = () => {
  return (
    <div>
      <Routes>
        <Route path="/regist" element={<ProductRegist />} />
        <Route path="/detail" element={<ProductDetail />} />
        <Route path="/chat" element={<ProductChatting />} />
      </Routes>
    </div>
  );
};

export default Product;
