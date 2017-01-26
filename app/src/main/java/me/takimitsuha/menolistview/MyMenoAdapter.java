package me.takimitsuha.menolistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.takimitsuha.views.MenoAdapter;

/**
 * Created by Taki on 2017/1/25.
 */
public class MyMenoAdapter<T> extends MenoAdapter<T> {

    private MainActivity mainActivity;
    private int itemLayoutRes;
    private ArrayList<T> itemList;

    public MyMenoAdapter(MainActivity mainActivity, int itemLayoutRes, ArrayList<T> itemList) {
        super(mainActivity, itemLayoutRes, itemList);
        this.mainActivity = mainActivity;
        this.itemLayoutRes = itemLayoutRes;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mainActivity.getLayoutInflater().inflate(itemLayoutRes, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String text = (String) itemList.get(position);
        if (text != null) {
            holder.text.setText(text);
        }
        return convertView;
    }

    @Override
    public void onLoadMoreRequired() {
        mainActivity.loadMore();
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onItemLongClick(int position) {
    }

    static class ViewHolder {
        TextView text;
    }
}
