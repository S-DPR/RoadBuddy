## 🗺️ RoadBuddy

### ♿ 교통약자를 위한 지도 앱

---

### 📢 프로젝트 소개
- 우리 사회는 빠르게 변화하지만, 교통 약자인 장애인, 노인, 임산부 등은 일상적인 이동에서 다양한 어려움을 겪고 있습니다. **RoadBuddy**는 교통약자들이 일상생활에서 목적지까지 더 쉽게 이동할 수 있도록 돕습니다. 장애인 콜택시 예상 대기시간과 실제 이동시간, 환승정보나 역 주변 급경사지, 엘리베이터 방향 등의 정보를 포함한 대중교통 경로 정보를 사용자에게 제시하여 사용자의 현 상황에 알맞은 이동 수단을 고를 수 있도록 길찾기를 제공합니다.

---
### ✅ 핵심 기능
- 장애인 콜택시 예상 대기시간 및 예상 이동시간 제공
- 지하철 엘리베이터 환승 정보 제공
- 역 주변 급경사지 정보 제공
- 대중교통 경로 정보 제공
- 길찾기 기능
---

### 🔧 기술 스택
- 개발 환경: Android, iOS
- 개발 언어: Kotlin, Swift, Java, Python (일부)
- 개발 도구: Android Studio, XCode, IntelliJ
- 주요 기술: Spring Boot, AWS
- 타 플랫폼 GitHub : [Android](https://github.com/songhm7/RoadBuddyAndroid) / [IOS](https://github.com/dpwns1234/RoadBuddy-iOS)
---
### 🗂️ 시스템 구조도
![](https://i.postimg.cc/hPvJQVB8/image.png)

---
### 💻 주요 화면

<div style="display: flex; gap: 10px;">
    <img src="https://i.postimg.cc/Y989hCDy/image.png" style="width: 24%;">
    <img src="https://i.postimg.cc/Y0sCzyH6/image.png" style="width: 24%;">
    <img src="https://i.postimg.cc/xTXC1Yvt/image.png" style="width: 24%;">
    <img src="https://i.postimg.cc/fT1b3F5Z/image.png" style="width: 24%;">
</div>

---
### 👩💻 개발자

| [김예준](https://github.com/dpwns1234) | [송형민](https://github.com/songhm7) | [서윤혜](https://github.com/YoonhyeSuh) | [추영광](https://github.com/S-DPR) |
|---|---|---|---|
| <img src="https://avatars.githubusercontent.com/u/52391722?v=4" width="128"> | <img src="https://avatars.githubusercontent.com/u/47211293?s=48&v=4" width="128"> | <img src="https://avatars.githubusercontent.com/u/106311524?s=64&v=4" width="128"> | <img src="https://avatars.githubusercontent.com/u/108619579?v=4" width="128"> |

---

## RoadBuddy 백엔드

---

## Overview

RoadBuddy 백엔드는 안드로이드와 iOS 플랫폼에서 API 요청을 대행하고, 응답을 가공하여 다시 모바일 클라이언트로 전달하는 역할을 합니다.

### 주요 기능
- **API 중계**: 안드로이드와 iOS에서 직접 API 요청을 보내지 않고, 서버에서 요청을 대행하여 처리합니다.
- **경로 보완**: **Google Maps API**에서 대중교통 경로를 받아오고, 한국에서 도보 경로가 표시되지 않는 경우 **TMap의 도보 경로 API**를 활용하여 정보를 보완합니다.
- **엘리베이터 환승 경로**: 각 경로의 지하철 엘리베이터 환승 경로를 **국가교통부 KRIC 레일포털의 API**에서 받아옵니다. 기존의 API와는 다르게 환승대상역, 현재 선로, 환승대상선 세 정보만으로 받을 수 있게 구성했습니다.
- **지하철역 주변 급경사지**: 지하철역 반경 500m내 급경사지의 위경도와 주소를 반환합니다. 급경사지의 출처는 **국민안전재난포털**입니다.

### API
![](https://i.postimg.cc/XqcGW6Tn/image.png)
![](https://i.postimg.cc/CMjfkbd9/image.png)
![](https://i.postimg.cc/WbTqntkz/image.png)
