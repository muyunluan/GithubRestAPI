package com.textme.feideng.textmegithubrestapi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SimpleAdapter mSimpleAdapter;
    private HashMap<String, String> mContents = new HashMap<>();
    private RetrieveURLContent mRetriveURLContent;
    private GithubContentParser githubContentParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        mRetriveURLContent = new RetrieveURLContent(this);
        mRetriveURLContent.execute();
    }

    private class RetrieveURLContent extends AsyncTask<Void,Void,Void> {
        private String urlString;
        private Activity mAcitivity;
        public RetrieveURLContent(Activity activity) {
            mAcitivity = activity;
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String time = s.format(c.getTime());
            urlString = String.format("https://api.github.com/search/repositories?q=created:>%s&sort=stars&order=desc", time);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            githubContentParser = new GithubContentParser(urlString);
            mContents = githubContentParser.getContent();
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            mSimpleAdapter = new SimpleAdapter(mAcitivity, mContents);
            mRecyclerView.setAdapter(mSimpleAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
