# 🚶🏻‍♂️ Walkie - 걷기에 재미를 더하다

> 그냥 걷기만 해도 캐릭터가 생긴다!  
> 지도를 따라 스팟을 탐험하고 알을 모아 캐릭터를 부화하세요. 일상이 모험이 되는 위치 기반 만보기, 워키!

## 🎯 프로젝트 소개

워키(Walkie)는 단순한 만보기를 넘어선 **산책+모험 서비스**입니다. 다양한 장소를 탐험하고, 특별한 캐릭터와 함께 알을 부화시키며, 친구들과 함께 걷는 재미까지 더했습니다.

### ✨ 주요 기능

- **🏞️ 스팟 탐험**: 매일 다른 장소에서 알을 받고 새로운 곳을 발견
- **🥚 알 부화 시스템**: 걸을수록 알이 부화되어 캐릭터 획득
- **🐣 캐릭터 수집**: 총 20종의 다양한 캐릭터 컬렉션 (일반~전설 4단계 희귀도)
- **📝 모험 기록**: 방문한 스팟 리뷰 작성 및 갤러리 관리
- **👣 걸음 수 추적**: 일상의 발걸음이 모험으로 변화

## 🚀 핵심 기능

### 🗺️ 스팟 시스템
- 내 주변의 다양한 장소를 방문하여 알 획득
- 같은 장소는 3일 후에 다시 방문 가능
- 4종류의 알 중 랜덤하게 획득

### 🥚 알 부화 시스템
- 걸음 수에 따라 알이 점진적으로 부화
- 하나의 알에만 걸음 수 집중 가능
- 부화 진행도 실시간 알림

### 🧍‍♂️ 캐릭터 시스템
- 총 20종의 고유한 캐릭터
- 각 캐릭터마다 특별한 스토리
- 일반, 레어, 에픽, 레전드 4단계 희귀도

## 🏗️ 기술 스택

### 🏗️ 아키텍처
- **Clean Architecture**: Domain, Data, Presentation 계층 분리
- **Multi-Module**: 기능별 모듈화로 확장성 및 유지보수성 향상
- **MVVM Pattern**: ViewModel을 활용한 UI 상태 관리

### ⚡ 주요 기술
- **Language**: Kotlin 100%
- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Hilt (Dagger)
- **Asynchronous**: Kotlin Coroutines + Flow
- **Navigation**: Jetpack Navigation Compose
- **Network**: Retrofit2 + OkHttp3 + Gson
- **Local Storage**: DataStore Preferences + Room Database
- **Authentication**: Kakao Login SDK
- **Serialization**: Kotlinx Serialization
- **Development Tools**: LeakCanary, KSP

## 📁 프로젝트 구조

```
📦 Walkie-Android
├── 📂 app/                     # 메인 애플리케이션 모듈
├── 📂 build-logic/             # Gradle 컨벤션 플러그인
├── 📂 core/                    # 공통 모듈
│   ├── 📂 common/              # 공통 유틸리티 및 베이스 클래스
│   ├── 📂 data/                # 데이터 계층 (Repository, DataSource)
│   ├── 📂 design-system/       # 디자인 시스템 (테마, 컴포넌트)
│   ├── 📂 domain/              # 도메인 계층 (UseCase, Repository Interface)
│   ├── 📂 model/               # 데이터 모델
│   ├── 📂 ga/                  # Google Analytics
│   └── 📂 resource/            # 리소스 (이미지, 폰트, 애니메이션)
├── 📂 feature/                 # 기능별 모듈
│   ├── 📂 home/                # 홈 화면 (메인, 마이페이지, 캐릭터 부화)
│   ├── 📂 login/               # 로그인 및 온보딩
│   ├── 📂 spot/                # 스팟 관련 기능
│   └── 📂 stepcounter/         # 만보기 기능
└── 📂 navigation/              # 네비게이션 관리
```

## 📱 다운로드

<a href="https://play.google.com/store/apps/details?id=com.startup.walkie">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80">
</a>

## 📱 스크린샷

<div align="center">
  <img src="https://github.com/user-attachments/assets/4d694861-e2a7-441f-adf4-f98ac8c98204" width="600" alt="워키 앱 소개">
</div>

<div align="center">
  <img src="https://github.com/user-attachments/assets/9465e1a3-4d94-4a86-bfcc-d273041fa607" width="250" alt="워키 메인 화면">
  <img src="https://github.com/user-attachments/assets/abd3d98a-e20a-4acb-b351-8877ab5954c4" width="250" alt="걸음 수 확인">
  <img src="https://github.com/user-attachments/assets/8cf2cfa4-445c-4d71-8762-c14daf0b6bbf" width="250" alt="스팟 탐험">
</div>

<div align="center">
  <img src="https://github.com/user-attachments/assets/4a90fc97-fbaa-429a-b1c3-32c73df27d7e" width="250" alt="캐릭터 부화">
  <img src="https://github.com/user-attachments/assets/f7d507d2-d02e-4c6c-9890-55f1303c9f56" width="250" alt="알 부화 진행">
  <img src="https://github.com/user-attachments/assets/aacd209a-f8b8-4714-818e-e988d44ee7fa" width="250" alt="캐릭터 컬렉션">
</div>

<p align="center">
  <i>지루했던 걷기에 재미를 더하고, 스팟을 탐험하며, 걸음으로 캐릭터를 부화시키세요!</i>
</p>

## 🌟 핵심 가치

- **재미있는 걷기**: 단순한 만보기를 넘어선 게임화된 산책 경험
- **탐험의 즐거움**: 새로운 장소를 발견하고 기록하는 재미
- **성취감**: 캐릭터 수집을 통한 지속적인 동기부여
- **소셜 연결**: 다른 사용자들과 함께하는 모험 공유

## 📞 연락처

📨 **E-mail:** walkieofficial@gmail.com

📱 **Instagram:** [@walkie__official](https://www.instagram.com/walkie__official?igsh=bTZhZjUwa2JkdW1u)

---

<div align="center">
  <b>일상이 모험이 되는 순간, 워키와 함께하세요! 🚶‍♂️✨</b>
</div>
