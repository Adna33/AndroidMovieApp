package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.SearchResult;

/**
 * Created by Korisnik on 21.04.2017..
 */

public interface SearchView {
    void DisplayResults(List<SearchResult> data);
}
