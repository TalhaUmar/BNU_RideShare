package Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Talha on 12/13/2016.
 */
public class RegexCheck {

    public static boolean StdEmpIdCheck(String s){
        Pattern pattern = Pattern.compile("([s|S|]|[F|f])[0-9]{4}[-]\\d{3}");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean UserNameCheck(String s){
        Pattern pattern = Pattern.compile("[a-zA-Z ]{3,25}[a-zA-Z]");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean PhoneCheck(String s){
        Pattern pattern = Pattern.compile("[0][3]\\d{9}");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()){
            return true;
        }
        return false;
    }

}
