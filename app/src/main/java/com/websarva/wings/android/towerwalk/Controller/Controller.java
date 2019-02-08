package com.websarva.wings.android.towerwalk.controller;

import com.websarva.wings.android.towerwalk.consts.GameConst.GameStatus;
import com.websarva.wings.android.towerwalk.item.CharacterIcon;

public interface Controller {
    GameStatus judgeGame(CharacterIcon player, CharacterIcon opponent, int[][] boardTowerList);
}
