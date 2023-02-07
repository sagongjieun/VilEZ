import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";
// import { useRecoilState } from "recoil";
// import { locationState } from "../../recoil/atom";
// import { getUserDetail } from "../../api/user";

const { kakao } = window;

/**
 * props 설명
 * @readOnly true: 지도제어불가능 / false: 지도제어가능
 * @sendLocation !readOnly일 때, 부모 컴포넌트로 위치, 위경도, 지도 레벨 정보를 전달
 * @selectedLat readOnly일 때, 부모 컴포넌트로부터 받은 위도
 * @selectedLng readOnly일 때, 부모 컴포넌트로부터 받은 경도
 */
const Map = ({
  readOnly,
  sendLocation,
  selectedLat,
  selectedLng,
  movedLat,
  movedLng,
  movedZoomLevel,
  movedMarker,
  disableMapLat,
  disableMapLng,
}) => {
  const [location, setLocation] = useState("마우스 우클릭으로 장소를 선택해주시면 돼요");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");
  const [zoomLevel, setZoomLevel] = useState(0);
  const [isMarker, setIsMarker] = useState(false);
  const [hasMarker, setHasMarker] = useState(false);

  const [markerLat, setMarkerLat] = useState("");
  const [markerLng, setMarkerLng] = useState("");
  let container, options, map;
  let marker = new kakao.maps.Marker();
  // const userId = localStorage.getItem("id");
  // const [located, setLocated] = useRecoilState(locationState);
  // useEffect(() => {
  //   if (userId) {
  //     getUserDetail(userId).then((res) => {
  //       if (res) {
  //         const userData = res;
  //         if (userData[0].areaLat === null) {
  //           alert("동네인증을 진행해주셔야 해요");
  //         }
  //         setLocated({ areaLat: userData[0].areaLat, areaLng: userData[0].areaLng });
  //       }
  //     });
  //   }
  // }, []);
  function initMap() {
    // 지도를 표시할 공간과 초기 중심좌표, 레벨 세팅
    container = document.getElementById("map");
    options = {
      center: new kakao.maps.LatLng(33.448842, 126.570379),
      level: 4,
    };

    map = new kakao.maps.Map(container, options);
    // 인증 유저 좌표로 기준하여 설정
    // const locPosition = new kakao.maps.LatLng(33.448842, 126.570379);
    // marker.setPosition(locPosition);
    // marker.setMap(map);

    // setLat(locPosition.getLat());
    // setLng(locPosition.getLng());
    // setZoomLevel(map.getLevel());
    // setIsMarker(true);
    // setHasMarker(true);
    // setMarkerLat(locPosition.getLat());
    // setMarkerLng(locPosition.getLng());

    // map.panTo(locPosition);
  }
  // function makeRectangle() {
  //   console.log(located.areaLat, located.areaLng);
  //   var sw = new kakao.maps.LatLng(located.areaLat - 0.03, located.areaLng - 0.03), // 사각형 영역의 남서쪽 좌표
  //     ne = new kakao.maps.LatLng(located.areaLat, located.areaLng); // 사각형 영역의 북동쪽 좌표

  //   // 사각형을 구성하는 영역정보를 생성합니다
  //   // 사각형을 생성할 때 영역정보는 LatLngBounds 객체로 넘겨줘야 합니다
  //   var rectangleBounds = new kakao.maps.LatLngBounds(sw, ne);

  //   // 지도에 표시할 사각형을 생성합니다
  //   var rectangle = new kakao.maps.Rectangle({
  //     bounds: rectangleBounds, // 그려질 사각형의 영역정보입니다
  //     strokeWeight: 4, // 선의 두께입니다
  //     strokeColor: "#FF3DE5", // 선의 색깔입니다
  //     strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
  //     strokeStyle: "shortdashdot", // 선의 스타일입니다
  //     fillColor: "#FF8AEF", // 채우기 색깔입니다
  //     fillOpacity: 0.8, // 채우기 불투명도 입니다
  //   });

  //   // 지도에 사각형을 표시합니다
  //   rectangle.setMap(map);
  // }

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
    kakao.maps.event.addListener(map, "rightclick", function (mouseEvent) {
      const latlng = mouseEvent.latLng;

      marker.setPosition(latlng);
      marker.setMap(map);

      setLat(latlng.getLat());
      setLng(latlng.getLng());
      setZoomLevel(map.getLevel());
      setIsMarker(true);
      setHasMarker(true);
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
    // makeRectangle();
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
  }, [location, lat, lng, zoomLevel, isMarker]);

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

        searchDetailAddrFromCoords(locPosition, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            setLocation(result[0].address.address_name);
          }
        });
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
      map.setDraggable(false);
      map.setZoomable(false);
    } else {
      if (disableMapLat && disableMapLng) {
        initMap();

        const latlng = new kakao.maps.LatLng(disableMapLat, disableMapLng);

        map.setCenter(latlng);
        map.setDraggable(false);
        map.setZoomable(false);
      }
    }
  }, [selectedLat, selectedLng, disableMapLat, disableMapLng]);

  return <div id="map" css={mapWrapper}></div>;
};

const mapWrapper = css`
  width: 100%;
  height: 100%;
  border-radius: 5px;
`;

export default Map;
