package groovys

import com.xxl.job.core.handler.annotation.JobHandler
import net.engining.pg.batch.bean.JobShardingRule
import net.engining.pg.batch.runner.AbstractBatchJobRunner
import net.engining.pg.batch.sdk.infrastructure.AbstractJobHandler
import net.engining.pg.batch.sdk.infrastructure.utils.BatchCommonLogicSdkUtils
import net.engining.pg.batch.sdk.infrastructure.utils.BatchCommonLogicUtils
import net.engining.template.batch.xxljob.executor.controller.DynamicJobHandler

@JobHandler(value="test1JobHandler")
class Test1JobHandler extends AbstractJobHandler implements DynamicJobHandler{

    @Override
    List<AbstractBatchJobRunner.ExecutionStatus> fireJob(JobShardingRule jobShardingRule) throws Exception {

        //xxljob调度中心发来的参数
        String xxljobParam = jobShardingRule.getExecuteParam()
        Map<String, String> map = BatchCommonLogicUtils.transformExecParam(xxljobParam);

        //call指定目录batch-cli
        String workPath = map.get("workPath");
        String jobName = map.get("jobName");
        String appName = map.get("appName");
        String shShell = BatchCommonLogicUtils.genStartBatchCliShell(
                workPath,
                jobName,
                appName
        )

        return BatchCommonLogicSdkUtils.execBatchCilShell(shShell)
    }
}
