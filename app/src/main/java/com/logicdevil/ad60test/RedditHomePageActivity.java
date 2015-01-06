package com.logicdevil.ad60test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RedditHomePageActivity extends ActionBarActivity {

    private String TAG = RedditHomePageActivity.class.getSimpleName();
    ArrayList<RedditItem> redditList;
    ArrayList<ThumbnailHolder> thumbnailList;
    boolean loadingMore = false;
    View loadMoreView;
    RedditListViewAdapter listadapter = null;
    private int m_PreviousTotalCount = 0;
    ListView redditListView;
    int start = 0;
    int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit_home_page);

        redditList = new ArrayList<RedditItem>();
        thumbnailList = new ArrayList<ThumbnailHolder>();
        redditListView = (ListView) findViewById(R.id.id_reddit_listView);

        final FetchStringAsyncTask fetchStringAsyncTask = new FetchStringAsyncTask();
        FetchImageAsyncTask fetchImageAsyncTask = new FetchImageAsyncTask();



        try {
            redditList = (ArrayList<RedditItem>) fetchStringAsyncTask.execute().get();
            thumbnailList = (ArrayList<ThumbnailHolder>) fetchImageAsyncTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listadapter = new RedditListViewAdapter(
                this, R.layout.single_list, redditList, thumbnailList
        );


        loadMoreView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loadmore, null, false);
        redditListView.addFooterView(loadMoreView);


        redditListView.setAdapter(listadapter);

        Log.v(TAG, "adapter was set");
        redditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RedditItem detailItem = (RedditItem) parent.getItemAtPosition(position);

                Intent i = new Intent(RedditHomePageActivity.this,
                        DetailActivity.class);

                i.putExtra("item", detailItem);


                if (detailItem.isSelftext()) {
                    Toast.makeText(getApplicationContext(), "The content is self-text"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "The content is url"
                            , Toast.LENGTH_SHORT).show();
                }

                startActivity(i);
            }
        });

        redditListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem
                    , int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (totalItemCount == 0 || listadapter ==null)
                    return;
                if (m_PreviousTotalCount == totalItemCount)
                    return;
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                if (loadMore)             {
                    m_PreviousTotalCount = totalItemCount;
                    fetchImage();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reddit_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchImage () {
        new FetchImageAsyncTask().execute();
    }
    private class FetchImageAsyncTask extends AsyncTask<String, Void, List<ThumbnailHolder>> {
        private final String TAG = FetchImageAsyncTask.class.getSimpleName();
        RedditItem redditItem;
        ThumbnailHolder thumbnailHolder;
        ArrayList<RedditItem> redditList;
        ArrayList<ThumbnailHolder> thumbnailList;

        @Override
        protected List<ThumbnailHolder> doInBackground(String... params) {
            Log.v(TAG, "doInbackground just ran");
            redditList = new ArrayList<RedditItem>();
            thumbnailList = new ArrayList<ThumbnailHolder>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String mJsonStr = null;

            try {

                URL url = new URL("http://www.reddit.com/.json");

                Log.v(TAG, "url: " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection == null) {
                    Log.v(TAG, "url conn not made");
                }
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                Log.v(TAG, ""+urlConnection.getResponseCode());

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    inputStream = urlConnection.getErrorStream();
                    Log.v(TAG, "it failed to get inputstream");
                    mJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    Log.v(TAG, "line was read to be: "+line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    mJsonStr = null;
                }
                mJsonStr = buffer.toString();

                Log.v(TAG, "mJsonStr is: "+mJsonStr);
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                mJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            String vipStr = mJsonStr;
            Bitmap bitmap = null;
            JSONObject jsonPicture = null;

            try {
                jsonPicture = new JSONObject(vipStr);

                JSONObject jsoN = jsonPicture.getJSONObject("data");
                JSONArray children = jsoN.getJSONArray("children");

                String urlImage = "";

                for (int i = 0; i < children.length(); i++) {
                    JSONObject jsonobject = children.getJSONObject(i)
                            .getJSONObject("data");

                    thumbnailHolder  = new ThumbnailHolder();

                    Drawable d = null;
                    try {
                        if (jsonobject.getString("thumbnail")!= "") {
                            urlImage = jsonobject.getString("thumbnail");
                            Log.v(TAG, "thumbnail: " + urlImage);
                        }
                        InputStream in=null;
                        try {
                            in = (InputStream) new URL(urlImage).getContent();
                            d = Drawable.createFromStream(in, "src name");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        thumbnailHolder.setThumbnail(d);

                    } catch (JSONException e) {

                    }
                    thumbnailList.add(thumbnailHolder);
//                    listadapter.add(thumbnailHolder);
//                    listadapter.notifyDataSetChanged();
//                    loadingMore = false;

                }
            } catch (JSONException ee) {
                ee.printStackTrace();
            }



            return thumbnailList;
        }

        @Override
        protected void onPostExecute(List<ThumbnailHolder> thumbnailHList) {
            String content = "";
            JSONObject responseObj = null;

            try {

                Gson gson = new Gson();
                responseObj = new JSONObject(content);
                JSONArray countryListObj = responseObj.getJSONArray("countryList");

                if(countryListObj.length() == 0){
                    redditListView.removeFooterView(loadMoreView);
                }
                else {
                    for (int i=0; i<countryListObj.length(); i++){
                        start++;
                        String redditItem = countryListObj.getJSONObject(i).toString();
                        RedditItem country = gson.fromJson(redditItem, RedditItem.class);
                        redditList.add(country);
                        listadapter.add(country);
                    }

                    listadapter.notifyDataSetChanged();
                    loadingMore = false;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
