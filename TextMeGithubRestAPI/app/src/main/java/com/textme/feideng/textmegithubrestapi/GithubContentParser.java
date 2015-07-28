package com.textme.feideng.textmegithubrestapi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class GithubContentParser {

    private String mURLString;
    private String content = null;
    private JSONArray mJSONArray;
    private JSONObject mJSONObject;
    private static final String repositoryName = "name";
    private static final String contributorsString = "contributors_url";
    private HashMap<String, String> trendingRepository = new HashMap<>();

    public GithubContentParser(String urlString) {
        if (!urlString.isEmpty()) {
            mURLString = urlString;
        }
        else {
            Log.w("GithubContentParser","empty url string");
            return;
        }
    }

    public HashMap<String, String> getContent() {
        BufferedReader reader = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(mURLString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                content = builder.toString();
                JSONObject temp = new JSONObject(content);
                mJSONArray = temp.getJSONArray("items");
                for (int i = 0; i < mJSONArray.length(); i++) {
                    mJSONObject = mJSONArray.getJSONObject(i);
                    trendingRepository.put(mJSONObject.getString(repositoryName),mJSONObject.getString(contributorsString));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GithubContentParser", "Error parsing data " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    httpURLConnection.disconnect();
                    content = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return trendingRepository;
    }
}
