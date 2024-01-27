# API 모듈
API 모듈은 API 서버를 구성하는 모듈입니다. 

## 기능
API 서버를 구성하는 모듈입니다. API 모듈은 사용자의 요청을 받아서 처리하고, 데이터베이스에 접근하여 데이터를 가져오는 역할을 합니다.

## 디렉토리 구성
```
module-api
├── README.md
├── docs
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── codingbottle
│   │   │           └── common
│   │   │           └── domain
│   │   │               └── category
│   │   │               └── image
│   │   │               └── information
│   │   │               └── post
│   │   │               └── quiz
│   │   │               └── rank
│   │   │               └── region
│   │   │               └── user
│   │   │               └── weather
│   │   │           └── security
│   │   └── resources
│   │       └── application.yml
│   └── test
|__ build.gradle
``` 