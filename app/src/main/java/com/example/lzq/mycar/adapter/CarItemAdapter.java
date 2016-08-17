package com.example.lzq.mycar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lzq.mycar.R;
import com.example.lzq.mycar.bean.Goods;
import com.example.lzq.mycar.interfacecommen.OnShopingDataChangeListener;
import com.example.lzq.mycar.view.CicleAddAndSubView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by LYF on 2016/6/21.
 */
public class CarItemAdapter extends BaseAdapter {
    private Context context;
    private List<Goods> listBean;
    private List<Boolean> booleanList;

    public CarItemAdapter(Context context, List<Goods> listBean, List<Boolean> booleanList) {
        this.context = context;
        this.listBean = listBean;
        this.booleanList = booleanList;
    }

    private OnShopingDataChangeListener listDataChangeListener;

    public OnShopingDataChangeListener getListDataChangeListener() {
        return listDataChangeListener;
    }

    public void setListDataChangeListener(OnShopingDataChangeListener listDataChangeListener) {
        this.listDataChangeListener = listDataChangeListener;
    }

    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_shop_cart, null);

            viewHolder.name = (TextView) view.findViewById(R.id.tv_car_goods_name);
            viewHolder.goodsPrice = (TextView) view.findViewById(R.id.tv_car_goods_price);
            viewHolder.mCbCheceked = (CheckBox) view.findViewById(R.id.Cb_checekd);
            viewHolder.ciaddview = (CicleAddAndSubView) view.findViewById(R.id.commid_numberview);//商品数量
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(listBean.get(position).getName());
        viewHolder.goodsPrice.setText(listBean.get(position).getTotal());
        viewHolder.mCbCheceked.setChecked(booleanList.get(position));


        final int num = Integer.parseInt(listBean.get(position).getCommodNumber());
        final int price = Integer.parseInt(listBean.get(position).getPicture());
        listBean.get(position).setTotal(String.valueOf(num * price));

        viewHolder.ciaddview.setMinValue(1);
        viewHolder.ciaddview.setInitial(Integer.valueOf(listBean.get(position).getCommodNumber()));
        viewHolder.ciaddview.setOnNumChangeListener(new CicleAddAndSubView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int num) {
                listBean.get(position).setCommodNumber(String.valueOf(num));
                int price = Integer.parseInt(listBean.get(position).getPicture());
                listBean.get(position).setTotal(String.valueOf(num * price));
                viewHolder.goodsPrice.setText(listBean.get(position).getTotal());
                if (listDataChangeListener != null) {
                    listDataChangeListener.onTotalPriceChange(listBean);
                }
            }
        });
        viewHolder.goodsPrice.setText(String.valueOf(num * price));
        //viewHolder.mCbCheceked.setChecked(listBean.get(position).isChecked());
        /*viewHolder.mCbCheceked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean choice) {
                listBean.get(position).setChecked(choice);
                if (listDataChangeListener!=null) {
                    listDataChangeListener.onSelectedItemChange();
                }
            }
        });*/

        viewHolder.mCbCheceked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDataChangeListener.onCheckState(position, booleanList);
            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView name;
        private TextView goodsPrice;
        private TextView addCound;
        private TextView delcount;
        private TextView delete;
        private CheckBox mCbCheceked;
        private EditText mCountNum;
        private CicleAddAndSubView ciaddview;
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
}
