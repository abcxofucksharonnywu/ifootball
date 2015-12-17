package com.abcxo.android.ifootball.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.constants.Constants;
import com.abcxo.android.ifootball.controllers.fragments.nav.ContactNavFragment;

/**
 * Created by shadow on 15/11/4.
 */
public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KEY_IS_SELECT, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ContactNavFragment.newInstance(bundle)).commit();
    }

}
