package atlant.moviesapp.model;


public enum MovieGenre {

    ACTION(28, "Action"),
    ADVENTURE(12, "Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    FANTASY(14,"Fantasy"),
    FOREIGN(10769, "Foreign"),
    HISTORY(36, "History"),
    HORROR(27, "Horror"),
    MUSIC(10402, "Music"),
    MYSTERY(9648, "Mystery"),
    ROMANCE(10749, "Romance"),
    SCIENCE_FICTION(878, "Science Fiction"),
    TV_MOVIE(10770, "TV Movie"),
    THRILLER(53, "Thriller"),
    WAR(10752, "War"),
    WESTERN(37, "Western");

    int id;
    String name;

    MovieGenre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MovieGenre getGenreById(int id) {
        for (MovieGenre movieGenre : values()) {
            if (movieGenre.id == id) {
                return movieGenre;
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