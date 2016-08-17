package com.example.lzq.mycar.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.lzq.mycar.R;
import com.example.lzq.mycar.adapter.CarItemAdapter;
import com.example.lzq.mycar.bean.Goods;
import com.example.lzq.mycar.interfacecommen.OnShopingDataChangeListener;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShopCarActivity extends Activity implements OnShopingDataChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener,OnRefreshListener, OnLoadMoreListener {

    private ListView carList;
    private CarItemAdapter carItemAdapter;
    private List<Goods> goodsbean;
    private List<Goods> showgoods;
    private ArrayList<Boolean> booleanList;
    private TextView submit;
    private TextView mTvTotalValue;
    private TextView deleteAll;
    private CheckBox checkbox;
    SwipeToLoadLayout swipeToLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);

        carList = (ListView) findViewById(R.id.swipe_target);

        swipeToLoadLayout.setOnRefreshListener(this);

        swipeToLoadLayout.setOnLoadMoreListener(this);

        submit = (TextView) findViewById(R.id.tv_submit);
        deleteAll = (TextView) findViewById(R.id.tv_delete_all);
        checkbox = (CheckBox) findViewById(R.id.Cb_checekd);
        mTvTotalValue = (TextView) findViewById(R.id.tv_total_value);

        goodsbean = DataSupport.findAll(Goods.class);
        booleanList = new ArrayList<>();
        for (int i = 0;i < goodsbean.size();i++){
            booleanList.add(false);
        }

        //Log.i("goodsbean==", String.valueOf(goodsbean));

        if (goodsbean.size() > 10){
            showgoods = new ArrayList<>();
            for (int i = 0;i< 10;i++){
                showgoods.add(goodsbean.get(i));
            }
            carItemAdapter = new CarItemAdapter(this, showgoods,booleanList);
        }else {
            carItemAdapter = new CarItemAdapter(this, goodsbean,booleanList);
        }

        carList.setAdapter(carItemAdapter);
        initListener();

        autoRefresh();
    }



    private void initListener() {
//        submit.setOnClickListener(this);
//        deleteAll.setOnClickListener(this);
//        carItemAdapter.setListDataChangeListener(this);
//
//        checkbox.setOnCheckedChangeListener(this);

    }

    private boolean isNeedChangeAllState = true;

    @Override
    public void onTotalPriceChange(List<Goods> beanlist) {
        mTvTotalValue.setText("合计：￥" + getSelectedTotalPrice(beanlist,booleanList));
    }

    /**
     * 当list列表某一项选中状态改变会调用这个方法
     */
    @Override
    public void onSelectedItemChange() {
        /*if (checkListState(goodsbean, true)) {
            checkbox.setChecked(true);
            carItemAdapter.notifyDataSetChanged();
        } else if (checkListState(goodsbean, false)) {
            checkbox.setChecked(false);
            carItemAdapter.notifyDataSetChanged();
        } else {
            isNeedChangeAllState = false;
            checkbox.setChecked(false);
            isNeedChangeAllState = true;
        }
        mTvTotalValue.setText("合计：￥" + getSelectedTotalPrice(goodsbean,booleanList));*/
    }


    @Override
    public void onCheckState(int position,List<Boolean> booleanList) {
        if (booleanList.get(position) == true){
            booleanList.set(position,false);
            carItemAdapter.notifyDataSetChanged();
        } else if(booleanList.get(position) == false){
            booleanList.set(position,true);
            carItemAdapter.notifyDataSetChanged();
        }
        mTvTotalValue.setText("合计：￥" + getSelectedTotalPrice(showgoods,booleanList));
    }

    /**
     * 监测列表是否都是某种状态
     *
     * @return
     */

    private boolean checkListState(List<Boolean> booleanList){
        boolean Ret = true;
        for (int i = 0; i < booleanList.size(); i++){
            if (booleanList.get(i) == true){
                Ret = false;
                break;
            }
        }
        return Ret;
    }

    /**
     * 获取选择的数量
     *
     * @param beanlist
     * @return
     */
    private int getSelectedTotal(List<Goods> beanlist) {
        int seletedTotal = 0;
        if (beanlist != null) {
            for (Goods bean : beanlist) {
                //boolean itemState = bean.isChecked();
                boolean itemState =false;
                if (itemState) {
                    seletedTotal += 1;

                }
            }
        }
        return seletedTotal;
    }

    /**
     * 选择的总价
     *
     * @param booleanlist
     * @return
     */
    private int getSelectedTotalPrice(List<Goods> beanlist,List<Boolean> booleanlist) {
        int seletedTotalPrice = 0;
        for (int i = 0; i < booleanlist.size(); i ++){
            if (booleanlist.get(i).booleanValue()){
                seletedTotalPrice += Integer.valueOf(beanlist.get(i).getTotal());
            }
        }
        return seletedTotalPrice;
    }

    /**
     * 点击全选或者全不选
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {

            if (isNeedChangeAllState) {
                changeListDataState(booleanList,isChecked);
                carItemAdapter.notifyDataSetChanged();
            }
            mTvTotalValue.setText("合计：￥" + getSelectedTotalPrice(goodsbean,booleanList));

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void changeListDataState(List<Boolean> booleanList,boolean stste){
        for (int i = 0; i < booleanList.size(); i++){
            booleanList.set(i,stste);
        }
    }

    /**
     * 点击事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete_all:
                checkBeforeDelete();
                break;

        }
    }

    /**
     * 删除前检测
     */
    private void checkBeforeDelete() {
        if (checkListState(booleanList)) {
            Toast.makeText(this, "请先选择要删除的项", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("删除提示").setMessage("您确定要删除么？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //取消
                }
            });
            AlertDialog ad = builder.create();
            ad.show();
        }
    }

    /**
     * 删除
     */
    private void delete() {
        if (goodsbean != null) {
            Iterator<Goods> iterator = goodsbean.iterator();
            for (int i = booleanList.size()-1; i >=0;i--){
                //Goods bean = iterator.next();
                boolean itemState = booleanList.get(i).booleanValue();
                if (itemState){
                    //iterator.remove();
                    //int id = bean.getId();
                    //DataSupport.delete(Goods.class, id);
                    DataSupport.delete(Goods.class,goodsbean.get(i).getId());
                    goodsbean.remove(i);
                    booleanList.remove(i);
                    carItemAdapter.notifyDataSetChanged();
                }
            }
            //carItemAdapter.notifyDataSetChanged();
            //ArrayList<Boolean> s = booleanList;
            //checkbox.setChecked(checkListState(s));
            mTvTotalValue.setText("合计：￥" + getSelectedTotalPrice(goodsbean,booleanList));
        }
    }

    /**
     * lyf小数点后两位
     *
     * @param db
     * @return
     */
    public String getPrice(double db) {
        DecimalFormat df = new DecimalFormat("0.00");
        String st = df.format(db);
        return st;
    }


    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                int j = showgoods.size();
                if (goodsbean.size() - showgoods.size() > 5){
                    for (int i = showgoods.size()+1;i < j+5;i++){
                        showgoods.add(goodsbean.get(i));
                    }
                }else {
                    for (int i = showgoods.size()+1;i < j ;i++){
                        showgoods.add(goodsbean.get(i));

                    }
                }
                carItemAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
//                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
}
