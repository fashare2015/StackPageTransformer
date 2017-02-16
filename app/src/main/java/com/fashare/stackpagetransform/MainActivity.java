package com.fashare.stackpagetransform;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.fashare.page_transform.StackPageTransformer;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager)findViewById(R.id.vp);

        mViewPager.setPageTransformer(true, new StackPageTransformer(mViewPager));

        mViewPager.setAdapter(new MyAdapter(this, Arrays.asList(
                R.drawable.a1,
                R.drawable.a2,
                R.drawable.a3,
                R.drawable.a4,
                R.drawable.a5,
                R.drawable.a6,
                R.drawable.a7,
                R.drawable.a8,
                R.drawable.a9
        )));
    }

    class MyAdapter extends BasePagerAdapter<Integer>{

        public MyAdapter(Context context, List<Integer> dataList) {
            super(context, dataList);
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.item_card;
        }

        @Override
        protected void onBind(Integer res, int position) {
            ImageView iv = (ImageView)mItemView.findViewById(R.id.iv);
            iv.setImageResource(res);
        }
    }
}
