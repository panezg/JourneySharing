package org.cs7cs3.team7.wifidirect;

public class DeviceDTO {
    private String deviceAddress;
    private String deviceName;
    public int status;

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public boolean equals(Object obj) {
        return deviceAddress.equals(((DeviceDTO)obj).deviceAddress);
    }
}
