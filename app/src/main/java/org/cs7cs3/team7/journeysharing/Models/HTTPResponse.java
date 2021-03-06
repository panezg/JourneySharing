package org.cs7cs3.team7.journeysharing.Models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class HTTPResponse {
    @Expose
    private String status;
    @Expose
    private JsonElement data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
