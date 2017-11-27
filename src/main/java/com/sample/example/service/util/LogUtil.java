package com.sample.example.service.util;

import java.util.Date;

public class LogUtil {
    public void logInfo(String info){
        System.out.println(new Date()+" "+Thread.currentThread()+" "+info);
    }
}
