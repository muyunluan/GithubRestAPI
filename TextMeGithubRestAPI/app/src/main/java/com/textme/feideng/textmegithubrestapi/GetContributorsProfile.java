package com.textme.feideng.textmegithubrestapi;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetContributorsProfile extends Activity {

    private String urlString;
    private int contributorsNum;
    private StringBuilder contributorsContents;
    private LinearLayout linearLayout;
    GetContributorsInfo getContributorsInfo;

    private static final String contributorsName = "login";
    private static final String contributorsPicture = "avatar_url";
    private static final String contributorsProfile = "html_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributors_info);
        linearLayout = (LinearLayout)findViewById(R.id.id_linear);
        Intent intent = getIntent();
        urlString = intent.getStringExtra("url");
        Log.i("intent",urlString);
        getContributorsInfo = new GetContributorsInfo(this, urlString);
        getContributorsInfo.execute();
    }



    private class GetContributorsInfo extends AsyncTask<Void, Void, Void> {

        private String contributorsURLString;
        private int contributorsNum;
        private Activity mActivity;
        private StringBuilder contributorsContents;

        public GetContributorsInfo(Activity activity, String urlString) {
            mActivity = activity;
            if (!urlString.isEmpty()) {
                contributorsURLString = urlString;
            }
            else {
                Log.w("SimpleAdapter Async", "empty url");
                return;
            }
            contributorsContents = new StringBuilder();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            getInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {

            TextView textView = new TextView(mActivity);
            Log.i("",contributorsContents.toString());
            textView.setText(contributorsContents.toString());
            linearLayout.addView(textView);

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
                            contributorsContents.append(contributorsJSONObject.getString(contributorsName) + "\n");
                            contributorsContents.append(contributorsJSONObject.getString(contributorsPicture) + "\n");
                            contributorsContents.append(contributorsJSONObject.getString(contributorsProfile) + "\n");
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
}
