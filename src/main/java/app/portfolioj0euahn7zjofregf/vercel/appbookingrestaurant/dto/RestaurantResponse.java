package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto;

import java.util.List;

public class RestaurantResponse {

    private List<RestaurantDTO> contents;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean isLast;

    public RestaurantResponse() {
    }

    public RestaurantResponse(List<RestaurantDTO> contents, long totalElements) {
        this.contents = contents;
        this.totalElements = totalElements;
    }

    public List<RestaurantDTO> getContents() {
        return contents;
    }

    public void setContents(List<RestaurantDTO> contents) {
        this.contents = contents;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
