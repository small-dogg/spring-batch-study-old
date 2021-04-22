package com.smalldogg.springbatchstudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StepNextConditionalJobWithCustomListenerConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /** 해당 Job의 문제점은,
     *  Step이 다당하는 역할이 2개이상 : 실제 처리해야하는 로직 이외에 분기ㅓ리를 위핸 ExitStatus 조작이 필요
      * 다양한 분기 로직 처리의 어려움 : ExitStatus를 커스텀하게 고치기 위해 Listener를 생성하고 Job Flow에 등록해야하는 번거로움
     */
    @Bean
    public Job stepNextConditionalJobWithCustomListener(){
        return jobBuilderFactory.get("stepNextConditionalJobWithCustomListener")
                .start(conditionalJobWithCustomListenerStep1())
                .on("FAILED WITH SKIPS") // FAILED 일 경우
                .to(conditionalJobWithCustomListenerStep3()) // Step3으로 이동한다.
                .on("*") // Step3 진행이후 어떠한결과라도
                .end()//Flow를 종료한다.
                .from(conditionalJobWithCustomListenerStep1())
                .on("*")// FAILED 외 다른 결과가 나오면
                .to(conditionalJobWithCustomListenerStep2()) // Step2로 이동한다.
                .next(conditionalJobWithCustomListenerStep3())
                .on("*")
                .end()
                .end() // Job을 종료한다.
                .build();
    }

    @Bean
    public Step conditionalJobWithCustomListenerStep1(){
        return stepBuilderFactory.get("conditionalJobWithCustomListenerStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> conditionalJobStep1");
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step conditionalJobWithCustomListenerStep2(){
        return stepBuilderFactory.get("conditionalJobWithCustomListenerStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> conditionalJobStep2");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step conditionalJobWithCustomListenerStep3(){
        return stepBuilderFactory.get("conditionalJobWithCustomListenerStep3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> conditionalJobStep3");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
