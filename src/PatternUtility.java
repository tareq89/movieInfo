import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class PatternUtility {

    private String FILENAME;




    public String cleanFileName(String fileName){

        this.FILENAME = fileName;
        try {
            makeFileNameClean();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return this.FILENAME;
    }


    private String makeFileNameClean() throws FileNotFoundException {

        FILENAME = FILENAME.toLowerCase();
        Scanner scanner = new Scanner(new File("keyWordsToIgnore.csv"));
        scanner.useDelimiter(";");

        while (scanner.hasNext())
        {
            String wordToBeRemoved = scanner.next().toLowerCase();
            boolean removeWord = FILENAME.contains(wordToBeRemoved);


            if (removeWord){
                int index = FILENAME.indexOf(wordToBeRemoved);

                try {
                    FILENAME = FILENAME.substring(0, index-1);
                }catch (IndexOutOfBoundsException e){
                    continue;
                }
            }
        }
        scanner.close();
        FILENAME = FILENAME.replaceAll("\\.|\\s", "+");

        return FILENAME;
    }


    public String aTagCleaner(String desCription) {

        desCription = desCription.replaceAll("<a .*?</a>", "");
        return desCription;
    }


    public String findPatternInString(String toParse, String pattern, String option){
        String matchedString = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(toParse);
        while (m.find()){
            if (option.equals("genre")) {
                matchedString += m.group(1) + " ";
            }
            else{
                matchedString = m.group(1);
                break;
            }
        }
        return matchedString;
    }
}
