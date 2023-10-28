package com.kepler_apiweb.keplerapi.DTO;
// SOLUCIÓN A PREGUNTA 3
public class ProductToStockDTO {
    private int _id;
    private String name;
    private Integer quantity;
    private int category_id;
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

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Integer getQuantity() {
        return quantity;
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
