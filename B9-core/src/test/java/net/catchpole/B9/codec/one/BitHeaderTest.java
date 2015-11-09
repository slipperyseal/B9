package net.catchpole.B9.codec.one;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BitHeaderTest {
    @Test
    public void bitHeaderTest1() {
        List<Boolean> list = new ArrayList<Boolean>();
        for (int x=0;x<4;x++) {
            list.add(true);
        }
        test(list);
    }

    @Test
    public void bitHeaderTest2() {
        List<Boolean> list = new ArrayList<Boolean>();
        for (int x=0;x<4;x++) {
            list.add(false);
        }
        list.add(true);
        for (int x=0;x<10;x++) {
            list.add(false);
        }
        test(list);
    }

    private void test(List<Boolean> list) {
        BitHeader bitHeader1 = new BitHeader();
        for (Boolean bool : list) {
            bitHeader1.writeFlag(bool);
        }
        byte[] bytes = bitHeader1.toByteArray();
        BitHeader bitHeader2 = new BitHeader(bytes);
        for (Boolean bool : list) {
            TestCase.assertEquals((boolean) bool, bitHeader2.readFlag());
        }
    }
}
