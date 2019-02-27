package Models;

/**
 * Created by Talha on 1/24/2017.
 */
public class Rating {
    private int Id;

    private int OneStar;

    private int TwoStar;

    private int ThreeStar;

    private int FourStar;

    private int FiveStar;

    private int UserId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
