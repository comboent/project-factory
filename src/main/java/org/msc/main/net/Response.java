package org.msc.main.net;

import org.msc.main.util.JsonUtils;

import java.io.Serializable;

public class Response implements Serializable {
    private int state;
    private String tip;
    private String data;

    public Response(){}

    public Response(int state, String tip) {
        this.state = state;
        this.tip = tip;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Response data(Object data) {
        this.data = JsonUtils.toJson(data);
        return this;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
