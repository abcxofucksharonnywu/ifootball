package com.abcxo.android.ifootball.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.controllers.fragments.detail.ChatDetailFragment;

/**
 * Created by shadow on 15/11/4.
 */
public class ChatDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ChatDetailFragment.newInstance(getIntent().getExtras())).commit();
    }

}