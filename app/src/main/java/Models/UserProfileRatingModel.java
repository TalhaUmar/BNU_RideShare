package Models;

/**
 * Created by Talha on 1/18/2017.
 */
public class UserProfileRatingModel {
    private int Id;
    private String Student_EmployeeId;
    private String UserName;
    private String Phone;
    private int PhoneStatus;
    private int OneStar;
    private int TwoStar;
    private int ThreeStar;
    private int FourStar;
    private int FiveStar;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getStudent_EmployeeId() {
        return Student_EmployeeId;
    }

    public void setStudent_EmployeeId(String student_EmployeeId) {
        Student_EmployeeId = student_EmployeeId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getPhoneStatus() {
        return PhoneStatus;
    }

    public void setPhoneStatus(int phoneStatus) {
        PhoneStatus = phoneStatus;
    }

    public int getOneStar() {
        return OneStar;
    }

    public void setOneStar(int oneStar) {
        OneStar = oneStar;
    }

    public int getTwoStar() {
        return TwoStar;
    }

    public void setTwoStar(int twoStar) {
        TwoStar = twoStar;
    }

    public int getThreeStar() {
        return ThreeStar;
    }

    public void setThreeStar(int threeStar) {
        ThreeStar = threeStar;
    }

    public int getFourStar() {
        return FourStar;
    }

    public void setFourStar(int fourStar) {
        FourStar = fourStar;
    }

    public int getFiveStar() {
        return FiveStar;
    }

    public void setFiveStar(int fiveStar) {
        FiveStar = fiveStar;
    }
}
