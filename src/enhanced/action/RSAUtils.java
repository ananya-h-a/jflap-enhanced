package enhanced.action;


import java.io.BufferedOutputStream;  
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.math.BigInteger;  
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.spec.InvalidKeySpecException;  
import java.security.spec.RSAPrivateKeySpec;  
import java.security.spec.RSAPublicKeySpec;  
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

  
 
public class RSAUtils {  
  
 private static final String PUBLIC_KEY_FILE = "Public.key";  
 private static final String PRIVATE_KEY_FILE = "Private.key";  
   
 public static void main(String[] args) throws IOException {  
  
	  genratePublicAndPrivateKeys();
   
  
 }
   
 /** 
  * Save Files 
  * @param fileName 
  * @param mod 
  * @param exp 
  * @throws IOException 
  */  
 private void saveKeys(String fileName,BigInteger mod,BigInteger exp) throws IOException{  
  FileOutputStream fos = null;  
  ObjectOutputStream oos = null;  
    
  try {  
   System.out.println("Generating "+fileName + "...");  
   fos = new FileOutputStream(fileName);  
   oos = new ObjectOutputStream(new BufferedOutputStream(fos));  
     
   oos.writeObject(mod);  
   oos.writeObject(exp);     
     
   System.out.println(fileName + " generated successfully");  
  } catch (Exception e) {  
   e.printStackTrace();  
  }  
  finally{  
   if(oos != null){  
    oos.close();  
      
    if(fos != null){  
     fos.close();  
    }  
   }  
  }    
 }  
   
 /** 
  * Encrypt Data 
  * @param data 
  * @throws IOException 
  */  
 public static void encryptData(String data) throws IOException {  
 
  try {  

	 PublicKey pubKey = readPublicKeyFromFile(PUBLIC_KEY_FILE);  
	   Cipher cipher = Cipher.getInstance("RSA");  
	   cipher.init(Cipher.ENCRYPT_MODE, pubKey);  

   
   byte[] encrypted = blockCipher(cipher,data.getBytes("UTF-8"),Cipher.ENCRYPT_MODE);

	char[] encryptedTranspherable = Hex.encodeHex(encrypted);

	   System.out.println("Encryted Data: " + new String(encryptedTranspherable));  

	//return new String(encryptedTranspherable).getBytes();
 	  String timeLog = new SimpleDateFormat("ddMMMyy_HH_mm_ss").format(
			        Calendar.getInstance().getTime());

 	  File file = new File(timeLog + "_Encrypted.xml");

 	  FileOutputStream outputStream = new FileOutputStream(file);
 	  
       outputStream.write(new String(encryptedTranspherable).getBytes());
       outputStream.close();

     
  } catch (Exception e) {  
	JOptionPane.showMessageDialog(null, "Error Saving Trace File","Auth Error",JOptionPane.ERROR_MESSAGE);

  }   
    
 }  
  
 /** 
  * Encrypt Data 
  * @param data 
  * @throws IOException 
  */  
 public static byte[] decryptData(byte[] data) throws IOException {
 
	 try{
	    PrivateKey privateKey = readPrivateKeyFromFile(PRIVATE_KEY_FILE);  
	    Cipher cipher = Cipher.getInstance("RSA");  
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	    /*descryptedData = cipher.doFinal(inputBytes);  
	    System.out.println("Decrypted Data: " + new String(descryptedData));  */
	    
	    byte[] bts = Hex.decodeHex(new String(data).toCharArray());

		byte[] decrypted = blockCipher(cipher, bts,Cipher.DECRYPT_MODE);

		//return new String(decrypted,"UTF-8");
		   System.out.println("Decrypted Data: " + new String(decrypted,"UTF-8"));  

	    return (new String(decrypted,"UTF-8").getBytes());
	    
	 }
	    catch (NoSuchPaddingException | NoSuchAlgorithmException
	               | InvalidKeyException | BadPaddingException
	               | IllegalBlockSizeException |IOException | DecoderException|InvalidKeySpecException |
	               ClassNotFoundException ex) { 
	    	   ex.printStackTrace();  

	    	return null;
	       }
	      
	   
 }
	 
