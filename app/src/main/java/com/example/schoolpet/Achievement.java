package com.example.schoolpet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Achievement extends AppCompatActivity {
    private List<RankingList> rankingLists=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pai_hang_bang);

        initRankingList();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.PaiHangBang_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RankingListAdapter adapter=new RankingListAdapter(rankingLists);
        recyclerView.setAdapter(adapter);
    }

    private void initRankingList() {
        RankingList rankingList=new RankingList(1,"林辉",30,30);
        rankingLists.add(rankingList);
        RankingList rankingList2=new RankingList(2,"林辉",30,30);
        rankingLists.add(rankingList2);
        RankingList rankingList3=new RankingList(3,"林辉",30,30);
        rankingLists.add(rankingList3);
        RankingList rankingList4=new RankingList(4,"林辉",30,30);
        rankingLists.add(rankingList4);
    }
}
