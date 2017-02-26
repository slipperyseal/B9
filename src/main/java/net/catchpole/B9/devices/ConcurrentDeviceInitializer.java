package net.catchpole.B9.devices;

import net.catchpole.B9.lang.Throw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConcurrentDeviceInitializer<T extends Device> {
    private List<Device> deviceList = new ArrayList<>();

    public ConcurrentDeviceInitializer() {
    }

    public ConcurrentDeviceInitializer(Collection<T> collection) {
        for (T device : collection) {
            addDevice(device);
        }
    }

    public void addDevice(T device) {
        deviceList.add(device);
    }

    public void initialize() throws Exception {
        List<DeviceThread> threadList = new ArrayList<>();

        for (Device device : deviceList) {
            threadList.add(new DeviceThread(device));
        }

        for (DeviceThread deviceThread : threadList) {
            deviceThread.start();
        }

        for (DeviceThread deviceThread : threadList) {
            try {
                deviceThread.join();
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
        }

        for (DeviceThread deviceThread : threadList) {
            if (deviceThread.getThrowable() != null) {
                throw Throw.unchecked(deviceThread.getThrowable());
            }
        }
    }

    class DeviceThread extends Thread {
        private Device device;
        private Throwable throwable;

        public DeviceThread(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            try {
                this.device.initialize();
            } catch (Throwable t) {
                this.throwable = t;
            }
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
