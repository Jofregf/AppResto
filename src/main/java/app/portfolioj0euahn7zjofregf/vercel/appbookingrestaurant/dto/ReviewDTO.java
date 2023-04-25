package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import jakarta.validation.constraints.*;

public class ReviewDTO {

    private String reviewId;

    @NotNull()
    @Min(1)
    @Max(5)
    private int ratingReview;

    @NotEmpty
    @Size(min = 10, message = "You must enter a comment of at least 10 characters")
    private String commentReview;

    private String userName;

    public ReviewDTO() {
    }

    public ReviewDTO(String reviewId, int ratingReview, String commentReview, String userName) {
        this.reviewId = reviewId;
        this.ratingReview = ratingReview;
        this.commentReview = commentReview;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "reviewId='" + reviewId + '\'' +
                ", ratingReview=" + ratingReview +
                ", commentReview='" + commentReview + '\'' +
                '}';
    }


}
