package com.takoyaki3466.mojibattle.config;

import jp.takoa.mojibattle.ability.ConfigView;
import jp.takoa.mojibattle.resource.ResourceId;

public final class ConfigValues {
    private ConfigValues() {
    }

    public static float positiveFloat(ConfigView config, String key, float fallback) {
        float value = config.floatValue(key, fallback);
        if (!Float.isFinite(value) || value <= 0f) {
            throw new IllegalArgumentException(key + " must be finite and positive");
        }
        return value;
    }

    public static float nonNegativeFloat(ConfigView config, String key, float fallback) {
        float value = config.floatValue(key, fallback);
        if (!Float.isFinite(value) || value < 0f) {
            throw new IllegalArgumentException(key + " must be finite and non-negative");
        }
        return value;
    }

    public static int positiveInt(ConfigView config, String key, int fallback) {
        int value = config.intValue(key, fallback);
        if (value <= 0) {
            throw new IllegalArgumentException(key + " must be positive");
        }
        return value;
    }

    public static ResourceId resourceId(ConfigView config, String key, String fallback) {
        return ResourceId.parse(config.stringValue(key, fallback));
    }
}
