package com.example.lzq.mycar.interfacecommen;

/**
 * Created by LYF on 2016/6/30.
 */

import com.example.lzq.mycar.bean.Goods;

import java.util.List;

/**
 *定义数据改变接口
 */
public interface OnShopingDataChangeListener{
    /**
     * 总价改变调用
     */
    void onTotalPriceChange(List<Goods> beanlist);
    /**
     * 选择改变调用
     */
    void onSelectedItemChange();

    //checkbox点击状态事件
    void onCheckState(int position, List<Boolean> booleanList);

}