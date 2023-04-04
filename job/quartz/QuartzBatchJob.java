@Slf4j
@PersistJobDataAfterExecution // jobDataMap 데이터 수정 이후 다음 실행에도 데이터 반영
@Component
public class QuartzBatchJob implements org.quartz.Job {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private BeanUtil beanUtil;

    private int executeCount = 0;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        if(jobDataMap.containsKey("executeCount")){
            executeCount = jobDataMap.getInt("executeCount");
        }
        jobDataMap.put("executeCount", ++executeCount);

        Job job = (Job) beanUtil.getBean((String) jobDataMap.get(QuartzService.JOB_NAME));

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("curDate", new Date())
                .toJobParameters();

        jobLauncher.run(job,jobParameters);
    }
}
