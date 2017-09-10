package viktor.tarasov.model;

import java.util.Collection;

import static viktor.tarasov.Utils.throwIfInvalid;

public class ProductFactory extends AbstractSuperFactory<Product> {

    public ProductFactory(String name) {
        super(name);
    }

    /**
     * @return Return previously associated with this name product prototype.
     */
    @Override
    public Product make(String name) {
        Product prototype = super.make(name);
        throwIfInvalid(prototype == null, "There is no such product in menu.");
        return prototype;
    }

    public Collection<Product> getAllProducts() {
        return getFactories().values();
    }
}
