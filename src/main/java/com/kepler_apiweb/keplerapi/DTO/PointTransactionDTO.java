package com.kepler_apiweb.keplerapi.DTO;

public class PointTransactionDTO {
    private int
            _id;
    private AdquisitionDTO
            adquisition;
    private int
            quantity_point;
    private String
            action;

    public void set_id(int id) {
        this._id =
                id;
    }

    public int get_id() {
        return _id;
    }

    public void setQuantity_point(int quantityPoint) {
        this.quantity_point =
                quantityPoint;
    }

    public int getQuantity_point() {
        return quantity_point;
    }

    public void setAction(String action) {
        this.action =
                action;
    }

    public String getAction() {
        return action;
    }

    public void setAdquisition(AdquisitionDTO adquisition) {
        this.adquisition =
                adquisition;
    }

    public AdquisitionDTO getAdquisition() {
        return adquisition;
    }
}
