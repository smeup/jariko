package com.smeup.rpgparser.fastmaps;

public class LongArrayMap<V> extends NumberArrayMap<Long, V> {

    private static final long serialVersionUID = -2304239764179127L;

    public LongArrayMap(int minBound, int maxBound) {
        super(minBound, maxBound);
    }

    @Override
    protected Long makeKeyFromInt(int k) {
        return (long) k;
    }

}
