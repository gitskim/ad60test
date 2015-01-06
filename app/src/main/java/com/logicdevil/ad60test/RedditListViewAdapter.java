package com.logicdevil.ad60test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by suhyunkim on 1/4/15.
 */
public class RedditListViewAdapter extends ArrayAdapter<RedditItem> {
    private String TAG = RedditListViewAdapter.class.getSimpleName();
    Context context;
    ArrayList<RedditItem> redditList;
    ArrayList<ThumbnailHolder> thumbnailList;

    public RedditListViewAdapter(Context context, int textviewResourceId
            , ArrayList<RedditItem> redditList
            , ArrayList<ThumbnailHolder> thumbnailList) {
        super(context, textviewResourceId, redditList);
        this.context = getContext();
        this.redditList = new ArrayList<RedditItem>();
        this.redditList.addAll(redditList);
        this.thumbnailList = new ArrayList<ThumbnailHolder>();
        this.thumbnailList.addAll(thumbnailList);
    }

    private class ViewHolder {
        TextView numComments, authorView, titleView, scoreView;
        ImageView imageView;
    }

    public void add(ThumbnailHolder redditItem){
        this.thumbnailList.add(redditItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG, "getView called");
        ViewHolder holder = null;
        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_list, null);

            holder = new ViewHolder();
            holder.numComments = (TextView) convertView.findViewById(R.id.id_subreddit);
            holder.titleView = (TextView) convertView.findViewById(R.id.id_title);
            holder.authorView = (TextView) convertView.findViewById(R.id.id_author);
            holder.scoreView = (TextView) convertView.findViewById(R.id.id_score);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_thumbnail_imageView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RedditItem redditItemWithPosition = redditList.get(position);
        ThumbnailHolder thumbnailWithPosition = thumbnailList.get(position);
        Log.v(TAG, "position is: "+position);
        holder.numComments.setText("# of Comments: "+ redditItemWithPosition.getSubredit());
        holder.titleView.setText("Title: "+ redditItemWithPosition.getTitle());
        holder.authorView.setText("Author: " + redditItemWithPosition.getAuthor());
        holder.scoreView.setText("Score: " + redditItemWithPosition.getScore());
        holder.imageView.setImageDrawable(thumbnailWithPosition.getThumbnail());
//        ScaleDrawable sd = new ScaleDrawable(redditItemWithPosition.readUrlImage(),
//                3, (float)2.0, (float)2.0);
//        holder.imageView.setImageDrawable(sd.getDrawable());

        return convertView;

    }
}
