package com.example.punchcard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punchcard.R;
import com.example.punchcard.bean.PunchCard;

import java.util.List;

public class PunchCardAdapter extends BaseAdapter {

    private static final String TAG = "PunchCardAdapter" ;

    private LayoutInflater layoutInflater;
    private List<PunchCard> list;
    public PunchCardAdapter(Context context, List<PunchCard> list){
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
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.punchcard_item_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PunchCard info=(PunchCard) getItem(position);
       /* PunchCard notepadBean = list.get(0);
        Log.d(TAG,"id=========="+notepadBean.getId());
        Log.d(TAG,"eventId=========="+notepadBean.getEventId());*/
        viewHolder.tvPunchCardItem.setText(info.getItem());
        viewHolder.tvPunchCardTimes.setText("你已经打卡" + info.getTimes() + "天");
        viewHolder.mImage.setBackgroundResource(info.getIconId());
        if(info.getActivity() == 0) {
            viewHolder.gouImage.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
    class ViewHolder{
        TextView tvPunchCardItem;
        TextView tvPunchCardTimes;
        ImageView mImage;
        ImageView gouImage;
        public ViewHolder(View view){
            tvPunchCardItem = (TextView) view.findViewById(R.id.item);
            tvPunchCardTimes = (TextView) view.findViewById(R.id.times);
            mImage = (ImageView)view.findViewById(R.id.mImage);
            gouImage = (ImageView)view.findViewById(R.id.gouImage);
        }
    }
}
