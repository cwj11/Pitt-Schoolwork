
public class LCS {

	private static String[][] memo;
	
	public static String findLCS(String s1, String s2) {
		memo = new String[s1.length()][s2.length()];
		for(int i = 0; i < memo.length; i++) {
			for(int j = 0; j < memo[0].length; j++) {
				memo[i][j] = null;
			}
		}
		return findLCS(s1, s2, s1.length() - 1, s2.length() - 1);
	}
	
	private static String findLCS(String s1, String s2, int c1, int c2) {
		if(c1 < 0 || c2 < 0)
			return "";
		if(memo[c1][c2] != null)
			return memo[c1][c2];
		if(s1.charAt(c1) == s2.charAt(c2))
			return findLCS(s1, s2, c1-1, c2-1) + s1.charAt(c1);
		String str1 = findLCS(s1, s2, c1-1, c2);
		String str2 = findLCS(s1, s2, c1, c2-1);
		if(str1.length() > str2.length()) {
			memo[c1][c2] = str1;
			return str1;
		}
		memo[c1][c2] = str2;
		return str2;
	}
}