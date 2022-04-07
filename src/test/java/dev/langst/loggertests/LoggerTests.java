package dev.langst.loggertests;

import dev.langst.utilities.LogLevel;
import dev.langst.utilities.Logger;
import org.junit.jupiter.api.Test;

public class LoggerTests {

    @Test
    void info_log_test(){

        Logger.log("Hello", LogLevel.INFO);
    }
}
