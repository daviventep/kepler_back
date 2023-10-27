package com.kepler_apiweb.keplerapi.DTO;

public class AdquisitionDTO {
    private int
            main_adquisition_id;
    private int
            user_id;
    private String
            first_name;
    private String
            last_name;
    private int
            identification;

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

    public void setIdentification(int identification) {
        this.identification =
                identification;
    }

    public int getIdentification() {
        return identification;
    }

    public void setMain_adquisition_id(int mainAdquisitionId) {
        this.main_adquisition_id =
                mainAdquisitionId;
    }

    public int getMain_adquisition_id() {
        return main_adquisition_id;
    }
}