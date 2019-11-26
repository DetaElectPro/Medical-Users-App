package com.detatech.vitaluser.Models;

/**
 * Created by Arbab on 8/24/2019.
 */

public class Emergency2 {

    private String hospital_name;
    private String address;
    private String price;
    private String type;
    private String available;
    private String id;
    private String needing;

    private String report;
    private String image;

    public Emergency2() {

    }

    public Emergency2(String hospital_name, String address, String price, String type, String available, String report, String image, String id, String needing) {

        this.hospital_name = hospital_name;
        this.address = address;
        this.price = price;
        this.type = type;
        this.available = available;
        this.id = id;
        this.needing = needing;

        this.report = report;
        this.image = image;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }






    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeeding() {
        return needing;
    }

    public void setNeeding(String needing) {
        this.needing = needing;
    }
}