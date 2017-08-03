package com.halove.core.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.halove.core.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by xieshangwu on 2017/8/3
 * @function 初始化UniverImageLoader, 并用来加载图片
 */

public class ImageLoaderManager {

    /**
     * 默认的参数值
     */
    public static final int THREAD_COUNT = 4; // UIL线程数
    public static final int PROPRITY = 2; // 图片加载优先级
    public static final int DISK_CACHE_SIZE = 50 * 1024; // 硬盘缓存大小
    public static final int CONNECTION_TIME_OUT = 5 * 1000; // 连接超时时间
    public static final int READ_TIME_OUT = 30 * 1000; // 读取超时时间

    private static ImageLoader mImageLoader;
    private static ImageLoaderManager mInstance;

    private ImageLoaderManager(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY - PROPRITY)
                .denyCacheImageMultipleSizesInMemory() //防止缓存多套尺寸图片到我们内存中
                .memoryCache(new WeakMemoryCache())    //使用弱引用缓存
                .diskCacheSize(DISK_CACHE_SIZE)        //分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())        //使用MD5命名
                .tasksProcessingOrder(QueueProcessingType.FIFO)                //图片下载顺序
                .defaultDisplayImageOptions(getDefultOptions())                //默认图片加载OPtions
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT,
                        READ_TIME_OUT))                                        //设置图片下载器
                .writeDebugLogs()                                              //debug环境下输出日志
                .build();

        ImageLoader.getInstance().init(configuration);
        mImageLoader = ImageLoader.getInstance();
    }

    /**
     * 返回单例的ImageLoaderManager
     * @param context
     * @return
     */
    public ImageLoaderManager getInstance(Context context) {
        if(mInstance == null) {
            synchronized(ImageLoaderManager.class) {
                if(mInstance == null) {
                    mInstance = new ImageLoaderManager(context);
                }
            }
        }
        return mInstance;
    }


    /**
     * 默认的OPtions
     * @return
     */
    private DisplayImageOptions getDefultOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.xadsdk_img_error)  //图片地址为空
                .showImageOnFail(R.drawable.xadsdk_img_error)       //图片下载失败的显示
                .cacheInMemory(true)                                //设置图片可以缓存在内存
                .cacheOnDisk(true)                                  //设置图片可以缓存在硬盘
                .bitmapConfig(Bitmap.Config.RGB_565)                //使用的图片解码类型
                .decodingOptions(new BitmapFactory.Options())       //图片解码配置
                .build();
        return options;
    }

    /**
     * 加载图片api
     * @param url
     * @param imageview
     * @param options
     * @param listener
     */
    public void displayImage(String url,
                             ImageView imageview,
                             DisplayImageOptions options,
                             ImageLoadingListener listener) {
        if(mImageLoader != null) {
            mImageLoader.displayImage(url, imageview, options, listener);
        }
    }

    public void displayImage(String url,
                             ImageView imageview,
                             ImageLoadingListener listener) {
        if(mImageLoader != null) {
            mImageLoader.displayImage(url, imageview, null, listener);
        }
    }


    public void displayImage(String url,
                             ImageView imageview) {
        if(mImageLoader != null) {
            mImageLoader.displayImage(url, imageview, null, null);
        }
    }

}
