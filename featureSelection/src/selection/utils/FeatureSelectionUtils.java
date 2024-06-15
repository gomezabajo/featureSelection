package selection.utils;

import java.util.Locale;
import java.util.Set;


/**
 * Utils to add two double numbers without losing accuracy
 * and to pretty print a feature set
 */
public class FeatureSelectionUtils {

	public static double sumDouble(double value1, double value2) {
		Locale.setDefault(new Locale ("en", "US"));
	    double sum = 0.0;
	    String value1Str = Double.toString(value1);
	    int decimalIndex = value1Str.indexOf(".");
	    int value1Precision = 0;
	    if (decimalIndex != -1) {
	        value1Precision = (value1Str.length() - 1) - decimalIndex;
	    }

	    String value2Str = Double.toString(value2);
	    decimalIndex = value2Str.indexOf(".");
	    int value2Precision = 0;
	    if (decimalIndex != -1) {
	        value2Precision = (value2Str.length() - 1) - decimalIndex;
	    }

	    int maxPrecision = value1Precision > value2Precision ? value1Precision : value2Precision;
	    sum = value1 + value2;
	    String s = String.format("%." + maxPrecision + "f", sum);
	    sum = Double.parseDouble(s);
	    return sum;
	}
	
	public static String featureSet2String(Set<Integer> features) {
		String print = "[";
		for (int v : features) {
			print += "x" + (v + 1) + ", ";
		}
		if (print.length() > 1) {
			print = print.substring(0, print.length() - 2);
		}
		print += "]";
		return print;
	}
}
