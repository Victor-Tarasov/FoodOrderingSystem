package viktor.tarasov;

import viktor.tarasov.model.CuisineFactory;
import viktor.tarasov.view.ConsolePageRenderer;

public class Main {

    public static void main(String[] args) {
        new Flow(new CuisineFactory("Console restaurant"), new ConsolePageRenderer()).start();
    }
}
