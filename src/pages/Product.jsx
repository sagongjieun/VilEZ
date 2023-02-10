import React, { useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import ProductRegist from "../components/product/ProductRegist";
import ProductDetail from "../components/product/ProductDetail";
import ProductChatting from "../components/product/ProductChatting";
import ProductList from "../components/product/ProductList";
import ProductPut from "../components/product/ProductPut";
import { useRecoilValue } from "recoil";
import { locationState } from "../recoil/atom";
import { useNavigate } from "react-router-dom";

const Product = () => {
  const located = useRecoilValue(locationState);
  const navigate = useNavigate();

  useEffect(() => {
    // 동네 인증이 안됐으면 Product관련 페이지 이용 불가
    if (located.areaLat === null) {
      alert("빌리지를 이용하시려면 동네 인증을 해주셔야해요.");
      navigate(`/profile/product`);
    }
  }, []);

  return (
    <div>
      <Routes>
        <Route path="/regist" element={<ProductRegist />} />
        <Route path="/detail/share/:boardId" element={<ProductDetail />} />
        <Route path="/detail/ask/:boardId" element={<ProductDetail />} />
        <Route path="/chat/:roomId" element={<ProductChatting />} />
        <Route path="/list/share" element={<ProductList />} />
        <Route path="/list/ask" element={<ProductList />} />
        {/* useEffect로, pathname에 share, ask포함되어있으면 요청글 */}
        <Route path="/edit/share/:boardId" element={<ProductPut />} />
        <Route path="/edit/ask/:boardId" element={<ProductPut />} />
      </Routes>
    </div>
  );
};

export default Product;
