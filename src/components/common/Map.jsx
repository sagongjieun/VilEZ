import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const { kakao } = window;

/**
 * props 설명
 * @readOnly true: 지도제어불가능 / false: 지도제어가능
 * @sendLocation !readOnly일 때, 부모 컴포넌트로 위치, 위경도, 지도 레벨 정보를 전달
 * @selectedLat readOnly일 때, 부모 컴포넌트로부터 받은 위도
 * @selectedLng readOnly일 때, 부모 컴포넌트로부터 받은 경도
 */
const Map = ({ readOnly, sendLocation, selectedLat, selectedLng, movedLat, movedLng, movedLevel }) => {
  const [location, setLocation] = useState("지도에서 장소를 선택해주세요!");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [level, setLevel] = useState(0);
  let container, options, map, marker;

  useEffect(() => {
    // 지도를 표시할 공간과 초기 중심좌표, 레벨 세팅
    container = document.getElementById("map");
    options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 4,
    };
    map = new kakao.maps.Map(container, options);

    // 현재 접속위치를 중심좌표로 두기
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function (position) {
        const lat = position.coords.latitude, // 위도
          lon = position.coords.longitude; // 경도

        const locPosition = new kakao.maps.LatLng(lat, lon);
        map.setCenter(locPosition);
      });
    } else {
      const locPosition = new kakao.maps.LatLng(33.450701, 126.570667);
      map.setCenter(locPosition);
    }

    marker = new kakao.maps.Marker();
    const geocoder = new kakao.maps.services.Geocoder();

    // 지도를 제어할 때
    if (!readOnly) {
      // 드래그 이동
      kakao.maps.event.addListener(map, "dragend", function () {
        const center = map.getCenter();

        setLat(center.getLat());
        setLng(center.getLng());
        setLevel(map.getLevel());

        searchDetailAddrFromCoords(center, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            setLocation(`중심 좌표 : ${result[0].address.address_name}`);
          }
        });
      });

      // 지도 레벨 변경
      kakao.maps.event.addListener(map, "zoom_changed", function () {
        const center = map.getCenter();

        setLat(center.getLat());
        setLng(center.getLng());
        setLevel(map.getLevel());

        searchDetailAddrFromCoords(center, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            setLocation(`중심 좌표 : ${result[0].address.address_name}`);
          }
        });
      });

      // 마커 찍기
      kakao.maps.event.addListener(map, "click", function (mouseEvent) {
        const latlng = mouseEvent.latLng;

        marker.setPosition(latlng);
        setLat(latlng.getLat());
        setLng(latlng.getLng());
        setLevel(map.getLevel());

        searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            setLocation(`선택된 장소 : ${result[0].address.address_name}`);
            marker.setMap(map);
          }
        });
      });
    }

    function searchDetailAddrFromCoords(coords, callback) {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    }
  }, []);

  // readOnly 아닐 때 (=지도 제어 가능)
  useEffect(() => {
    if (!readOnly) {
      sendLocation(location, lat, lng, level);
    }
  }, [location, lat, lng, level]);

  // 실시간 공유 지도 위경도, 지도 레벨 받기
  useEffect(() => {
    if (movedLat && movedLng && movedLevel) {
      setLat(movedLat);
      setLng(movedLng);
      setLevel(movedLevel);
    }
  }, [movedLat, movedLng, movedLevel]);

  // readOnly 일 때 (=지도 제어 불가능)
  useEffect(() => {
    if (selectedLat && selectedLng) {
      container = document.getElementById("map");
      options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 4,
      };
      map = new kakao.maps.Map(container, options);

      marker = new kakao.maps.Marker();

      const latlng = new kakao.maps.LatLng(selectedLat, selectedLng);

      marker.setMap(map);
      map.setCenter(latlng);
      marker.setPosition(latlng);
    }
  }, [selectedLat, selectedLng]);

  return <div id="map" css={mapWrapper}></div>;
};

const mapWrapper = css`
  width: 100%;
  height: 100%;
  border-radius: 5px;
`;

export default Map;
