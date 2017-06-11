package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Backdrop;

/**
 * Created by Korisnik on 11.06.2017..
 */

public interface GalleryView {
    void showImages(List<Backdrop> backdrops);
}
