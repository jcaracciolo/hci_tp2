package ar.edu.itba.dreamtrip.common.model;

public class MyCurrency {
    private String symbol;
    private String name;
    private String id;
    private Double ratio;

    public MyCurrency(String id, String name, String symbol, Double ratio) {
        this.symbol = symbol;
        this.name = name;
        this.id = id;
        this.ratio = ratio;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public Double getRatio() {
        return ratio;
    }

}
