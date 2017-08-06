package com.halove.business.entity.recommand;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/8/6
 */

public class HeadEntity {

    public ArrayList<String> ads;
    public ArrayList<String> middle;
    public ArrayList<FooterBean> footer;


    public static class FooterBean {
        public String title;
        public String info;
        public String from;
        public String imageOne;
        public String imageTwo;
        public String destationUrl;
    }
}
