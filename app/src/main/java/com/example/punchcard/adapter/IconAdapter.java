package com.example.punchcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.punchcard.R;
import com.example.punchcard.bean.Icon;

import java.util.List;

public class IconAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Icon> list;
    public IconAdapter(Context context,List<Icon> list){
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_icon_view,null);
            viewHolder = new IconAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (IconAdapter.ViewHolder) convertView.getTag();
        }
        Icon info=(Icon) getItem(position);
        viewHolder.imageView.setImageResource((Integer) list.get(position).getId());
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        public ViewHolder(View view){
            imageView = (ImageView)view.findViewById(R.id.grid_img);
        }
    }
}

