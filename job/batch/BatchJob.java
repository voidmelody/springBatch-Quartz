import kr.co.nice.nicein.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchJob {
    private final ApiService apiService;

    @Bean(name="apiJob")
    public Job apiJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("apiJob", jobRepository)
                .start(getUserApiStep(jobRepository,platformTransactionManager))
                .next(getDeptApiStep(jobRepository,platformTransactionManager))
                .build();
    }
  
    @Bean
    public Step getUserApiStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("getUserApiStep", jobRepository)
                .tasklet(new getUserApiTasklet(apiService), platformTransactionManager)
                .build();
    }
    @Bean
    public Step getDeptApiStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("getDeptApiStep", jobRepository)
                .tasklet(new getDeptApiTasklet(apiService), platformTransactionManager)
                .build();
    }
}