  public static ByteArrayInputStream decryptData(File inputFile){
		 try{
	  FileInputStream inputStream = new FileInputStream(inputFile);
	  byte[] inputBytes = new byte[(int) inputFile.length()];
	  inputStream.read(inputBytes);
	  inputStream.close();
	     
	 
	    PrivateKey privateKey = readPrivateKeyFromFile(PRIVATE_KEY_FILE);  
	    Cipher cipher = Cipher.getInstance("RSA");  
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);  
	  
	    byte[] bts = Hex.decodeHex(new String(inputBytes).toCharArray());

		byte[] decrypted = blockCipher(cipher,bts,Cipher.DECRYPT_MODE);

		return new ByteArrayInputStream(new String(decrypted,"UTF-8").getBytes());
	      
	   } catch (NoSuchPaddingException | NoSuchAlgorithmException
               | InvalidKeyException | BadPaddingException
               | IllegalBlockSizeException |IOException | DecoderException |InvalidKeySpecException |
               ClassNotFoundException
                ex) {
     	return null;
       }

 }  
   
 /** 
  * read Public Key From File 
  * @param fileName 
  * @return PublicKey 
  * @throws IOException 
  */  
 public static PublicKey readPublicKeyFromFile(String fileName) throws IOException,InvalidKeySpecException,NoSuchAlgorithmException,ClassNotFoundException{  
  FileInputStream fis = null;  
  ObjectInputStream ois = null;  
  try {
		 String path = "/enhanced/"+ fileName;
   ois = new ObjectInputStream(new RSAUtils().getClass().getResourceAsStream(path));  

   BigInteger modulus = (BigInteger) ois.readObject();  
      BigInteger exponent = (BigInteger) ois.readObject();  
     
      //Get Public Key  
      RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);  
      KeyFactory fact = KeyFactory.getInstance("RSA");  
      PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);  
              
      return publicKey;  
        
  } catch (InvalidKeySpecException| NoSuchAlgorithmException | ClassNotFoundException e) {  

	  throw e;
   
  }  
  finally{  
   if(ois != null){  
    ois.close();  
    if(fis != null){  
     fis.close();  
    }  
   }  
  }  
 }  
   
 /** 
  * read Public Key From File 
  * @param fileName 
  * @return 
  * @throws IOException 
  */  
 public static PrivateKey readPrivateKeyFromFile(String fileName) throws IOException,InvalidKeySpecException,NoSuchAlgorithmException,ClassNotFoundException {  
  FileInputStream fis = null;  
  ObjectInputStream ois = null;  
  try {  
	
  // fis = new FileInputStream(new File(fileName));  --> for eclipse debugging
	  String path = "/enhanced/"+ fileName;
   ois = new ObjectInputStream(new RSAUtils().getClass().getResourceAsStream(path));  
     
   BigInteger modulus = (BigInteger) ois.readObject();  
      BigInteger exponent = (BigInteger) ois.readObject();  
     
      //Get Private Key  
      RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);  
      KeyFactory fact = KeyFactory.getInstance("RSA");  
      PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);  
              
      return privateKey;  
        
  } catch (InvalidKeySpecException| NoSuchAlgorithmException | ClassNotFoundException e) {  
	  throw e;
   
  }  
  finally{  
   if(ois != null){  
    ois.close();  
    if(fis != null){  
     fis.close();  
    }  
   }  
  }  
 }
 
 public static boolean compareHash(String password){
	 try{
		 String s="";
			
		 List<Integer> li = Arrays.asList(97,100,109,105,110);
		 for(int i : li){
		 s +=  new Character((char) i).toString();
		 }
		 for(int i=1;i<=3;i++)
			 s+=i;
		 System.out.println("pass:" + s);
		MessageDigest mdigest = null;
		mdigest = MessageDigest.getInstance("MD5");
		mdigest.reset();
		mdigest.update(s.getBytes());
		byte[] digest = mdigest.digest();
		BigInteger big = new BigInteger(1,digest);
		String hash1 = big.toString(16);
		
		mdigest.reset();
		mdigest.update(password.getBytes());
		digest = mdigest.digest();
		big = new BigInteger(1,digest);
		String hash2 = big.toString(16);
		
		return (hash1.equals(hash2));
	 }
	 catch(Exception e){
		 return false;
	 }
 }
 private static byte[] blockCipher(Cipher cipher,byte[] bytes, int mode) throws InvalidKeyException,IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException,NoSuchAlgorithmException,IOException{
	
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
		//int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 128;
		int length = (mode == Cipher.ENCRYPT_MODE) ? (2048 / 8 ) - 11 : (2048 / 8 );
		// another buffer. this one will hold the bytes that have to be modified in this step
		byte[] buffer = new byte[length];

		for (int i=0; i< bytes.length; i++){

			// if we filled our buffer array we have our block ready for de- or encryption
			if ((i > 0) && (i % length == 0)){
				//execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn,scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
				if (i + length > bytes.length) {
					 newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i%length] = bytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn,scrambled);

		return toReturn;
	}
 private static byte[] append(byte[] prefix, byte[] suffix){
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i=0; i< prefix.length; i++){
			toReturn[i] = prefix[i];
		}
		for (int i=0; i< suffix.length; i++){
			toReturn[i+prefix.length] = suffix[i];
		}
		return toReturn;
	}
 
 public static void genratePublicAndPrivateKeys() throws IOException{
	 try{
	 System.out.println("-------GENRATE PUBLIC and PRIVATE KEY-------------");  
	   KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");  
	   keyPairGenerator.initialize(2048); //1024 used for normal securities  
	   KeyPair keyPair = keyPairGenerator.generateKeyPair();  
	   PublicKey publicKey = keyPair.getPublic();  
	   PrivateKey privateKey = keyPair.getPrivate();  
	   System.out.println("Public Key - " + publicKey);  
	   System.out.println("Private Key - " + privateKey);  
	  
	   //Pullingout parameters which makes up Key  
	   System.out.println("\n------- PULLING OUT PARAMETERS WHICH MAKES KEYPAIR----------\n");  
	   KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
	   RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);  
	   RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);  
	   System.out.println("PubKey Modulus : " + rsaPubKeySpec.getModulus());  
	   System.out.println("PubKey Exponent : " + rsaPubKeySpec.getPublicExponent());  
	   System.out.println("PrivKey Modulus : " + rsaPrivKeySpec.getModulus());  
	   System.out.println("PrivKey Exponent : " + rsaPrivKeySpec.getPrivateExponent());  
	     
	   //Share public key with other so they can encrypt data and decrypt thoses using private key(Don't share with Other)  
	   System.out.println("\n--------SAVING PUBLIC KEY AND PRIVATE KEY TO FILES-------\n");  
	   RSAUtils rsaObj = new RSAUtils();  
	   rsaObj.saveKeys(PUBLIC_KEY_FILE, rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());  
	   rsaObj.saveKeys(PRIVATE_KEY_FILE, rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());  
	     
	   //Encrypt Data using Public Key  
	  // byte[] encryptedData = rsaObj.encryptData("default data !");  
	     
	   //Decrypt Data using Private Key  
	   //rsaObj.decryptData(encryptedData); 
	     
	  } 
	 catch (NoSuchAlgorithmException e) {  
		   e.printStackTrace();  
		  }catch (InvalidKeySpecException e) {  
		   e.printStackTrace();  
		  }  
 }
 
 public static boolean decryptToFile() throws CryptoException {
	  try {
		  JFileChooser chooser = new JFileChooser();
		  File workingDirectory = new File(System.getProperty("user.dir"));
		  chooser.setCurrentDirectory(workingDirectory);
	         FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
	              "xml files (*.xml)", "xml");
	         chooser.setFileFilter(xmlfilter);
	         chooser.setDialogTitle("Open Trace file");
	         int val = chooser.showOpenDialog(null);
	         if(val == JFileChooser.APPROVE_OPTION){
	        	
	            File inputFile =  new File(chooser.getSelectedFile().getPath());
	            if(!inputFile.getPath().contains("Encrypted")){
		        	JOptionPane.showMessageDialog(null,"The selected Trace File is not Encrypted!","Invalid Trace File",JOptionPane.ERROR_MESSAGE);
		        	return false;
				}
	            JPasswordField pwd = new JPasswordField(10);  
	     	    int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);
	             if(action != 0){
	             	JOptionPane.showMessageDialog(null,"Please enter password to proceed","Auth Error",JOptionPane.ERROR_MESSAGE);
	             	return false;
	             }
	             
	             if(compareHash(new String(pwd.getPassword()))){
	            	 FileInputStream inputStream = new FileInputStream(inputFile);
	           	  byte[] inputBytes = new byte[(int) inputFile.length()];
	           	  inputStream.read(inputBytes);
	           	  inputStream.close();
	           	     
	         

		     File outputFile = new File("Decrypted_" + inputFile.getName().replace("_Encrypted", ""));
		     FileOutputStream outputStream = new FileOutputStream(outputFile);
		     outputStream.write(decryptData(inputBytes));

		      
		     outputStream.close();
		     JOptionPane.showMessageDialog(null, "The decoded file \"" + outputFile.getName() + "\" is saved in the current folder", "File saved" , JOptionPane.INFORMATION_MESSAGE);
			         }
			            else{
		   	           	JOptionPane.showMessageDialog(null,"Please Enter the Correct Password","Auth Error",JOptionPane.ERROR_MESSAGE);
	             return false;
			            }
		
	         } 	  
			         
	         }
	  catch(IOException ex){
	           	JOptionPane.showMessageDialog(null,"Error while Decoding Trace File","Decoding Error",JOptionPane.ERROR_MESSAGE);
	           	return false;
	  }
	  
	  return true;
	  
	 
 }
	  
}
