package com.smeup.rpgparser.fastmaps;

public class ByteArrayMap<V> extends NumberArrayMap<Byte, V> {

    private static final long serialVersionUID = -2304239764179124L;

    public ByteArrayMap() {
        this(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public ByteArrayMap(byte minBound, byte maxBound) {
        super(minBound, maxBound);
    }

    @Override
    protected Byte makeKeyFromInt(int k) {
        return (byte) k;
    }

}
