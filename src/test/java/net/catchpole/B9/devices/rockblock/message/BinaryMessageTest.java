package net.catchpole.B9.devices.rockblock.message;

import junit.framework.TestCase;
import org.junit.Test;

public class BinaryMessageTest {
    @Test
    public void testBinaryMessage() {
        BinaryMessage binaryMessage = new BinaryMessage("hello".getBytes());

        TestCase.assertEquals("68 65 6C 6C 6F 02 14", binaryMessage.toString());
    }
}
