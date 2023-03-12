# 빌려주는 이웃, 빌리지 (Vilez)

![image](/uploads/bc8dfe608bbf6bf2ad4d7b146d39e445/image.png)



## 프로젝트 소개

![mockup3](/uploads/eed1276f0195756363904c0679409c6c/mockup3.png)

- **주제** : 이웃 간 물건 공유 플랫폼
- **설명** : 같은 지역 내에서 누군가에게는 필요하지만, 다른 누군가에게는 당장 필요하지 않은 물건을 서로 공유하여 공유문화를 활성화시켜, 지역 사회 구성원의 화합을 이끌어내는 서비스
- **기간** : 2023.01.03 - 2023.02.17



## ⚙️ Android 기술 스택

| Android Studio                                               | Kotlin                                                       | Android Jetpack                                              | Retrofit                                                     | FCM                                                          |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![File:Android logo 2019 (stacked).svg - Wikimedia Commons](https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Android_logo_2019_(stacked).svg/600px-Android_logo_2019_(stacked).svg.png) | ![File:Kotlin Icon.svg - Wikimedia Commons](https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Kotlin_Icon.svg/600px-Kotlin_Icon.svg.png) | <img src="https://4.bp.blogspot.com/-NnAkV5vpYuw/XNMYF4RtLvI/AAAAAAAAI70/kdgLm3cnTO4FB4rUC0v9smscN3zHJPlLgCLcBGAs/s1600/Jetpack_logo%2B%282%29.png" width="800"> | ![CodingWithMitch.com](https://codingwithmitch.s3.amazonaws.com/static/blog/f099482c-28a2-11e9-b183-2aabe8ede8eb/retrofit2_getting_started.png) | <img src="https://storage.googleapis.com/atori_wordpress/2020/11/e53d7725-firebase-cloud-messaging-3-vertical-lockup-light.png" > |



## 🖇 구현 기능

- Retrofit을 이용한 AWS EC2의 MySQL, MongoDB와 REST API 통신
- 웹소켓 통신 STOMP를 이용한 채팅 기능
- STOMP로 실시간 채팅을 하며 동시에 공유 지도를 사용할 수 있는 기능
- Firebase Cloud Messaging을 이용한 푸시 알림 서비스



## 📱 스크린샷 및 설명

### 스플래시

<img src="/uploads/042394be49b74578c554d34e89769549/Group_220.png" alt="Group 220" width="200" />

### 로그인

<img src="/uploads/7a3dcde81f3e65e0dea9c1bf0ff901f5/Group_222.png" alt="Group 220" width="200" />

1. 로그인 성공시 홈 화면으로 이동, 실패시 다이얼로그 띄움
2. 비밀번호 재설정 페이지 이동
3. 카카오 로그인
4. 네이버 로그인
5. 회원가입 페이지로 이동



### 회원가입

<img src="/uploads/4ef6d5110266ec3d88eca1151837b651/Group_224.png" alt="Group 220" width="200" />

1. 이메일 인증 및 중복 확인
2. 닉네임 중복 확인
3. 회원가입 신청



### 공유 물품 등록

- multipart/form-data로 물품 이미지 업로드
- Update 구성 과정에서 사용자가 입력했던 data를 그대로 받아와서 수정할 수 있게 구현함





### 공유 물품 목록 페이지

<img src="/uploads/5ae8bb9cbca4f4aa89f8d9024cebd1f1/Group_225.png" alt="Group 220" width="200" />

1. 검색 버튼
2. 목록으로 검색
3. 물품 공유 게시글 목록
4. 물품 요청 게시글 목록
5. on : 공유 가능 목록만 보기 / off : 전체 목록 보기
6. 게시글 상세 내용 페이지 이동
7. 공유글 작성 페이지 이동
8. 요청글 작성 페이지 이동
9. 메인 화면 이동
10. 채팅 목록 화면 이동
11. 마이 페이지 이동



### 공유 물품 상세보기

<img src="/uploads/5d936291d4012d72358bad1f485d57f1/Group_287.png" alt="Group 220" width="200" />



### 키워드 검색 및 카테고리 검색

<img src="/uploads/389b7ecfaaf1941385216f297ae38a02/image.png" alt="Group 220" width="200" /><img src="/uploads/be86f7230c356a81fee382bb77549245/Group_285.png" alt="Group 220" width="200" />

- 키워드 검색

1. 검색 내용 입력
   1. 한 글자 이상 입력후 엔터를 누르면 검색
   2. 물품 공유 탭의 경우 공유 가능만 보기 필터링 가능
2. 카테고리 설정 버튼
   1. 해당 카테고리로 필터링 된 게시글을 확인할 수 있음

- 카테고리 검색



### 실시간 채팅 & 지도

- 채팅 목록

<img src="/uploads/b26dddfcb2712e0de22be9db6ef5e789/Group_441.png" alt="Group 441"  width="200" />



- Websocket과 STOMP를 활용한 실시간 채팅

<img src="/uploads/ce15863c53faaa7a4305cac555585524/Group_440.png" alt="Group 220" width="200" />

1. 클릭시 메뉴 페이지 닫음
2. 채팅 입력 후 클릭시 채팅이 전송
3. 공유기간 설정
   1. 공유자 : 공유자가 대여해주는 기간을 설정할 수 있도록 캘린더 피커를 띄움
   2. 피공유자 : 공유자가 선택해준 공유 날짜 확인 가능
4. 서약서 작성 및 확인
   1. 피공유자 : 피공유자가 물건을 깨끗하게 사용 후 반납하겠다는 서약서를 작성하는 다이얼로그
   2. 공유자 : 피공유자가 작성한 서약서 확인 가능
5. 지도 공유하기
   1. 채팅 화면 상단에 지도 화면이 나타나서 공유자, 피공유자가 동시에 지도를 제어하고 마커를 찍는 등 지도 사용 가능
6. 만남 확정하기
   1. 공유자가 만남 확정하기 버튼을 누르면 공유가 시작됨
7. 반납하기
   1. 피공유자가 물건 반납 후 공유자가 반납하기 버튼을 누르면 피공유자에 대한 평가를 별점으로 할 수 있는 다이얼로그
8. 만남 취소
   1. 만남 확정 전 만남을 취소 한 뒤 채팅이 불가해짐



- 캔버스를 이용한 사인 및 서약서 기능

<img src="/uploads/7251e571f7ca2e6c5a02afea980e19f5/Group_436.png" alt="Group 220" width="200" />

1. 확인 시 다이얼로그 닫힘



- 실시간으로 1:1 채팅과 kakao map 을 동시에 제어할 수 있음

<img src="/uploads/f9d6d37e84b8eb28e6ffaa63d91f6892/Group_437.png" alt="Group 220" width="200" />

1. 공유 지도 서비스
   1. 꾹 누르면 마커가 찍히며 상대방의 지도가 이동
   2. 드래그 엔 드롭을 하면 지도가 이동하며 상대방의 지도가 이동
   3. 줌인아웃을 하면 지도가 확대되며 상대방의 지도가 확대

### 

### 마이 페이지

- 사용자 프로필 / 포인트 내역 / 동네인증

<img src="/uploads/23c8ea080220119a0a8ce1327bc8f1a2/Group_422.png" alt="Group 220" width="200" /><img src="/uploads/37ddaf5bd0e68ddb0d31a4261d464ab9/Untitled__1_.png" alt="Group 220" width="200" />

1. 프로필 편집 페이지로 이동
2. 매너지수
   1. 클릭시 매너지수에 대한 안내가 나옴
3. 현재 마감 임박, 시작 임박인 공유, 대여 물품을 보여줌
4. 공유 캘린더 페이지로 이동
5. 나의 작성글 페이지로 이동
6. 나의 관심글 페이지로 이동
7. 포인트 내역 페이지로 이동
8. 나의 공유 물품 페이지로 이동
9. 나의 대여 물품 페이지로 이동
10. 나의 대여 물품 페이지로 이동
11. 내 동네 설정 페이지로 이동
12. 로그아웃 후 로그인 페이지로 이동


- 내 동네 설정 : GPS 기반으로 사용자의 동네를 가져옴
1. 현재 저장된 사용자의 동네를 보여줌
2. 버튼 클릭시 사용자 위치를 감지하여 동네 설정
   1. 성공시 성공 다이얼로그 띄운 후 확인 버튼 클릭시메인 화면으로 이동
   2. 실패시 실패 다이얼로그

<img src="/uploads/1cd211058c3cd2865aca504a5db1ae2c/Group_445.png" alt="Group 220" width="200" /><img src="/uploads/28f55615cf3844b39641d4becd953446/Untitled__2_.png" alt="Group 220" width="200" />

- 포인트 내역
<img src="/uploads/9c2cf4de2611ff821df5e59cdcdd2bb4/Group_446.png" alt="Group 220" width="200" />


1. 총 포인트, 총 적립 포인트, 총 사용 포인트 시각화
   1. 총 포인트 대비 사용 포인트를 progress bar로 제공
2. 공유 영수증
   1. 포인트 적립/차감 내역을 최신순으로 정렬



## 📝 배운점 및 아쉬운 점

- 박정은
  - 코루틴과 Retrofit을 이용한 REST 통신을 이해 할 수 있었습니다.
  - STOMP를 이용한 웹소켓 통신을 처음 사용하면서 작동 원리를 이해할 수 있었습니다.
  - 안드로이드 제트팩 라이브러리를 좀 더 다양하게 사용하지 못한 점이 아쉬웠습니다.
