@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;
    public static final String JOB_NAME = "JOB_NAME";
    private final ApiService apiService;

    @Value("${api.gwcd}")
    private String gwcd;
    @Value("${api.passwd}")
    private String passwd;

    @PostConstruct
    public void init() throws Exception{
        scheduler.clear();
        scheduler.getListenerManager().addJobListener(new QuartzJobListener());
        scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());

        ResponseTokenDto loginResult = apiService.login(new RequestDto(gwcd, passwd));

        addJob(QuartzBatchJob.class, "apiJob", "api를 가져오는 Job입니다.", null, "0/50 * * * * ?"); 
    }

    // Job 추가
    public <T extends Job> void addJob(Class<? extends Job> job, String name, String desc, Map paramMap, String cron) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job,name,desc,paramMap);
        Trigger trigger = BuildCronTrigger(cron);
        if(scheduler.checkExists(jobDetail.getKey())){
            scheduler.deleteJob(jobDetail.getKey());
        }
        scheduler.scheduleJob(jobDetail,trigger);
    }

    // JobDetail 생성
    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String name, String desc, Map paramMap){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_NAME, name);
        jobDataMap.put("executeCount", 1);

        return JobBuilder
                .newJob(job)
                .withIdentity(name)
                .withDescription(desc)
                .usingJobData(jobDataMap)
                .build();
    }

    //Trigger 생성
    private Trigger BuildCronTrigger(String cronExp){
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }
}
