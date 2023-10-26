package com.kepler_apiweb.keplerapi.DTO;

import java.util.Date;
import java.util.List;

public class MainAdquisitionCompleteDTO {
    private int
            _id;
    private Double
            money_total_value;
    private Integer
            point_total_value;
    private Date
            creation_date;
    private Date
            delivery_date;
    private int
            user_id;
    private String
            first_name;
    private String
            last_name;
    private String
            identification;
    private String
            status;
    private List<AdquisitionDetailsDTO>
            adquisition_details;

    public void set_id(int id) {
        this._id = id;
    }

    public int get_id() {
        return _id;
    }

    public void setMoney_total_value(Double moneyTotalValue) {
        this.money_total_value =
                moneyTotalValue;
    }

    public Double getMoney_total_value() {
        return money_total_value;
    }

    public void setPoint_total_value(Integer pointTotalValue) {
        this.point_total_value =
                pointTotalValue;
    }

    public Integer getPoint_total_value() {
        return point_total_value;
    }

    public void setCreation_date(Date creationDate) {
        this.creation_date =
                creationDate;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setDelivery_date(Date deliveryDate) {
        this.delivery_date =
                deliveryDate;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setUser_id(int userId) {
        this.user_id =
                userId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setFirst_name(String firstName) {
        this.first_name =
                firstName;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setLast_name(String lastName) {
        this.last_name =
                lastName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setIdentification(String identification) {
        this.identification =
                identification;
    }

    public String getIdentification() {
        return identification;
    }

    public void setStatus(String status) {
        this.status =
                status;
    }

    public String getStatus() {
        return status;
    }

    public void setAdquisition_details(List<AdquisitionDetailsDTO> adquisitionDetails) {
        this.adquisition_details =
                adquisitionDetails;
    }

    public List<AdquisitionDetailsDTO> getAdquisition_details() {
        return adquisition_details;
    }
}
