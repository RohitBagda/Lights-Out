import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
/**
 * The Special number formatter is used to convert the textfield input to an Integer and only return the 1st two
 * digits of the input.
 * Created by Katya Kelly, Chukwubueze Hosea Ogeleka, and Rohit Bagda on 12/14/2017.
 */
public class SpecialNumberFormatter extends NumberFormatter {

    private int characterLimit;

    public SpecialNumberFormatter(NumberFormat format, int limit){
        super(format);
        this.characterLimit = limit;
    }

    /**
     * Converts textfield input to an Integer Value of only two digits.
     * @param str textfield Input
     * @return Input in the form of an Integer less than 100.
     * @throws ParseException
     */
    @Override
    public Object stringToValue(String str) throws ParseException{

        if (str.equals("")){
            return null;
        }

        for(int i=0;i<str.length();i++){
            if(!Character.isDigit(str.charAt(i))){
                return null;
            }
        }

        if (str.length()> characterLimit){
            return Integer.parseInt(str.substring(0, characterLimit));
        }
        return Integer.parseInt(str);
    }
}
