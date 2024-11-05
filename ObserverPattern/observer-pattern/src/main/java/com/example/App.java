package com.example;

import com.example.Listener.EmailNotificationListener;
import com.example.Listener.EventListener;

import com.example.Listener.LogOpenListener;
import com.example.Publisher.Editor;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Starting Application..." );

        String filePath1 = "/home/ashish/Documents/personal/ObserverPattern/log1.txt";
        String filePath2 = "/home/ashish/Documents/personal/ObserverPattern/log2.txt";

        EventListener logListener1 = new LogOpenListener(filePath1);
        EventListener logListener2 = new LogOpenListener(filePath2);

        EventListener emailListener1 = new EmailNotificationListener("a@gmail.com");
        EventListener emailListener2 = new EmailNotificationListener("b@gmail.com");

        Editor editor1 = new Editor();
        try {
            editor1.eventManager.subscribe("open", logListener1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            editor1.eventManager.subscribe("save", emailListener1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        editor1.openFile(filePath1);
        try {
            editor1.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println( "Starting Application..." );
    }
}
