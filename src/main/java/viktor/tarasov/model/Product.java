package viktor.tarasov.model;

import static viktor.tarasov.Utils.notEmpty;
import static viktor.tarasov.Utils.throwIfInvalid;

public class Product implements Named {
    private String name;
    private int price;

    public Product(String name, int price) {
        notEmpty("Name", name);
        throwIfInvalid(price < 0, "Price can't be negative.");
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " " + price + " PLN";
    }
}
