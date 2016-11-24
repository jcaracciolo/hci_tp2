package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Objects;

/**
 * Created by Julian Benitez on 11/23/2016.
 */

public class AirlinesReviewDependency extends Dependency {
    private String airlineID;

    public String getAirlineID() {
        return airlineID;
    }

    public AirlinesReviewDependency(String airlineID) {
        super(DependencyType.AIRLINE_REVIEWS);
        this.airlineID = airlineID.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AirlinesReviewDependency that = (AirlinesReviewDependency) o;
        return Objects.equals(airlineID, that.airlineID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), airlineID);
    }
}
