package com.example.Publisher;

import java.io.File;
import com.example.Model.EventManager;

public class Editor {
    private File file;
    public EventManager eventManager;

    public Editor() {
        this.eventManager = new EventManager("open", "save");
    }

    public void openFile(String filePath) {
        file = new File(file, filePath);
        System.out.println("FIle Created and calling notify.");
        this.eventManager.notify("open", filePath);
    }

    public void saveFile() throws Exception {
        if(this.file!=null) {
            this.eventManager.notify("save", this.file.getAbsolutePath());
        } else {
            throw new Exception("file is null");
        }

    }
}
