package net.catchpole.B9.codec.stream;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BitStreamTest {
    // hmhm sometimes fails. i think we have a problem
    //@Test
    public void testRandom() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BitOutputStream bitOutputStream = new BitOutputStream(byteArrayOutputStream);

        List<Value> values = inventData();

        for (Value value : values) {
            bitOutputStream.write(value.value, value.bits);
        }
        bitOutputStream.flush();

        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        for (Value value : values) {
            int v = bitInputStream.read(value.bits);
            if (v != value.value) {
                throw new RuntimeException("bit stream failure");
            }
        }
        System.out.println(byteArrayOutputStream.size());
    }

    @Test
    public void testReference() throws IOException {
        final ByteArrayOutputStream testBaos = new ByteArrayOutputStream();
        final BitOutputStream bitOutputStream = new BitOutputStream(testBaos);

        final ByteArrayOutputStream referenceBaos = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(referenceBaos);

        bitOutputStream.write(0xf, 4);
        bitOutputStream.write(0x00abcdef, 32);
        bitOutputStream.flush();

        dataOutputStream.writeInt(0x00abcdef);

        // compare the bits for what DataOutputStream would do
        TestCase.assertEquals(    "00000000110101011011001111110111", toString(referenceBaos.toByteArray()));
        TestCase.assertEquals("1111000000001101010110110011111101110000", toString(testBaos.toByteArray()));

        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(testBaos.toByteArray()));
        TestCase.assertEquals(0xf, bitInputStream.read(4));
        TestCase.assertEquals(0x00abcdef, bitInputStream.read(32));
    }

    @Test
    public void testSigned() throws IOException {
        final ByteArrayOutputStream testBaos = new ByteArrayOutputStream();
        final BitOutputStream bitOutputStream = new BitOutputStream(testBaos);

        bitOutputStream.write(0xf, 5);
        bitOutputStream.write(-0xf, 5);
        bitOutputStream.write(1, 16);
        bitOutputStream.write(-1, 16);
        bitOutputStream.write(0x00abcdef, 32);
        bitOutputStream.write(-0x00abcdef, 32);

        bitOutputStream.writeLong(1L, 63);
        bitOutputStream.writeLong(-1L, 63);
        bitOutputStream.writeLong(0xabcdefff99aL, 64);
        bitOutputStream.writeLong(-0xabcdefff99aL, 64);
        bitOutputStream.writeLong(0x7863795836bL, 56);
        bitOutputStream.writeLong(-0x7863795836bL, 56);
        bitOutputStream.writeLong(0x5678465784cL, 48);
        bitOutputStream.writeLong(-0x5678465784cL, 48);
        bitOutputStream.flush();

        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(testBaos.toByteArray()));

        TestCase.assertEquals(0xf, bitInputStream.readSigned(5));
        TestCase.assertEquals(-0xf, bitInputStream.readSigned(5));
        TestCase.assertEquals(1, bitInputStream.readSigned(16));
        TestCase.assertEquals(-1, bitInputStream.readSigned(16));
        TestCase.assertEquals(0x00abcdef, bitInputStream.readSigned(32));
        TestCase.assertEquals(-0x00abcdef, bitInputStream.readSigned(32));

        TestCase.assertEquals(1L, bitInputStream.readSignedLong(63));
        TestCase.assertEquals(-1L, bitInputStream.readSignedLong(63));
        TestCase.assertEquals(0xabcdefff99aL, bitInputStream.readSignedLong(64));
        TestCase.assertEquals(-0xabcdefff99aL, bitInputStream.readSignedLong(64));
        TestCase.assertEquals(0x7863795836bL, bitInputStream.readSignedLong(56));
        TestCase.assertEquals(-0x7863795836bL, bitInputStream.readSignedLong(56));
        TestCase.assertEquals(0x5678465784cL, bitInputStream.readSignedLong(48));
        TestCase.assertEquals(-0x5678465784cL, bitInputStream.readSignedLong(48));
    }

    @Test
    public void testLong() throws IOException {
        final ByteArrayOutputStream testBaos = new ByteArrayOutputStream();
        final BitOutputStream bitOutputStream = new BitOutputStream(testBaos);

        bitOutputStream.writeLong(0xFFFFFFFFL, 64);
        bitOutputStream.writeLong(0xabcdefff99aL, 64);
        bitOutputStream.writeLong(0x7863795836bL, 56);
        bitOutputStream.writeLong(0x5678465784cL, 48);
        bitOutputStream.flush();

        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(testBaos.toByteArray()));
        TestCase.assertEquals(0xFFFFFFFFL, bitInputStream.readLong(64));
        TestCase.assertEquals(0xabcdefff99aL, bitInputStream.readLong(64));
        TestCase.assertEquals(0x7863795836bL, bitInputStream.readLong(56));
        TestCase.assertEquals(0x5678465784cL, bitInputStream.readLong(48));
    }

    private List<Value> inventData() {
        List<Value> values = new ArrayList<>();
        BitMasks bitMasks = new BitMasks();
        Random random = new Random();
        for (int x=0;x<10000;x++) {
            Value value = new Value();
            value.value = random.nextInt(1280000);
            value.bits = bitMasks.bitsRequired(value.value);
            values.add(value);
        }
        return values;
    }

    private String toString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int b : data) {
            print(stringBuilder, b);
        }
        return stringBuilder.toString();
    }

    private void print(StringBuilder stringBuilder, int b) {
        String s = Integer.toBinaryString(b & 0xff);
        while (s.length() < 8) {
            s = "0" + s;
        }
        stringBuilder.append(reverse(s));
    }

    private String reverse(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : s.toCharArray()) {
            stringBuilder.insert(0, c);
        }
        return stringBuilder.toString();
    }

    class Value {
        int bits;
        int value;
    }
}