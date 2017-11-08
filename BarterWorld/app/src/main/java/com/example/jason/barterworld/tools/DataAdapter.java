package com.example.jason.barterworld.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jason.barterworld.MainActivity;
import com.example.jason.barterworld.R;
import com.example.jason.barterworld.model.Post;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



/**
 * Created by User on 9/18/2017.
 */


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<Post> mArrayList;
    private ArrayList<Post> mFilteredList;

    public DataAdapter(ArrayList<Post> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.barter_name.setText(mFilteredList.get(i).getTitle());
        viewHolder.barter_desc.setText(mFilteredList.get(i).getDescription());
        viewHolder.barter_likeCount.setText(mFilteredList.get(i).getLike_count());

        viewHolder.barter_time.setText(mFilteredList.get(i).getTime());
        viewHolder.barter_type.setText(mFilteredList.get(i).getType());
        viewHolder.barter_username.setText(mFilteredList.get(i).getUsername());
        viewHolder.barter_value.setText(mFilteredList.get(i).getValue());





    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Post> filteredList = new ArrayList<>();

                    for (Post post : mArrayList) {

                        if (post.getDescription().toLowerCase().contains(charString) || post.getTitle().toLowerCase().contains(charString) || post.getType().toLowerCase().contains(charString) || post.getValue().toLowerCase().contains(charString) || post.getLatitude().toLowerCase().contains(charString)  || post.getLongitude().toLowerCase().contains(charString) || post.getLike_count().toLowerCase().contains(charString)   || post.getUsername().toLowerCase().contains(charString)   || post.getTime().toLowerCase().contains(charString)     ) {

                            filteredList.add(post);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Post>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView barter_name,barter_desc,barter_type,barter_value,barter_username,barter_time,barter_likeCount;
        private ImageView barter_Img,barter_likeBbtnImg,barter_shareBtnImg;
        public ViewHolder(View view) {
            super(view);


            /*
            *      //txt_post_name , txt_comment, txt_post_type, txt_post_value, txt_post_user, txt_post_upload_time, textView_likeNum,
            // img_post , shareBtn, likeBtn
            *
            * */

            barter_name = (TextView)view.findViewById(R.id.txt_post_name);
            barter_desc = (TextView)view.findViewById(R.id.txt_comment);
            barter_type = (TextView)view.findViewById(R.id.txt_post_type);
            barter_value = (TextView)view.findViewById(R.id.txt_post_value);
            barter_username = (TextView)view.findViewById(R.id.txt_post_user);
            barter_time = (TextView)view.findViewById(R.id.txt_post_upload_time);
            barter_likeCount = (TextView)view.findViewById(R.id.textView_likeNum);

            barter_Img = (ImageView) view.findViewById(R.id.img_post);
            barter_likeBbtnImg = (ImageView)view.findViewById(R.id.likeBtn);
            barter_shareBtnImg = (ImageView)view.findViewById(R.id.shareBtn);





        }
    }

}