package com.abcxo.android.ifootball.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.controllers.fragments.add.AddTweetFragment;

/**
 * Created by shadow on 15/11/4.
 */
public class AddTweetActivity extends CommonActivity {
    private AddTweetFragment addTweetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tweet);
        addTweetFragment = AddTweetFragment.newInstance(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.content, addTweetFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (!addTweetFragment.onBackPressed()) {
            super.onBackPressed();
        }

    }
}
