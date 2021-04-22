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

###4. Batch Scenario
전통적인 배치 프로그램은
 1. 데이터베이스, 파일, 큐에서 다량의 데이터를 조회
 2. 특정 방법을 통해, 데이터 가공
 3. 가공된 데이터 다시 저장
의 절차로 동작합니다.
    
스프링 배치는 유사한 트랜잭션을 하나로 묶어 처리함으로써, 반복적인 작업을 자동화합니다.

#### Business Scenarios
 - 배치 프로세스를 주기적으로 커밋
 - 동시 다발적인 배치 처리 : job을 병렬 처리
 - 단계적인 엔터프라이즈 메시지 중심 처리
 - 다량의 병렬 배치 처리
 - 실패 후 수동 또는 스케줄링에 의한 재시작
 - 의존관계가 있는 step 여러 개를 순차적으로 처리 (워크플로우 중심 배치로 확장)
 - 부분 처리 : 레코드 스킵 (e.g. 롤백 진행 시)
 - 배치 규모가 작거나, 저장 프로시저나 스크립트가 이미 있는 경우, 배치 전체에 걸친 트랜잭션

###5. Study

#### Consist of Spring Batch
Job
Step : 실제 Batch 작업을 수행하는 역할 - Batch로 실제 처리하고자 하는 기능과 설정을 모두 포함

#### 지정된 Job만 수행하기
Spring Batch가 실행될 때, **Program Arguments** 로 job.name이 입력되면, 입력된 값과 일치하는 job만 수행합니다.
또한, job.name이 입력되지 않으면, 그 어떠한 job도 수행되지 않습니다.
```
spring.batch.job.names: ${job.name: NONE}
```

#### 실제 운영환경에서의 Batch 실행
```
java -jar batchApplication.jar --job.name=nextStepJob
```

#### 조건별 흐름 제어(Flow)
Job이 수행되면서, Job 내 특정 Step의 성공 여부에 따라 조건별 그 다음 Step을 진행할 수 있습니다.

  - on()
    - 캐치할 ExitStatus 지정
    - \* 일 경우 모든 ExitStatus가 지정
  - to()
    - 다음으로 이동한 Step 지정
  - from()
    - 일종의 이벤트 리스너 역할
    - 상태값을 보고 일치하는 상태라면 to()에 포함된 step을 호출
    - step1의 이벤트 캐치가 FAILED로 되어있는 상태에서, 추가로 이벤트 캐치를 수행하기 위해 from을 사용해야함.
  - end()
    - end는 FlowBuilder를 반환하는 end와 FlowBuilder를 종료하는 end 2개가 있음
    - on("\*") 뒤에 작성되는 end의 경우, FlowBuilder를 반환하고,
    - build() 바로 앞에 작성되는 end의 경우, FlowBuilder를 종료하는 end
    - FlowBuilder를 반환하는 end() 호출 시, 계속적으로 From을 작성하여 Flow를 이어나갈 수 있음
    
#### Batch Status vs. Exit Status
  - Batch Status :Job 또는 Step의 실행 결과를 Spring에서 기록할 때 사용하는 Enum
```    
Batch Status {COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN}
```

만약
```
.on("FAILED").to(stepB())
```
와 같은 경우, on메서드의 exitCode가 FAILED로 끝나게 되면 stepB를 진행해라 라는 뜻 입니다.
즉, on() 메서드가 참조하는 것은 Batch Status가 아닌 Step의 Exit Status입니다.

  - Exit Status : Step 실행 이후의 상태

Spring Batch는 기본적으로 ExitStatus의 exitCode가 BatchStatus와 일치하도록 설정되어있습니다.
만약, 커스텀한 exitCode를 작성하려면, StepExecutionListenerSupport를 상속받아, Step에 대한 exitCode에 대한
새로운 정의가 필요합니다. ***SkipCheckingListener.java 참고***