package org.cs7cs3.team7.wifidirect;

public class DeviceDTO {
    private String deviceMACAddress;
    private String deviceName;
    public int status;

    public DeviceDTO(String deviceAddress, String deviceName, int status) {
        this.deviceMACAddress = deviceAddress;
        this.deviceName = deviceName;
        this.status = status;
    }

    public String getDeviceMACAddress() {
        return deviceMACAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    /*
    public void setDeviceAddress(String deviceMACAddress) {
        this.deviceMACAddress = deviceMACAddress;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    */

    @Override
    public boolean equals(Object obj) {
        return deviceMACAddress.equals(((DeviceDTO)obj).deviceMACAddress);
        //and deviceName?
    }
}
