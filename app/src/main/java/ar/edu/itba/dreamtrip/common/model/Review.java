package ar.edu.itba.dreamtrip.common.model;

import java.util.Objects;

/**
 * Created by Julian Benitez on 11/23/2016.
 */

public class Review {
    private String airlineID;
    private Integer flightNumber;

    private Integer overall;
    private Integer friendliness;
    private Integer food;
    private Integer punctuality;
    private Integer milageProgram;
    private Integer confort;
    private Integer qualityPriceRatio;

    private Boolean recommended;
    private String comment;

    public Review(String airlineID, Integer flightNumber, Integer overall, Integer friendliness,
                  Integer food, Integer punctuality, Integer milageProgram, Integer confort,
                  Integer qualityPriceRatio,Boolean recommended, String comment) {
        this.airlineID = airlineID;
        this.flightNumber = flightNumber;
        this.overall = minimax(overall,1,10);
        this.friendliness = minimax(friendliness,1,10);
        this.food = minimax(food,1,10);
        this.punctuality = minimax(punctuality,1,10);
        this.milageProgram = minimax(milageProgram,1,10);
        this.confort = minimax(confort,1,10);
        this.qualityPriceRatio = minimax(qualityPriceRatio,1,10);
        this.recommended = recommended;
        if(comment != null)
            this.comment = comment.length()> 256? comment.substring(0,255):comment;
    }

    private Integer minimax(Integer value, Integer min, Integer max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

    public String getAirlineID() {
        return airlineID;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

    public Integer getOverall() {
        return overall;
    }

    public Integer getFriendliness() {
        return friendliness;
    }

    public Integer getFood() {
        return food;
    }

    public Integer getPunctuality() {
        return punctuality;
    }

    public Integer getMilageProgram() {
        return milageProgram;
    }

    public Integer getConfort() {
        return confort;
    }

    public Integer getQualityPriceRatio() {
        return qualityPriceRatio;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public String getComment() {
        return comment;
    }

    public String getIdentifier(){
        return airlineID + " " + flightNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(airlineID, review.airlineID) &&
                Objects.equals(flightNumber, review.flightNumber) &&
                Objects.equals(overall, review.overall) &&
                Objects.equals(friendliness, review.friendliness) &&
                Objects.equals(food, review.food) &&
                Objects.equals(punctuality, review.punctuality) &&
                Objects.equals(milageProgram, review.milageProgram) &&
                Objects.equals(confort, review.confort) &&
                Objects.equals(qualityPriceRatio, review.qualityPriceRatio) &&
                Objects.equals(recommended, review.recommended) &&
                Objects.equals(comment, review.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airlineID, flightNumber, overall, friendliness, food,
                punctuality, milageProgram, confort, qualityPriceRatio, recommended, comment);
    }
}
