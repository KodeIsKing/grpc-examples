package com.sample.example.service.util;

public class ThreadUtil {
    public void threadSleep(long sleepTime){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
