package com.kepler_apiweb.keplerapi.DTO;

public class AdquisitionDetailsDTO {

    private int
            product_id;
    private String
            product_name;
    private Double
            money_unit_value;
    private Integer
            point_unit_value;
    private int
            quantity;
    private String
            description;

    public void setProduct_id(int productId) {
        this.product_id =
                productId;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_name(String productName) {
        this.product_name =
                productName;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setMoney_unit_value(Double moneyUnitValue) {
        this.money_unit_value =
                moneyUnitValue;
    }

    public Double getMoney_unit_value() {
        return money_unit_value;
    }

    public void setPoint_unit_value(Integer pointUnitValue) {
        this.point_unit_value =
                pointUnitValue;
    }

    public Integer getPoint_unit_value() {
        return point_unit_value;
    }

    public void setQuantity(int quantity) {
        this.quantity =
                quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDescription(String description) {
        this.description =
                description;
    }

    public String getDescription() {
        return description;
    }
}
