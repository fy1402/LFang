package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by i-feng on 2018/7/3.
 */


@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
