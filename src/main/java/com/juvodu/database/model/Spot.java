package com.juvodu.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juvodu.database.converter.ContinentTypeConverter;
import com.juvodu.database.converter.DateTypeConverter;
import com.juvodu.forecast.model.Forecast;

import java.util.Date;

/**
 * Model representing the complete Spot table
 *
 * @author Juvodu
 */
@DynamoDBTable(tableName = "spot")
public class Spot extends BaseSpot{

    @DynamoDBAttribute
    private String description;

    @DynamoDBAttribute
    private String image;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ContinentTypeConverter.class)
    private Continent continent;

    @JsonIgnore // no need to return the geohash as JSON, its for internal use only
    @DynamoDBAttribute
    private String geohash;

    @DynamoDBAttribute
    private String walk;

    @DynamoDBAttribute
    private String waveQuality;

    @DynamoDBAttribute
    private String experience;

    @DynamoDBAttribute
    private String frequency;

    @DynamoDBAttribute
    private String type;

    @DynamoDBAttribute
    private String direction;

    @DynamoDBAttribute
    private String bottom;

    @DynamoDBAttribute
    private String power;

    @DynamoDBAttribute
    private String normalDayLength;

    @DynamoDBAttribute
    private String goodDayLength;

    @DynamoDBAttribute
    private String goodSwellDirection;

    @DynamoDBAttribute
    private String goodWindDirection;

    @DynamoDBAttribute
    private String swellSize;

    @DynamoDBAttribute
    private String bestTidePosition;

    @DynamoDBAttribute
    private String bestTideMovement;

    @DynamoDBAttribute
    private String weekCrowd;

    @DynamoDBAttribute
    private String weekEndCrowd;

    @JsonIgnore
    @DynamoDBAttribute // last time cron updated spot
    @DynamoDBTypeConverted(converter = DateTypeConverter.class)
    private Date cronDate;

    @DynamoDBIgnore // weather information will be requested on demand
    private Forecast forecast;

    @DynamoDBAttribute
    private String topicArn;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWalk() {
        return walk;
    }

    public void setWalk(String walk) {
        this.walk = walk;
    }

    public String getWaveQuality() {
        return waveQuality;
    }

    public void setWaveQuality(String waveQuality) {
        this.waveQuality = waveQuality;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getNormalDayLength() {
        return normalDayLength;
    }

    public void setNormalDayLength(String normalDayLength) {
        this.normalDayLength = normalDayLength;
    }

    public String getGoodDayLength() {
        return goodDayLength;
    }

    public void setGoodDayLength(String goodDayLength) {
        this.goodDayLength = goodDayLength;
    }

    public String getGoodSwellDirection() {
        return goodSwellDirection;
    }

    public void setGoodSwellDirection(String goodSwellDirection) {
        this.goodSwellDirection = goodSwellDirection;
    }

    public String getGoodWindDirection() {
        return goodWindDirection;
    }

    public void setGoodWindDirection(String goodWindDirection) {
        this.goodWindDirection = goodWindDirection;
    }

    public String getSwellSize() {
        return swellSize;
    }

    public void setSwellSize(String swellSize) {
        this.swellSize = swellSize;
    }

    public String getBestTidePosition() {
        return bestTidePosition;
    }

    public void setBestTidePosition(String bestTidePosition) {
        this.bestTidePosition = bestTidePosition;
    }

    public String getBestTideMovement() {
        return bestTideMovement;
    }

    public void setBestTideMovement(String bestTideMovement) {
        this.bestTideMovement = bestTideMovement;
    }

    public String getWeekEndCrowd() {
        return weekEndCrowd;
    }

    public void setWeekEndCrowd(String weekEndCrowd) {
        this.weekEndCrowd = weekEndCrowd;
    }

    public String getWeekCrowd() {
        return weekCrowd;
    }

    public void setWeekCrowd(String weekCrowd) {
        this.weekCrowd = weekCrowd;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public Date getCronDate() {
        return cronDate;
    }

    public void setCronDate(Date cronDate) {
        this.cronDate = cronDate;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }
}
