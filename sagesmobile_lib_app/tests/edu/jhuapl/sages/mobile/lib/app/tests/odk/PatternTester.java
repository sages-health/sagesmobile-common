package edu.jhuapl.sages.mobile.lib.app.tests.odk;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pokuam1
 * @created May 31, 2012
 */
public class PatternTester {

	public static void main (String[] args){
	
      	Pattern pattern = Pattern.compile("([\\P{InBasic Latin}]+)");
    	Matcher matcher = pattern.matcher("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    	System.out.println("did it match? " + matcher.find());
    	Matcher matcher2 = pattern.matcher("alexrñnñ© ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    	System.out.println("did it (alexrñnñ© ABCDEFGHIJKLMNOPQRSTUVWXYZ) match? " + matcher2.find());
    	Matcher matcher3 = pattern.matcher("alexㅇㄱㄷ  ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    	System.out.println("did it(alexㅇㄱㄷ  ABCDEFGHIJKLMNOPQRSTUVWXYZ) match? " + matcher3.find());
	}
}
