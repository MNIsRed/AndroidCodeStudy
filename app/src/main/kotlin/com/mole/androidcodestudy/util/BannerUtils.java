package com.mole.androidcodestudy.util;

public class BannerUtils {

    //banner的最大数量用于无限轮播
    public static final int MAX_VALUE = 500;

    //pager最初的位置，方便初始的左右滑动以及到底的重置
    public static int getOriginalPosition(int pageSize) {
        return MAX_VALUE / 2 - ((MAX_VALUE / 2) % pageSize);
    }

    public static int getOriginalPositionByMax(int pageSize, int maxV){
        return maxV / 2 - ((maxV / 2) % pageSize);
    }

    //获取viewpager（显示）的位置对应的 list（数据）位置
    public static int getRealPosition(int currPosition, int size) {
        return (currPosition + size) % size;
    }

}
