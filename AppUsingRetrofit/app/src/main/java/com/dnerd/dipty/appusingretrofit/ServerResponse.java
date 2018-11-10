package com.dnerd.dipty.appusingretrofit;

import java.io.Serializable;

public class ServerResponse implements Serializable{
    private String ip;
    private Integer ip_decimal;
    private  String country;
    private String country_iso;
    private String city;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getIp_decimal() {
        return ip_decimal;
    }

    public void setIp_decimal(Integer ip_decimal) {
        this.ip_decimal = ip_decimal;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_iso() {
        return country_iso;
    }

    public void setCountry_iso(String country_iso) {
        this.country_iso = country_iso;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
