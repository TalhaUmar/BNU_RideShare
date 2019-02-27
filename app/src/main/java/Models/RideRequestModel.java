package Models;

/**
 * Created by Talha on 1/13/2017.
 */
public class RideRequestModel {

    private int Id;

    private int RideId;

    private int PassengerId;

    private String PickupLocation;

    private String Destination;

    private String RequestStatus;

    private int TotalAmount;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRideId() {
        return RideId;
    }

    public void setRideId(int rideId) {
        RideId = rideId;
    }

    public int getPassengerId() {
        return PassengerId;
    }

    public void setPassengerId(int passengerId) {
        PassengerId = passengerId;
    }

    public String getPickupLocation() {
        return PickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        PickupLocation = pickupLocation;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }
}

