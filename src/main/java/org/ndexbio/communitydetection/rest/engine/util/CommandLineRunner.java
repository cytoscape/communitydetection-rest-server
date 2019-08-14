package org.ndexbio.communitydetection.rest.engine.util;

import java.util.List;
import java.util.Map;

/**
 *
 * @author churas
 */
public interface CommandLineRunner {
    
    public void setWorkingDirectory(final String workingDir);

    public void setEnvironmentVariables(Map<String, String> envVars);

    public String getLastCommand();
    
    /**
     * Runs command line program specified by first argument.
     * @param command - First argument should be full path to command followed by arguments
     * @return String containing standard error and standard out generated by program.  
     * @throws java.lang.Exception if there was an error invoking the process or if the process returns non zero exit code
     */
    public String runCommandLineProcess(String... command) throws Exception;
    
}
