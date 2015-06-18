import java.nio.file.Path;
import java.util.Set;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class Main {

    public static void main(String ... args){

        String movieRootDirectory = "/home/tareq.aziz/movies3/";
        String outputHtmlFilePath = movieRootDirectory + "output.html";


        DirectoryNames directoryNames = new DirectoryNames();
        Set<Path> movieFolders = directoryNames.makeDirectoryList(movieRootDirectory);


        ImdbInfoDownloadUtility imdbInfoDownloadUtility = new ImdbInfoDownloadUtility();
        Set<MovieInfo> movieInfoSet = imdbInfoDownloadUtility.getMoviesInfos(movieFolders);


        HtmlUtility htmlUtility = new HtmlUtility();
        String htmlOutput = htmlUtility.htmlOutput(movieInfoSet);
        htmlUtility.writeFile(outputHtmlFilePath, htmlOutput);
    }
}
