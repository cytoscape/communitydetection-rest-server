package org.ndexbio.communitydetection.rest.engine.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author churas
 */
public class CommandLineRunnerImpl implements CommandLineRunner {
      private static final Logger _log
            = LoggerFactory.getLogger(CommandLineRunnerImpl.class.getName());
    
    private String _workingDirectory;
    private Map<String, String> _environVars;
    private String _lastCommand;
    
    @Override
    public void setWorkingDirectory(final String workingDir) {
        _workingDirectory = workingDir;
    }

    @Override
    public void setEnvironmentVariables(Map<String, String> envVars) {
        _environVars = envVars;
    }

    @Override
    public String getLastCommand() {
        return _lastCommand;
    }
    
    @Override
    public String runCommandLineProcess(String... command) throws Exception {        
        ArrayList<String> mCmd = new ArrayList<String>();
        _lastCommand = null;
        StringBuilder lastCmdSb = new StringBuilder();
        for (String c : command) {
            if (c.equals("")){
                continue;
            }
            mCmd.add(c);
            if (lastCmdSb.length() > 0){
                lastCmdSb.append(" ");
            }
            lastCmdSb.append(c);
        }
        _lastCommand = lastCmdSb.toString();
        
        _log.debug("Running command: " + _lastCommand);
        String[] yo = new String[mCmd.size()];
        ProcessBuilder pb = new ProcessBuilder(mCmd.toArray(yo));

        //lets caller set working directory
        if (_workingDirectory != null) {
            pb.directory(new File(_workingDirectory));
        }

        //lets caller set 1 or more environment variables
        if (_environVars != null && _environVars.isEmpty() == false) {
            Map<String, String> env = pb.environment();
            for (String key : _environVars.keySet()) {
                env.remove(key);
                env.put(key, _environVars.get(key));
            }
        }

        pb.redirectErrorStream(true);

        Process proc = pb.start();

        StringBuilder sb = new StringBuilder();

        BufferedInputStream bis = new BufferedInputStream(proc.getInputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(bis));
        String line = br.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = br.readLine();
        }
        br.close();
        int retVal = proc.waitFor();
        if (retVal != 0){
            throw new Exception("Non zero exit code ("+retVal+") received from "+
                    mCmd.get(0) + ": " + sb.toString());
        }
        return sb.toString();
    }
}
