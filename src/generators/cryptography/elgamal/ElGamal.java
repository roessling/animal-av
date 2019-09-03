package generators.cryptography.elgamal;

import java.awt.Font;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;

/**
 * 
 * @author letzkus
 *
 */
public class ElGamal implements Generator {
	
	// ElGamal-Variablen
	private SecureRandom __random = new SecureRandom();
	
	private BigInteger a; // Private Key
	private BigInteger b;
	private BigInteger A; // Public Key
	private BigInteger B;
	private BigInteger k;
	
	private boolean aSet = false;
	private boolean bSet = false;
	private boolean BSet = false;
	private boolean kSet = false;
	
	public class CipherText<T> {
		public T B,c;
		public CipherText(T B, T c) {
			this.B = B;
			this.c = c;
		}
	}
	
	public class SignText<T> {
		public T r,s;
		public String hm;
		public SignText(T r, T s, String hm) {
			this.r = r;
			this.s = s;
			this.hm = hm;
		}
	}
	
	// Konfigurierbar
	private int bitLength = 128;
	private BigInteger p,g; // Public Key
	

	//------------------------------------------------------------------------------------------------
	// ElGamal-Funktionen
	//------------------------------------------------------------------------------------------------
	
	public void initializeCryptoSystem() {
		l.newText(contentRoot, "Im Ersten Teil wird das Cryptosystem initialisiert.", "init1", null);
		l.newText(new Offset(0,25, "init1", defaultFontDirection), "Das heisst, es wird das Schlüsselpaar mit den zugehörigen Werten generiert.", "init2", null);
		l.newText(new Offset(0,25, "init2", defaultFontDirection), "Im ersten Schritt wählen wir den privaten Schlüssel a zwischen 2 und p-2 zufällig und gleichverteilt", "init3", null);
		
		// Schritt 1: Wähle zufälliges a zwischen 2 und p-2, welches Private Key darstellt
		if (!aSet) {
		  a = new BigInteger(bitLength, __random);
		  while(a.compareTo(new BigInteger("2"))==-1 || a.compareTo(p.subtract(new BigInteger("2")))==1)
			a = new BigInteger(bitLength, __random);
		}
		
		
    CheckpointUtils.checkpointEvent(this, "aEvent", new Variable ("a", a));
		
		l.newText(new Offset(codeIndentation,25, "init3", defaultFontDirection), "Privater Schlüssel: "+a, "init4", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		
		// Schritt 2: Berechne A = g^a mod b und damit den Public Key aus dem Private Key
		l.newText(new Offset(-codeIndentation,25, "init4", defaultFontDirection), "im zweiten Schritt berechnen wir aus dem privaten Schlüssel a den öffentlichen Schlüssel (p,g,A) mit A=g^a mod p", "init5", null);
		l.nextStep();
		A = g.modPow(a, p);
		
		CheckpointUtils.checkpointEvent(this, "AEvent", new Variable ("A", A));
		
		l.newText(new Offset(codeIndentation,25, "init5", defaultFontDirection), "Öffentlicher Schlüssel: (p="+p+",g="+g+",A="+g+"^"+a+" mod "+p+"="+A+")", "init6", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);

	}

	public CipherText<String> encrypt(String m) {
		l.newText(contentRoot, "In diesem Schritt soll der angegebene Text mittels dem ElGamal-Verfahren verschlüsselt werden.", "encrypt1", null);
		l.newText(new Offset(0,25, "encrypt1", defaultFontDirection), "Die Verschlüsselung erzeugt ein Paar (B,c), von dem beide Werte auch zum Entschlüsseln vorhanden sein müssen.", "encrypt2", null);
		l.newText(new Offset(0,25, "encrypt2", defaultFontDirection), "Im ersten Schritt wird ein zufälliges b zwischen 2 und p-2 gewählt.", "encrypt3", null);
		l.nextStep();
		BigInteger message = new BigInteger(m);
		
		// Schritt 1: Wähle zufälliges 2 < b < p-2
		if (!bSet) {
		  BigInteger b = new BigInteger("0");
		  while(b.compareTo(new BigInteger("2"))==-1 || b.compareTo(p.subtract(new BigInteger("2")))==1)
			b = new BigInteger(bitLength, __random);
		}
		
		CheckpointUtils.checkpointEvent(this, "bEvent", new Variable ("b", b));
		
		l.newText(new Offset(codeIndentation,25, "encrypt3", defaultFontDirection), "b="+b, "encrypt4", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);;
		l.nextStep();
		l.newText(new Offset(-codeIndentation,25, "encrypt4", defaultFontDirection), "Im zweiten Schritt berechnen wir den ersten Teil des CipherTextes: B=g^b mod p", "encrypt5", null);
		l.nextStep();
		
		// Schritt 2: Berechne B = g^b mod p
		if (!BSet) {
		  B = g.modPow(b, p);
		}
		
    CheckpointUtils.checkpointEvent(this, "BEvent", new Variable ("B", B));
		
    l.newText(new Offset(codeIndentation,25, "encrypt5", defaultFontDirection), "B="+g+"^"+b+" mod "+p+"="+B, "encrypt6", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		l.newText(new Offset(-codeIndentation,25, "encrypt6", defaultFontDirection), "Im letzten Schritt erzeugen wir den Ciphertext: c=(A^b)*m mod p", "encrypt7", null);
		l.nextStep();
		
		
		// Schritt 3: Berechne c = A^b*m mod p
		BigInteger c = message.multiply(A.modPow(b, p)).mod(p);

    CheckpointUtils.checkpointEvent(this, "cEvent", new Variable ("c", c));
		
		l.newText(new Offset(codeIndentation,25, "encrypt7", defaultFontDirection), "c="+A+"^"+b+"*"+message+" mod "+p+"="+c, "encrypt8", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		l.newText(new Offset(-codeIndentation,25, "encrypt8", defaultFontDirection), "Mit Hilfe dieser Formeln erhalten wir nun das Chiffretext-Paar ("+B+","+c+").", "encrypt9", null);
		return new CipherText<String>(B.toString(), c.toString());
		
	}
	
	public String decrypt(CipherText<String> d) {
		l.newText(contentRoot, "In diesem Schritt soll aus dem Chiffretext-Paar ("+new String(d.B)+","+new String(d.c)+") der Orginaltext berechnet werden.", "decrypt1", null);
		l.newText(new Offset(0,25, "decrypt1", defaultFontDirection), "Die Entschlüsselung erzeugt den Orginaltext m mit der folgenden Formel: m=B^(p-1-a)*c mod p", "decrypt2", null);
		BigInteger cipherText_B = new BigInteger(d.B);
		BigInteger cipherText_c = new BigInteger(d.c);
		l.newText(new Offset(0,25, "decrypt2", defaultFontDirection), "Im ersten Schritt wird der Exponent x=p-1-a berechnet, um die darauffolgende Rechnung zu vereinfachen.", "decrypt3", null);
		l.nextStep();
		
		// Schritt 1: Berechne Exponenten x = p-1-a
		BigInteger x = p.subtract(new BigInteger("1")).subtract(a);

    CheckpointUtils.checkpointEvent(this, "xEvent", new Variable ("x", x));
		
		l.newText(new Offset(codeIndentation,25, "decrypt3", defaultFontDirection), "x="+p+"-1-"+a+"="+x, "decrypt4", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		
		// Schritt 2: Berechne Plaintext
		l.newText(new Offset(-codeIndentation,25, "decrypt4", defaultFontDirection), "Im zweiten Schritt führen wir den Chiffretext auf den Orginaltext zurück: m=(B^x)*c mod p", "decrypt5", null);
		l.nextStep();
		BigInteger m = cipherText_B.modPow(x, p).multiply(cipherText_c).mod(p);
		
    CheckpointUtils.checkpointEvent(this, "mEvent", new Variable ("m", m));
		
		l.newText(new Offset(codeIndentation,25, "decrypt5", defaultFontDirection), "m="+cipherText_B+"^"+x+"*"+cipherText_c+" mod "+p+"="+m, "decrypt6", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		
		return m.toString();
	}
	
	public SignText<String> sign(String m, String mdInstance) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(mdInstance);
		} catch (NoSuchAlgorithmException e) {
			md = null;
		}
		
		l.newText(contentRoot, "In diesem Schritt soll der angegebene Text noch zusätzlich signiert werden.", "sign1", null);
		l.newText(new Offset(0,25, "sign1", defaultFontDirection), "Zur Signaturerzeugung wird eine Hashfunktion benötigt. Wurde keine Hashfunktion angegeben,", "sign2", null);
		l.newText(new Offset(0,25, "sign2", defaultFontDirection), "so wird die Nachricht selbst in die Berechnung mit einbezogen. Dadurch wird jedoch ein", "sign3", null);
		l.newText(new Offset(0,25, "sign3", defaultFontDirection), "Angriff möglich, mit dem sich eine exeistenzielle Fälschung herstellen lässt!", "sign4", null);
		l.nextStep();
		
		// Schritt 1: Wähle 0<k<p-1 und gcd(k,p-1)=1
		l.newText(new Offset(0,25, "sign4", defaultFontDirection), "Die Signaturerzeugung nutzt den privaten Schlüssel derjenigen Person, die die Nachricht signiert.", "sign5", null);
		l.newText(new Offset(0,25, "sign5", defaultFontDirection), "Im ersten Schritt wird ein k zufällig und gleichverteilt zwischen 0 und p-1 mit gcd(k, p-1)=1 gewählt.", "sign6", null);		
		if (!kSet) {
		  k = new BigInteger(bitLength, __random);
		  while(k.compareTo(new BigInteger("1"))==-1 || a.compareTo(p.subtract(new BigInteger("2")))==1 || !k.gcd(p.subtract(new BigInteger("1"))).equals(new BigInteger("1")))
			k = new BigInteger(bitLength, __random);
		}
		

    CheckpointUtils.checkpointEvent(this, "kEvent", new Variable ("k", k));
		
		l.newText(new Offset(codeIndentation,25, "sign6", defaultFontDirection), "k="+k, "sign7", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);	
		l.nextStep();
		
		// Schritt 2: Berechne r = g^k mod p		
		l.newText(new Offset(-codeIndentation,25, "sign7", defaultFontDirection), "Im zweiten Schritt wird der erste Teil r=g^k mod p berechnet:", "sign8", null);		
		BigInteger r = g.modPow(k, p);		

    CheckpointUtils.checkpointEvent(this, "rEvent", new Variable ("r", r));
    
		l.newText(new Offset(codeIndentation,25, "sign8", defaultFontDirection), "r="+g+"^"+k+" mod "+p+"="+r, "sign9", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);		
		l.nextStep();

		// Schritt 3: Berechne H(m) für Beispiel md5
		// Bei Fehler wird H(m)=m angenommen, z.B. für Angriffsbeispiele
		l.newText(new Offset(-codeIndentation,25, "sign9", defaultFontDirection), "Im dritten Schritt wird der Hashwert der Nachricht berechnet.", "sign10", null);		
		l.newText(new Offset(0,25, "sign10", defaultFontDirection), "Die Wahl des Hashalgorithmuses ist essentiell für die Authenzität der Signatur!", "sign11", null);		
		BigInteger hm;
		if(md!=null) {
			md.update(m.toString().getBytes(),0,m.length());
			hm = new BigInteger(1,md.digest()).mod(p.subtract(new BigInteger("1")));
		}else{
			// l.newText(Vorsicht: Unsicher, da keiner oder falscher MD angegeben!)
			hm = new BigInteger(m).mod(p.subtract(new BigInteger("1")));
		}	
    l.newText(new Offset(codeIndentation,25, "sign11", defaultFontDirection), "h("+m+")="+hm, "sign12", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);		
		l.nextStep();
		
		// Schritt 4: s = (H(m)-ar)*k^(-1) mod (p-1)
		l.newText(new Offset(-codeIndentation,25, "sign12", defaultFontDirection), "Im letzten Schritt wird der zweite Teil der Signatur berechnet.", "sign13", null);		
		l.newText(new Offset(0,25, "sign13", defaultFontDirection), "Die Formel dazu lautet: s = (H(m)-ar)*k^(-1) mod (p-1)", "sign14", null);		
		BigInteger s = hm.subtract(a.multiply(r)).multiply(k.modPow(new BigInteger("-1"), p.subtract(new BigInteger("1")))).mod(p.subtract(new BigInteger("1")));
		
		CheckpointUtils.checkpointEvent(this, "sEvent", new Variable ("H", hm), new Variable ("s", s));
		
		l.newText(new Offset(codeIndentation,25, "sign14", defaultFontDirection), "s=("+hm+"-"+a+"*"+r+")*"+k+"^(-1) mod ("+p+"-1)="+s, "sign15", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		
		SignText<String> sign = new SignText<String>(r.toString(),s.toString(), hm.toString());
		l.newText(new Offset(-codeIndentation,25, "sign15", defaultFontDirection), "Damit erhalten wir die Signatur (r="+sign.r+",s="+sign.s+").", "sign16", null);		

		return sign;
	}
	
	public boolean verifySignature(SignText<String> sign) {
		l.newText(contentRoot, "Die Verifikation der Signatur erfolgt mittels einiger Überprüfungen der Werte r und s.", "veri1", null);
		l.newText(new Offset(0,25, "veri1", defaultFontDirection), "Zuerst wird überprüft, ob r größer 0 und kleiner als p ist:", "veri2", null);
		
		// 1) 0 < r < p
		if(new BigInteger(sign.r).compareTo(new BigInteger("0"))!=1 || new BigInteger(sign.r).compareTo(p)!=-1) {
			l.newText(new Offset(0,25, "veri2", defaultFontDirection), "r hat den wert "+sign.r+" und liegt damit nicht zwischen 0 und p. Die Signatur ist damit falsch.", "veri_false", null);
			l.nextStep();
			return false;
		}
		l.newText(new Offset(0,25, "veri2", defaultFontDirection), "r hat den Wert "+sign.r+" und liegt damit zwischen 0 und p.", "veri3", null);
		l.nextStep();
			
		l.newText(new Offset(0,25, "veri3", defaultFontDirection), "Als letztes muss überprüft werden, ob g^H(m) mod p = (A^r)*(r^s) mod p ist:", "veri4", null);
		
		// 2) g^(H(m)) mod p = A^r*r^s mod p
		l.nextStep();
		BigInteger ghm = g.modPow(new BigInteger(sign.hm), p);
		l.newText(new Offset(codeIndentation,25, "veri4", defaultFontDirection), "g^H(m) mod p="+g+"^"+sign.hm+" mod "+p+"="+ghm, "veri5", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		BigInteger Arrs = A.modPow(new BigInteger(sign.r), p).multiply(new BigInteger(sign.r).modPow(new BigInteger(sign.s), p)).mod(p);
		l.newText(new Offset(0,25, "veri5", defaultFontDirection), "(A^r)*(r^s) mod p = "+A+"^"+sign.r+"*"+sign.r+"^"+sign.s+" mod +"+p+"="+Arrs, "veri6", null).setFont(new Font("Courier", Font.PLAIN, 12), null, null);
		l.nextStep();
		
		CheckpointUtils.checkpointEvent(this, "verifyEvent", new Variable ("ghm", ghm), new Variable ("Arrs", Arrs));
		
		if(!ghm.equals(Arrs)) {
			l.newText(new Offset(-codeIndentation,25, "veri6", defaultFontDirection), "g^H(m) ist nicht kongruent zu A^r*r^s mod p. Die Verifikation ist damit fehlgeschlagen und die Signatur falsch.", "veri_false", null);
			l.nextStep();
			return false;
		}
		l.newText(new Offset(-codeIndentation,25, "veri6", defaultFontDirection), "g^H(m) ist kongruent zu A^r*r^s mod p. Die Verifikation war mit diesem Schritt erfolgreich und die Signatur ist korrekt.", "veri7", null);
			
		return true;
	}

	//------------------------------------------------------------------------------------------------
	// Animalfunktionen
	//------------------------------------------------------------------------------------------------
		
	// Animal-Variablen
	private Language l;
	private Coordinates headRoot = new Coordinates(10,10);
	private Coordinates contentRoot = new Coordinates(10,50);
	private String defaultFontDirection = AnimalScript.DIRECTION_NW;
	private int codeIndentation = 10;
	
	private void hideAllButHead(String part) {
		l.hideAllPrimitives();
		l.newText(headRoot, "ElGamal Verschlüsselung"+part, "heading_"+__random.nextInt(), null).setFont(new Font("SansSerif", Font.BOLD, 16), null, null);
	}
	
	private void introduceElGamal(String m) {
		l.newText(headRoot, "ElGamal Ver-, Entschlüsselung und Signaturerzeugung", "heading", null).setFont(new Font("SansSerif", Font.BOLD, 16), null, null);;
		l.newText(contentRoot, "Die ElGamal-Verschlüsselung beruht auf dem Diskreten Logarithmus Problem.", "intro1", null);
		l.newText(new Offset(0,25,"intro1",defaultFontDirection), "Sie ist eine asymmetrische Verschlüsselung und besitzt daher öffentliche und private Schlüssel.", "intro2", null);
		l.newText(new Offset(0,25,"intro2",defaultFontDirection), "Bei der Initialisierung wird zuerst der private Schlüssel zufällig und gleichverteilt gewählt.", "intro3", null);
		l.newText(new Offset(0,25,"intro3",defaultFontDirection), "Aus dem privaten Schlüssel wird dann mit Hilfe vorher bestimmter großer Primzahlen ein öffentlicher Schlüssel gebildet.", "intro4", null);
		l.newText(new Offset(0,25,"intro4",defaultFontDirection), "Der öffentliche Schlüssel wird zum Verschlüsseln der Nachricht genutzt, der private zum Entschlüsseln.", "intro5", null);
		l.newText(new Offset(0,25,"intro5",defaultFontDirection), "Für die Qualität und Sicherheit der Verschlüsselung ist es wichtig, große Primzahlen und gute Zufallsgeneratoren zu verwenden.", "intro6", null);
		l.newText(new Offset(0,25,"intro6",defaultFontDirection), "In diesem Beispiel wird die Nachricht "+m+" ver- und wieder entschlüsselt.", "intro7", null);
		l.newText(new Offset(0,25,"intro7",defaultFontDirection), "Dazu werden die Parameter p="+p+" und g="+g+" verwendet.", "intro8", null);
	}

	private void summerizeElGamal(String m, CipherText<String> c, SignText<String> s) {
		l.newText(contentRoot, "Im ersten Teil haben wir den öffentlichen und den privaten Schlüssel generiert:", "sum1", null);
		l.newText(new Offset(0,25,"sum1",defaultFontDirection), "Öffentlicher Schlüssel: ("+p+","+g+","+A+")    Privater Schlüssel: "+a, "sum2", null);
		l.nextStep();
		l.newText(new Offset(0,25,"sum2",defaultFontDirection), "Im zweiten Teil haben wir dann aus dem angegebenen Text das Schlüsseltext-Paar berechnet:", "sum3", null);		
		l.newText(new Offset(codeIndentation,25,"sum3",defaultFontDirection), m+" ---("+p+","+g+","+A+")--> ("+c.B+","+c.c+")", "sum4", null);
		l.nextStep();		
		l.newText(new Offset(-codeIndentation,25,"sum4",defaultFontDirection), "Im dritten Teil wurde das Schlüsseltext-Paar wieder entschlüsselt, um zu zeigen, dass die Verschlüsselung funktioniert:", "sum5", null);
		l.newText(new Offset(codeIndentation,25,"sum5",defaultFontDirection), "("+c.B+","+c.c+") ---"+a+"--> "+m, "sum6", null);
		l.nextStep();
		l.newText(new Offset(-codeIndentation,25,"sum6",defaultFontDirection), "Im vierten und fünften Teil haben wir mit Hilfe des Privaten Schlüssels die Signatur der Nachricht erzeugt und diese verifiziert.", "sum7", null);
		l.newText(new Offset(codeIndentation,25,"sum7",defaultFontDirection), m+"---"+a+"--> ("+s.r+","+s.s+")", "sum8", null);
		l.nextStep();
		l.newText(new Offset(-codeIndentation,50,"sum8",defaultFontDirection), "Anmerkung:", "sum9", null);
		l.newText(new Offset(0,25,"sum9",defaultFontDirection), "Die Sicherheit des Verfahrens hängt maßgeblich von der Wahl des Exponenten b ab, mit dem die Nachricht verschlüsselt wird.", "sum10", null);
		l.newText(new Offset(0,25,"sum10",defaultFontDirection), "Wird b immer gleich gewählt sind lässt sich zwar die Effizienz steigern, da bestimmte Berechnungen vorberechnet werden können,", "sum11", null);
		l.newText(new Offset(0,25,"sum11",defaultFontDirection), "jedoch wird dadurch ein Known-Plaintext-Angriff möglich, mit dem mit einem bekannten m der Aufwand zur Entschlüsselung anderer", "sum12", null);
		l.newText(new Offset(0,25,"sum12",defaultFontDirection), "Orginalnachrichten stark sinkt.", "sum13", null);
	}
	
	@Override
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		
		g = new BigInteger((String)primitives.get("g"));
    String m = (String)primitives.get("Message");
    p = new BigInteger((String)primitives.get("p"));
    bitLength = (Integer)primitives.get("Bit length");
    String mdInstance = (String)primitives.get("MessageDigest");
    l.setStepMode(true);
    
    //try {
      a = new BigInteger((String)primitives.get("a"));
    //wenn a == 0, dann soll a zufällig gewählt werden
      if (a.compareTo(new BigInteger("0")) == 0)
        aSet = false;
      else 
        aSet = true;
    //} catch (Exception e) {
    //  aSet = false;
    //}
    
    try {
      b = new BigInteger((String)primitives.get("b"));
      //wenn b == 0, dann soll b zufällig gewählt werden
      if (b.compareTo(new BigInteger("0")) == 0)
        bSet = false;
      else 
        bSet = true;
    } catch (Exception e) {
      bSet = false;
    }
    
    /*try {
      B = new BigInteger((String)primitives.get("B"));
      //wenn B == -1, dann soll B zufällig gewählt werden
      if (b.compareTo(new BigInteger("-1")) == 0)
        BSet = false;
      else 
        BSet = true;
    } catch (Exception e) {
      BSet = false;
    }*/
    
    try {
      k = new BigInteger((String)primitives.get("k"));
      //wenn k == 0, dann soll k zufällig gewählt werden
      if (k.compareTo(new BigInteger("0")) == 0)
        kSet = false;
      else
        kSet = true;
    } catch (Exception e) {
      kSet = false;
    }
    
        
		// Einführung
		introduceElGamal(m);
		
		l.nextStep("Initialisierung");
		hideAllButHead(": Initialisierung");
		initializeCryptoSystem();
		
		l.nextStep("Verschlüsselung");
		hideAllButHead(": Verschlüsselung");
		CipherText<String> c = encrypt(m);
		
		l.nextStep("Entschlüsselung");
		hideAllButHead(": Entschlüsselung");
		decrypt(c);
		
		l.nextStep("Signaturerzeugung");
		hideAllButHead(": Signaturerstellung");
		SignText<String> s = sign(m,mdInstance);
		
		l.nextStep("Verifikation");
		hideAllButHead(": Verifikation der Signatur");
		boolean verified = verifySignature(s);
		
		CheckpointUtils.checkpointEvent(this, "verifiedEvent", new Variable("verified", verified));
		
		l.nextStep("Zusammenfassung");
		hideAllButHead(": Zusammenfassung");
		summerizeElGamal(m,c,s);
		
		return l.toString();
	}

	@Override
	public void init() {
		l = new AnimalScript(getName(),getAnimationAuthor(),1024,860);		
	}

   public String getName() {
        return "ElGamal [DE]";
    }

    public String getAlgorithmName() {
        return "ElGamal";
    }

    public String getAnimationAuthor() {
        return "Jan Dillmann,Fabian Letzkus";
    }

    public String getDescription(){
    	return "Im Folgenden soll der Verschlüsselungsalgorithmus von Taher ElGamal beschrieben werden. "
    			 +"\n"
    			 +"Er basiert auf dem Problem, den diskreten Logarithmus zweier Zahlen zu bestimmen."
    			 +"\n"
    			 +"\n"
    			 +"ElGamal ist ein asymmetrischer Verschlüsselungsalgorithmus, d.h. zur Verschlüsselung wird "
    			 +"\n"
    			 +"ein öffentlicher Schlüssel, zur Entschlüsselung dagegen ein privater Schlüssel benutzt. Der "
    			 +"\n"
    			 +"private Schlüssel ist nur dem Empfänger der Nachricht bekannt."
    			 +"\n"
    			 +"\n";
    }

    public String getCodeExample(){
        return "// Initialisierung"
        		 +"\n"
        		 +"wähle p als möglichst große Primzahl."
        		 +"\n"
        		 +"wähle g modulo p als Primitivwurzel modulo p."
        		 +"\n"
        		 +"wähle a zwischen 2 und p-2."
        		 +"\n"
        		 +"berechne A = g^a mod p"
        		 +"\n"
        		 +"öffentlicher Schlüssel: (p,g,A)"
        		 +"\n"
        		 +"privater Schlüssel: a"
        		 +"\n"
        		 +"\n"
        		 +"// Verschlüsselung"
        		 +"\n"
        		 +"wähle Nachricht m zwischen 0 und p-1"
        		 +"\n"
        		 +"wähle b zwischen 2 und p-2"
        		 +"\n"
        		 +"berechne B=g^b mod p"
        		 +"\n"
        		 +"berechne c=(A^b)*m mod p"
        		 +"\n"
        		 +"Verschlüsselte Nachricht ist (B,c)"
        		 +"\n"
        		 +"\n"
        		 +"// Entschlüsselung"
        		 +"\n"
        		 +"berechne Exponent x=p-1-a"
        		 +"\n"
        		 +"berechne Orginalnachricht m=(B^x)*c mod p";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
}
