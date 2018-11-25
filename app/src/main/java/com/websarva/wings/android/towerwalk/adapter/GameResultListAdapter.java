package com.websarva.wings.android.towerwalk.adapter;

import android.content.Context;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.db.CompetitionHistoryTable;
import com.websarva.wings.android.towerwalk.db.Database;
import com.websarva.wings.android.towerwalk.fragment.GameResultFragment;
import com.websarva.wings.android.towerwalk.util.DateUtils;
import com.websarva.wings.android.towerwalk.util.LogUtils;

import java.util.Map;

/**
 * 対戦記録のアダプター
 */
public class GameResultListAdapter extends RecyclerView.Adapter<GameResultListAdapter.GameResultViewHolder> {

    // コンテキスト
    private Context mContext;
    // 対戦記録リストデータ
    private Database.ResultSet mGameResultList;
    // リストのアイテム押下時のリスナ
    private OnItemClickListener mOnItemClickListener;

    public GameResultListAdapter(Context context, Database.ResultSet gameResultList) {
        mContext = context;
        mGameResultList = gameResultList;
    }

    /**
     * 対戦記録のビューホルダー
     */
    class GameResultViewHolder extends RecyclerView.ViewHolder {

        // カード全体のビュー
        private View mItemView;
        // 対戦記録のシーケンスID
        private int mSequenceId;
        // 対戦記録の名前
        private TextView mTextGameResultName;
        // 対戦記録の日付
        private TextView mTextGameResultDate;
        // 対戦記録のステータスアイコン
        private ImageView mImageGameResultStatusIcon;
        // 対戦記録のステータス
        private ImageView mImageGameResultStatus;

        /**
         * GameResultViewHolderを構築する
         */
        GameResultViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextGameResultName = itemView.findViewById(R.id.text_game_result_name);
            mTextGameResultDate = itemView.findViewById(R.id.text_game_result_date);
            mImageGameResultStatusIcon = itemView.findViewById(R.id.image_game_result_status_icon);
            mImageGameResultStatus = itemView.findViewById(R.id.image_game_result_status);
        }
    }

    @NonNull
    @Override
    public GameResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.recycler_item_game_result, parent, false);
        return new GameResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GameResultViewHolder holder, int position) {
        Map<String, String> gameResultData = mGameResultList.get(position);
        holder.mSequenceId = Integer.parseInt(gameResultData.get(BaseColumns._ID));
        holder.mTextGameResultName.setText(gameResultData.get(CompetitionHistoryTable.COLUMN_NAME));
        long gameResultDate = Long.parseLong(gameResultData.get(CompetitionHistoryTable.COLUMN_CREATE_DATE));
        holder.mTextGameResultDate.setText(DateUtils.convertUnixTimeToDetailDate(gameResultDate));

        int gameResultStatusCode = -1;
        int gameResultStatusIconCode = -1;
        int statusCode = Integer.parseInt(gameResultData.get(CompetitionHistoryTable.COLUMN_GAME_RESULT));
        if (statusCode == CompetitionHistoryTable.GameResult.WIN.getResult()) {
            gameResultStatusCode = R.drawable.result_status_win;
            gameResultStatusIconCode = R.drawable.result_status_icon_win;

        } else if (statusCode == CompetitionHistoryTable.GameResult.DRAW.getResult()) {
            gameResultStatusCode = R.drawable.result_status_draw;
            gameResultStatusIconCode = R.drawable.result_status_icon_draw;

        } else if (statusCode == CompetitionHistoryTable.GameResult.LOSE.getResult()) {
            gameResultStatusCode = R.drawable.result_status_lose;
            gameResultStatusIconCode = R.drawable.result_status_icon_lose;
        } else {
            return;
        }

        holder.mImageGameResultStatus.setImageResource(gameResultStatusCode);
        holder.mImageGameResultStatusIcon.setImageResource(gameResultStatusIconCode);

        // リスナを登録する
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.mSequenceId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGameResultList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int sequenceId);
    }

    /**
     * リストのアイテム押下時のリスナを登録する
     *
     * @param onItemClickListener リストのアイテム押下時のリスナ
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
