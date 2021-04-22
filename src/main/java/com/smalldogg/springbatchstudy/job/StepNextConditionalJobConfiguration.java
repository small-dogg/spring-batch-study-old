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
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob(){
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                .on("FAILED") // FAILED 일 경우
                .to(conditionalJobStep3()) // Step3으로 이동한다.
                .on("*") // Step3 진행이후 어떠한결과라도
                .end()//Flow를 종료한다.
                .from(conditionalJobStep1())
                .on("*")// FAILED 외 다른 결과가 나오면
                .to(conditionalJobStep2()) // Step2로 이동한다.
                .end() // Job을 종료한다.
                .build();
    }

    @Bean
    public Step conditionalJobStep1(){
        return stepBuilderFactory.get("conditionalJobStep1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> conditionalJobStep1");
//                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step conditionalJobStep2(){
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> conditionalJobStep2");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step conditionalJobStep3(){
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>>>> conditionalJobStep3");
                    return RepeatStatus.FINISHED;
                })).build();
    }
}
