# Domain 모듈
Domain 모듈은 서버에서 사용되는 모든 도메인을 정의합니다.

## 기능
Domain 모듈은 엔티티를 정의하고 엔티티 간의 관계를 정의합니다. 또한 엔티티의 상태를 변경하는 메소드를 정의합니다.
레포지토리 인터페이스를 정의하고, 레포지토리 인터페이스를 구현하는 클래스를 정의합니다.

## 디렉토리 구성
```
module-domain
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── codingbottle
│   │   │           └── common
│   │   │               └── config
│   │   │               └── entity
│   │   │           └── domain
│   │   │               └── category
│   │   │               └── image
│   │   │               └── information
│   │   │               └── post
│   │   │               └── quiz
│   │   │               └── region
│   │   │               └── user
│   └── test
|__ build.gradle
``` 