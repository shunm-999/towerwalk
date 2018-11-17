package com.websarva.wings.android.towerwalk.algorithm;

import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.consts.KeyMapConst;
import com.websarva.wings.android.towerwalk.item.CharacterIcon;

public abstract class Algorithm {

    /**
     * 次に進む座標を決定する
     *
     * @param boardTowerList 盤面の状態
     * @param player         プレーヤー
     * @param opponent       対戦者
     * @return 次に進む座標
     */
    public final int[] decideNextPosition(int[][] boardTowerList, CharacterIcon player, CharacterIcon opponent) {

        // 配列をコピー
        int[][] towerList = new int[boardTowerList.length][];
        for (int i = 0; i < boardTowerList.length; i++) {
            int array[] = new int[boardTowerList[i].length];
            System.arraycopy(boardTowerList[i], 0, array, 0, boardTowerList[i].length);
            towerList[i] = array;
        }

        int[] playerPosition = new int[2];
        System.arraycopy(player.getCurrentPosition(), 0, playerPosition, 0, player.getCurrentPosition().length);
        int[] opponentPosition = new int[2];
        System.arraycopy(opponent.getCurrentPosition(), 0, opponentPosition, 0, opponent.getCurrentPosition().length);

        return seek(towerList, playerPosition, opponentPosition).getDistance();

    }

    /**
     * 次の座標に移動可能か調べる
     *
     * @param boardTowerList    盤面の状態
     * @param characterPosition キャラクターの座標
     * @param keyMap            移動の方向
     * @return 次の座標に移動可能かどうか
     */
    protected boolean canMoveNextPosition(int[][] boardTowerList, int[] characterPosition, KeyMapConst.KeyMap keyMap) {
        int currentPositionHigh = boardTowerList[characterPosition[0]][characterPosition[1]];
        int[] nextPosition = new int[]{characterPosition[0] + keyMap.getDistance()[0], characterPosition[1] + keyMap.getDistance()[1]};
        int nextPositionHigh = boardTowerList[nextPosition[0]][nextPosition[1]];

        return (nextPositionHigh != GameConst.OUTSIDE_TOWER_HIGH) && (nextPositionHigh < GameConst.MAX_TOWER_HIGH) && (Math.abs(currentPositionHigh - nextPositionHigh) <= GameConst.DIFFERENCE_IN_HEIGHT);
    }

    /**
     * 探索する
     *
     * @param boardTowerList   盤面の状態
     * @param playerPosition   プレーヤーの座標
     * @param opponentPosition 対戦相手の座標
     * @return 次に進む座標
     */
    protected abstract KeyMapConst.KeyMap seek(int[][] boardTowerList, int[] playerPosition, int[] opponentPosition);
}
