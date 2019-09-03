package generators.compression.huffman.coding;

import java.util.HashMap;

import algoanim.primitives.StringArray;

import generators.compression.huffman.guielements.EncodingTable;

public class Encoder {

	private HashMap<Character, String> encodings;
	
	public Encoder(EncodingTable encodingTable) {
		
		initEncodings(encodingTable);
	}
	
	private void initEncodings(EncodingTable encodingTable) {
		
		encodings = new HashMap<Character, String>();
		
		for (int i = 0; i < encodingTable.size(); i++) {

			StringArray encodingTableCol = encodingTable.getElement(i);
			encodings.put(encodingTableCol.getData(0).charAt(0),
					encodingTableCol.getData(1));
		}
	}
	
	public String encode(String inputString) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inputString.length(); i++) {
			sb.append(encodings.get(inputString.charAt(i)));
		}

		return sb.toString();
	}
}
