package com.textme.feideng.textmegithubrestapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;

    private HashMap<String, String> mContents = new HashMap<>();
    private ArrayList<String> repositoryNameStringList = new ArrayList<>();

    public SimpleAdapter(Context context, HashMap<String, String> contents) {
        mContext = context;
        mContents = contents;
        for (String key : mContents.keySet()) {
            repositoryNameStringList.add(key);
        }
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_single_textview, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        viewHolder.tv.setText(repositoryNameStringList.get(i));
    }

    @Override
    public int getItemCount() {
        return repositoryNameStringList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_tv);
            tv.setClickable(true);
            tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), GetContributorsProfile.class);
            intent.putExtra("url",mContents.get(repositoryNameStringList.get(getAdapterPosition())));
            view.getContext().startActivity(intent);
        }
    }
}






