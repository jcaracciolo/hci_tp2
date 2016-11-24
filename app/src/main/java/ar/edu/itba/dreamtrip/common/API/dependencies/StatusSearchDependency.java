package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Objects;

import ar.edu.itba.dreamtrip.common.model.StatusSearch;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class StatusSearchDependency extends Dependency {

    private StatusSearch statusSearch;

    public StatusSearchDependency(StatusSearch statusSearch) {
        super(DependencyType.FLIGHT_SEARCH);
        this.statusSearch = statusSearch;
    }

    public StatusSearch getStatusSearch() {
        return statusSearch;
    }

    @Override
    public String toString(){
        return getDependencyType() + " " + statusSearch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StatusSearchDependency that = (StatusSearchDependency) o;
        return Objects.equals(statusSearch, that.statusSearch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statusSearch);
    }
}
