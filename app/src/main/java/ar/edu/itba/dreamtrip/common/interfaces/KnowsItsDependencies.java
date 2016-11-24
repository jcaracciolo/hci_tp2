package ar.edu.itba.dreamtrip.common.interfaces;

import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;


public interface KnowsItsDependencies {
    public HashSet<Dependency> getDependencies();
}
