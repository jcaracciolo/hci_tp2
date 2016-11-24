package ar.edu.itba.dreamtrip.main;

import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.interfaces.Identificable;

/**
 * Created by Pedro on 19/11/2016.
 */

public class ResultElement {
    Identificable element;
    DependencyType depType;

    public ResultElement(Identificable element, DependencyType depType) {
        this.element = element;
        this.depType = depType;
    }

    public Identificable getElement() {
        return element;
    }

    public DependencyType getDepType() {
        return depType;
    }
}
