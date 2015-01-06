//package com.logicdevil.ad60test;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ImageView;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by suhyunkim on 1/5/15.
// */
//public class FetchImageAsyncTask extends AsyncTask <String, Void, List<ThumbnailHolder>>{
//    private static final String TAG = FetchImageAsyncTask.class.getSimpleName();
//    RedditItem redditItem;
//    ThumbnailHolder thumbnailHolder;
//    ArrayList<RedditItem> redditList;
//    ArrayList<ThumbnailHolder> thumbnailList;
//    RedditListViewAdapter redditListViewAdapter;
//
//    @Override
//    protected List<ThumbnailHolder> doInBackground(String... params) {
//        Log.v(TAG, "doInbackground just ran");
//        redditList = new ArrayList<RedditItem>();
//        thumbnailList = new ArrayList<ThumbnailHolder>();
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        String mJsonStr = null;
//
//        try {
//
//            URL url = new URL("http://www.reddit.com/.json");
//
//            Log.v(TAG, "url: " + url.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            if(urlConnection == null) {
//                Log.v(TAG, "url conn not made");
//            }
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//
//            Log.v(TAG, ""+urlConnection.getResponseCode());
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                inputStream = urlConnection.getErrorStream();
//                Log.v(TAG, "it failed to get inputstream");
//                mJsonStr = null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                Log.v(TAG, "line was read to be: "+line);
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                mJsonStr = null;
//            }
//            mJsonStr = buffer.toString();
//
//            Log.v(TAG, "mJsonStr is: "+mJsonStr);
//        } catch (IOException e) {
//            Log.e(TAG, "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attempting
//            // to parse it.
//            mJsonStr = null;
//        } finally{
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(TAG, "Error closing stream", e);
//                }
//            }
//        }
//
//        String vipStr = mJsonStr;
//        Bitmap bitmap = null;
//        JSONObject jsonPicture = null;
//
//        try {
//            jsonPicture = new JSONObject(vipStr);
//
//            JSONObject jsoN = jsonPicture.getJSONObject("data");
//            JSONArray children = jsoN.getJSONArray("children");
//
//            String urlImage = "";
//
//            for (int i = 0; i < children.length(); i++) {
//                JSONObject jsonobject = children.getJSONObject(i)
//                        .getJSONObject("data");
//
//                thumbnailHolder  = new ThumbnailHolder();
////                redditItem = new RedditItem();
////                String num_comments = jsonobject.getString("num_comments");
////                redditItem.setSubredit(num_comments);
////
////                String author = jsonobject.getString("author");
////                redditItem.setAuthor(author);
////
////                String title = jsonobject.getString("title");
////                redditItem.setTitle(title);
////
////                String score = jsonobject.getString("score");
////                redditItem.setScore(score);
////
////
////                if (jsonobject.getString("selftext_html").equals("null")) {
////                    String detail = jsonobject.getString("url");
////                    redditItem.setDetail(detail);
////                    redditItem.setSelftext(false);
////                } else {
////                    String detail = jsonobject.getString("selftext");
////                    redditItem.setDetail(detail);
////                    redditItem.setSelftext(true);
////                }
//
//                Drawable d = null;
//                try {
//                    if (jsonobject.getString("thumbnail")!= "") {
//                        urlImage = jsonobject.getString("thumbnail");
//                        Log.v(TAG, "thumbnail: " + urlImage);
//                    }
//                    InputStream in=null;
//                    try {
//                        in = (InputStream) new URL(urlImage).getContent();
//                        d = Drawable.createFromStream(in, "src name");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    thumbnailHolder.setThumbnail(d);
//
////                    redditItem.writeObject(in);
////                    holder.imageView = (ImageView) convertView.findViewById(R.id.id_thumbnail_imageView);
//
//
//                } catch (JSONException e) {
////                    redditItem.setUrlImage(null);
//                }
//                thumbnailList.add(thumbnailHolder);
//
//
////                redditListViewAdapter.add(thumbnailHolder);
////                redditList.add(redditItem);
//            }
//        } catch (JSONException ee) {
//            ee.printStackTrace();
//        }
//
//
//
//        return thumbnailList;
//    }
//}
