package com.logicdevil.ad60test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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

/**
 * Created by suhyunkim on 1/6/15.
 */
public class FetchStringAsyncTask extends AsyncTask<String, Void, List<RedditItem>> {
    private static final String TAG = FetchStringAsyncTask.class.getSimpleName();
    RedditItem redditItem;

    ArrayList<RedditItem> redditList;

    @Override
    protected List<RedditItem> doInBackground(String... urlInputArray) {
        Log.v(TAG, "doInbackground just ran");
        redditList = new ArrayList<RedditItem>();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String mJsonStr = null;
        String soonToBeURL = urlInputArray[0];
        try {

            URL url = new URL(soonToBeURL);

            Log.v(TAG, "url: " + url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection == null) {
                Log.v(TAG, "url conn not made");
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            Log.v(TAG, "" + urlConnection.getResponseCode());

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
                Log.v(TAG, "line was read to be: " + line);
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                mJsonStr = null;
            }
            mJsonStr = buffer.toString();

            Log.v(TAG, "mJsonStr is: " + mJsonStr);
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            mJsonStr = null;
        } finally {
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
        JSONObject jsonPicture = null;

        try {
            jsonPicture = new JSONObject(vipStr);

            JSONObject jsoN = jsonPicture.getJSONObject("data");
            JSONArray children = jsoN.getJSONArray("children");


            for (int i = 0; i < children.length(); i++) {
                JSONObject jsonobject = children.getJSONObject(i)
                        .getJSONObject("data");

                redditItem = new RedditItem();
                String num_comments = jsonobject.getString("num_comments");
                redditItem.setSubredit(num_comments);

                String author = jsonobject.getString("author");
                redditItem.setAuthor(author);

                String title = jsonobject.getString("title");
                redditItem.setTitle(title);

                String score = jsonobject.getString("score");
                redditItem.setScore(score);


                if (jsonobject.getString("selftext_html").equals("null")) {
                    String detail = jsonobject.getString("url");
                    redditItem.setDetail(detail);
                    redditItem.setSelftext(false);
                } else {
                    String detail = jsonobject.getString("selftext");
                    redditItem.setDetail(detail);
                    redditItem.setSelftext(true);
                }

                redditList.add(redditItem);

            }


            String after = jsoN.getString("after");
            redditItem.setAfterTag(after);
            Log.d(TAG, "after tag is: " + after);
        } catch (JSONException ee) {
            ee.printStackTrace();
        }


        return redditList;
    }

    @Override
    protected void onPostExecute(List<RedditItem> items) {
        Log.e(TAG, "onpostexecute item");
            for(RedditItem item : items) {
                RedditHomePageActivity.listadapter.addRedditItem(item);
            }
    }
}
