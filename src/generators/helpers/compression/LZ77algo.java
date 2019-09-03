package generators.helpers.compression;

public class LZ77algo {


	
	public static String compress(String[] text) {
		String window = "";
		String buffer = "";
		String result = "";
		// initialize window and buffer
		for (int i=0;i<text.length;i++) {
			buffer += text[i];
		}
		
		while (buffer.length() != 0) {
			String tmp = "" + buffer.charAt(0);
			int cnt = 0;
			
			while (cnt < buffer.length()-1 && window.contains(tmp)) {
				cnt++;
				if (!window.contains(tmp + buffer.charAt(cnt))) break;
				tmp += "" + buffer.charAt(cnt);
			}
			
			if (!window.contains(tmp)) {
				result += "(0,0," + tmp + ") ";
				
				// shift
				window += buffer.substring(0,1);
				buffer = buffer.substring(1,buffer.length());
			}
			else {
				if (cnt+1 < buffer.length()) {
					result += "(" + (window.length()-window.indexOf(tmp)-1) + "," + tmp.length() + "," + buffer.charAt(cnt) + ") ";
					
					// shift
					window += buffer.substring(0,tmp.length() + 1);
					buffer = buffer.substring(tmp.length() + 1,buffer.length());
				}
				else {
					result += "(" + (window.length()-window.indexOf(tmp)-1) + "," + tmp.length() + "," + "EOF)";
					break;
				}
			}
		}
		return result;
	}
}
