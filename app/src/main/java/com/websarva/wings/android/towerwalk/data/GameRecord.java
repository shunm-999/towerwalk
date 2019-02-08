package com.websarva.wings.android.towerwalk.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameRecord extends ArrayList<Map<Integer, Integer>> {

    public void append(@NonNull int[] distance) {
        Map<Integer, Integer> keyMap = new HashMap<>();
        if (distance.length != 2) {
            return;
        }
        keyMap.put(distance[0], distance[1]);
        add(keyMap);
    }
}
