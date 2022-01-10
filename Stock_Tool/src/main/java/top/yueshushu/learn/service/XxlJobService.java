package top.yueshushu.learn.service;
/**
 * 定时任务的远程调用
 */
public interface XxlJobService {
    /**
     * 添加定时任务
     * @param cron
     * @param jobDesc
     * @param group
     * @param jobHandler
     * @param creator
     * @param executorParam
     * @return
     */
    Integer addJob(String cron, String jobDesc, Integer group, String jobHandler, String creator, String executorParam);

    /**
     * 修改定时任务
     * @param cron
     * @param jobDesc
     * @param group
     * @param jobHandler
     * @param creator
     * @param executorParam
     * @param jobId
     */
    void updateJob(String cron, String jobDesc, Integer group, String jobHandler, String creator, String executorParam,
                   Integer jobId);

    /**
     * 启用定时任务
     * @param jobId
     */
    void enableJob(Integer jobId);

    /**
     * 停用定时任务
     * @param jobId
     */
    void disableJob(Integer jobId);

    /**
     * 删除定时任务
     * @param jobId
     */
    void removeJob(Integer jobId);
}