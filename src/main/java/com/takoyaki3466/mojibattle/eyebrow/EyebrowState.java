package com.takoyaki3466.mojibattle.eyebrow;

import jp.takoa.mojibattle.ability.support.SupportStateStore;

final class EyebrowState {
    private static final String LEVEL_KEY = "eyebrowLevel";

    private EyebrowState() {
    }

    static int level(SupportStateStore state, int maximum) {
        return Math.clamp(state.getInt(LEVEL_KEY, 0), 0, maximum);
    }

    static int add(SupportStateStore state, int amount, int maximum) {
        int value = Math.clamp(level(state, maximum) + amount, 0, maximum);
        state.putInt(LEVEL_KEY, value);
        return value;
    }

    static void halve(SupportStateStore state, int maximum) {
        state.putInt(LEVEL_KEY, level(state, maximum) / 2);
    }
}
