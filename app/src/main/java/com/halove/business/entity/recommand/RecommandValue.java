package com.halove.business.entity.recommand;

import com.halove.business.entity.BaseEntity;
import com.halove.business.entity.monitor.Monitor;
import com.halove.business.entity.monitor.emevent.EMEvent;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class RecommandValue extends BaseEntity {

    public int type;
    public String logo;
    public String title;
    public String info;
    public String price;
    public String text;
    public String site;
    public String from;
    public String zan;
    public ArrayList<String> url;

    //视频专用
    public String thumb;
    public String resource;
    public String resourceID;
    public String adid;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;

}
