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
 * 相手の行動可能ポイント[3]）
 */
public class ReductionAndConquerAlgorithm extends Algorithm {

    // 探索の深さ
    private int searchDepth = 9;

    @Override
    protected KeyMapConst.KeyMap seek(int[][] boardTowerList, int[] playerPosition, int[] opponentPosition) {
        // 探索の結果を保存するリスト
        Map<KeyMapConst.KeyMap, Integer[]> algorithmMap = new HashMap<>();

        // 上下左右のどれが最適か調べる
        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            if (canMoveNextPosition(boardTowerList, playerPosition, keyMap)) {
                Integer[] resultMap = new Integer[]{0, 0, 0, 0};
                algorithmMap.put(keyMap, resultMap);

                playerPosition[0] += keyMap.getDistance()[0];
                playerPosition[1] += keyMap.getDistance()[1];
                boardTowerList[playerPosition[0]][playerPosition[1]]++;

                loop(boardTowerList, playerPosition, opponentPosition, resultMap, 11, true);

                boardTowerList[playerPosition[0]][playerPosition[1]]--;
                playerPosition[0] -= keyMap.getDistance()[0];
                playerPosition[1] -= keyMap.getDistance()[1];

            }
        }

        // todo ソート処理を入れる
        int currentPoint = 0;
        KeyMapConst.KeyMap result = null;

        for (Map.Entry<KeyMapConst.KeyMap, Integer[]> entry : algorithmMap.entrySet()) {
            try {
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                Integer[] value = entry.getValue();
                int point = value[2] - value[0];
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
            } catch (NoSuchAlgorithmException nsae) {
                // 例外ハンドラへ
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
    private void loop(int[][] boardTowerList, int[] playerPosition, int[] opponentPosition, Integer[] point, int count, boolean playerFrag) {

        if (count <= 1) {
            return;
        }

        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            if (canMoveNextPosition(boardTowerList, playerPosition, keyMap)) {

                if (playerFrag) {
                    // 自分の行動可能ポイントを増やす
                    point[2] += count;
                } else {
                    // 相手の行動可能ポイントを増やす
                    point[3] += count;
                }

                playerPosition[0] += keyMap.getDistance()[0];
                playerPosition[1] += keyMap.getDistance()[1];
                boardTowerList[playerPosition[0]][playerPosition[1]]++;

                playerFrag = !playerFrag;
                count--;

                // 再帰的に呼ぶ
                loop(boardTowerList, opponentPosition, playerPosition, point, count, playerFrag);

                boardTowerList[playerPosition[0]][playerPosition[1]]--;
                playerPosition[0] -= keyMap.getDistance()[0];
                playerPosition[1] -= keyMap.getDistance()[1];

                playerFrag = !playerFrag;
                count++;

            } else {
                if (playerFrag) {
                    // 自分の行動不能ポイントを増やす
                    point[0] += count;
                } else {
                    // 相手の行動不能ポイントを増やす
                    point[1] += count;
                }
            }
        }
    }
}
