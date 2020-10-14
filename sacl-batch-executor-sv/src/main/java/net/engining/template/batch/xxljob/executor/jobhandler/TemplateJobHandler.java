package net.engining.template.batch.xxljob.executor.jobhandler;

import net.engining.pg.batch.bean.JobShardingRule;
import net.engining.pg.batch.runner.AbstractBatchJobRunner;
import net.engining.pg.batch.sdk.infrastructure.AbstractJobHandler;
import net.engining.pg.batch.sdk.infrastructure.utils.BatchCommonLogicSdkUtils;
import net.engining.pg.batch.sdk.infrastructure.utils.BatchCommonLogicUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 启动Job，由xxl-job admin 远程调度-示例
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
//@JobHandler(value="templateJobHandler")
//@Component
public class TemplateJobHandler extends AbstractJobHandler {

    private final static Logger log = LoggerFactory.getLogger(TemplateJobHandler.class);

    @Override
    public List<AbstractBatchJobRunner.ExecutionStatus> fireJob(JobShardingRule jobShardingRule) throws Exception {

        //xxljob调度中心发来的参数
        String xxljobParam = jobShardingRule.getExecuteParam();
        Map<String, String> map = BatchCommonLogicUtils.transformExecParam(xxljobParam);

        //call指定目录batch-cli
        String workPath = map.get("workPath");
        String jobName = map.get("jobName");
        String appName = map.get("appName");
        String shShell = BatchCommonLogicUtils.genStartBatchCliShell(
                workPath,
                jobName,
                appName
        );

        return BatchCommonLogicSdkUtils.execBatchCilShell(shShell);
    }

}
