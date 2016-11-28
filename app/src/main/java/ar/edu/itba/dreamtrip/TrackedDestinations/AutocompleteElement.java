package ar.edu.itba.dreamtrip.TrackedDestinations;

/**
 * Created by Pedro on 28/11/2016.
 */

public class AutocompleteElement {
    String id;
    String name;

    public AutocompleteElement(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
