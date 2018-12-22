package budgetbuddy.domain;

import budgetbuddy.Helpper;
import java.util.Date;
import java.util.Objects;


public class Item /*implements Comparable<Item>*/ {
    private String name;
    private String type;
    private Date date;
    private int price; //induvidual price
    private int amount;
    private int id;

    public Item(String name, String type, Date date, int price, int amount, int id) {
        this.name = name;
        this.type = type.trim().toLowerCase();
        this.date = date;
        this.price = price;
        this.amount = amount;
        this.id = id;
    }

    public Item(String name, String type, Date date, int price, int amount) {
        this(name, type, date, price, amount, -1);
    }
    

    public String getName() {
        return name;
    }

    public String getType() {
        return type.toLowerCase().trim();
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }
     
    

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.date);
        hash = 59 * hash + this.price;
        hash = 59 * hash + this.amount;
        hash = 59 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + ": " + id + ", '" + type + "', " 
                + Helpper.dateToYearMonthDay(date) + ", "
                + (double) price / 100 + "€, " + amount;
    }

//    @Override
//    public int compareTo(Item item) {
//        if (this.date.before(item.date)) {
//            return -1;
//        } else if (this.date.after(item.date)) {
//            return 1;
//        }
//        if (this.id < item.id) {
//            return -1;
//        } else if (this.id > item.id) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }
}
