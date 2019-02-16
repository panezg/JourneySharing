package com.example.myapplication1;

public class DeviceDTO {
    private String deviceAddress;
    private String deviceName;

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
