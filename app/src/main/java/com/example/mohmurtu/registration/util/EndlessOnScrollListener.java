package com.example.mohmurtu.registration.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Apresh on 9/6/2015.
 */
public abstract class EndlessOnScrollListener extends
        RecyclerView.OnScrollListener {
    public static String TAG = EndlessOnScrollListener.class
            .getSimpleName();

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 15;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public void setCurrent_page(int current_page){
        this.current_page = current_page;
    }

    public EndlessOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        Log.v("visibleItemCount",""+visibleItemCount);
        Log.v("totalItemCount",""+totalItemCount);
        Log.v("firstVisibleItem",""+firstVisibleItem);
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
       if (!loading
                && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) && current_page > 2) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}