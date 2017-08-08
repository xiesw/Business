package com.halove.business.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.business.entity.recommand.RecommandValue;
import com.halove.core.imageloader.ImageLoaderManager;
import com.halove.core.utils.Utils;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/8/6
 */

public class HomeSalePagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<RecommandValue> mData;
    private ImageLoaderManager mImageLoaderManager;

    public HomeSalePagerAdapter(Context context, ArrayList<RecommandValue> data) {
        mContext = context;
        mData = data;
        mImageLoaderManager = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final RecommandValue value = mData.get(position % mData.size());
        View rootView = View.inflate(mContext, R.layout.item_product_card_viewpager_item, null);
        TextView titleView = (TextView) rootView.findViewById(R.id.item_title_view);
        TextView infoView = (TextView) rootView.findViewById(R.id.item_info_view);
        TextView priceView = (TextView) rootView.findViewById(R.id.item_price_view);
        TextView textView = (TextView) rootView.findViewById(R.id.item_text_view);
        LinearLayout photoLayout = (LinearLayout) rootView.findViewById(R.id.prodct_photo_layout);

        titleView.setText(value.title);
        infoView.setText(value.info);
        priceView.setText(value.price);
        textView.setText(value.text);

        for(String s : value.url) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px
                    (mContext, 80), ViewGroup.LayoutParams.MATCH_PARENT);
            params.leftMargin = Utils.dip2px(mContext, 5);

            mImageLoaderManager.displayImage(s, imageView);
            photoLayout.addView(imageView, params);
        }

        container.addView(rootView);

        return rootView;
    }
}
