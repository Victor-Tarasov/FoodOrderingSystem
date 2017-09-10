package viktor.tarasov;

import viktor.tarasov.model.AbstractSuperFactory;
import viktor.tarasov.model.Cuisine;
import viktor.tarasov.model.Product;
import viktor.tarasov.model.ProductFactory;
import viktor.tarasov.view.PageModel;
import viktor.tarasov.view.PageRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static viktor.tarasov.model.CuisineFactory.*;

public class Flow {
    private static final String SHOW_CUISINES = "Cuisines";
    private static final String TO_CART = "Cart";
    private static final String TO_MENU = "Menu";
    private static final String CLOSE_ACTION = "close";

    private final AbstractSuperFactory<Cuisine> cuisineFactory;
    private final PageRenderer renderer;
    private final List<Product> cart = new ArrayList<>();

    private Cuisine cuisine;
    private ProductFactory productFactory;

    public Flow(AbstractSuperFactory<Cuisine> cuisineFactory, PageRenderer renderer) {
        this.cuisineFactory = cuisineFactory;
        this.renderer = renderer;
    }

    public void start() {
        showCuisines();
    }

    private void showCuisines() {
        List<String> names = new ArrayList<>();
        names.addAll(cuisineFactory.getAllNames());
        singleOptionChoice("Cuisines", names, this::showProductTypes);
    }

    private void showProductTypes(String cuisineName) {
        this.cuisine = cuisineFactory.make(cuisineName);
        showProductTypes();
    }

    private void showProductTypes() {
        List<String> names = new ArrayList<>();
        names.add(MAIN_COURSE);
        names.add(DESERT);
        names.add(DRINK);
        names.add(ADDITIONAL_TO_DRINK);
        names.retainAll(cuisine.getAllNames());
        singleOptionChoice("MENU\nWhat would you like to offer?", names, this::showProducts);
    }

    private void singleOptionChoice(String header, List<String> options, Consumer<String> selectionHandler) {
        PageModel pageModel = new PageModel(header, options, Arrays.asList(CLOSE_ACTION));
        pageModel.addSelectHandler(selections -> selectionHandler.accept(selections.iterator().next()));
        renderer.render(pageModel);
    }

    private void showProducts(String productType) {
        this.productFactory = cuisine.make(productType);
        List<String> names = productFactory.getAllProducts().stream().map(Product::toString).collect(Collectors.toList());
        List<String> actions = Arrays.asList(CLOSE_ACTION, SHOW_CUISINES, TO_MENU, TO_CART);
        PageModel productChoicePage
                = new PageModel(productType.toUpperCase() + "\nWhat would you like to offer?", names, actions);
        productChoicePage.setSingleSelect(false);
        productChoicePage.addSelectHandler(this::processChosenProduct);
        productChoicePage.addActionHandler(this::handleActions);
        renderer.render(productChoicePage);
    }

    private void handleActions(String action) {
        switch (action) {
            case SHOW_CUISINES:
                showCuisines();
                return;
            case TO_CART:
                showCart();
                return;
            case TO_MENU:
                showProductTypes();
                return;
        }
    }

    private void processChosenProduct(Collection<String> productsNames) {
        productsNames.stream()
                .map(productsName -> productsName.split(" [0-9]+ PLN$")[0])
                .map(productFactory::make)
                .forEach(cart::add);

        switch (productFactory.getName()) {
            case MAIN_COURSE:
                showProducts(DESERT);
                return;
            case DESERT:
                showProducts(DRINK);
                return;
            case DRINK:
                showProducts(ADDITIONAL_TO_DRINK);
                return;
            default:
                showCart();
                return;
        }
    }

    private void showCart() {
        List<String> actions = new ArrayList<>();
        String approval = "yes";
        actions.add(approval);
        actions.add("no");
        String header = describeOffer() + "\n\nWould you like to offer something else:";
        PageModel pageModel = new PageModel(header, new ArrayList<>(), actions);
        pageModel.addActionHandler(action -> {
            if (action.equals(approval)) showCuisines();
        });
        renderer.render(pageModel);
    }

    private String describeOffer() {
        String offeredProducts = cart.stream().map(Product::toString).collect(Collectors.joining("\n"));
        int totalPrice = cart.stream().mapToInt(Product::getPrice).sum();
        return "You offered:\n" + offeredProducts + "\n" + "Total price: " + totalPrice + " PLN";
    }
}
