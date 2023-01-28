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
const Map = ({ readOnly, sendLocation, selectedLat, selectedLng, movedLat, movedLng, movedZoomLevel, movedMarker }) => {
  const [location, setLocation] = useState("지도에서 장소를 선택해주세요!");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [zoomLevel, setZoomLevel] = useState(0);
  const [isMarker, setIsMarker] = useState(false);
  const [hasMarker, setHasMarker] = useState(false);
  const [markerLat, setMarkerLat] = useState("");
  const [markerLng, setMarkerLng] = useState("");

  let container, options, map;
  let marker = new kakao.maps.Marker();

  function initMap() {
    // 지도를 표시할 공간과 초기 중심좌표, 레벨 세팅
    container = document.getElementById("map");
    options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 4,
    };
    map = new kakao.maps.Map(container, options);
  }

  function geolocationMap() {
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
  }

  function eventDragEnd() {
    // 드래그 이동
    kakao.maps.event.addListener(map, "dragend", function () {
      const center = map.getCenter();

      setLat(center.getLat());
      setLng(center.getLng());
      setZoomLevel(map.getLevel());
      setIsMarker(false);
    });
  }

  function eventZoomChanged() {
    // 지도 레벨 변경
    kakao.maps.event.addListener(map, "zoom_changed", function () {
      const center = map.getCenter();

      setLat(center.getLat());
      setLng(center.getLng());
      setZoomLevel(map.getLevel());
      setIsMarker(false);
    });
  }

  function eventSetMarker() {
    // 마커 찍기
    kakao.maps.event.addListener(map, "click", function (mouseEvent) {
      const latlng = mouseEvent.latLng;

      marker.setPosition(latlng);
      marker.setMap(map);

      setLat(latlng.getLat());
      setLng(latlng.getLng());
      setZoomLevel(map.getLevel());
      setIsMarker(true);
      setMarkerLat(latlng.getLat());
      setMarkerLng(latlng.getLng());

      map.panTo(latlng);

      searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
          setLocation(result[0].address.address_name);
        }
      });
    });
  }

  function searchDetailAddrFromCoords(coords, callback) {
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
  }

  useEffect(() => {
    initMap();
    geolocationMap();

    /** readOnly : 지도 제어 가능 */
    if (!readOnly) {
      eventDragEnd();
      eventZoomChanged();
      eventSetMarker();
    }
  }, []);

  /** 지도 데이터 보내기 */
  useEffect(() => {
    if (!readOnly) {
      sendLocation(location, lat, lng, zoomLevel, isMarker);
    }
  }, [location, lat, lng, zoomLevel]);

  /** 실시간 공유 지도 데이터 받기 */
  useEffect(() => {
    if (movedLat && movedLng && movedZoomLevel) {
      initMap();
      const locPosition = new kakao.maps.LatLng(movedLat, movedLng);

      map.setLevel(movedZoomLevel); // 지도 레벨 동기화

      if (movedMarker) {
        marker.setPosition(locPosition);
        marker.setMap(map);
        setHasMarker(true);
        setMarkerLat(locPosition.getLat());
        setMarkerLng(locPosition.getLng());
      } else {
        // dragend, zoomchange 이벤트의 경우 이전 마커의 위치에 마커 유지
        if (hasMarker) {
          const prevMarkerPosition = new kakao.maps.LatLng(markerLat, markerLng);
          marker.setPosition(prevMarkerPosition);
          marker.setMap(map);
        }
      }

      map.panTo(locPosition);

      // 상대방이 제어하고나서 나도 제어할 수 있게
      eventDragEnd();
      eventZoomChanged();
      eventSetMarker();
    }
  }, [movedLat, movedLng, movedZoomLevel]);

  /** readOnly : 지도 제어 불가능 */
  useEffect(() => {
    if (selectedLat && selectedLng) {
      initMap();

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
