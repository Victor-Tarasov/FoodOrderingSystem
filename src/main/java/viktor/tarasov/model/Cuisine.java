package viktor.tarasov.model;

public class Cuisine extends AbstractSuperFactory<ProductFactory> {

    public Cuisine(String name) {
        super(name);
    }

    @Override
    public ProductFactory make(String name) {
        ProductFactory productFactory = super.make(name);
        if (productFactory == null) {
            productFactory = new ProductFactory(name);
            addFactory(productFactory);
        }
        return productFactory;
    }
}
