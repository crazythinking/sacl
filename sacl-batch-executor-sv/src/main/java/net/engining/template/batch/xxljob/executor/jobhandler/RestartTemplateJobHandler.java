package net.engining.template.batch.xxljob.executor.jobhandler;

import com.xxl.job.core.handler.annotation.JobHandler;
import net.engining.pg.batch.bean.JobShardingRule;
import net.engining.pg.batch.runner.AbstractBatchJobRunner;
import net.engining.pg.batch.sdk.infrastructure.AbstractJobHandler;
import net.engining.pg.batch.sdk.infrastructure.utils.BatchCommonLogicSdkUtils;
import net.engining.pg.batch.sdk.infrastructure.utils.BatchCommonLogicUtils;
import net.engining.pg.props.CommonProperties;
import net.engining.pg.support.utils.ValidateUtilExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 重启失败的Job，由xxl-job admin 远程调度，可指定EXECUTION_ID-示例
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
@JobHandler(value="restartTemplateJobHandler")
@Component
public class RestartTemplateJobHandler extends AbstractJobHandler {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(RestartTemplateJobHandler.class);

    @Override
    public List<AbstractBatchJobRunner.ExecutionStatus> fireJob(JobShardingRule jobShardingRule) throws Exception {

        //xxljob调度中心发来的参数
        String xxljobParam = jobShardingRule.getExecuteParam();
        Map<String, String> map = BatchCommonLogicUtils.transformExecParam(xxljobParam);

        //call指定目录batch-cli
        String workPath = map.get("workPath");
        String jobName = map.get("jobName");
        String appName = map.get("appName");
        String shShell;
        if (ValidateUtilExt.isNotNullOrEmpty(map.get(BatchCommonLogicUtils.EXECUTION_ID))){
            shShell = BatchCommonLogicUtils.genRestartBatchCliShell(
                    map.get(BatchCommonLogicUtils.EXECUTION_ID),
                    workPath,
                    jobName,
                    appName
            );
        }
        else {
            shShell = BatchCommonLogicUtils.genRestartBatchCliShell(
                    null,
                    workPath,
                    jobName,
                    appName
            );
        }

        return BatchCommonLogicSdkUtils.execBatchCilShell(shShell);
    }
}
