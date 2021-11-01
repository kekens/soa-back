package com.kekens.soa_back_2.model;

public enum Difficulty {
    VERY_EASY(0),
    NORMAL(1),
    IMPOSSIBLE(2);

    public final int value;

    Difficulty(int value) {
        this.value = value;
    }

    public static Difficulty valueOf(int i) {
        switch (i) {
            case 0:
                return Difficulty.VERY_EASY;
            case 1:
                return Difficulty.NORMAL;
            case 2:
                return Difficulty.IMPOSSIBLE;
            default:
                return null;
        }
    }
}
