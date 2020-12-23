package com.jariko.samples.java;

import com.smeup.dbnative.ConnectionConfig;
import com.smeup.dbnative.DBNativeAccessConfig;
import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.ReloadConfig;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.interpreter.FileMetadata;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class WorkWithFileMetadata {

    private static final String PGM = "     FVERAPG9L  IF   E           K DISK\n";


    // Serialize metadata in json format
    private static String toJson(FileMetadata metadata) {
        return metadata.toJson();
    }

    private static FileMetadata createInstance(String dbFile) {
        try {
            URL url = WorkWithFileMetadata.class.getResource("/metadata/" + dbFile + ".json");
            if (url == null) {
                throw new IOException("Not found resource for " + dbFile);
            } else {
                return FileMetadata.createInstance(url.openStream());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ConnectionConfig connectionConfig = new ConnectionConfig("*", "jdbc:blabla",
                "user", "pass", "driver.com", null, new HashMap<>());

        ReloadConfig reloadConfig = new ReloadConfig(
                new DBNativeAccessConfig(Arrays.asList(connectionConfig)),
                dbFile -> WorkWithFileMetadata.createInstance(dbFile)
        );
        Configuration configuration = new Configuration();
        configuration.setReloadConfig(reloadConfig);
        // Load inline pgm
        CommandLineProgram jariko = RunnerKt.getProgram(PGM.toString());
        jariko.singleCall(Arrays.asList(), configuration);
    }
}
