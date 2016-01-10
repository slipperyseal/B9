package net.catchpole.B9.codec.stream;

public class BitMasks {
    public int bitsRequired(int value) {
        if (value == 0) {
            return 0;
        }
        int bits = 32;
        while ((value & 0x80000000) == 0) {
            bits--;
            value <<= 1;
        }
        return bits;
    }

    public int[] createBitMasks(int total) {
        int mask = 0;
        int[] masks = new int[total];
        for (int x=0;x<total;x++) {
            masks[x] = mask;
            mask <<= 1;
            mask |= 1;
        }
        return masks;
    }

    public long[] createBitMasksLong(int total) {
        long mask = 0;
        long[] masks = new long[total];
        for (int x=0;x<total;x++) {
            masks[x] = mask;
            mask <<= 1;
            mask |= 1;
        }
        return masks;
    }
}
