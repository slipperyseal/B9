package net.catchpole.B9.codec.stream;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class BitMasksTest {
    @Test
    public void testBitsRequired() {
        BitMasks bitMasks = new BitMasks();
        TestCase.assertEquals(0, bitMasks.bitsRequired(0));
        TestCase.assertEquals(1, bitMasks.bitsRequired(1));
        TestCase.assertEquals(8, bitMasks.bitsRequired(0xf0));
        TestCase.assertEquals(32, bitMasks.bitsRequired(0x80000000));
    }

    @Test
    public void testMasks() {
        BitMasks bitMasks = new BitMasks();
        int[] masks = bitMasks.createBitMasks(9);
        Assert.assertArrayEquals(new int[] { 0x00,0x01,0x03,0x07,0x0f,0x1f,0x3f,0x7f,0xff }, masks);
    }

    @Test
    public void testMasksLong() {
        BitMasks bitMasks = new BitMasks();
        long[] masks = bitMasks.createBitMasksLong(9);
        Assert.assertArrayEquals(new long[] { 0x00,0x01,0x03,0x07,0x0f,0x1f,0x3f,0x7f,0xff }, masks);
    }
}
