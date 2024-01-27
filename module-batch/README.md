# Batch 모듈
Batch 모듈은 스프링 배치를 이용하여 데이터를 수집하고, 데이터를 가공하여 데이터베이스에 저장하는 역할을 합니다.

## 기능
Batch 서버를 구성하는 모듈입니다. 특정 시간에 데이터를 수집하고, 데이터를 가공하여 데이터베이스에 저장합니다.

## 디렉토리 구성
```
module-batch
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── codingbottle
│   │   │           └── config
│   │   │           └── domain
│   │   │               └── likes
│   │   │                   └── job
│   │   │                   └── scheduler
│   │   │                   └── service
│   │   │               └── weather
│   │   │                   └── config
│   │   │                   └── job
│   │   │                   └── model
│   │   │                   └── scheduler
│   │   │                   └── service
│   │   └── resources
│   │       └── application.yml
│   └── test
|__ build.gradle
``` 