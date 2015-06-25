import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class ImdbInfoDownload {

    private String URLString;
    private MovieInfo movieInfo;
    private static final String HTTP_WWW_IMDB_COM = "http://www.imdb.com";
    private final String IMAGE_NOT_FOUND_URL = "http://www.jordans.com/~/media/Jordans%20Redesign/No-image-found.jpg";
    private String imdbSearchUrl = HTTP_WWW_IMDB_COM + "/find?q=";

    private final String FIRST_SEARCH_RESULT  = "<td class=\"result_text\"> <a href=\"(.*?)\"?>.*?</a>";
    private final String NOT_FOUND  = "No results found for";


    private final String NAME  = "<h1 class=\"header\"> <span class=\"itemprop\" itemprop=\"name\">(.*?)</span>";
    private final String RATING  = "<span itemprop=\"ratingValue\">(.*?)</span>";
    private final String GENRE  = "<span class=\"itemprop\" itemprop=\"genre\">(.*?)</span>";
    private final String DESCRIPTION  = "<p itemprop=\"description\">(.*?)</p>";
    private final String IMAGE  = "<div class=\"image\">.*?src=\"(.*?)\".*?</div>";





    private String downloadFromWeb(String urlString){

        String html = "";
        this.URLString = urlString;
        try {
            html = downloadFromWeb();
        } catch (MalformedURLException e) {
            System.out.println("mall formed URL  : " + this.URLString);
        } catch (IOException e) {
            System.out.println("URL not found : " + this.URLString);
        } finally {
            return html;
        }
    }





    private String downloadFromWeb() throws IOException {
        String s = "";
        String html = "";


        URL url = new URL(URLString);
        InputStream inputStream = url.openStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

        while((s = dataInputStream.readLine())!=null){
            html += s;
        }
        inputStream.close();
        bufferedInputStream.close();
        dataInputStream.close();

        return html;

    }





    private String getImdbMovieID(String toParse, String regex){

        String movieID = PatternUtility.findPatternInString(toParse, regex, "id");
        movieID = movieID.replace("\"", "");
        return movieID;
    }





    private String getIMDBMoviePage(String movieName){

        String movieSearchUrl = imdbSearchUrl + movieName;
        String imdbSearchResultHtml = downloadFromWeb(movieSearchUrl);


        String imdbMovieID = getImdbMovieID(imdbSearchResultHtml, FIRST_SEARCH_RESULT);
        String imdbMoviePageUrl = HTTP_WWW_IMDB_COM + imdbMovieID;


        movieInfo.setImdbLink(imdbMoviePageUrl);
        String moviePageHtml = downloadFromWeb(imdbMoviePageUrl);

        if (imdbSearchResultHtml.contains(NOT_FOUND))
            moviePageHtml = NOT_FOUND;

        return moviePageHtml;
    }





    public Set<MovieInfo> getMoviesInfos(Set<Path> paths){

        Set<MovieInfo> movieinfoSet = new TreeSet<MovieInfo>();
        for(Path path : paths){
            movieInfo = getMovieInfo(path);
            if (!movieInfo.getMovieName().equals("")){
                movieinfoSet.add(movieInfo);
            }
        }

        return movieinfoSet;
    }





    private MovieInfo getMovieInfo(Path path){

        String movieRootDir = path.getFileName().toString();
        MovieInfo movieInfo = getMovieInfo(movieRootDir);
        movieInfo.setDiskPath(path.getParent().toString());

        return movieInfo;
    }





    private MovieInfo getMovieInfo(String movieFolderPath){

        movieInfo = new MovieInfo();

        if (movieFolderPath.contains("anch")){
            System.out.println(movieFolderPath);
        }
        String movieFileName = PatternUtility.cleanFileName(movieFolderPath);
        System.out.println("MovieFile Name : " + movieFileName);


        String moviePageHtml = getIMDBMoviePage(movieFileName);

        if (!moviePageHtml.equals("not found")){
            boolean movieFound = !moviePageHtml.equals("not found");

            if (movieFound){
                populateMovieInfo(moviePageHtml);
            }
        }

        return movieInfo;
    }


    private void populateMovieInfo(String moviePageHtml){

        String movieName = PatternUtility.findPatternInString(moviePageHtml, NAME, "name");
        movieInfo.setMovieName(movieName);
        System.out.println("Movie Name     : " + movieName);
        System.out.println("\n\n\n");


        String rating = PatternUtility.findPatternInString(moviePageHtml, RATING, "rating");
        movieInfo.setRating(rating);


        String genre = PatternUtility.findPatternInString(moviePageHtml, GENRE, "genre");
        movieInfo.setGenre(genre);


        String description = PatternUtility.findPatternInString(moviePageHtml, DESCRIPTION, "description");
        description = PatternUtility.aTagCleaner(description);

        if (isDescriptionAvailable(description)){
            movieInfo.setDesCription("Description not available");
        } else{
            movieInfo.setDesCription(description);
        }


        String imageLink = PatternUtility.findPatternInString(moviePageHtml, IMAGE, "image");

        if (isImageLinkAvailable(imageLink)){
            movieInfo.setImageLink(IMAGE_NOT_FOUND_URL);
        } else {
            movieInfo.setImageLink(imageLink);
        }
    }




    private boolean isDescriptionAvailable(String description){

        boolean descriptionNotAvailable = description.trim().equals("");
        return descriptionNotAvailable;
    }



    private boolean isImageLinkAvailable(String imagelink){

        boolean imageNotAvailable = imagelink.trim().equals("");
        return imageNotAvailable;
    }
}
