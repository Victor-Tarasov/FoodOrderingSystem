package viktor.tarasov.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static viktor.tarasov.Utils.notEmpty;

public class PageModel {
    private final String header;
    private final List<String> options;
    private final List<String> actions;
    private List<Consumer<Collection<String>>> selectHandlers = new ArrayList<>();
    private List<Consumer<String>> actionHandlers = new ArrayList<>();
    private boolean singleSelect = true;

    public PageModel(String header, List<String> options, List<String> actions) {
        this.options = nullToEmpty(options);
        this.actions = nullToEmpty(actions);
        options.forEach(option -> notEmpty("Option", option));
        actions.forEach(action -> notEmpty("Action", action));
        this.header = header;
    }

    private List<String> nullToEmpty(List<String> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void addActionHandler(Consumer<String> actionHandler) {
        actionHandlers.add(actionHandler);
    }

    public void addSelectHandler(Consumer<Collection<String>> selectHandler) {
        selectHandlers.add(selectHandler);
    }

    void publishAction(String action) {
        actionHandlers.forEach(stringConsumer -> stringConsumer.accept(action));
    }

    void publishSelection(Set<String> selections) {
        selectHandlers.forEach(stringConsumer -> stringConsumer.accept(selections));
    }

    public String getHeader() {
        return header;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getActions() {
        return actions;
    }

    public boolean isSingleSelect() {
        return singleSelect;
    }

    public void setSingleSelect(boolean singleSelect) {
        this.singleSelect = singleSelect;
    }
}
