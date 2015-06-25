import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class Main {

    public static void main(String ... args){

        String movieRootDirectory = "/home/tareq.aziz/movies/";
        String outputHtmlFilePath = movieRootDirectory + "output.html";


        Set<Path> movieFolders = FileServiceUtility.makeDirectoryList(movieRootDirectory);


        ImdbInfoDownload infoDownload = new ImdbInfoDownload();
        Set<MovieInfo> movieInfoSet = infoDownload.getMoviesInfos(movieFolders);


        String htmlOutput = HtmlUtility.htmlOutput(movieInfoSet);

        FileServiceUtility.writeFile(outputHtmlFilePath, htmlOutput);

        try {
            Desktop.getDesktop().open(new File(outputHtmlFilePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
