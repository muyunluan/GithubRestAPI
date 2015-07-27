package com.textme.feideng.textmegithubrestapi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by feideng on 7/25/15.
 */
public class SimpleAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mDatas;

    private HashMap<String, String> mContents = new HashMap<>();
    private ArrayList<String> repositoryNameStringList = new ArrayList<>();


    private static final String contributorsName = "login";
    private static final String contributorsPicture = "avatar_url";
    private static final String contributorsProfile = "html_url";

    public SimpleAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

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
        viewHolder.tv.setTag(mContents.get(repositoryNameStringList.get(i)));
    }

    @Override
    public int getItemCount() {
        return repositoryNameStringList.size();
    }
}


class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

    GetContributorsInfo getContributorsInfo;
    TextView tv;

    public MyViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.id_tv);
        tv.setClickable(true);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("", "click" + tv.getTag(getAdapterPosition()));

        //getContributorsInfo = new GetContributorsInfo(view, );
    }
}


class GetContributorsInfo extends AsyncTask<Void, Void, Void> {

    private String contributorsURLString;
    private int contributorsNum;
    private View viewInfo;

    public GetContributorsInfo(View itemView, String urlString) {
        viewInfo = itemView.findViewById(R.id.id_contributors).findViewById(R.id.id_linear);
        if (!urlString.isEmpty()) {
            contributorsURLString = urlString;
        }
        else {
            Log.w("SimpleAdapter Async", "empty url");
            return;
        }
    }
    @Override
    protected Void doInBackground(Void... voids) {
        getInfo();
        return null;
    }

    @Override
    protected void onPostExecute(Void results) {

    }

    private void getInfo() {
        //analyze content of contributors
        BufferedReader contributorsReader = null;
        HttpURLConnection contributorsURLConn = null;
        String content = null;
        try {
            URL contributorsURL = new URL(contributorsURLString);
            contributorsURLConn = (HttpURLConnection) contributorsURL.openConnection();
            contributorsURLConn.setRequestMethod("GET");
            contributorsURLConn.connect();
            if (contributorsURLConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                contributorsReader = new BufferedReader(new InputStreamReader(contributorsURLConn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = contributorsReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                content = builder.toString();
                JSONArray contributorsJSONArray = null;
                try {
                    contributorsJSONArray = new JSONArray(content);
                    contributorsNum = contributorsJSONArray.length();
                    JSONObject contributorsJSONObject;
                    for (int j = 0; j < contributorsNum; j++) {
                        contributorsJSONObject = contributorsJSONArray.getJSONObject(j);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (contributorsReader != null) {
                try {
                    contributorsReader.close();
                    contributorsURLConn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}



