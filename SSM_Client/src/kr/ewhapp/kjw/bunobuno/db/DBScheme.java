package kr.ewhapp.kjw.bunobuno.db;

import kr.ewhapp.kjw.bunobuno.NumberData;
import kr.ewhapp.kjw.bunobuno.encryption.Encrypt;
import kr.ssm.sniffing.userinfo.UserData;


public class DBScheme {

	public static String createUserInfoTable() {
		return "create table USER ( PhoneNumber text, Password text, GCMRegId text, Hint text, PasswordFlag integer );";
	}
	
	public static String createAccountTable() {
		return "create table ACCOUNT ( Title text, Bank integer, Name text, Number text, PRIMARY KEY (Title) );";
	}
	
	public static String createSiteTable() {
		return "create table SITE ( Title text, Name text, Password text, PRIMARY KEY (Title) );";
	}
	
	public static String createCustomTable() {
		return "create table CUSTOM ( Title text, Name text, Password text, PRIMARY KEY (Title) );";
	}
	
	public static String createPhotoTable () {
		return "create table PHOTO ( Title text, Uri text, PRIMARY KEY (Uri) );";
	}
	
	public static String selectUserInfo() {
		return "select * From USER;";
	}
	
	public static String selectAllTitle() {
//		return "select ACCOUNT.Title, SITE.Title, CUSTOM.Title, PHOTO.Uri From ACCOUNT, SITE, CUSTOM, PHOTO;"; 
		return "select ACCOUNT.Title, SITE.Title, CUSTOM.Title From ACCOUNT, SITE, CUSTOM;";
//		return "select ACCOUNT.Title from ACCOUNT;";
	}
	
	public static String selectAccountTitle() {
		return "select Title From ACCOUNT;";
	}
	
	public static String selectSiteTitle() {
		return "select Title From SITE;";
	}
	
	public static String selectCustomTitle() {
		return "select Title From CUSTOM;";
	}
	
	public static String selectPhotoUri() {
		return "select Uri From PHOTO;";
	}
	
	public static String selectAccountInfo(String title) {
		String sql = "select * From ACCOUNT where Title = '%s'; ";
		return String.format(sql, title);
	}
	
	public static String selectSiteInfo(String title) {
		String sql = "select * From SITE where Title = '%s'; ";
		return String.format(sql, title);
	}
	
	public static String selectCustomInfo(String title) {
		String sql = "select * From CUSTOM where Title = '%s'; ";
		return String.format(sql, title);
	}
	
	public static String selectPhotoInfo(String uri) {
		String sql = "select * From PHOTO where Uri = '%s'; ";
		return String.format(sql, uri);
	}
	
	public static String addUserInfo(UserData data) {
		String sql = "INSERT INTO USER(PhoneNumber, Password, GCMRegId, Hint, PasswordFlag) VALUES('%s', '%s', '%s', '%s', %d);";
		return String.format(sql, data.phoneNumber, data.password, Encrypt.encode(data.regId), data.hint, data.passwordFlag);
	}
	
	public static String addAccountInfo(NumberData data) {
		String sql = "INSERT INTO ACCOUNT(Title, Bank, Name, Number) VALUES('%s', %d, '%s', '%s');";
		return String.format(sql, data.title, data.bank, data.name, data.number);
	}
	
	public static String addSiteInfo(NumberData data) {
		String sql = "INSERT INTO SITE(Title, Name, Password) VALUES('%s', '%s', '%s');";
		return String.format(sql, data.title, data.name, Encrypt.encode(data.number));		
	}
	
	public static String addCustomInfo(NumberData data) {
		String sql = "INSERT INTO CUSTOM(Title, Name, Password) VALUES('%s', '%s', '%s');";
		return String.format(sql, data.title, data.name, Encrypt.encode(data.number));		
	}
	
	public static String addPhotoInfo(NumberData data) {
		String sql = "INSERT INTO PHOTO( Uri ) VALUES('%s');";
		return String.format(sql, data.uri);		
	}
	
	public static String updateGCMRegId(String key) {
		String sql = "UPDATE USER SET GCMRegId = '%s';";
		return String.format(sql, Encrypt.encode(key));
	}

	public static String updateAccountInfo(NumberData data) {
		String sql = "UPDATE ACCOUNT SET Title='%s', Bank=%d, Name='%s', Number='%s'; ";
		return String.format( sql, data.title, data.bank, data.name, data.number);
	}
	
	public static String updateSiteInfo(NumberData data) {
		String sql = "UPDATE SITE SET Title='%s', Name='%s', Password='%s'; ";
		return String.format( sql, data.title, data.name, Encrypt.encode(data.number));
	}
	
	public static String updateCustomInfo(NumberData data) {
		String sql = "UPDATE CUSTOM SET Title='%s', Name='%s', Password='%s'; ";
		return String.format( sql, data.title, data.name, Encrypt.encode(data.number));
	}
	
	public static String updatePasswordFlag(UserData data) {
		String url = "UPDATE USER SET PasswordFlag = %d;";
		return String.format(url, data.passwordFlag);
	}

	public static String updatePassword(UserData data) {
		String url = "UPDATE USER SET Password = %s;";
		return String.format(url, Encrypt.encode(data.password));
	}
	
	public static String updatePasswordHint(UserData data) {
		String url = "UPDATE USER SET Hint = %s;";
		return String.format(url, data.hint);
	}
	
	public static String deleteUserData() {
		return "DELETE FROM USER;";
	}

	public static String deleteAccountData() {
		return "DELETE FROM ACCOUNT;";
	}

	public static String deleteSiteData() {
		return "DELETE FROM SITE;";
	}

	public static String deleteCustomData() {
		return "DELETE FROM CUSTOM;";
	}

	public static String deletePhotoData() {
		return "DELETE FROM PHOTO;";
	}
	
	
	public static String deleteAccontInfo(NumberData data) {
		String sql = "DELETE FROM ACCOUNT WHERE Title = '%s'; ";
		return String.format( sql, data.title );
	}
	
	public static String deleteSiteInfo(NumberData data) {
		String sql = "DELETE FROM SITE WHERE Title = '%s'; ";
		return String.format( sql, data.title );
	}
	
	public static String deleteCustomInfo(NumberData data) {
		String sql = "DELETE FROM CUSTOM WHERE Title = '%s'; ";
		return String.format( sql, data.title );
	}
	
	public static String deletePhotoInfo(NumberData data) {
		String sql = "DELETE FROM PHOTO WHERE Uri = '%s'; ";
		return String.format( sql, data.uri );
	}
	
}
