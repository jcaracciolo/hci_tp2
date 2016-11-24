package ar.edu.itba.dreamtrip.common.API.dependencies;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class Dependency {
    private DependencyType dependencyType;
    private HashSet<Dependency> dependencies ;

    public DependencyType getDependencyType() {
        return dependencyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dependency that = (Dependency) o;

        return dependencyType == that.dependencyType;

    }

    @Override
    public int hashCode() {
        return dependencyType.hashCode();
    }

    private void fillPreDependencies(){
        switch (dependencyType){
            case CITIES:
                dependencies.add(new Dependency(DependencyType.COUNTRIES));
                break;
            case AIRPORTS:
                dependencies.add(new Dependency(DependencyType.CITIES));
                break;
            case FLIGHTS:
                dependencies.add(new Dependency(DependencyType.AIRPORTS));
                dependencies.add(new Dependency(DependencyType.AIRLINES));
                break;
            case AIRLINES:
                dependencies.add(new Dependency(DependencyType.AIRLINES_DATA));
                dependencies.add(new Dependency(DependencyType.AIRLINES_LOGOS));
                break;
            case AIRLINES_LOGOS:
                dependencies.add(new Dependency(DependencyType.AIRLINES_DATA));
                break;
            case TRACKED_FLIGHTS:
                if(this.getClass() != TrackedFlightsDependency.class){
                    throw new RuntimeException("You should not ask for tracked flight like this\n" +
                            "Create a new TrackedFlightsDependency(contex)");
                }
        }
    }

    public Dependency(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
        dependencies = new HashSet<>();
        fillPreDependencies();
    }

    public Dependency(DependencyType dependencyType, HashSet<Dependency> dependencies) {
        this.dependencyType = dependencyType;
        this.dependencies = dependencies;
    }

    public boolean addDependency(Dependency dependency){
        return dependencies.add(dependency);
    }

    public boolean dependenciesLoaded(HashSet<Dependency> loaded){
        if(dependencies == null || dependencies.size() == 0){
            return true;
        }
        for(Dependency dep: dependencies){
            if(!loaded.contains(dep) || !dep.dependenciesLoaded(loaded)   ){
                return false;
            }
        }
        return true;
    }

    public HashSet<Dependency> getFlattenedDependencies() {
        HashSet<Dependency> deps = new HashSet<>();
        getFlattenedDependenciesRec(deps);
        return deps;
    }

    private void getFlattenedDependenciesRec(HashSet<Dependency> deps) {
        deps.add(this);
        if(dependencies == null || dependencies.size() ==0) return ;
        for (Dependency dep : dependencies){
            dep.getFlattenedDependenciesRec(deps);
        }
    }

    @Override
    public String toString() {
        return dependencyType.toString();
    }
}

