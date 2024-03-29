package unsw.dungeon.Map;
// This file is copied from https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/

// Java program generate a random AlphaNumeric String 
// using CharSet method 

import java.util.*; 
import java.nio.charset.*; 

class RandomString { 

    public RandomString() {

    }

	public String getAlphaNumericString(int n) 
	{ 

		// length is bounded by 256 Character 
		byte[] array = new byte[256]; 
		new Random().nextBytes(array); 

		String randomString 
			= new String(array, Charset.forName("UTF-8")); 

		// Create a StringBuffer to store the result 
		StringBuffer r = new StringBuffer(); 

		// Append first 20 alphanumeric characters 
		// from the generated random String into the result 
		for (int k = 0; k < randomString.length(); k++) { 

			char ch = randomString.charAt(k); 

			if (((ch >= 'a' && ch <= 'z') 
				|| (ch >= 'A' && ch <= 'Z') 
				|| (ch >= '0' && ch <= '9')) 
				&& (n > 0)) { 

				r.append(ch); 
				n--; 
			} 
		} 

		// return the resultant string 
		return r.toString(); 
	} 

} 


