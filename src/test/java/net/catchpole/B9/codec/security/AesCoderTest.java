package net.catchpole.B9.codec.security;

import junit.framework.TestCase;
import org.junit.Test;

public class AesCoderTest {
    @Test
    public void testAes() throws Exception {
        AesCoder aesCoder = new AesCoder("0123456789abcdef".getBytes(), "0123456789abcdef".getBytes());
        for (int x=0;x<10;x++) {
            String value = "1234" + x;
            byte[] result = aesCoder.encrypt(value.getBytes());
            TestCase.assertEquals(16, result.length);
            TestCase.assertEquals(value, new String(aesCoder.decrypt(result)));
        }
    }
}
