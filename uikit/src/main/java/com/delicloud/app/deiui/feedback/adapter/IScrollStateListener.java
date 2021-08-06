package com.delicloud.app.deiui.feedback.adapter;

/**
 * 滑动状态监听接口
 */
public interface IScrollStateListener {

    /**
     * move to scrap heap
     */
    public void reclaim();


    /**
     * on idle
     */
    public void onImmutable();
}
