package com.halove.core.entity;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/8/5
 * 广告的实体
 */

public class VideoVaule {

    public String resourceID;
    public String adid;
    public String resource;
    public String thumb;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
}
