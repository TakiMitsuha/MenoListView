package me.takimitsuha.menolistview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import me.takimitsuha.views.MenoListView;

/**
 * Created by Taki on 2017/1/25.
 */
public class MainActivity extends AppCompatActivity {

    private final int ITEM_COUNT_TO_LOAD = 20;
    private final int ITEM_COUNT_LIMIT = 600;
    private final int TIME_TO_LOAD = 1500;

    private int itemOffset = 0;

    private MenoListView menoListView;

    private ArrayList<String> itemList;
    private MyMenoAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menoListView = (MenoListView) findViewById(R.id.meno_list_view);

        itemList = new ArrayList<>();
        adapter = new MyMenoAdapter<>(this, R.layout.item_text, itemList);

        menoListView.setAdapter(adapter);

        loadMore();
    }

    public void loadMore() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                menoListView.startLoading();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(TIME_TO_LOAD);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                if (itemOffset >= ITEM_COUNT_LIMIT) {
                    menoListView.hasMore(false);
                } else {
                    for (int i = itemOffset; i < itemOffset + ITEM_COUNT_TO_LOAD; i++) {
                        String item = "" + i;
                        menoListView.addMore(item);
                    }
                    itemOffset += ITEM_COUNT_TO_LOAD;
                    menoListView.hasMore(true);
                }
                menoListView.stopLoading();
            }
        }.execute();
    }
}
