/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budgetbuddy.domain;


/**
 *
 * @author markus
 */
public class TypePrice implements Comparable<TypePrice> {
    private String type;
    private int price;

    public TypePrice(String type, int price) {
        this.type = type;
        this.price = price;
    }
    
    public void addPrice(int add) {
        price += add;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int compareTo(TypePrice comp) {
        int difference = this.price - comp.price;
        if (difference == 0) {
            return this.type.compareTo(comp.type);
        }
        return difference;
    }
}
