package com.kepler_apiweb.keplerapi.DTO;

public class ProductWithCategoryDTO {
    private int
            _id;
    private String name;
    private Double money_unit_price;
    private Integer point_unit_price;
    private String description;
    private Integer quantity;
    private Double weight;
    private String measure_unit;
    private Boolean status;
    private String image_product;
    private int
            category_id;
    private String category_name;

    public void set_id(int id) {
        this._id = id;
    }
    public int get_id() {
        return _id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setMoney_unit_price(Double moneyUnitPrice) {
        this.money_unit_price = moneyUnitPrice;
    }
    public Double getMoney_unit_price() {
        return money_unit_price;
    }

    public void setPoint_unit_price(Integer pointUnitPrice) {
        this.point_unit_price = pointUnitPrice;
    }
    public Integer getPoint_unit_price() {
        return point_unit_price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public Double getWeight() {
        return weight;
    }

    public void setMeasure_unit(String measureUnit) {
        this.measure_unit = measureUnit;
    }
    public String getMeasure_unit() {
        return measure_unit;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Boolean getStatus() {
        return status;
    }

    public void setImage_product(String imageProduct) {
        this.image_product = imageProduct;
    }
    public String getImage_product() {
        return image_product;
    }

    public void setCategory_name(String categoryName) {
        this.category_name = categoryName;
    }
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_id(int categoryId) {
        this.category_id = categoryId;
    }
    public int getCategory_id() {
        return category_id;
    }
}

