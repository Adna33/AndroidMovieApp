package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TvShowsResponse{
        @SerializedName("page")
        private int page;

        @SerializedName("results")
        private List<TvShow> results;

        @SerializedName("total_results")
        private int totalResults;

        @SerializedName("total_pages")
        private int totalPages;

        public TvShowsResponse(int page, List<TvShow> results, int totalResults, int totalPages) {
            this.page = page;
            this.results = results;
            this.totalResults = totalResults;
            this.totalPages = totalPages;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<TvShow> getResults() {
            return results;
        }

        public void setResults(List<TvShow> results) {
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
