package Models;

/**
 * Created by Sh Junaid on 12/17/2016.
 */
public class RideModel {
    private int Id;
    private String DepartureDate;
    private String DepartureTime;
    private String PostDate;
    private String Source;
    private String Destination;
    private String AvailableSeats;
    private String VehicleType;
    private String Checkpoints;
    private String CostPerKm;
    private String RideFrequency;
    private String Smoking;
    private String FoodDrinks;
    private String RideStatusId;
    private int UserId;

    public String getCheckpoints() {
        return Checkpoints;
    }

    public void setCheckpoints(String checkpoints) {
        this.Checkpoints = checkpoints;
    }

    public String getCostPerKm() {
        return CostPerKm;
    }

    public void setCostPerKm(String cost) {
        this.CostPerKm = cost;
    }

    public String getSmoking() {
        return Smoking;
    }

    public void setSmoking(String smoking) {
        this.Smoking = smoking;
    }

    public String getFoodDrinks() {
        return FoodDrinks;
    }

    public void setFoodDrinks(String food) {
        this.FoodDrinks = food;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vtype) {
        this.VehicleType = vtype;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(String date) { this.DepartureDate = date;}

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String time) {
        this.DepartureTime = time;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String pdate) {
        this.PostDate = pdate;
    }

    public String getRideFrequency() {
        return RideFrequency;
    }

    public void setRideFrequency(String rfrequency) {
        this.RideFrequency = rfrequency;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        this.Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        this.Destination = destination;
    }

    public String getAvailableSeats() {
        return AvailableSeats;
    }

    public void setAvailableSeats(String seats) {
        this.AvailableSeats = seats;
    }

    public int getUserId() { return UserId; }

    public void setUserId(int userid) {
        this.UserId = userid;
    }

    public String getRideStatusId() {
        return RideStatusId;
    }

    public void setRideStatusId(String rideStatusId) {
        RideStatusId = rideStatusId;
    }
}
