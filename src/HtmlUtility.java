import java.io.*;
import java.util.Collection;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class HtmlUtility {


    public final static String UPPER_HTML = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<style>\n" +
            "table, th, td {\n" +
            "    border: 1px solid black;\n" +
            "    border-collapse: collapse;\n" +
            "}\n" +
            "th, td {\n" +
            "    padding: 5px;\n" +
            "    text-align: left;\n" +
            "}\n" +
            "#left{" +
            "float: left}"+
            "#right{" +
            "float: right}"+
            "</style>"+
            "</head>\n" +
            "<body>\n" +
            "\t<center><h1>My Movie Collection</h1></center>"+
            "\n" +
            "\t<table>\n" +
            "\t\t<tr>\n" +
            "\t\t\t<th>Name</th>\n" +
            "\t\t\t<th>Genre</th>\n" +
            "\t\t\t<th>Rating</th>\n" +
            "\t\t\t<th>Description</th>\n" +
            "\t\t</tr>";


    public final static String LOWER_HTML = "\t</table>\n" +"</body>\n" +"</html>";





    public static String htmlOutput(Collection<MovieInfo> movieInfoCollection){

        String htmlOutput = UPPER_HTML;

        for(MovieInfo movieInfo : movieInfoCollection){


            htmlOutput += "\t<tr>";
            htmlOutput += "\t\t<td>";

            htmlOutput += "<img height=\"113\" width=\"76\" src=\"" + movieInfo.getImageLink()+ "\">" ;
            htmlOutput += "<h2>"+ movieInfo.getMovieName() + "</h2>";

            htmlOutput += "<div>";
            htmlOutput += "<a target=\"_blank\" id=\"left\" href=\""+ movieInfo.getDiskPath() + "\"> Disk Location</a>";
            htmlOutput += "<a target=\"_blank\" id=\"right\" href=\""+ movieInfo.getImdbLink() + "\"> IMDB</a>";
            htmlOutput += "</div>";

            htmlOutput += "</td>";

            htmlOutput += "\t\t<td>" + movieInfo.getGenre() + "</td>\n";
            htmlOutput += "\t\t<td>" + movieInfo.getRating() + "</td>\n";
            htmlOutput += "\t\t<td>" + movieInfo.getDescription() + "</td>\n";
            htmlOutput += "\t</tr>";

        }
        htmlOutput += LOWER_HTML;

        return htmlOutput;
    }


}
