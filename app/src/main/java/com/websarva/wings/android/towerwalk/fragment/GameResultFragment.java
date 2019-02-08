package com.websarva.wings.android.towerwalk.fragment;


import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.adapter.GameResultListAdapter;
import com.websarva.wings.android.towerwalk.db.Database;
import com.websarva.wings.android.towerwalk.db.TowerWalkDB;
import com.websarva.wings.android.towerwalk.util.TransitionUtils;

import java.util.ArrayList;

/**
 * 対戦記録画面
 */
public class GameResultFragment extends BaseFragment {

    // リサイクラービュー
    private RecyclerView mRecyclerViewGameResult;
    // 対戦記録のリスト
    private Database.ResultSet mGameResultList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_game_result, container, false);
        setupLayout(contentView);
        return contentView;
    }

    @Override
    protected void setupLayout(View contentView) {
        // 画面タイトルをセット
        TextView textBarTitle = contentView.findViewById(R.id.text_bar_title);
        if (textBarTitle != null) {
            textBarTitle.setText(R.string.fragment_game_result_title);
        }

        mRecyclerViewGameResult = contentView.findViewById(R.id.recycler_view_game_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewGameResult.setLayoutManager(layoutManager);
        mGameResultList = TowerWalkDB.getGameResultData();
        if (mGameResultList == null) {
            mGameResultList = new Database.ResultSet();
        }

        GameResultListAdapter gameResultListAdapter = new GameResultListAdapter(getContext(), mGameResultList);
        mRecyclerViewGameResult.setAdapter(gameResultListAdapter);

        gameResultListAdapter.setOnItemClickListener(new GameResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int sequenceId) {
                // 対戦記録詳細画面にシーケンスIDを渡す
                if (!TransitionUtils.isTransitionAvailable() || getActivity() == null) {
                    return;
                }
                GameResultDetailFragment gameResultDetailFragment = new GameResultDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(BaseColumns._ID, sequenceId);
                gameResultDetailFragment.setArguments(bundle);

                // 対戦記録詳細画面に遷移する
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.layout_activity_transition, gameResultDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
