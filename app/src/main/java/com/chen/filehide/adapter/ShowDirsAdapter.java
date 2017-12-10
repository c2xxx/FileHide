package com.chen.filehide.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chen.filehide.R;
import com.chen.filehide.bean.ContentBean;

import java.util.List;

/**
 * Created by hui on 2017/4/23.
 */

public class ShowDirsAdapter extends BaseAdapter {
    private Context context;
    private List<ContentBean> list;

    public ShowDirsAdapter(Context context, List<ContentBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_dir, null);
            vh = new ViewHolder();
            vh.initView(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ContentBean item = list.get(position);
        vh.title.setText(item.getTitle());
        return convertView;
    }

    private class ViewHolder {
        private TextView title;

        public void initView(View view) {
            title = (TextView) view.findViewById(R.id.tv_title);
        }
    }

}
