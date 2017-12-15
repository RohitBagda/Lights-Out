import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Rohit Bagda on 12/14/2017.
 */
public class SpecialNumberFormatter extends NumberFormatter {

    private int characterLimit;

    public SpecialNumberFormatter(NumberFormat format, int limit){
        super(format);
        this.characterLimit = 2;
    }

    @Override
    public Object stringToValue(String str) throws ParseException{
        if (str.equals("")){
            return null;
        } if (str.length()> characterLimit){
            return Integer.parseInt(str.substring(0, characterLimit));
        }

        return Integer.parseInt(str);
    }
}
