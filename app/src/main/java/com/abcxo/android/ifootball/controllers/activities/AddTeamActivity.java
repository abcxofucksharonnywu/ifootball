package com.abcxo.android.ifootball.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.controllers.fragments.add.AddTeamFragment;
import com.abcxo.android.ifootball.controllers.fragments.sign.LoginSignFragment;

/**
 * Created by shadow on 15/11/4.
 */
public class AddTeamActivity extends CommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, AddTeamFragment.newInstance()).commit();
    }

}
