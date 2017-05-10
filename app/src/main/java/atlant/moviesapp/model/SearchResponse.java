package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Korisnik on 21.04.2017..
 */

public class SearchResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<SearchResult> results;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
