package org.cleroux.passwdservice.fsmon;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit tests for the User class.
 */
public class MonitoredFileTest {

    private static String testFilePath;
    private static String testFilePathChanged;
    private static MonitoredFile testMonitoredFile;

    @BeforeClass
    public static void init() {
        testFilePath = "/home/cleroux/test";
        MonitorHandler testHandler = new MonitorHandler() {
            public void changed(String filePath) {
                testFilePathChanged = filePath;
            }
        };

        testMonitoredFile = new MonitoredFile(testFilePath, testHandler);
    }

    @Before
    public void beforeTest() {
        testFilePathChanged = null;
    }

    /**
     * Test object creation.
     */
    @Test
    public void objectCreation() {
        assertEquals("MonitoredFile.getFilePath()", testFilePath,
                     testMonitoredFile.getFilePath());
    }

    /**
     * Test changed event handler.
     */
    @Test
    public void textChangedHandler() {
        testMonitoredFile.changed();
        assertEquals("MonitoredFile.changed() handler ran", testFilePath,
                     testFilePathChanged);
    }

}
