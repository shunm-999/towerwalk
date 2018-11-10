package com.websarva.wings.android.towerwalk.Controller;

import com.websarva.wings.android.towerwalk.CustomView.TowerWalkBoardView.GameStatus;
import com.websarva.wings.android.towerwalk.Item.CharacterIcon;

public interface Controller {
    public abstract GameStatus judgeGame(CharacterIcon player, CharacterIcon opponent, int[][] boardTowerList);
}
