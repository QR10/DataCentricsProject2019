package com.shops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DAO {

	private DataSource mysqlDS;

	
	/* ======================================================================================================
	 * Constructor
	 * ====================================================================================================== */
	public DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/shops";
		mysqlDS = (DataSource) context.lookup(jndiName);
	}
	
	// Loads Stores into an array list
	public ArrayList<Store> loadStores() throws Exception {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();

		String sql = "select * from store";

		myStmt = myConn.createStatement();

		myRs = myStmt.executeQuery(sql);
		
		ArrayList<Store> stores = new ArrayList<Store>();

		// process result set
		while (myRs.next()) {
			Store s = new Store();
			s.setId(myRs.getInt("id"));
			s.setName(myRs.getString("name"));
			s.setFounded(myRs.getString("founded"));
			stores.add(s);
		}	
		return stores;
	}
	
	// Loads Products into an array list 
	public ArrayList<Product> loadProducts() throws Exception {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();

		String sql = "select * from product";

		myStmt = myConn.createStatement();

		myRs = myStmt.executeQuery(sql);
		
		ArrayList<Product> products = new ArrayList<Product>();

		// process result set
		while (myRs.next()) {
			Product p = new Product();
			p.setPid(myRs.getInt("pid"));
			p.setSid(myRs.getInt("sid"));
			p.setProdName(myRs.getString("prodName"));
			p.setPrice(myRs.getDouble("price"));
			products.add(p);
		}	
		return products;
	}
	
	// Loads products according to store into an array list
	public ArrayList<StoreProduct> loadProductsFromStore(int storeID) throws Exception {
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();

		String sql = "select * from store inner join product on id = sid where id = " + storeID;

		myStmt = myConn.createStatement();

		myRs = myStmt.executeQuery(sql);
		
		ArrayList<StoreProduct> storeProducts = new ArrayList<StoreProduct>();

		// process result set
		while (myRs.next()) {
			StoreProduct sp = new StoreProduct();
			sp.setId(myRs.getInt("id"));
			sp.setName(myRs.getString("name"));
			sp.setFounded(myRs.getString("founded"));
			sp.setPid(myRs.getInt("pid"));
			sp.setSid(myRs.getInt("sid"));
			sp.setProdName(myRs.getString("prodName"));
			sp.setPrice(myRs.getDouble("price"));
			storeProducts.add(sp);
		}	
		return storeProducts;
	}
	
	// Add new store into database
	public void addStore(Store store) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "insert into store (name, founded) values (?, ?)";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setString(1, store.getName());
		myStmt.setString(2, store.getFounded());
		myStmt.execute();				
	}
	
	// Delete Store from database
	public void deleteStore(int storeId) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "delete from store where id = ?";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, storeId);
		myStmt.execute();
	}
	
	// Delete Product from database
	public void deleteProduct(int productId) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "delete from product where pid = ?";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, productId);
		myStmt.execute();
	}
	
	// Check if store exists
	public boolean storeExists(int storeId) throws SQLException {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();

		String sql = "select id from store";

		myStmt = myConn.createStatement();

		myRs = myStmt.executeQuery(sql);

		// process result set
		while (myRs.next()) {
			if (myRs.getInt("id") == storeId)
				return true;
		}	
		
		return false;
	}
		
}
