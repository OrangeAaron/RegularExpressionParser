
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author khalil, awilson40, jcanida
 */
public class RegularExpParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //** Creating a scanner for user's input
        Scanner scanner = new Scanner(System.in);

        //* Getting user's input
        System.out.println("Enter regular expression: ");
        String regularExpression = scanner.nextLine();

        System.out.println("Enter maximum accepted string length: ");
        int length = scanner.nextInt();
        scanner.nextLine();

        Parser parser = new Parser();
        String postfixRegEx = parser.ParseRegExString(regularExpression);
        //System.out.println(postfixRegEx);
        NFA nfa = new NFA();
        nfa.generate(postfixRegEx);
        Set<String> stringSet = new HashSet();
        stringSet = GraphTraverser.TraverseGraph(nfa, length);

        if (stringSet != null && !stringSet.isEmpty()) {
            System.out.println("Printing result");
            System.out.println(stringSet);
            //Test.TestRegex(regularExpression.replaceAll("\\s+", ""), stringSet, length, false);
        }else{
        System.out.println("There are no accepted strings.");
        }
    }
}
