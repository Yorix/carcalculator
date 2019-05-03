package com.yorix.autometer.config;

import com.yorix.autometer.service.DataStorageService;
import com.yorix.autometer.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class Start {
    private final ImageStorageService imageStorageService;
    private final DataStorageService dataStorageService;
    private String updateAns;
    private String rootLocation;
    private String dbLocation;

    @Autowired
    public Start(AppProperties properties,
                 DataStorageService dataStorageService,
                 ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
        this.dataStorageService = dataStorageService;
        this.rootLocation = properties.getRootLocation();
        this.dbLocation = properties.getDbBackupLocation();
    }

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        imageStorageService.init();
        Files.createDirectories(Path.of(dbLocation));
        dataStorageService.save();

        String command = String.format("cmd /c cd /d \"%s\" && git pull>.gitAns", rootLocation);
        Runtime.getRuntime().exec(command).waitFor();
        checkUpdate(new File(rootLocation + "/.gitAns"));
        Runtime.getRuntime().exec("cmd /c explorer http://localhost:8080/");
    }

    public void installUpdate() {
        String command = "cmd /c " +
                "cd /d \"" + rootLocation + "\" && " +
                "SetLocal EnableExtensions && " +
                "SET ProcessName=javaw.exe && " +
                "TaskList /FI \"ImageName EQ %ProcessName%\" | Find /I \"%ProcessName%\" && " +
                "IF NOT %ERRORLEVEL% NEQ 0 (taskkill /IM javaw.exe /f) && " +
                "call mvn package -am -o -Dmaven.test.skip -T 1C && " +
                "run.cmd";
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkUpdate(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        updateAns = new String(is.readAllBytes());
    }

    public String getUpdateAns() {
        return updateAns;
    }
}
