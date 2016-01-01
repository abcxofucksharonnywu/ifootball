package com.abcxo.android.ifootball.controllers.fragments.main;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcxo.android.ifootball.BR;
import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.constants.Constants;
import com.abcxo.android.ifootball.models.Tweet;
import com.abcxo.android.ifootball.restfuls.RestfulError;
import com.abcxo.android.ifootball.restfuls.TweetRestful;
import com.abcxo.android.ifootball.utils.FileUtils;
import com.abcxo.android.ifootball.utils.Utils;
import com.abcxo.android.ifootball.utils.ViewUtils;
import com.abcxo.android.ifootball.views.DividerItemDecoration;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class TweetFragment extends Fragment {


    protected List<Tweet> list = new ArrayList<>();
    protected long uid;

    protected SuperRecyclerView recyclerView;
    protected TweetAdapter adapter;

    protected SwipeRefreshLayout.OnRefreshListener onRefreshListener;


    protected int pageIndex;

    public static TweetFragment newInstance() {
        return newInstance(null);
    }

    public static TweetFragment newInstance(Bundle args) {
        TweetFragment fragment = new TweetFragment();
        if (args != null) fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            uid = args.getLong(Constants.KEY_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (SuperRecyclerView) view.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL));

        adapter = new TweetAdapter(list);
        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshingColorResources(R.color.color_refresh_1, R.color.color_refresh_2, R.color.color_refresh_3, R.color.color_refresh_4);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                loadData(true);
            }
        };
        recyclerView.setRefreshListener(onRefreshListener);

        recyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                loadData(false);
            }
        }, Constants.MAX_LEFT_MORE);
        if (needFirstRefresh()) {
            load();
        }

    }


    protected void load() {
        ArrayList<Tweet> tweets = (ArrayList<Tweet>) FileUtils.getObject(getKey());
        if (tweets != null) {
            refreshTweets(tweets);
        } else {
            refresh();
        }

    }

    protected String getKey() {
        return Utils.md5(String.format("uid=%s;getsType=%s;", uid, getGetsType().name()));
    }

    public boolean needFirstRefresh() {
        return true;
    }

    public void refresh() {
        recyclerView.getSwipeToRefresh().post(new Runnable() {
            @Override
            public void run() {
                recyclerView.getSwipeToRefresh().setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

    protected void loadData(final boolean first) {
        TweetRestful.INSTANCE.gets(getGetsType(), uid, getKeyword(),
                pageIndex, new TweetRestful.OnTweetRestfulList() {
                    @Override
                    public void onSuccess(List<Tweet> tweets) {
                        if (first) {
                            refreshTweets(tweets);
                            FileUtils.setObject(getKey(), new ArrayList<>(tweets));
                        } else {
                            addTweets(tweets);
                        }

                    }

                    @Override
                    public void onError(RestfulError error) {
                        ViewUtils.toast(error.msg);
                    }

                    @Override
                    public void onFinish() {
                        if (first) {
                            recyclerView.getSwipeToRefresh().setRefreshing(false);
                        } else {
                            recyclerView.hideMoreProgress();
                        }


                    }
                });
    }


    protected String getKeyword() {
        return "";
    }

    protected TweetRestful.GetsType getGetsType() {
        return TweetRestful.GetsType.USER;
    }


    protected void refreshTweets(List<Tweet> tweets) {
        list.clear();


        if (tweets != null && tweets.size() > 0) {
            list.addAll(tweets);
            pageIndex++;

        }
        adapter.notifyDataSetChanged();
    }


    protected void addTweets(List<Tweet> tweets) {
        if (tweets != null && tweets.size() > 0) {
            int bCount = list.size();
            list.addAll(tweets);
            adapter.notifyItemRangeInserted(bCount, tweets.size());
            pageIndex++;
        }
    }


    public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.BindingHolder> {

        private List<Tweet> tweets;


        public TweetAdapter(List<Tweet> tweets) {
            this.tweets = tweets;
        }

        public class BindingHolder extends RecyclerView.ViewHolder {
            public ViewDataBinding binding;
            public View view;

            public BindingHolder(View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
                view = rowView;
            }
        }

        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int type) {
            BindingHolder holder = new BindingHolder(getItemLayoutView(parent, type));
            return holder;
        }

        public View getItemLayoutView(ViewGroup parent, int type) {
            if (type == Tweet.TweetMainType.TEAM.getIndex()) {
                return LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tweet_team, parent, false);
            } else if (type == Tweet.TweetMainType.NEWS.getIndex()) {
                return LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tweet_news, parent, false);
            } else if (type == Tweet.TweetMainType.IMAGE.getIndex()) {
                return LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tweet_image, parent, false);
            } else {
                return LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_tweet_normal, parent, false);
            }

        }

        @Override
        public void onBindViewHolder(BindingHolder holder, int position) {
            final Tweet tweet = tweets.get(position);
            holder.binding.setVariable(BR.tweet, tweet);
        }


        @Override
        public int getItemViewType(int position) {
            Tweet tweet = tweets.get(position);
            return tweet.getMainType().getIndex();
        }

        @Override
        public int getItemCount() {
            return tweets.size();
        }

    }
}
