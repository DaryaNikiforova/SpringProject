package ru.tsystems.tsproject.sbb.transferObjects;

import javax.validation.constraints.*;

/**
 * Represents client entity of Train.
 * @author Daria Nikiforova
 */
public class TrainTO {

    @NotNull
    @Min(value = 0)
    @Max(value = 999999)
    private int number;

    @Pattern(regexp = "([0-9A-Za-z\\p{L}][0-9A-Za-z- \\p{L}]{0,98}[0-9A-Za-z\\p{L}])*")
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000)
    private int seatCount;

    @Min(value = 0)
    @NotNull
    private int rateId;
    private String rateName;

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
    public int getRateId() { return rateId; }
    public void setRateId(int rateId) { this.rateId = rateId; }
    public String getRateName() { return rateName; }
    public void setRateName(String rateName) { this.rateName = rateName; }
}
