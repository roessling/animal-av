package generators.cryptography.util;

import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;

public interface KeyInterface {
     
	public void drawKey(Language lang,String baseIDRef );
	public int[]calculateWithDecryptionKey(String initial_vector);

}