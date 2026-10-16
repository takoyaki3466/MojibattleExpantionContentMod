package com.takoyaki3466.mojibattle.tree;

public enum WoodRank {
    NONE,
    TREE,
    GROVE,
    FOREST,
    UNIVERSE;

    public static WoodRank fromTreeUnits(int treeUnits, Progression progression) {
        if (treeUnits >= progression.treesPerUniverse()) {
            return UNIVERSE;
        }
        if (treeUnits >= progression.treesPerForest()) {
            return FOREST;
        }
        if (treeUnits >= progression.treesPerGrove()) {
            return GROVE;
        }
        return treeUnits > 0 ? TREE : NONE;
    }

    public interface Progression {
        int treesPerGrove();

        int grovesPerForest();

        int forestsPerUniverse();

        default int treesPerForest() {
            return Math.multiplyExact(treesPerGrove(), grovesPerForest());
        }

        default int treesPerUniverse() {
            return Math.multiplyExact(treesPerForest(), forestsPerUniverse());
        }
    }
}
