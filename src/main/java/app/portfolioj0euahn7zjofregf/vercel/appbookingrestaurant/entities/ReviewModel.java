package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name="reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "review_id", updatable = false, columnDefinition = "VARCHAR(50)", unique = true)
    private String reviewId;

    @Column(name = "rating", nullable = false)
    @Range(min = 0, max = 5)
    private int ratingReview;

    @Column(name = "comment", nullable = false)
    private String commentReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId", nullable = false)
    @JsonIgnore
    private RestaurantModel restaurant;


    public ReviewModel() {
    }

    public ReviewModel(String reviewId, int ratingReview, String commentReview,
                       UserModel user, RestaurantModel restaurant) {
        this.reviewId = reviewId;
        this.ratingReview = ratingReview;
        this.commentReview = commentReview;
        this.user = user;
        this.restaurant = restaurant;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public int getRatingReview() {
        return ratingReview;
    }

    public void setRatingReview(int ratingReview) {
        this.ratingReview = ratingReview;
    }

    public String getCommentReview() {
        return commentReview;
    }

    public void setCommentReview(String commentReview) {
        this.commentReview = commentReview;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "ReviewModel{" +
                "reviewId='" + reviewId + '\'' +
                ", ratingReview=" + ratingReview +
                ", commentReview='" + commentReview + '\'' +
                ", user=" + user +
                '}';
    }

}