import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Rohit Bagda on 12/14/2017.
 */
public class SpecialNumberFormatter extends NumberFormatter {

    public SpecialNumberFormatter(NumberFormat format){
        super(format);
    }

    @Override
    public Object stringToValue(String str) throws ParseException{
        if (str.equals("")){
            return null;
        }

        return Integer.parseInt(str);
    }
}
