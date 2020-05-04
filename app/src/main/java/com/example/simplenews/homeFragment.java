package com.example.simplenews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.example.simplenews.R;
import com.example.simplenews.activity.NewsDetailActivity;
import com.example.simplenews.adapter.NewsForReadRecyclerViewAdapter;
import com.example.simplenews.db.DatabaseOperationDao;
import com.example.simplenews.entity.NewsBean;
import com.example.simplenews.entity.NewsForReadItem;
import com.example.simplenews.global.GlobalApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private NewsForReadRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    //private SearchView searchView;
    private List<NewsBean.ResultBean.DataBean> newsBeanList;
    private final int GET_NEWS_FROM_INTERNET = 0;
    private final int GET_NEWS_FROM_DB = 1;
    private int page = 0;
    private int len = 10;
    private Handler newsMessageHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //String uniquekey,title,date, category,author_name,url,thumbnail_pic_s,thumbnail_pic_s02,thumbnail_pic_s03;

            //super.handleMessage(msg);
            switch (msg.what) {
                case GET_NEWS_FROM_INTERNET:
                    newsBeanList = ((NewsBean)msg.obj).getResult().getData();
					/*mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));    //设置为垂直布局
					adapter = new NewsForReadRecyclerViewAdapter(getActivity(), newsBeanList);
					mRecyclerView.setAdapter(adapter);*/
                    refresh();
                    adapter.notifyDataSetChanged();
                    break;
                case GET_NEWS_FROM_DB:
                    newsBeanList = ((NewsBean)msg.obj).getResult().getData();
                    refresh();
                    break;
            }
        }
    };
    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //onAttach(getContext());
        initView();
        //Log.d("hello", "onActivityCreated");
    }

}
