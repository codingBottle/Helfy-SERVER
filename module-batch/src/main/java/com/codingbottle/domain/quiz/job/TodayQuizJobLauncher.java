package com.codingbottle.domain.quiz.job;

import com.codingbottle.domain.quiz.service.TodayQuizBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TodayQuizJobLauncher {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TodayQuizBatchService todayQuizBatchService;

    @Bean
    public Job todayQuizJob() {
        return new JobBuilder("todayQuizJob", jobRepository)
                .start(todayQuizStep())
                .preventRestart()
                .build();
    }

    @Bean
    public Step todayQuizStep() {
        return new StepBuilder("todayQuizStep", jobRepository)
                .tasklet(todayQuizTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet todayQuizTasklet() {
        return (contribution, chunkContext) -> {
            todayQuizBatchService.initializeData();
            return null;
        };
    }
}
