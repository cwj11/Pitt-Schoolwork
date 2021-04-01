//////////////////////////////////////////////////////////////////////
///
/// Contents: Test LCS program.
/// Author:   John Aronis
/// Date:     April 2013
///
//////////////////////////////////////////////////////////////////////

public class TestLCS {

  public static void main(String[] args) {
    String word1, word2 ;
    word1 = "axxbyyycz" ;
    word2 = "1a2b333c44444" ;
    word1 = randomString(1500);
    word2 = randomString(1500);
    System.out.println("LCS of " + word1 + " and " + word2 + ": ") ;
    System.out.println( LCS.findLCS(word1,word2) ) ;
  }

  
  public static String randomString(int length) {
	  String letters = "qwertyuiopasdfghjklzxcvbnm1234567890";
	  String ans = "";
	  for(int i = 0; i < length; i++) {
		  int rand = (int) (Math.random() * 36);
		  ans += letters.charAt(rand);
	  }
	  return ans;
  }
}

/// End-of-File
