/**
 * 
 */
package edu.jhuapl.sages.mobile.lib.app.tests.odk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pokuam1
 * @created Jun 3, 2012
 */
public class PatternUTF8Tester {

	public static void main(String[] args){
		System.out.println("The Pattern: ([\\\\P{InBasic Latin}]+)");
		
      	Pattern pattern = Pattern.compile("([\\P{InBasic Latin}]+)");
    	Matcher matcher = pattern.matcher("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    	System.out.println("did it match? " + matcher.find());
    	Matcher matcher2 = pattern.matcher("alexrñnñ© ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    	System.out.println("did it (alexrñnñ© ABCDEFGHIJKLMNOPQRSTUVWXYZ) match? " + matcher2.find());
    	Matcher matcher3 = pattern.matcher("alexㅇㄱㄷ  ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    	System.out.println("did it(alexㅇㄱㄷ  ABCDEFGHIJKLMNOPQRSTUVWXYZ) match? " + matcher3.find());
    	Matcher matcher4 = pattern.matcher("ㅇㄱㄷ  ㅇㄱㄷ  ㅇㄱㄷ  ㅇㄱㄷ  ABCDEFGHIJKLMNOPQRSTUVWXYZ");    	
    	System.out.println("did it(ㅇㄱㄷ  ㅇㄱㄷ  ㅇㄱㄷ  ㅇㄱㄷ  ABCDEFGHIJKLMNOPQRSTUVWXYZ) match? " + matcher4.find());
    	
	}
}
