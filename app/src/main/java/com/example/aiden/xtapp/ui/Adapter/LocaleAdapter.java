package com.example.aiden.xtapp.ui.Adapter;

/**
 * Created by ldn on 2017/11/24.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aiden.xtapp.R;
import com.example.aiden.xtapp.entity.base.Area;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LocaleAdapter extends BaseAdapter {
    private List<Area> list;
    private final LayoutInflater inflater;

    public LocaleAdapter(Context context,List list) {
        inflater = LayoutInflater.from(context);
        this.list=list;
    }

    @Override public int getCount() {
        return this.list!=null? this.list.size(): 0 ;
    }

    @Override public Area getItem(int position) {
        return this.list.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

//        holder.text_item_city_areaid.setText(list.get(position).getAREAID().toString());
        holder.text_item_city_districtcn.setText(list.get(position).getNAMECN().toString()+",");
        holder.text_item_city_provcn.setText(list.get(position).getDISTRICTCN().toString()+",");
        holder.text_item_city_namecn.setText(list.get(position).getPROVCN().toString());

        return view;
    }

    static class ViewHolder {
//        @BindView(R.id.text_item_city_areaid) TextView text_item_city_areaid;
        @BindView(R.id.text_item_city_districtcn) TextView text_item_city_districtcn;
        @BindView(R.id.text_item_city_provcn) TextView text_item_city_provcn;
        @BindView(R.id.text_item_city_namecn) TextView text_item_city_namecn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}