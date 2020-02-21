package generators.cryptography;

import java.util.Locale;
import java.util.Vector;

import generators.cryptography.blowfish.BlowfishDecryptionGenerator;
import generators.cryptography.blowfish.BlowfishEncryptionGenerator;
import generators.cryptography.caesarcipher.Caesar;
import generators.cryptography.caesarcipher.CaesarChiffre3;
import generators.cryptography.caesarcipher.CaesarChiffre4b;
import generators.cryptography.caesarcipher.CaesarChiffreGutAnnotated;
import generators.cryptography.caesarcipher.CaesarCipher;
import generators.cryptography.caesarcipher.CaesarCipherAlgorithm;
import generators.cryptography.caesarcipher.CaesarCipherJMSS;
import generators.cryptography.caesarcipher.CaesarCipherTF;
import generators.cryptography.caesarcipher.CaesarImp;
import generators.cryptography.caesarcipher.CaesarTC;
import generators.cryptography.caesarcipher.Caesar_Chiffre;
import generators.cryptography.caesarcipher.MyCaesar;
import generators.cryptography.des.DESGenDE;
import generators.cryptography.des.DESGenEN;
import generators.cryptography.elgamal.ElGamal;
import generators.cryptography.elgamal.ElGamalVerfahren;
import generators.cryptography.feistel.FeistelChiffre;
import generators.cryptography.onetimepad.OneTimePad;
import generators.cryptography.onetimepad.OneTimePadString;
import generators.cryptography.rsa.RSAMO;
import generators.cryptography.rsa.RSA_XML;
import generators.framework.Generator;
import generators.framework.GeneratorBundle;

//import de.ahrgr.animal.kohnert.generators.VigenereDecode;

public class DummyGenerator implements GeneratorBundle {

  @Override
  public Vector<Generator> getGenerators() {
    Vector<Generator> generators = new Vector<Generator>(35, 15);
    generators.add(new CaesarCipherJMSS());
    generators.add(new Caesar());
    generators.add(new CaesarChiffre3());
    generators.add(new CaesarChiffre4b());
    generators.add(new CaesarChiffreGutAnnotated());
    generators.add(new CaesarCipher());
    generators.add(new CaesarCipherAlgorithm());
    generators.add(new CaesarCipherTF());
    generators.add(new CaesarImp());
    generators.add(new CaesarTC());
    generators.add(new MyCaesar());
    generators.add(new OneTimePad());
    generators.add(new RSAMO());
    // generators.add(new VigenereDecode());

    // TODO "under probation"
    // generators.add(new BabystepGiantstep());
    // generators.add(new CBC());
    // generators.add(new CFBgenerator());
    // generators.add(new CFBModeEncrypt());
    // generators.add(new XORCipherAlgorithm());
    // generators.add(new AnimalCaesar());
    // generators.add(new AnnotatedCaesarChiffre());
    // generators.add(new AnnotatedCaesarWithKey());
    // generators.add(new Caesar_AB());
    // generators.add(new CaesarChiffre());
    // generators.add(new CaesarChiffre2());
    // generators.add(new CaesarChiffre4());
    // generators.add(new CaesarChiffreAH());
    // generators.add(new CaesarChiffreGenerator());
    // generators.add(new CaesarChiffreIPJW());
    // generators.add(new CaesarCipherAnnotated());
    // generators.add(new CaesarCipherFB());
    // generators.add(new CaesarGenerator());
    // generators.add(new CaesarWithVariableKey());
    // generators.add(new CaesarWithKey());
    // generators.add(new RSA());
    // generators.add(new VigenereDecodeWrapper());
    // generators.add(new VigenereDecodeWrapperEN());
    // generators.add(new VigenereEncodeWrapper());
    // generators.add(new VigenereEncodeWrapperEN());

    // Generators from the AlgoAnim course in summer semester 2011.
    // generators.add(new BabystepGiantstep()); // superseded 2013.
    generators.add(new BinExp());
    generators.add(new CFBModeEncrypt());
    generators.add(new DiffieHellman());
    generators.add(new ElGamal());
    generators.add(new LamportDiffie());
    generators.add(new Merkle());
    generators.add(new OFBGenerator());
    generators.add(new OneTimePadString());
    generators.add(new Playfair());
    generators.add(new Polybios());
    generators.add(new RSA_XML());

    // Generators from the AlgoAnim course in summer semester 2012.
    generators.add(new ADFGXgenerator());
    // generators.add(new generators.cryptography.CaesarCipher());
    generators.add(new CBCDecrypt());
    generators.add(new CBCEncrypt());
    generators.add(new CtrGenerator());
    generators.add(new ECBGenerator());
    generators.add(new ErweiterterEuklidischerAlgorithmus());
    generators.add(new NeedhamSchroederAsym());
    generators.add(new NeedhamSchroederSym());
    generators.add(new OFBBlockChiffre(Locale.GERMAN));
    generators.add(new SubstitutionGenerator());

    // GR
    generators.add(new Caesar_Chiffre());

    // Generators from the AlgoAnim course in summer semester 2013.
    generators.add(new Adder());
    generators.add(new AddRoundKeyGenerator());
    generators.add(new AES());
    generators.add(new Atbasch()); // Schlicht, aber gut.
    generators.add(new BabystepGiantstepGenerator()); // 25 / 30.
    // generators.add(new CFBgenerator2());
    generators.add(new DESGenDE());
    generators.add(new DESGenEN());
    generators.add(new ElGamalVerfahren());
    generators.add(new KeyExpansion()); // Gut.
    generators.add(new MixColumnsGenerator());
    generators.add(new Multpl());
    generators.add(new RabinKryptosystem()); // Gut.
    generators.add(new ShiftRowsGenerator());
    generators.add(new SubBytes()); // Exzellent.

    // Generators from the AlgoAnim course in summer semester 2014.
    generators.add(new FeistelChiffre());
    generators.add(new CFB_Generator());
    generators.add(new OFB_Generator());
    generators.add(new DESGenDE());
    generators.add(new DESGenEN());

    // Generators from the AlgoAnim course in summer semester 2015.
    generators.add(new Vigenere(Locale.US)); // gut
    generators.add(new Vigenere(Locale.GERMANY)); // gut
    generators.add(new Rot47());
    generators.add(new ARC4());

    // Generators from the AlgoAnim course in summer term 2016.
    generators.add(new BB84());
    generators.add(new BlowfishDecryptionGenerator());
    generators.add(new BlowfishEncryptionGenerator());

    // Generators from the AlgoAnim course in summer term 2017.
    generators.add(new MACAlgo());
    generators.add(new MorseCode());
    generators.add(new SecretSharingGenerator());

    // Generators from the AlgoAnim course in summer term 2018.
    generators.add(new Treyfer());
    generators.add(new TEA());
   

    // Generators from the AlgoAnim course in summer term 20189.
    generators.add(new BitkettenOTP());
    generators.add(new HillChiffre("resources/HillChiffre", Locale.GERMANY));
    generators.add(new HillChiffre("resources/HillChiffre", Locale.US));
    generators.add(new VMPCGenerator());
    
    return generators;
  }
}
