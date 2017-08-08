package com.halove.business.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.business.ui.activity.PhotoViewActivity;
import com.halove.business.entity.recommand.RecommandValue;
import com.halove.core.imageloader.ImageLoaderManager;
import com.halove.core.utils.Utils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.halove.core.utils.Utils.dip2px;

/**
 * Created by xieshangwu on 2017/8/4
 */

public class HomeListAdapter extends BaseAdapter {

    /**
     * item类型
     */
    public static final int CARD_COUNT = 4;
    public static final int VIDEO_TYPE = 0x00;
    public static final int CARD_MULTI_PIC = 0x01;
    public static final int CARD_SIGNAL_PIC = 0x02;
    public static final int CARD_VIEW_PAGER = 0x03;

    private Context mContext;
    private ArrayList<RecommandValue> mData;
    private ImageLoaderManager mImageLoader;

    public HomeListAdapter(Context context, ArrayList<RecommandValue> data) {
        mContext = context;
        mData = data;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RecommandValue getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        RecommandValue value = mData.get(position);
        return value.type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        // 获取数据的type类型
        int type = getItemViewType(position);
        final RecommandValue value = getItem(position);
        if(convertView == null) {
            switch(type) {
                case CARD_SIGNAL_PIC:
                    holder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.item_product_card_one_layout,
                            null);
                    // 初始化viewholder中用到的控件
                    holder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    holder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    holder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    holder.mProudctPhotoView = (ImageView) convertView.findViewById(R.id
                            .product_photo_view);
                    holder.mLogoView = (CircleImageView) convertView.findViewById(R.id
                            .item_logo_view);
                    holder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    holder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    holder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    break;
                case CARD_MULTI_PIC:
                    holder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.item_product_card_two_layout,
                            null);
                    // 初始化viewholder中用到的控件
                    holder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    holder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    holder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    holder.mProductViewLayout = (LinearLayout) convertView.findViewById(R.id
                            .prodct_photo_layout);
                    holder.mLogoView = (CircleImageView) convertView.findViewById(R.id
                            .item_logo_view);
                    holder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    holder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    holder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    break;
                case VIDEO_TYPE:
                    holder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.item_product_card_two_layout,
                            null);
                    break;
                case CARD_VIEW_PAGER:
                    holder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.item_product_card_three_layout,
                            null);
                    holder.mViewPager = (ViewPager) convertView.findViewById(R.id.item_view_pager);
                    holder.mViewPager.setPageMargin(dip2px(mContext, 8));
                    holder.mViewPager.setOffscreenPageLimit(3);
                    ViewGroup.LayoutParams layoutParams = holder.mViewPager.getLayoutParams();
                    layoutParams.width = Utils.getScreenWidth(mContext) - Utils.dip2px
                            (mContext, 60);
                    holder.mViewPager.setLayoutParams(layoutParams);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        switch(type) {
            case CARD_SIGNAL_PIC:

                holder.mTitleView.setText(value.title);
                holder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                holder.mFooterView.setText(value.text);
                holder.mPriceView.setText(value.price);
                holder.mFromView.setText(value.from);
                holder.mZanView.setText(value.zan);
                ImageLoaderManager.getInstance(mContext)
                        .displayImage(value.url.get(0), holder.mProudctPhotoView);

                break;
            case CARD_MULTI_PIC:
                holder.mTitleView.setText(value.title);
                holder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                holder.mFooterView.setText(value.text);
                holder.mPriceView.setText(value.price);
                holder.mFromView.setText(value.from);
                holder.mZanView.setText(value.zan);
                mImageLoader.displayImage(value.logo, holder.mLogoView);
                holder.mProductViewLayout.removeAllViews();
                // 动态添加Imageview到水平scrollview
                for(String u : value.url) {
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px
                            (mContext, 100), ViewGroup.LayoutParams.MATCH_PARENT);
                    params.leftMargin = dip2px(mContext, 5);

                    ImageLoaderManager.getInstance(mContext).displayImage(u, imageView);
                    holder.mProductViewLayout.addView(imageView, params);
                }
                holder.mProductViewLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
                        intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case CARD_VIEW_PAGER:
                ArrayList<RecommandValue> recommandValues = handlerViewPagerData(value);
                HomeSalePagerAdapter pagerAdapter = new HomeSalePagerAdapter(mContext,
                        recommandValues);
                holder.mViewPager.setAdapter(pagerAdapter);
                holder.mViewPager.setCurrentItem(recommandValues.size() * 10000);
                break;
            default:
                break;
        }
        return convertView;
    }

    private ArrayList<RecommandValue> handlerViewPagerData(RecommandValue value) {
        ArrayList<RecommandValue> list = new ArrayList<>();
        String[] titles = value.title.split("@");
        String[] infos = value.info.split("@");
        String[] prices = value.price.split("@");
        String[] texts = value.text.split("@");
        ArrayList<String> urls = value.url;
        int start = 0;
        for(int i = 0; i < titles.length; i++) {
            RecommandValue tempValue = new RecommandValue();
            tempValue.title = titles[i];
            tempValue.info = infos[i];
            tempValue.price = prices[i];
            tempValue.text = texts[i];
            tempValue.url = new ArrayList<>();
            for(int j = start; j < (i+1) *3; j ++) {
                tempValue.url.add(urls.get(j));
                start++;
            }
            list.add(tempValue);
        }

        return list;
    }

    private ImageView createImageView(String url) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(mContext,
                100), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = dip2px(mContext, 5);
        imageView.setLayoutParams(params);
        mImageLoader.displayImage(url, imageView);
        return imageView;
    }

    private static class ViewHolder {
        //所有Card共有属性
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        //Video Card特有属性
        private RelativeLayout mVieoContentLayout;
        private ImageView mShareView;

        //Video Card外所有Card具有属性
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;
        //Card One特有属性
        private ImageView mProudctPhotoView;
        //Card Two特有属性
        private LinearLayout mProductViewLayout;
        //Card three特有属性
        private ViewPager mViewPager;
    }
}
