package net.catchpole.B9.devices.rockblock.webservice;

public class MOMessagePrinter implements MOMessageHandler {
    @Override
    public void handle(MOMessage moMessage) {
        System.out.println(moMessage);
    }
}
