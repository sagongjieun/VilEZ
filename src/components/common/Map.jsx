import React, { useEffect, useState } from "react";
/** @jsxImportSource @emotion/react */
import { css } from "@emotion/react";

const { kakao } = window;

const Map = ({ sendLocation }) => {
  const [location, setLocation] = useState("");
  const [lat, setLat] = useState("");
  const [lng, setLng] = useState("");

  useEffect(() => {
    sendLocation(location, lat, lng);
  }, [location, lat, lng]);

  useEffect(() => {
    const container = document.getElementById("map");
    const options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 4,
    };
    const map = new kakao.maps.Map(container, options);

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

    const marker = new kakao.maps.Marker();
    const geocoder = new kakao.maps.services.Geocoder();

    kakao.maps.event.addListener(map, "click", function (mouseEvent) {
      const latlng = mouseEvent.latLng;

      marker.setPosition(latlng);
      setLat(latlng.getLat());
      setLng(latlng.getLng());

      searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
          setLocation(result[0].address.address_name);
          marker.setMap(map);
        }
      });
    });

    function searchDetailAddrFromCoords(coords, callback) {
      geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    }
  }, []);

  return <div id="map" css={map}></div>;
};

const map = css`
  width: 100%;
  height: 479px;
  border-radius: 5px;
`;

export default Map;
