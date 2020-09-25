import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String [] args) throws FileNotFoundException {

        Lexeme lexeme = new Lexeme();
        File file = new File("test.txt");
        Scanner scanner = new Scanner(file);
        String source;
        while (scanner.hasNextLine()) {
            //source +=scanner.nextLine() + " ";
            source = scanner.nextLine();
            lexeme.tokenize(source);
        }

        //lexeme.tokenize(source);

        System.out.println(lexeme.getTokens().size());

        for(int i = 0; i < lexeme.getTokens().size(); i++)
        System.out.println("( " + lexeme.getTokens().get(i).getTokenString() + ", "+ lexeme.getTokens().get(i).getTokenType() + " )");
    }
}
