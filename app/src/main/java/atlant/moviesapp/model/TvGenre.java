package atlant.moviesapp.model;

/**
 * Created by Korisnik on 13.04.2017..
 */

public enum TvGenre {

    ACTION_ADVENTURE(10759, "Action & Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    KIDS(10762, "Kids"),
    MYSTERY(9648, "Mystery"),
    NEWS(10763, "News"),
    REALITY(10764, "Reality"),
    SCIFI_FANTASY(10765, "Sci-Fi & Fantasy"),
    SOAP(10766, "Soap"),
    TALK(10767, "Talk"),
    WAR_POLITICS(10768, "War & Politics"),
    WESTERN(37, "Western");

    int id;
    String name;

    TvGenre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TvGenre getTvGenreById(int id) {
        for (TvGenre tvGenre : values()) {
            if (tvGenre.id == id) {
                return tvGenre;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}