package com.example.todoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.todoapp.Model.SpinnerItem;
import com.example.todoapp.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private List<SpinnerItem> spinnerItemList;
    private Context activity;
    private int layout;

    public SpinnerAdapter(Context activity, int layout, List<SpinnerItem> spinnerItemList) {
        this.activity = activity;
        this.layout = layout;
        this.spinnerItemList = spinnerItemList;
    }

    @Override
    public int getCount() {
        return spinnerItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return spinnerItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView spinnerName;
        ImageView spinnerImage;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
            holder = new ViewHolder();
            holder.spinnerName = view.findViewById(R.id.spinnerName);
            holder.spinnerImage = view.findViewById(R.id.spinnerImage);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SpinnerItem item = spinnerItemList.get(i);
        holder.spinnerImage.setImageResource(item.getImage());
        holder.spinnerName.setText(item.getName());

        return view;
    }
}
