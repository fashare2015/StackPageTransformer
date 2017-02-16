package com.fashare.page_transform;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2017-02-16
 * Time: 22:26
 * <br/><br/>
 *
 * 让 ViewPager 堆成栈
 * <br/>
 *
 * 灵感来源:
 * <a href="http://hukai.me/android-training-course-in-chinese/animations/screen-slide.html">Depth Page Transformer<a/>
 */
public class StackPageTransformer implements ViewPager.PageTransformer {
    private float mMinScale;    // 栈底: 最小页面缩放比
    private float mMaxScale;    // 栈顶: 最大页面缩放比
    private int mStackCount;    // 栈内页面数

    private float mPowBase;     // 基底: 相邻两 page 的大小比例

    /**
     *
     * @param minScale 栈底: 最小页面缩放比
     * @param maxScale 栈顶: 最大页面缩放比
     * @param stackCount 栈内页面数
     */
    public StackPageTransformer(ViewPager viewPager, float minScale, float maxScale, int stackCount) {
        viewPager.setOffscreenPageLimit(stackCount);
        mMinScale = minScale;
        mMaxScale = maxScale;
        mStackCount = stackCount;

        if(mMaxScale < mMinScale)
            throw new IllegalArgumentException("The Argument: maxScale must bigger than minScale !");
        mPowBase = (float) Math.pow(mMinScale/mMaxScale, 1.0f/mStackCount);
    }

    public StackPageTransformer(ViewPager viewPager) {
        this(viewPager, 0.8f, 0.9f, 5);
    }

    public final void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        view.setPivotX(pageWidth/2);
        view.setPivotY(0);

        float bottomPos = mStackCount-1;

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);

            view.setTranslationX(0);
            view.setScaleX(mMaxScale);
            view.setScaleY(mMaxScale);

            if(!view.isClickable())
                view.setClickable(true);

        } else if (position <= bottomPos) { // (0, mStackCount-1]
            int index = (int)position;  // 整数部分
            float minScale = mMaxScale * (float) Math.pow(mPowBase, index+1);
            float maxScale = mMaxScale * (float) Math.pow(mPowBase, index);
            float curScale = mMaxScale * (float) Math.pow(mPowBase, position);

            // Fade the page out.
            view.setAlpha(1);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
            view.setTranslationY(pageHeight * (1-curScale)
                    - pageHeight * (1-mMaxScale) * (bottomPos-position) / bottomPos);

            // Scale the page down (between minScale and maxScale)
            float scaleFactor = minScale
                    + (maxScale - minScale) * (1 - Math.abs(position - index));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            if(position == 1 && view.isClickable())
                view.setClickable(false);

        } else { // (mStackCount-1, +Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}  