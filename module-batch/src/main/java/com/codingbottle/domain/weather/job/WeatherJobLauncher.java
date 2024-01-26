package com.codingbottle.domain.weather.job;

import com.codingbottle.domain.weather.service.WeatherBatchService;
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
public class WeatherJobLauncher {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final WeatherBatchService weatherBatchService;

    @Bean
    public Job weatherJob(){
        return new JobBuilder("weatherJob", jobRepository)
                .start(weatherStep())
                .preventRestart()
                .build();
    }

    @Bean
    public Step weatherStep() {
        return new StepBuilder("weatherStep", jobRepository)
                .tasklet(weatherTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet weatherTasklet() {
        return (contribution, chunkContext) -> {
            weatherBatchService.setWeather();
            return RepeatStatus.FINISHED;
        };
    }
}
