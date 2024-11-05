package com.example.Listener;

import java.io.File;

public class LogOpenListener implements EventListener {
    
    private File file;

    public LogOpenListener(String filePath) {
        this.file = new File(filePath);
    }

    public File getFile() {
        return file;
    }

    @Override
    public void update(String eventType, String fileName) {
        System.out.println("Someone has performed event: " + eventType + "to log file: " + fileName);
    }
}
