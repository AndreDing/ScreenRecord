package com.ialways.screenrecord.ui.manager;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baidu.ops.appunion.sdk.AppUnionSDK;
import com.baidu.ops.appunion.sdk.InterstitialAdListener;
import com.baidu.ops.appunion.sdk.banner.BaiduBanner;
import com.baidu.ops.appunion.sdk.banner.BannerType;
import com.ialways.screenrecord.ui.Initializable;
import com.ialways.screenrecord.ui.MainApp;

/**
 * 项目名称：ScreenRecord 类名称：AdMgr 类描述： 广告管理器 创建人：dingchao 创建时间：2015年10月22日
 * 下午1:37:13 修改人：dingchao 修改时间：2015年10月22日 下午1:37:13 修改备注：
 * 
 * @version 1.0
 */
public class AdMgr implements Initializable {

    private Context mCtx;

    private BaiduBanner mAdImageText;

    private BaiduBanner mAdImage;
    
    private boolean isAdOpen = true;

    public static AdMgr shared() {

        return MainApp.shared().get(AdMgr.class);
    }

    @Override
    public void init(Context ctx) {

        this.mCtx = ctx;
        AppUnionSDK.getInstance(mCtx).initSdk();

        initInterAd(false);
    }

    /**
     * initInterAd(初始化插屏广告，需预加载此代码)
     * 
     * @param
     * @return
     */
    public void initInterAd(boolean needShow) {

        // 插屏预加载代码
        AppUnionSDK.getInstance(mCtx).loadInterstitialAd(mCtx, new InterAdListener(false), false);
    }

    public void clear() {
        AppUnionSDK.getInstance(mCtx).quitSdk();
    }

    public void showAppList() {
        AppUnionSDK.getInstance(mCtx).showAppList();
    }

    public void showInterAd() {
        if (AppUnionSDK.getInstance(mCtx).isInterstitialAdReady()) {
            AppUnionSDK.getInstance(mCtx).showInterstitialAd();
        } else {
            initInterAd(true);
        }
    }

    public void showImageTextBanner(Activity activity) {
        if (mAdImageText == null || mAdImageText.getVisibility() == View.GONE) {

            if (mAdImageText != null) {
                mAdImageText.setVisibility(View.GONE);
            }

            FrameLayout.LayoutParams imageTextContainer = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageTextContainer.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            mAdImageText = new BaiduBanner(mCtx);
            mAdImageText.setBannerType(BannerType.IMAGE_TEXT);
            activity.addContentView(mAdImageText, imageTextContainer);
        }
    }

    public void showImageBanner(Activity activity) {
        if (mAdImage == null || mAdImage.getVisibility() == View.GONE) {
            if (mAdImage != null) {
                mAdImage.setVisibility(View.GONE);
            }
            FrameLayout.LayoutParams imageContainer = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageContainer.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            mAdImage = new BaiduBanner(mCtx);
            mAdImage.setBannerType(BannerType.IMAGE_ONLY);
            activity.addContentView(mAdImage, imageContainer);
        }
    }

    public void hideAd() {

        if (mAdImageText != null) {
            mAdImageText.setVisibility(View.GONE);
        }
        
        if (mAdImage != null) {
            mAdImage.setVisibility(View.GONE);
        }
    }

    private class InterAdListener implements InterstitialAdListener {

        private boolean needShowAd;

        public InterAdListener(boolean showAd) {

            this.needShowAd = showAd;
        }

        @Override
        public void onAdDismissed() {

            if (needShowAd) {
                initInterAd(false);
            }
        }

        @Override
        public void onAdFailed(String string) {
            initInterAd(true);
        }

        @Override
        public void onAdPresent() {

        }

        @Override
        public void onAdReady() {
            if (needShowAd) {
                AppUnionSDK.getInstance(mCtx).showInterstitialAd();
            }
        }
    }

}
