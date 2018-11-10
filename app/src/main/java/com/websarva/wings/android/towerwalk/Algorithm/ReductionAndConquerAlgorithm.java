package com.websarva.wings.android.towerwalk.Algorithm;

import android.util.Log;

import com.websarva.wings.android.towerwalk.Const.KeyMapConst;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * 深さ探索で次の手を決定するアルゴリズム
 * <p>
 * リストの中身は、（自分の行動不能ポイント[0]、相手の行動不能ポイント[1]、自分の行動可能ポイント[2]、
 * 相手の行動可能ポイント[3]、自分の詰みフラグ[4]、相手の詰みフラグ[5]、自分の詰みカウント[6]、相手の詰みカウント[7]）
 */
public class ReductionAndConquerAlgorithm extends Algorithm {

    // タグ
    private static final String TAG = ReductionAndConquerAlgorithm.class.getSimpleName();
    // 探索の深さ
    private static final int SEARCH_DEPTH = 10;

    @Override
    protected KeyMapConst.KeyMap seek(int[][] boardTowerList, int[] playerPosition, int[] opponentPosition) {
        // 探索の結果を保存するリスト
        Map<KeyMapConst.KeyMap, Double[]> algorithmMap = new HashMap<>();

        // 上下左右のどれが最適か調べる
        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            if (canMoveNextPosition(boardTowerList, playerPosition, keyMap)) {
                Double[] resultMap = new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                algorithmMap.put(keyMap, resultMap);

                playerPosition[0] += keyMap.getDistance()[0];
                playerPosition[1] += keyMap.getDistance()[1];
                boardTowerList[playerPosition[0]][playerPosition[1]]++;

                loop(boardTowerList, playerPosition, opponentPosition, resultMap, 1, true);

                boardTowerList[playerPosition[0]][playerPosition[1]]--;
                playerPosition[0] -= keyMap.getDistance()[0];
                playerPosition[1] -= keyMap.getDistance()[1];
            }
        }

        // TODO ソート処理を入れる
        double currentPoint = 0;
        KeyMapConst.KeyMap result = null;

        // 詰み手があるか調べる
        for (Map.Entry<KeyMapConst.KeyMap, Double[]> entry : algorithmMap.entrySet()) {
            Double[] value = entry.getValue();
            if ((value[5] == 1.0 && value[4] == 0) || (value[5] == 1.0 && value[4] == 1.0 && value[6] > value[7])) {
                return entry.getKey();
            }
        }

        int checkmateCount = 0;
        for (Map.Entry<KeyMapConst.KeyMap, Double[]> entry : algorithmMap.entrySet()) {
            Double[] value = entry.getValue();
            if ((value[5] == 0.0 && value[4] == 1.0) || (value[5] == 1.0 && value[4] == 1.0 && value[6] < value[7])) {
                checkmateCount++;
            }
        }

        if (checkmateCount == algorithmMap.size()) {
            int checkmateStep = 0;
            for (Map.Entry<KeyMapConst.KeyMap, Double[]> entry : algorithmMap.entrySet()) {
                Double[] value = entry.getValue();
                if (value[6] > checkmateStep) {
                    result = entry.getKey();
                }
            }
            return result;

        } else if ((checkmateCount != 0) || (checkmateCount < algorithmMap.size())) {

            for (Map.Entry<KeyMapConst.KeyMap, Double[]> entry : algorithmMap.entrySet()) {
                Double[] value = entry.getValue();
                if ((value[5] == 0.0 && value[4] == 1.0) || (value[5] == 1.0 && value[4] == 1.0 && value[6] < value[7])) {
                    algorithmMap.remove(entry);
                }
            }
        }

        // 最もポイントの高い手を選択する
        for (Map.Entry<KeyMapConst.KeyMap, Double[]> entry : algorithmMap.entrySet()) {
            try {
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                Double[] value = entry.getValue();
                double point = value[2] - value[0];

                if ((currentPoint == 0) && (point <= 0) && (result == null)) {
                    currentPoint = point;
                    result = entry.getKey();
                }

                if ((currentPoint == point) && (secureRandom.nextInt(1) == 0)) {
                    result = entry.getKey();
                    currentPoint = point;
                } else if (currentPoint <= point) {
                    result = entry.getKey();
                    currentPoint = point;
                }
            } catch (NoSuchAlgorithmException exception) {
                Log.i(TAG, exception.getMessage());
            }
        }

        return result;
    }

    /**
     * 再帰的に次の手を調べる
     *
     * @param boardTowerList   盤面の状態
     * @param playerPosition   プレーヤーの座標
     * @param opponentPosition 対戦相手の座標
     */
    private void loop(int[][] boardTowerList, int[] playerPosition, int[] opponentPosition, Double[] point, int count, boolean playerFrag) {

        if (playerFrag) {
            // 自分の詰みフラグを解除
            point[4] = 0.0;
        } else {
            // 相手の詰みフラグを解除
            point[5] = 0.0;
        }

        if (SEARCH_DEPTH <= count) {
            return;
        }

        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            if (canMoveNextPosition(boardTowerList, playerPosition, keyMap)) {

                if (playerFrag) {
                    // 自分の行動可能ポイントを増やす
                    point[2] += Math.pow(0.25, (count + 1) / 2);
                    // 自分の詰みフラグを解除
                    point[4] = 0.0;
                } else {
                    // 相手の行動可能ポイントを増やす
                    point[3] += Math.pow(0.25, count / 2);
                    // 相手の詰みフラグを解除
                    point[5] = 0.0;
                }

                playerPosition[0] += keyMap.getDistance()[0];
                playerPosition[1] += keyMap.getDistance()[1];
                boardTowerList[playerPosition[0]][playerPosition[1]]++;

                playerFrag = !playerFrag;
                count++;

                // 再帰的に呼ぶ
                loop(boardTowerList, opponentPosition, playerPosition, point, count, playerFrag);

                playerFrag = !playerFrag;
                count--;

                boardTowerList[playerPosition[0]][playerPosition[1]]--;
                playerPosition[0] -= keyMap.getDistance()[0];
                playerPosition[1] -= keyMap.getDistance()[1];

            } else {
                if (playerFrag) {
                    // 自分の行動不能ポイントを増やす
                    point[0] += Math.pow(0.25, (count + 1) / 2);
                    if (keyMap == KeyMapConst.KeyMap.LEFT) {
                        // 自分の詰みフラグをセット
                        point[4] = 1.0;
                        if (count > point[6]) {
                            // 一番深い葉を探す
                            point[6] = (double) count;
                        }
                    }
                } else {
                    // 相手の行動不能ポイントを増やす
                    point[1] += Math.pow(0.25, count / 2);
                    if (keyMap == KeyMapConst.KeyMap.LEFT) {
                        // 相手の詰みフラグをセット
                        point[5] = 1.0;
                        if (count > point[7]) {
                            // 一番深い葉を探す
                            point[7] = (double) count;
                        }
                    }
                }
            }
        }
    }
}
