package com.detatech.vitaluser.Models;

/**
 * Created by Arbab on 8/7/2019.
 */

public class AdminRequests {
    private String medical_name;
    private String medical_name_specialties;
    private String address;
    private String price;
    private String start_time;
    private String end_time;
    private String request_admin_name;
    private String request_admin_phone;
    private String request_id;
    private String request_status;

    public AdminRequests() {

    }

    public AdminRequests(String medical_name, String medical_name_specialties, String address, String price, String start_time, String end_time, String request_admin_name, String request_admin_phone, String request_id, String request_status) {
        this.medical_name = medical_name;
        this.medical_name_specialties = medical_name_specialties;
        this.address = address;
        this.price = price;
        this.start_time = start_time;
        this.end_time = end_time;
        this.request_admin_name = request_admin_name;
        this.request_admin_phone = request_admin_phone;
        this.request_id = request_id;
        this.request_status = request_status;
    }

    public String getMedical_name() {
        return medical_name;
    }

    public String getMedical_name_specialties() {
        return medical_name_specialties;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getRequest_admin_name() {
        return request_admin_name;
    }

    public String getRequest_admin_phone() {
        return request_admin_phone;
    }

    public void setMedical_name(String medical_name) {
        this.medical_name = medical_name;
    }

    public void setMedical_name_specialties(String medical_name_specialties) {
        this.medical_name_specialties = medical_name_specialties;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setRequest_admin_name(String request_admin_name) {
        this.request_admin_name = request_admin_name;
    }

    public void setRequest_admin_phone(String request_admin_phone) {
        this.request_admin_phone = request_admin_phone;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

}