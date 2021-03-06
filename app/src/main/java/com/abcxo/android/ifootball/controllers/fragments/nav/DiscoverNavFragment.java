package com.abcxo.android.ifootball.controllers.fragments.nav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.controllers.fragments.main.DiscoverUserFragment;

public class DiscoverNavFragment extends NavFragment {
    public static DiscoverNavFragment newInstance() {
        return newInstance(null);
    }

    public static DiscoverNavFragment newInstance(Bundle args) {
        DiscoverNavFragment fragment = new DiscoverNavFragment();
        if (args != null) fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover_nav, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNavActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content, DiscoverUserFragment.newInstance(getArguments()))
                .commit();
    }


}
