package kr.ewhapp.kjw.bunobuno.encryption;

import java.util.ArrayList;
import java.util.Collections;

public class Encrypt {

	public static final String table = "Aa0Bb1Cc2Dd3Ee4Ff5Gg6Hh7Ii8Jj9KkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz+/";
	
	public static String encode(String input) {

		String encodedResult="";
		String binary = "";
		ArrayList<Integer> binary8BitList = new ArrayList<Integer>();
		ArrayList<String> binary6BitList = new ArrayList<String>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		for(int i=0;i<input.length();i++) {
			for (int j=0;j<8;j++)  
				tempList.add( 1 & ( (int)input.charAt(i) ) >> j );
			
			Collections.reverse(tempList);
			binary8BitList.addAll(tempList);
			tempList.removeAll(tempList);
		}
		
		while(binary8BitList.size() % 6 != 0)
			binary8BitList.add(0);
		
		for(int i=0;i<binary8BitList.size();i++) {
			binary += binary8BitList.get(i);
			if( ( i+1 ) % 6 == 0) {
				binary6BitList.add(binary);
				binary = "";
			}
		}
		
		for(int i=0;i<binary6BitList.size();i++) 
			encodedResult += table.charAt(Integer.valueOf(binary6BitList.get(i), 2));
		
		return encodedResult;
		
	}
	
	public static String decode(String input) {
		
		String decodedResult="";
		String binary = "";
		ArrayList<String> binary8BitList = new ArrayList<String>();
		ArrayList<Integer>  binary6BitList = new ArrayList<Integer>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		for(int i=0;i<input.length();i++) {
			for (int j=0;j<6;j++)  
				tempList.add(  1 & ( table.indexOf(input.charAt(i)) >> j ) );
			
			Collections.reverse(tempList);
			binary6BitList.addAll(tempList);
			tempList.removeAll(tempList);
		}

		while(binary6BitList.size() % 8 != 0)
			binary6BitList.add(0);
//		System.out.println(binary6BitList);
		
		for(int i=0;i<binary6BitList.size();i++) {
			binary += binary6BitList.get(i);
			
			if( ( i+1 ) % 8 == 0) {
				binary8BitList.add(binary);
				binary = "";
			}
		}
		

		for(int i=0;i<binary8BitList.size();i++) {
			int asciiCode = Integer.valueOf(binary8BitList.get(i), 2);
//			System.out.println(asciiCode);
			if(asciiCode==0)
				break;
			decodedResult += Character.toString((char) asciiCode);
		}
		
		return decodedResult;
	}
}
