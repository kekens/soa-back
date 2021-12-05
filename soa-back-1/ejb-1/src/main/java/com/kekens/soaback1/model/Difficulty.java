package com.kekens.soaback1.model;

import java.io.Serializable;

public enum Difficulty implements Serializable {
    VERY_EASY(0),
    NORMAL(1),
    IMPOSSIBLE(2);

    public final int value;

    Difficulty(int value) {
        this.value = value;
    }
}
