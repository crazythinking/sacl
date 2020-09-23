package net.engining.sacl.init.kettle;

import net.engining.pg.support.utils.ExceptionUtilsExt;
import net.engining.pg.support.utils.ValidateUtilExt;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Kettle API 封装
 *
 * @author Eric Lu
 */
public class KettleManagerService {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(KettleManagerService.class);

    private String kettleRepoPath;

    private String kettleRepoId;

    private String kettleRepoName;

    private String kettleLogLevel;

    private String username = "";
    private String password = "";

    public KettleManagerService(String kettleRepoPath, String kettleRepoId,
                                String kettleRepoName, String kettleLogLevel) {
        this.kettleRepoPath = kettleRepoPath;
        this.kettleRepoId = kettleRepoId;
        this.kettleRepoName = kettleRepoName;
        this.kettleLogLevel = kettleLogLevel;

        try {
            KettleEnvironment.init();
        } catch (KettleException e) {
            ExceptionUtilsExt.dump(e);
        }
    }

    /**
     * 从 ktr 文件执行 Transformation
     *
     * @param transFileName ktr文件名
     * @param params        KV参数列表
     * @throws KettleException
     */
    public void runTransformationFromFile(String transFileName, Map<String, String> params) throws KettleException {

        if (!KettleEnvironment.isInitialized()) {
            KettleEnvironment.init();
        }

        // Loading the transformation file from file system into the TransMeta object.
        // The TransMeta object is the programmatic representation of a transformation definition.
        String transformationFilePath = kettleRepoPath + File.separator + transFileName;
        TransMeta tm = new TransMeta(transformationFilePath);

        // Creating a transformation object which is the programmatic representation of a transformation
        // A transformation object can be executed, report success, etc.
        Trans trans = new Trans(tm);

        // adjust the log level
        trans.setLogLevel(this.getLogerLevel(kettleLogLevel));

        // setup parameters
        if (params != null) {
            Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                trans.setParameterValue(entry.getKey(), entry.getValue());
            }
        }

        // starting the transformation, which will execute asynchronously
        trans.execute(null);
        // waiting for the transformation to finish
        trans.waitUntilFinished();
        // retrieve the result object, which captures the success of the transformation
        Result result = trans.getResult();

        logger.info(
                "Trans {} executed with successfully status: {}, and {} errors",
                transformationFilePath,
                result.getResult(),
                result.getNrErrors()
        );

    }

    public void runJobFromFile(String jobName, Map<String, String> params) throws KettleException {

        if (!KettleEnvironment.isInitialized()) {
            KettleEnvironment.init();
        }

        // Loading the job file from file system into the JobMeta object.
        // The JobMeta object is the programmatic representation of a job definition.
        String jobFilePath = kettleRepoPath + File.separator + jobName;
        JobMeta jm = new JobMeta(jobFilePath, null);

        // Creating a Job object which is the programmatic representation of a job
        // A Job object can be executed, report success, etc.
        Job job = new Job(null, jm);

        // adjust the log level
        job.setLogLevel(this.getLogerLevel(kettleLogLevel));

        if (params != null) {
            Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                job.setVariable(entry.getKey(), entry.getValue());
            }
        }

        // starting the job thread, which will execute asynchronously
        job.start();
        // waiting for the job to finish
        job.waitUntilFinished();
        // retrieve the result object, which captures the success of the job
        Result result = job.getResult();

        logger.info(
                "Job {} executed with successfully status: {}, and {} errors",
                jobName,
                result.getResult(),
                result.getNrErrors()
        );
    }

    /**
     * This method executes a job stored in a repository.
     * <p>
     * It demonstrates the following:
     * <p>
     * - Loading a job definition from a repository - Setting named parameters
     * for the job - Setting the log level of the job - Executing the job,
     * waiting for it to finish - Examining the result of the job
     * <p>
     * When calling this method, kettle will look for the given repository name
     * in $KETTLE_HOME/.kettle/repositories.xml
     * <p>
     * If $KETTLE_HOME is not set explicitly, the user's home directory is assumed
     *
     * @param jobName        the name of the job to execute (i.e. "parameterized_job")
     * @param params
     * @return the job that was executed, or null if there was an error
     */
    public Job runJobFromRepository(String jobName, String subdirectory, Map<String, String> params) throws KettleException {

        if (!KettleEnvironment.isInitialized()) {
            KettleEnvironment.init();
        }

        // read the repositories.xml file to determine available repositories
        RepositoriesMeta repositoriesMeta = new RepositoriesMeta();
        repositoriesMeta.readData();

        // find the repository definition using its name
//        RepositoryMeta repositoryMeta = repositoriesMeta.findRepositoryById(kettleRepoId);
//        if (ValidateUtilExt.isNullOrEmpty(repositoryMeta)){
//
//        }

        RepositoryMeta repositoryMeta = repositoriesMeta.findRepository(kettleRepoName);

        if (repositoryMeta == null) {
            throw new KettleException(
                    String.format(
                            "Cannot find repository %s . Please make sure it is defined in your %s  file",
                            kettleRepoName,
                            Const.getKettleUserRepositoriesFile()
                    )

            );
        }

        // use the plug-in system to get the correct repository implementation
        // the actual implementation will vary depending on the type of given repository (File-based, DB-based, EE, etc.)
        PluginRegistry registry = PluginRegistry.getInstance();
        Repository repository = registry.loadClass(RepositoryPluginType.class, repositoryMeta, Repository.class);

        // connect to the repository using given username and password
        repository.init(repositoryMeta);
        repository.connect(username, password);

        // find the directory we want to load from
        RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree();

        // load latest revision of the job
        // The JobMeta object is the programmatic representation of a job definition.
        JobMeta jobMeta;
        if (ValidateUtilExt.isNotNullOrEmpty(subdirectory)){
            jobMeta = repository.loadJob(jobName, tree.findChild(subdirectory), null, null);
        }
        else {
            jobMeta = repository.loadJob(jobName, tree, null, null);
        }

        // Creating a Job object which is the programmatic representation of a job
        // A Job object can be executed, report success, etc.
        Job job = new Job(repository, jobMeta);

        // adjust the log level
        job.setLogLevel(this.getLogerLevel(kettleLogLevel));

        for (String key : params.keySet()) {
            job.setVariable(key, params.get(key));
        }

        // starting the job, which will execute asynchronously
        job.start();

        // waiting for the job to finish
        job.waitUntilFinished();

        // retrieve the result object, which captures the success of the job
        Result result = job.getResult();

        logger.info(
                "Job {} executed with successfully status: {}, and {} errors",
                jobName,
                result.getResult(),
                result.getNrErrors()
        );

        return job;
    }

    /**
     * 取得kettle的日志级别
     *
     */
    private LogLevel getLogerLevel(String level) {
        LogLevel logLevel;
        if ("basic".equals(level)) {
            logLevel = LogLevel.BASIC;
        } else if ("detail".equals(level)) {
            logLevel = LogLevel.DETAILED;
        } else if ("error".equals(level)) {
            logLevel = LogLevel.ERROR;
        } else if ("debug".equals(level)) {
            logLevel = LogLevel.DEBUG;
        } else if ("minimal".equals(level)) {
            logLevel = LogLevel.MINIMAL;
        } else if ("rowlevel".equals(level)) {
            logLevel = LogLevel.ROWLEVEL;
        } else if ("nothing".endsWith(level)) {
            logLevel = LogLevel.NOTHING;
        } else {
            logLevel = null;
        }
        return logLevel;
    }

}
