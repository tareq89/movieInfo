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
public class ImdbInfoDownloadUtility {

    private static final String HTTP_WWW_IMDB_COM = "http://www.imdb.com";
    private static final String IMAGE_NOT_FOUND_URL = "http://www.jordans.com/~/media/Jordans%20Redesign/No-image-found.jpg";
    private String URLString;
    private static String imdbSearchUrl = HTTP_WWW_IMDB_COM + "/find?q=";
    private MovieInfo movieInfo;

    private final String FIRST_SEARCH_RESULT  = "<td class=\"result_text\"> <a href=\"(.*?)\"?>.*?</a>";
    private final String NAME  = "<h1 class=\"header\"> <span class=\"itemprop\" itemprop=\"name\">(.*?)</span>";
    private final String RATING  = "<span itemprop=\"ratingValue\">(.*?)</span>";
    private final String GENRE  = "<span class=\"itemprop\" itemprop=\"genre\">(.*?)</span>";
    private final String DESCRIPTION  = "<p itemprop=\"description\">(.*?)</p>";
    private final String IMAGE  = "<div class=\"image\">.*?src=\"(.*?)\".*?</div>";
    private final String NOT_FOUND  = "No results found for";



    public String downloadFromWeb(String urlString){

        String html = "";
        this.URLString = urlString;
        try {
            html = downloadFromWeb();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return html;
        }
    }


    private String downloadFromWeb() throws IOException {
        String s = "";
        String html = "";
        InputStream inputStream;
        BufferedInputStream bufferedInputStream;
        DataInputStream dataInputStream;

        URL url = new URL(URLString);
        inputStream = url.openStream();
        bufferedInputStream = new BufferedInputStream(inputStream);
        dataInputStream = new DataInputStream(bufferedInputStream);

        while((s = dataInputStream.readLine())!=null){
            html += s;
        }
        inputStream.close();
        bufferedInputStream.close();
        dataInputStream.close();

        return html;

    }


    private String getImdbMovieID(String toParse, String regex){

        PatternUtility patternUtility = new PatternUtility();
        String movieID = patternUtility.findPatternInString(toParse, regex, "id");
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
            moviePageHtml = "not found";

        return moviePageHtml;
    }


    public Set<MovieInfo> getMoviesInfos(Set<Path> paths){

        Set<MovieInfo> movieinfoSet = new TreeSet<MovieInfo>();
        movieInfo = new MovieInfo();
        for(Path path : paths){
            movieInfo = getMovieInfo(path);
            if (movieInfo.getMovieName()!=null){
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

        PatternUtility patternUtility = new PatternUtility();
        String movieFileName = patternUtility.cleanFileName(movieFolderPath);

        String moviePageHtml = getIMDBMoviePage(movieFileName);
        if (!moviePageHtml.equals("not found")){
            boolean movieFound = !moviePageHtml.equals("not found");

            if (movieFound){

                String movieName = patternUtility.findPatternInString(moviePageHtml, NAME, "name");
                movieInfo.setMovieName(movieName);


                String rating = patternUtility.findPatternInString(moviePageHtml, RATING, "rating");
                movieInfo.setRating(rating);


                String genre = patternUtility.findPatternInString(moviePageHtml, GENRE, "genre");
                movieInfo.setGenre(genre);


                String description = patternUtility.findPatternInString(moviePageHtml, DESCRIPTION, "description");
                description = patternUtility.aTagCleaner(description);

                if (isDescriptionAvailable(description)){
                    movieInfo.setDesCription("Description not available");
                } else{
                    movieInfo.setDesCription(description);
                }


                String imageLink = patternUtility.findPatternInString(moviePageHtml, IMAGE, "image");

                if (isImageLinkAvailable(imageLink)){
                    movieInfo.setImageLink(IMAGE_NOT_FOUND_URL);
                } else {
                    movieInfo.setImageLink(imageLink);
                }
            }
        }

        return movieInfo;
    }
    private boolean isDescriptionAvailable(String description){
        return description.trim().equals("");
    }
    private boolean isImageLinkAvailable(String imagelink){
        return imagelink.trim().equals("");
    }
}
