package net.catchpole.B9.devices.rockblock.webservice;

import java.io.IOException;

public class MTMessageSenderTest {
    //@Test
    public void test() throws IOException {
        MTMessageSender mtMessageSender = new MTMessageSender();

        MTMessage mtMessage = new MTMessage();
        mtMessage.setImei("<add>");
        mtMessage.setUsername("<add>");
        mtMessage.setPassword("<add>");
        mtMessage.setData("test message".getBytes());

        System.out.println(mtMessageSender.send(mtMessage));
    }
}
