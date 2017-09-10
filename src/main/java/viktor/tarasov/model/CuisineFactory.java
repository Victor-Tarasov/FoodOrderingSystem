package viktor.tarasov.model;

public class CuisineFactory extends AbstractSuperFactory<Cuisine> {
    // COSINES NAMES
    public static final String POLISH = "POLISH";
    public static final String MEXICAN = "MEXICAN";
    public static final String ITALIAN = "ITALIAN";

    // PRODUCTS TYPES
    public static final String MAIN_COURSE = "main course";
    public static final String DESERT = "desert";
    public static final String DRINK = "drink";
    public static final String ADDITIONAL_TO_DRINK = "additional to drink";

    private ProductFactory additionsToDrink = new ProductFactory(ADDITIONAL_TO_DRINK);

    public CuisineFactory(String name) {
        super(name);
        init();
    }

    protected void init() {
        initAdditionsToDrinks();
        initPolishCuisine();
        initMexicanCuisine();
        initItalianCuisine();
    }


    protected void initAdditionsToDrinks() {
        addAdditionToDrink(new Product("Ice", 0));
        addAdditionToDrink(new Product("Lemon", 1));
        make(POLISH).addFactory(additionsToDrink);
        make(MEXICAN).addFactory(additionsToDrink);
        make(ITALIAN).addFactory(additionsToDrink);
    }

    public Product addAdditionToDrink(Product product) {
        return additionsToDrink.addFactory(product);
    }

    protected void initPolishCuisine() {
        addProduct(POLISH, MAIN_COURSE, new Product("Dumplings with meat", 15));
        addProduct(POLISH, MAIN_COURSE, new Product("Dumplings with potatoes", 10));
        addProduct(POLISH, MAIN_COURSE, new Product("Bigos", 12));
        addProduct(POLISH, DESERT, new Product("Babka", 9));
        addProduct(POLISH, DESERT, new Product("Wuzetka", 17));
        addProduct(POLISH, DRINK, new Product("Kompot", 5));
        addProduct(POLISH, DRINK, new Product("Oranzada", 12));
    }

    protected void initMexicanCuisine() {
        addProduct(MEXICAN, MAIN_COURSE, new Product("Pozole", 21));
        addProduct(MEXICAN, MAIN_COURSE, new Product("Taco", 12));
        addProduct(MEXICAN, DESERT, new Product("Cajeta", 8));
        addProduct(MEXICAN, DESERT, new Product("Flan", 14));
        addProduct(MEXICAN, DRINK, new Product("Horchata", 9));
        addProduct(MEXICAN, DRINK, new Product("Aguas frescas", 7));
    }

    protected void initItalianCuisine() {
        addProduct(ITALIAN, MAIN_COURSE, new Product("Panzenella", 21));
        addProduct(ITALIAN, MAIN_COURSE, new Product("Bruschetta", 19));
        addProduct(ITALIAN, DESERT, new Product("Tiramisu", 14));
        addProduct(ITALIAN, DESERT, new Product("Panettone", 6));
        addProduct(ITALIAN, DRINK, new Product("Turin", 19));
        addProduct(ITALIAN, DRINK, new Product("Novara", 22));
    }

    public Product addProduct(String cousineName, String productType, Product product) {
        return make(cousineName).make(productType).addFactory(product);
    }

    @Override
    public Cuisine make(String name) {
        Cuisine cuisine = super.make(name);
        if (cuisine == null) {
            cuisine = new Cuisine(name);
            addFactory(cuisine);
        }
        return cuisine;
    }
}
