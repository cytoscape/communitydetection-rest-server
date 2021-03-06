package org.ndexbio.communitydetection.rest.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.ndexbio.communitydetection.rest.model.exceptions.CommunityDetectionException;
import org.ndexbio.communitydetection.rest.services.Configuration;

/**
 *
 * @author churas
 */
public class TestConfiguration {
    
    @Rule
    public TemporaryFolder _folder = new TemporaryFolder();
    
    
    @Test
    public void testConfigurationNoConfigurationFound() throws IOException {
        File tempDir = _folder.newFolder();
        try {
            File configFile = new File(tempDir.getAbsolutePath() + File.separator + "conf");
           
            Configuration.setAlternateConfigurationFile(configFile.getAbsolutePath());
            try {
                Configuration config = Configuration.reloadConfiguration();
                fail("Expected CommunityDetectionException");
            } catch(CommunityDetectionException ee){
                assertTrue(ee.getMessage().contains("FileNotFound Exception"));
            }
        } finally {
            _folder.delete();
        }
    }
    
    @Test
    public void testConfigurationAlternatePathNoMatchingProps() throws CommunityDetectionException, IOException {
        File tempDir = _folder.newFolder();
        try {
            File configFile = new File(tempDir.getAbsolutePath() + File.separator + "conf");
            Properties props = new Properties();
            props.setProperty("foo", "hello");
            FileOutputStream fos = new FileOutputStream(configFile);
            props.store(fos, "hello");
            fos.flush();
            fos.close();
            Configuration.setAlternateConfigurationFile(configFile.getAbsolutePath());
            Configuration config = Configuration.reloadConfiguration();
            assertEquals("/tmp", config.getTaskDirectory());
            assertNull(config.getCommunityDetectionEngine());
            assertEquals(1, config.getNumberWorkers());
            assertEquals("docker", config.getDockerCommand());
            
            assertEquals(null, config.getAlgorithms());
        } finally {
            _folder.delete();
        }
    }
    
    @Test
    public void testConfigurationValidConfiguration() throws CommunityDetectionException, IOException {
        File tempDir = _folder.newFolder();
        try {
            File configFile = new File(tempDir.getAbsolutePath() + File.separator + "conf");
            File taskDir = new File(tempDir.getAbsolutePath() + File.separator + "tasks");
            Properties props = new Properties();
            props.setProperty(Configuration.TASK_DIR, taskDir.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(configFile);
            props.store(fos, "hello");
            fos.flush();
            fos.close();
            Configuration.setAlternateConfigurationFile(configFile.getAbsolutePath());
            Configuration config = Configuration.reloadConfiguration();
            assertEquals(taskDir.getAbsolutePath(), config.getTaskDirectory());
            assertNull(config.getCommunityDetectionEngine());
        } finally {
            _folder.delete();
        }
    }
    
    @Test
    public void testParseAlgorithmMapInvalidData() throws CommunityDetectionException, IOException {
        File tempDir = _folder.newFolder();
        try {
            File configFile = new File(tempDir.getAbsolutePath() + File.separator + "conf");
            File taskDir = new File(tempDir.getAbsolutePath() + File.separator + "tasks");
            Properties props = new Properties();
            props.setProperty(Configuration.TASK_DIR, taskDir.getAbsolutePath());
            props.setProperty(Configuration.ALGORITHM_MAP, "haha");
            FileOutputStream fos = new FileOutputStream(configFile);
            props.store(fos, "hello");
            fos.flush();
            fos.close();
            Configuration.setAlternateConfigurationFile(configFile.getAbsolutePath());
            Configuration config = Configuration.reloadConfiguration();
            assertEquals(taskDir.getAbsolutePath(), config.getTaskDirectory());
            assertNull(config.getCommunityDetectionEngine());
            assertEquals(180, config.getAlgorithmTimeOut());
            assertEquals(null, config.getAlgorithms());
        } finally {
            _folder.delete();
        }
    }
}
