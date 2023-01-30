import React from "react";
import { Routes, Route } from "react-router-dom";
import ProductRegist from "../components/product/ProductRegist";
import ProductDetail from "../components/product/ProductDetail";
import ProductChatting from "../components/product/ProductChatting";
import ProductList from "../components/product/ProductList";

const Product = () => {
  return (
    <div>
      <Routes>
        <Route path="/regist" element={<ProductRegist />} />
        <Route path="/detail/share/:boardId" element={<ProductDetail />} />
        <Route path="/detail/ask/:boardId" element={<ProductDetail />} />
        <Route path="/chat/:roomId" element={<ProductChatting />} />
        <Route path="/list" element={<ProductList />} />
      </Routes>
    </div>
  );
};

export default Product;
