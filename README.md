# 把`ViewPager`撸成栈
最近有看到一些栈式列表的效果，突然发现`ViewPager`也能做到较为简易的效果，实在是迫不及待。

# 效果图
这还是你熟悉的`ViewPager`么？

![stack](https://github.com/fashare2015/StackPageTransformer/blob/master/screen-record/stack.gif)

# 使用
很简单就一个类 —— `StackPageTransformer`.

没有打成jar包，使用时直接复制源码即可：![StackPageTransformer](https://github.com/fashare2015/StackPageTransformer/blob/master/page-transform/src/main/java/com/fashare/page_transform/StackPageTransformer.java)
```java
// 默认配置
mViewPager.setPageTransformer(true, new StackPageTransformer(mViewPager));

// 或
mViewPager.setPageTransformer(true, new StackPageTransformer(mViewPager, 0.8f, 0.9f, 5));
0.8f -> // 栈底: 最小页面缩放比
0.9f -> // 栈顶: 最大页面缩放比
5    -> // 栈内页面数
```

# 实现
主要依赖`ViewPager.PageTransformer.transformPage(View view, float position)`.

其中的`position`落在`0~mStackCount-1`时，进行相应的位移，从而堆成栈的效果.

还有一些渐变过渡的处理比较繁琐，有兴趣的话自行看代码吧.

# 灵感来源
![官方教程 —— Depth Page Transformer](http://hukai.me/android-training-course-in-chinese/animations/screen-slide.html)
