# spring-batch-study
Study for Spring Batch

본 프로젝트는 Spring Batch를 학습하고, Spring Batch 실습 위주로 Batch에 대한 사용 방법을 제시한다.

---

###1. Prerequisite
  - MariaDB 10.5
  - Java 11
  - Gradle

###2. Consist of Project
  - Spring Batch 2.4.5
  - Lombok 2.9.2

###3. What's Spring Batch
Spring Batch를 통해, 대량의 정보를 집계하여 처리하는 벌크 프로세싱을 개발합니다.
예를들어, 월별 사용 데이터를 수집하여 통계 정보로써 이를 활용한다던가하는 프로세스 말입니다.

이 Spring Batch는 이러한 벌크 프로세싱들을 수행하는 작업(Batch)을 위한 프레임 워크입니다.
공식 문서에서는 스프링 배치를 포괄적인 경량 프레임워크로, 엔터프라이즈 시스템 우녕에 일상적으로 꼭 필요한 견고한 배치 어플리케이션 개발을 위해 설계되었다 라고 표현하고 있습니다.

```
"A lightweight, comprehensive batch framework designed to enable the development of robust batch applications vital for the daily operations of enterprise systems."
```

스프링 배치는 아래와 같은 대용량 데이터 처리에 필요한 필수 기능을 제공합니다.
  - 로깅 추적
  - 트랙잭션 관리
  - Job 프로세싱 통계
  - Job 관리
  - 리소스 관리
  - etc..

###4. Scenario
전통적인 배치 프로그램은
 1. 데이터베이스, 파일, 큐에서 다량의 데이터를 조회
 2. 특정 방법을 통해, 데이터 가공
 3. 가공된 데이터 다시 저장
의 절차로 동작합니다.
    
스프링 배치는 유사한 트랜잭션을 하나로 묶어 처리함으로써, 반복적인 작업을 자동화합니다.

####Business Scenarios
 - 배치 프로세스를 주기적으로 커밋
 - 동시 다발적인 배치 처리 : job을 병렬 처리
 - 단계적인 엔터프라이즈 메시지 중심 처리
 - 다량의 병렬 배치 처리
 - 실패 후 수동 또는 스케줄링에 의한 재시작
 - 의존관계가 있는 step 여러 개를 순차적으로 처리 (워크플로우 중심 배치로 확장)
 - 부분 처리 : 레코드 스킵 (e.g. 롤백 진행 시)
 - 배치 규모가 작거나, 저장 프로시저나 스크립트가 이미 있는 경우, 배치 전체에 걸친 트랜잭션

####Technical Objectives
