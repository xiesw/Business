package com.halove.core.ui.banner;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/7/24
 */

public class BannerCreator {

    public static void setDefault(ConvenientBanner<String> convenientBanner, ArrayList<String>
            banners, OnItemClickListener clickListener) {
        convenientBanner.setPages(new HolderCreator(), banners)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(clickListener)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }
}
