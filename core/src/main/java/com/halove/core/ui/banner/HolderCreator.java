package com.halove.core.ui.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

/**
 * Created by xieshangwu on 2017/7/24
 */

public class HolderCreator implements CBViewHolderCreator<ImageHolder>{
    @Override
    public ImageHolder createHolder() {
        return new ImageHolder();
    }
}
