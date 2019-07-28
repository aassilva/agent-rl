package br.ufrgs.inf.beans;

import java.io.Serializable;

public class Order implements Serializable {

    private Banda product;

    private int quantity;

    public Order(Banda product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String toString() {
        return "Quantity="+quantity;
    }

    public void setProduct(Banda product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Banda getProduct() {
        return product;
    }

}
