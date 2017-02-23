package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.Review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReviewListDto {
    private List<ReviewDto> reviews = new ArrayList<>();

    private int total;

    public ReviewListDto() {}

    public ReviewListDto(Collection<Review> reviews) {
        for (Review r : reviews){
            this.reviews.add(new ReviewDto(r));
        }
    }

    public int getTotal() {
        return reviews.size();
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}