package com.smeup.rpgparser.fastmaps;

public class IntArrayMap<V> extends NumberArrayMap<Integer, V> {

    private static final long serialVersionUID = -2304239764179126L;

    public IntArrayMap(int minBound, int maxBound) {
        super(minBound, maxBound);
    }

    @Override
    protected Integer makeKeyFromInt(int k) {
        return k;
    }

}
