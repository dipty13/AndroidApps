package com.dnerd.dipty.retrofitexample.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dnerd.dipty.retrofitexample.R;
import com.dnerd.dipty.retrofitexample.model.GithubRepo;

import java.util.List;

public class GithubRepoAdapter extends ArrayAdapter<GithubRepo> {
    private Context mContext;
    private List<GithubRepo> mRepos;
    LayoutInflater inflater;

    public GithubRepoAdapter(Context mContext, List<GithubRepo> mRepos) {
        super(mContext, R.layout.list_items, mRepos);
        this.mContext = mContext;
        this.mRepos = mRepos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View row = convertView;

        if(row == null)
        {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_items, parent, false);


        }
        TextView textView = row.findViewById(R.id.list_text_view);
        GithubRepo item = mRepos.get(position);
        String message = item.getName();
        textView.setText(message);
        return row;

    }


    /*@Nullable
    @Override
    public GithubRepo getItem(int position) {
        return super.getItem(position);

    }*/
}
