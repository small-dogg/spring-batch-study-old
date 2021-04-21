package com.smalldogg.springbatchstudy.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class TutorialTaskletStepExecutionListener implements Tasklet, StepExecutionListener {

//    private final ARepository aRepository;
//    private final AService aService;
    // List<User> userList;

    @Override// Override by StepExecutionListener
    public void beforeStep(StepExecution stepExecution) {
        // Item reader
        //this.userList = aRepository.findAll();
    }

    @Override// Override by Tasklet
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //Processor
        return null;
    }

    @Override//Override by StepExecutionListener
    public ExitStatus afterStep(StepExecution stepExecution) {
        //Writer
        return null;
    }
}
