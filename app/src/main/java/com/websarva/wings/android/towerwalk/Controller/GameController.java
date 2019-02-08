package com.websarva.wings.android.towerwalk.controller;

import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.consts.KeyMapConst;
import com.websarva.wings.android.towerwalk.consts.GameConst.GameStatus;
import com.websarva.wings.android.towerwalk.item.CharacterIcon;

public class GameController implements Controller {

    @Override
    public GameStatus judgeGame(CharacterIcon player, CharacterIcon opponent, int[][] boardTowerList) {

        boolean playerCanMove = false;
        boolean opponentCanMove = false;

        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            int[] playerPosition = new int[]{player.getCurrentPosition()[0] + keyMap.getDistance()[0], player.getCurrentPosition()[1] + keyMap.getDistance()[1]};
            int playerCurrentPositionHigh = boardTowerList[player.getCurrentPosition()[0]][player.getCurrentPosition()[1]];
            int playerPositionHigh = boardTowerList[playerPosition[0]][playerPosition[1]];

            int[] opponentPosition = new int[]{opponent.getCurrentPosition()[0] + keyMap.getDistance()[0], opponent.getCurrentPosition()[1] + keyMap.getDistance()[1]};
            int opponentCurrentPositionHigh = boardTowerList[opponent.getCurrentPosition()[0]][opponent.getCurrentPosition()[1]];
            int opponentPositionHigh = boardTowerList[opponentPosition[0]][opponentPosition[1]];

            if ((playerPositionHigh != GameConst.OUTSIDE_TOWER_HIGH) && (playerPositionHigh < GameConst.MAX_TOWER_HIGH) && Math.abs(playerCurrentPositionHigh - playerPositionHigh) <= GameConst.DIFFERENCE_IN_HEIGHT) {
                playerCanMove = true;
            }

            if ((opponentPositionHigh != GameConst.OUTSIDE_TOWER_HIGH) && (opponentPositionHigh < GameConst.MAX_TOWER_HIGH) && Math.abs(opponentCurrentPositionHigh - opponentPositionHigh) <= GameConst.DIFFERENCE_IN_HEIGHT) {
                opponentCanMove = true;
            }
        }

        if (!playerCanMove && !opponentCanMove) {
            return GameConst.GameStatus.DRAW;
        } else if (!playerCanMove) {
            return GameConst.GameStatus.LOSE;
        } else if (!opponentCanMove) {
            return GameConst.GameStatus.WIN;
        }

        return GameConst.GameStatus.PLAYING;
    }
}
