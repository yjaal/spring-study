package win.iot4yj.dto;

public class JobTrigger {

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务所在组
     */
    private String jobGroup;

    /**
     * 任务类名
     */
    private String jobClassName;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 触发器所在组
     */
    private String triggerGroup;

    /**
     * 表达式
     */
    private String cronExp;

    /**
     * 时区
     */
    private String zone;

    /**
     * 状态
     */
    private String triggerStatus;

    /**
     * 创建时间
     */
    private String startTime;

    /**
     * 上次执行时间
     */
    private String prevFireTime;

    /**
     * 下次执行时间
     */
    private String nextFireTime;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(String triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(String prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public String getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }
}
