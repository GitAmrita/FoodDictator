package amrita.fooddictator.Objects;

/**
 * Created by amritachowdhury on 8/9/17.
 */

public class Restaurant {
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    private String name;
    private String address;
    private String phoneNumber;
    private String category;
    private int reviewCount;
    private float rating;

    public Restaurant(String name, String address, String phoneNumber, String category,
                      int reviewCount, float rating) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

}
