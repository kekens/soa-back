package com.kekens.soa_lab_1.model;

public enum Difficulty {
    VERY_EASY(0),
    NORMAL(1),
    IMPOSSIBLE(2);

    public final int value;

    Difficulty(int value) {
        this.value = value;
    }
}
