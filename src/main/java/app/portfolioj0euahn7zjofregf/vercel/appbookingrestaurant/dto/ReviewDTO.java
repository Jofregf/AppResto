package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;



public class ReviewDTO {

    private String reviewId;

    private int ratingReview;

    private String commentReview;

    public ReviewDTO() {
    }

    public ReviewDTO(String reviewId, int ratingReview, String commentReview, String userId, String restaurantId) {
        this.reviewId = reviewId;
        this.ratingReview = ratingReview;
        this.commentReview = commentReview;
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

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "reviewId='" + reviewId + '\'' +
                ", ratingReview=" + ratingReview +
                ", commentReview='" + commentReview + '\'' +
                '}';
    }


}
