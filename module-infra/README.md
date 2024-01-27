# Infra 모듈
Infra 모듈은 시스템 구성에 필요한 인프라를 정의합니다.

## 기능
Infra 모듈은 캐싱, 메시징, 파일 저장소 등의 기능을 제공하고 외부 시스템과의 연동을 위한 인터페이스를 제공합니다.

## 디렉토리 구성
```
module-infra
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── codingbottle
│   │   │           └── redis
│   │   │               └── config
│   │   │               └── domain
│   │   │                   └── post
│   │   │                   └── quiz
│   │   │                   └── user
│   │   │                   └── weather
│   │   │           └── s3
│   │   │               └── config
│   │   │               └── model
│   │   │               └── service
│   └── test
|__ build.gradle
``` 