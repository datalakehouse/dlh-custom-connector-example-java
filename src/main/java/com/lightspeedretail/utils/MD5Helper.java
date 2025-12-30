/**
|**********************************************************************;
* Project           : DataLakeHouse
*                   : datalakehouse.io
* License           : attribution required
* File Name         : MD5Heloper.java
*
* Author            : nisarg
* 
* Date Created      : 202100??
*
* Purpose           : Helper for MD5 instructions
* Purpose Details   : ...
*
* Dependency/Notes  : ...
*     - 
*
* Revision History  : (Date in YYYYMMDD format)
* ---------------------------------------------------------------------
* Date          Author        Ref       Revision  
* 20211115		CS				X		- Updates to give delimiter to MD5 hash to prevent null or similar outputs joining separate column likeness
* 
* 
* 
|**********************************************************************;
*/
package com.lightspeedretail.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Helper {

	private static final Logger LOGGER = Logger.getLogger(MD5Helper.class.getName());

	/**
	 * MD5 hashing of the connection schema row to make it a unique identifier of required fields
	 * 20211015CS - added deprecated. see other md5 functions and issue with delimiters not present but needed to avoid concat conflicts due to likeness
	 * @param orgId
	 * @param prjId
	 * @param intgId
	 * @param apexName
	 * @param catalogName
	 * @param schemaName
	 * @param entityName
	 * @param attributeName
	 * @return
	 */
	@Deprecated
	public static String getMD5(int orgId, int prjId, int intgId, String apexName, 
			String catalogName, String schemaName, String entityName, String attributeName) {
		String schemaRowConcString = orgId + prjId + intgId + apexName + 
				catalogName + schemaName + entityName + attributeName;
        try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(schemaRowConcString.getBytes()); 
            BigInteger no = new BigInteger(1, messageDigest); 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
		} catch (NoSuchAlgorithmException e) {
			LOGGER.severe(e.getLocalizedMessage());
		} 
		return null;
	}
	
	
	/**
	 * MD5 to take any number of arguments and hash them together with no separators
	 * or use this.., https://www.baeldung.com/java-md5
	 * @param args
	 * @return
	 */
	public static String getMD5FromArguments(String ... args) {
		
		String schemaRowConcString = "";
		String _md5SeparaterStr =  "|^|";	//md5SeparaterStr.isEmpty() ? "|^|" : md5SeparaterStr;
		
		for (String a:args) {
			schemaRowConcString += a + _md5SeparaterStr;
		}
		
        try {
        	String hashtext = DigestUtils.md5Hex(schemaRowConcString);
            
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			byte[] messageDigest = md.digest(schemaRowConcString.getBytes()); 
//            BigInteger no = new BigInteger(1, messageDigest); 
//            String hashtext = no.toString(16); 
//            while (hashtext.length() < 32) { 
//                hashtext = "0" + hashtext; 
//            } 
            return hashtext; 
		} 
//        catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
        catch (Exception e) {
        	LOGGER.severe(e.getLocalizedMessage());
        }
		return null;
	}
}
