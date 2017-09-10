package viktor.tarasov.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static viktor.tarasov.Utils.notEmpty;

abstract public class AbstractSuperFactory<T extends Named> implements SuperFactory<T>, Named {
    private Map<String, T> factories = new HashMap<>();
    private String name;

    public AbstractSuperFactory(String name) {
        this.name = name;
    }

    public T addFactory(T factory) {
        notEmpty("Name", factory.getName());
        return factories.put(factory.getName(), factory);
    }

    public Set<String> getAllNames() {
        return factories.keySet();
    }

    public T make(String name) {
        notEmpty("Name", name);
        return factories.get(name);
    }

    @Override
    public String getName() {
        return name;
    }

    protected Map<String, T> getFactories() {
        return factories;
    }
}
