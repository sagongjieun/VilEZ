import React from "react";
import { Routes, Route } from "react-router-dom";
import ProductRegist from "../components/product/ProductRegist";
import ProductDetail from "../components/product/ProductDetail";

const Product = () => {
  return (
    <div>
      <Routes>
        <Route path="/regist" element={<ProductRegist />} />
        <Route path="/detail" element={<ProductDetail />} />
      </Routes>
    </div>
  );
};

export default Product;
