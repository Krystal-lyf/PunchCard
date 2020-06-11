package com.example.punchcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.punchcard.R;
import com.example.punchcard.bean.Note;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Note> list;
    public NoteAdapter(Context context, List<Note> list){
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
            convertView = layoutInflater.inflate(R.layout.notes_item_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Note info=(Note) getItem(position);
        viewHolder.tvContent.setText(info.getContent());
        viewHolder.tvTime.setText(info.getTime());
        return convertView;
    }
    class ViewHolder{
        TextView tvContent;
        TextView tvTime;
        public ViewHolder(View view){
            tvContent=(TextView) view.findViewById(R.id.notes);
            tvTime=(TextView) view.findViewById(R.id.days);
        }
    }
}

