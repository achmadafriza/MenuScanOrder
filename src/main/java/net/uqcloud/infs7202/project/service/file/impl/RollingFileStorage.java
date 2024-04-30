package net.uqcloud.infs7202.project.service.file.impl;

import net.uqcloud.infs7202.project.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service("rollingFileStorage")
public class RollingFileStorage implements FileStorageService {
    @Value("${service.storage.root}")
    private String rootDir;

    @Value("${service.storage.rolling.dir}")
    private String directory;

    @Value("${service.storage.url}")
    private String rootUrl;

    @Override
    public String getDirectory() {
        Calendar systemDate = Calendar.getInstance();
        return String.format("%s/%d/%d", directory, systemDate.get(Calendar.YEAR), systemDate.get(Calendar.MONTH)+1);
    }

    @Override
    public String getRootDirectory() {
        return rootDir;
    }

    @Override
    public String resolveAbsoluteURL(String directory, String filename) {
        return String.format("%s%s/%s", rootUrl, directory, filename);
    }
}
