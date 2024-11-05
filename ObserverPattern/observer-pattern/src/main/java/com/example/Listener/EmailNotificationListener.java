package com.example.Listener;

public class EmailNotificationListener implements EventListener{

    private String email;

    public EmailNotificationListener(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void update(String eventType, String fileName) {
        System.out.println("Email to : "+ this.email + " for event type: " + eventType + ". Someone Has modified file : "+ fileName);
    }
    
}
