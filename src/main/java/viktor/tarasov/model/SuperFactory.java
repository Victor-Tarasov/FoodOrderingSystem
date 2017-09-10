package viktor.tarasov.model;

import java.util.Set;

public interface SuperFactory<T extends Named> {

    /**
     * @return previous factory associated with this name.
     **/
    T addFactory(T factory);

    Set<String> getAllNames();

    T make(String name);
}
