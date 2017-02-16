package com.fashare.stackpagetransform;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * User: fashare(153614131@qq.com)
 * Date: 2016-08-27
 * Time: 17:04
 * <br/><br/>
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter{
    protected final String TAG = this.getClass().getSimpleName();
    private static final int MAX_PAGE_SIZE = 10;
//    protected Context mContext;
    private WeakReference<Context> mContextWeakReference;
    private List<T> mDataList;
    private List<View> mViewList;

    protected View mItemView;

    protected Context getContext(){
//        return mContext;
        return mContextWeakReference.get();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
    }

    public BasePagerAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BasePagerAdapter(Context context, List<T> dataList) {
        mContextWeakReference = new WeakReference<>(context);
        mDataList = dataList;
        initViewList(MAX_PAGE_SIZE);
    }

    private void initViewList(int size) {
        if(getContext() == null)
            return ;
        if(mViewList == null)
            mViewList = new ArrayList<>();
        mViewList.clear();
        for(int i=0; i<size; i++)
            mViewList.add(LayoutInflater.from(getContext()).inflate(getLayoutRes(), null));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO mItemView 复用优化
        mItemView = mViewList.get(position);
        if(mItemView.getParent() == null)
            container.addView(mItemView);

        onBind(mDataList.get(position), position);

        return mItemView;   // 返回 view 作为 key
    }

    protected abstract @LayoutRes int getLayoutRes();

    protected abstract void onBind(T t, int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public int getCount() {
        return mDataList==null? 0: mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object key) {
        return key == view;
    }
}
