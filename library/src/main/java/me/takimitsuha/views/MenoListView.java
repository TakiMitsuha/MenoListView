package me.takimitsuha.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Taki on 2017/1/25.
 */
public class MenoListView<T> extends FrameLayout {

    private ListView listView;

    private View loadingView;

    private boolean loading = false;

    private boolean hasMore = false;

    private MenoAdapter menoAdapter;

    public MenoListView(Context context) {
        super(context);
        this.init(context, null);
    }

    public MenoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public MenoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.menolistview, this);
        loadingView = LayoutInflater.from(context).inflate(R.layout.layout_loading, null, false);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setFooterDividersEnabled(false);

        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MenoListView, 0, 0);
            try {
                boolean scrollbarVisible = typedArray.getBoolean(R.styleable.MenoListView_scrollbarVisible, true);
                listView.setVerticalScrollBarEnabled(scrollbarVisible);
                boolean dividerVisible = typedArray.getBoolean(R.styleable.MenoListView_dividerVisible, true);
                if (!dividerVisible) {
                    listView.setDividerHeight(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }

    }

    public void setAdapter(MenoAdapter<T> menoAdapter) {
        this.menoAdapter = menoAdapter;
        listView.setAdapter(menoAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!hasMore)
                    return;
                int lastVisibleItem = visibleItemCount + firstVisibleItem;
                if (lastVisibleItem >= totalItemCount && !loading) {
                    MenoListView.this.menoAdapter.onLoadMoreRequired();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenoListView.this.menoAdapter.onItemClick(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MenoListView.this.menoAdapter.onItemLongClick(position);
                return true;
            }
        });

    }

    public void addMore(T newItem) {
        menoAdapter.addNewItem(listView, newItem);
    }

    public void addAll(List<T> newItems) {
        menoAdapter.addAll(listView, newItems);
    }

    public void clearList() {
        hasMore = false;
        menoAdapter.clearList(listView);
    }

    public void startLoading() {
        if (listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(loadingView);
        }
        loading = true;
        if (listView.getFooterViewsCount() == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                listView.addFooterView(loadingView, null, false);
            } else {
                listView.setAdapter(null);
                listView.addFooterView(loadingView, null, false);
                listView.setAdapter(menoAdapter);
                listView.setSelection(menoAdapter.getCount() - 1);
            }
        }
    }

    public void stopLoading() {
        if (listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(loadingView);
        }
        loading = false;
    }

    public void hasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}
