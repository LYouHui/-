package com.example.schoolpet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林尤辉 on 2017/7/7.
 */

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.ViewHolder>{
    private List<RankingList> mRankingList=new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView number;
        TextView name;
        TextView studytime;
        TextView sporttime;

        public ViewHolder(View view){
            super(view);
            number=(TextView) view.findViewById(R.id.rankingList_textView_number);
            name=(TextView)view.findViewById(R.id.rankingList_textView_name);
            studytime=(TextView)view.findViewById(R.id.rankingList_textView_studytime);
            sporttime=(TextView)view.findViewById(R.id.rankingList_textView_sporttime);
        }
    }
    public RankingListAdapter(List<RankingList> friendList){
        mRankingList=friendList;
    }

    public RankingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    public void onBindViewHolder(ViewHolder holder, int position){
        RankingList rankingList=mRankingList.get(position);
        holder.number.setText(rankingList.getNumber());
        holder.name.setText(rankingList.getName());
        holder.studytime.setText(rankingList.getStudytime());
        holder.sporttime.setText(rankingList.getSporttime());
    }
    public int getItemCount(){
        return mRankingList.size();
    }
}
