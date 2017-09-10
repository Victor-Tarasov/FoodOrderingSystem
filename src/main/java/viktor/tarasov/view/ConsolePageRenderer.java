package viktor.tarasov.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static viktor.tarasov.Utils.throwIfInvalid;

public class ConsolePageRenderer implements PageRenderer {
    private static final int DEFAULT_PAGE_SIZE = 150;
    private static final String SELECTION_EXPLANATION = "You may select options only using numbers separated by commas.";
    private String skipPageString;
    private PageModel pageModel;
    private List<String> options;

    public ConsolePageRenderer() {
        this(DEFAULT_PAGE_SIZE);
    }

    public ConsolePageRenderer(int pageSize) {
        char[] chars = new char[pageSize];
        Arrays.fill(chars, '\n');
        this.skipPageString = new String(chars);
    }

    @Override
    public void render(PageModel pageModel) {
        this.pageModel = pageModel;
        this.options = new ArrayList<>();
        options.addAll(pageModel.getOptions());
        options.addAll(pageModel.getActions());
        drawThePage();
        processInput();
    }

    private void drawThePage() {
        cleanPage();
        System.out.println(pageModel.getHeader() + "\n\n");
        if (!pageModel.getOptions().isEmpty()) {
            System.out.println("Options:");
        }
        for (int i = 0; i < options.size(); i++) {
            if (i == pageModel.getOptions().size()) {
                System.out.println("\nActions:");
            }
            System.out.println((i + 1) + ") " + options.get(i));
        }

        System.out.println("\n" + SELECTION_EXPLANATION + "\n");
    }

    private void cleanPage() {
        System.out.print(skipPageString);
    }

    private void errorNotification(Exception e) {
        cleanPage();
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            message = e.getClass().getName();
        }

        try {
            Thread.sleep(100);
            System.err.println(message);
            Thread.sleep(message.length() * 100);
        } catch (InterruptedException e1) {}
    }

    private void processInput() {
        try {
            processUserInput(new BufferedReader(new InputStreamReader(System.in)).readLine());
        } catch (Exception e) {
            errorNotification(e);
            render(pageModel);
        }
    }

    private void processUserInput(String userInput) {
        throwIfInvalid(userInput.isEmpty(), "You need to choose at last one option.");

        Set<Integer> selections = Arrays.stream(userInput.split(" *, *"))
                .filter(s -> !s.isEmpty())
                .map(this::convertToIndex)
                .distinct()
                .collect(Collectors.toSet());
        throwIfInvalid(selections.isEmpty(), "You need to choose at last one option.");

        if (selections.stream().noneMatch(this::isAction)) {
            processOptionSelection(selections);
            return;
        }

        processActionSelection(selections);
    }

    private void processOptionSelection(Set<Integer> selections) {
        throwIfInvalid(pageModel.isSingleSelect() && selections.size() != 1,
                "You can't chose more than one option.");
        Set<String> selectedOptions = selections.stream()
                .map(integer -> --integer)
                .map(pageModel.getOptions()::get)
                .collect(Collectors.toSet());
        pageModel.publishSelection(selectedOptions);
    }

    private void processActionSelection(Set<Integer> selections) {
        boolean containsOptions = selections.stream().anyMatch(selection -> !isAction(selection));
        throwIfInvalid(containsOptions, "You need chose from choices or actions. " +
                "You can't use an option that is not in one of this lists or option from both of them.");
        throwIfInvalid(selections.size() != 1, "You can't select more than one action.");
        pageModel.publishAction(options.get(selections.iterator().next() - 1));
    }

    private boolean isAction(Integer selection) {
        return selection > pageModel.getOptions().size();
    }

    private Integer convertToIndex(String token) {
        throwIfInvalid(!token.matches("[\\-0-9]+"), SELECTION_EXPLANATION);
        Integer selection = Integer.valueOf(token);
        boolean isNotInRange = selection > options.size() || selection <= 0;
        throwIfInvalid(isNotInRange, "There is no option under number " + selection);
        return selection;
    }
}
