# VilEZ 🏠
![](./preview_image/메인페이지.gif)

[1️⃣ 프로젝트 소개](#프로젝트-소개) <br />
[2️⃣ 기술 스택](#기술-스택) <br />
[3️⃣ 폴더 구조](#폴더-구조) <br />
[4️⃣ 나의 역할 및 화면 구성](#나의-역할-및-화면-구성) <br />
[5️⃣ 회고](#회고)

<br />

## 프로젝트 소개
### 주제
이웃 간 물건 공유 플랫폼 웹 / 앱 서비스
### 목적
이웃주민과 소통이 단절된 현대사회에서 서로 필요한 물품을 공유하고, 요청하는 플랫폼을 만들어 지역사회를 따뜻하게 만들고자 함
### 진행 기간
2023.01.03 - 2023.02.17
### 구성원
* FrontEnd 3명 (본인 포함)
* BackEnd 1명
* Mobile 1명
* Fullstack 1명
<br />

## 기술 스택
|JavaScript|React|recoil|emotion.js|axios|
|---|---|---|---|---|
|![javascript](https://user-images.githubusercontent.com/72495712/224549841-a910d8c0-1843-4680-8a0f-9ab1520998e8.png)|![react](https://user-images.githubusercontent.com/72495712/224549934-d0427153-3767-4c3d-bc47-3dd5cfae2d2d.png)|![recoil](https://user-images.githubusercontent.com/72495712/224549994-ca4d52a5-c455-4c6b-9062-acab0798c22a.png)|![emotion](https://user-images.githubusercontent.com/72495712/224550022-477cfb7a-c9a3-4666-b4b4-9f15e1775b59.png)|![axios](https://user-images.githubusercontent.com/72495712/224550044-9daafaeb-5859-4f5e-9098-81e6814f3e77.png)|
<br />

## 폴더 구조
```markdown
src
│  App.js
│  GlobalStyle.jsx
│  index.js
│  
├─ api
│      instance.js
│      
├─ assets
│  └─images
│          
├─ components
│  │  StompRealTime.jsx
│  │  
│  ├─ button
│  │      
│  ├─ common
│  │      
│  ├─ login
│  │      
│  ├─ modal
│  │      
│  ├─ mybox
│  │      
│  ├─ product
│  │      
│  ├─ profile
│  │      
│  └─ signup
│          
├─ hooks
│      
├─ pages
│      
└─ recoil
        atom.js
```
<br />

## 나의 역할 및 화면 구성

### 공유 물품 등록 & 상세보기
![](./preview_image/글등록.gif)
- multipart/form-data 를 활용한 다중 파일 업로드

### 실시간 채팅 & 지도
|피공유자|공유자|
|---|---|
|![](./preview_image/피공유자-예약.gif)|![](./preview_image/공유자-예약.gif)|

- Websocket과 STOMP를 활용한 실시간 통신
<br />

## 회고
- API나 라이브러리의 공식 문서를 보는 데 능숙해지고, 이를 활용해 많은 궁금증과 오류를 해결할 수 있었습니다.
- recoil 사용법에 대해 공부하고, 이를 활용해 여러 컴포넌트에서 사용되는 데이터들의 관리를 효율적으로 하였습니다.
- axios interceptor에 대해 공부하고, 토큰을 관리하는 법을 터득하였습니다.
- 테스트와 디버깅 과정에서 버그를 고치면서, 더 효율적인 컴포넌트 구조에 대해 고민해볼 수 있었습니다.
- 백엔드에서 제공하는 Swagger를 사용해 API 요청 테스트를 효율적으로 하였습니다.
- 실시간 통신을 구현하며, React의 생명 주기에 대해 더 자세히 공부할 수 있었습니다.
