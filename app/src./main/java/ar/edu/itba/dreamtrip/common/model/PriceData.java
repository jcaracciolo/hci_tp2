package ar.edu.itba.dreamtrip.common.model;


/**
 * Created by Julian Benitez on 11/13/2016.
 */

public class PriceData {
    private Integer infantsAmount = 0;
    private Integer childrenAmount = 0;
    private Integer adultsAmount = 0;

    private Double infantBaseFare = 0.0;
    private Double childBaseFare = 0.0;
    private Double adultBaseFare = 0.0;

    private Double totalBaseFare = 0.0;
    private Double totalCharges = 0.0;
    private Double totalTaxes = 0.0;
    private Double totalPrice = 0.0;

    public PriceData(Double infantBaseFare, Double adultBaseFare,Double childBaseFare,
                     Double totalTaxes, Double totalCharges){
        this.totalTaxes = totalTaxes;
        this.totalCharges = totalCharges;
        this.adultBaseFare = adultBaseFare;
        this.childBaseFare = childBaseFare;
        this.infantBaseFare = infantBaseFare;
        this.adultsAmount = 1;
        this.childrenAmount = 0;
        this.infantsAmount = 0;
        totalBaseFare = (childrenAmount * childBaseFare)+(adultsAmount * adultBaseFare)+(infantBaseFare * infantsAmount);
        totalPrice = totalCharges + totalTaxes + totalBaseFare ;

    }

    public PriceData(Integer infantsAmount, Integer childrenAmount,Integer adultsAmount,
                     Double infantBaseFare, Double adultBaseFare,Double childBaseFare,
                     Double totalTaxes, Double totalCharges) {
        this.totalTaxes = totalTaxes;
        this.totalCharges = totalCharges;
        this.adultBaseFare = adultBaseFare;
        this.childBaseFare = childBaseFare;
        this.infantBaseFare = infantBaseFare;
        this.adultsAmount = adultsAmount;
        this.childrenAmount = childrenAmount;
        this.infantsAmount = infantsAmount;
        totalBaseFare = (childrenAmount * childBaseFare)+(adultsAmount * adultBaseFare)+(infantBaseFare * infantsAmount);
        totalPrice = totalCharges + totalTaxes + totalBaseFare ;
    }

    public Integer getInfantsAmount() {
        return infantsAmount;
    }

    public Integer getChildrenAmount() {
        return childrenAmount;
    }

    public Integer getAdultsAmount() {
        return adultsAmount;
    }

    public Double getInfantBaseFare() {
        return infantBaseFare;
    }

    public Double getChildBaseFare() {
        return childBaseFare;
    }

    public Double getAdultBaseFare() {
        return adultBaseFare;
    }

    public Double getTotalBaseFare() {
        return totalBaseFare;
    }

    public Double getTotalCharges() {
        return totalCharges;
    }

    public Double getTotalTaxes() {
        return totalTaxes;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }
}
