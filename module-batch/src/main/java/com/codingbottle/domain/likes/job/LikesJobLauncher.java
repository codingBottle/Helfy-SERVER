package com.codingbottle.domain.likes.job;

import com.codingbottle.domain.likes.service.LikesBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LikesJobLauncher {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final LikesBatchService batchService;
    
    @Bean
    public Job likesJob(){
        return new JobBuilder("likesJob", jobRepository)
                .start(likesStep())
                .preventRestart()
                .build();
    }

    @Bean
    public Step likesStep() {
        return new StepBuilder("likesStep", jobRepository)
                .tasklet(likesTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet likesTasklet() {
        return (contribution, chunkContext) -> {
            batchService.cacheToDb();
            return RepeatStatus.FINISHED;
        };
    }
}
