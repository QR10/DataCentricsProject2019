package com.shops;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.CommunicationsException;

@ManagedBean
@SessionScoped
public class ShopController {
	ArrayList<Product> products;
	ArrayList<Store> stores;
	ArrayList<StoreProduct> storeProducts;
	ArrayList<HeadOffice> headOffices;
		
	DAO dao;
	MongoDAO mongodao;
	
	// Constructor
	public ShopController() {
		super();
		try {
			dao = new DAO();
			mongodao = new MongoDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Store> getStores() {
		return stores;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public ArrayList<StoreProduct> getStoreProducts(){
		return storeProducts;
	}
	
	public ArrayList<HeadOffice> getHeadOffices() {
		return headOffices;
	}
	public String moveToHomePage() {
		return "index";
	}
	
	// Load Head Offices or display error
	public String loadHeadOffices() {
		System.out.println("In loadHeadOffices()");
		try {
			headOffices = mongodao.loadHeadOffices();
		} catch (Exception e) {
			FacesMessage message = 
					new FacesMessage("Error: Cannot connect to Mongo Database");
					FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		return null;
	}
	
	// Load Stores or display error
	public String loadStores() {
		System.out.println("In loadStores()");
		try {
			stores = dao.loadStores();
		} catch (Exception e) {
			FacesMessage message = 
					new FacesMessage("Error: Cannot connect to MySQL Database");
					FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		return null;
	}
	
	// Load Products or display error
	public String loadProducts() {
		System.out.println("In loadProducts()");
		try {
			products = dao.loadProducts();
		} catch (Exception e) {
			FacesMessage message = 
					new FacesMessage("Error: Cannot connect to MySQL Database");
					FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		return null;
	}
	
	// Load Products from a store
	public String loadStoreProducts(int storeID) {
		System.out.println("In loadStoreProducts(" + storeID + ")");
		try {
			storeProducts = dao.loadProductsFromStore(storeID);
			return "list_product_by_store";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// Validate and add a new store
	public String addStore(Store s) {
		System.out.println(s.getName() + " " + s.getFounded());
		try {
			dao.addStore(s);
			return "index";
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = 
					new FacesMessage("Error: Store Name already exists");
					FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (CommunicationsException e) {
			FacesMessage message = 
					new FacesMessage("Error: Can't communicate with DB");
					FacesContext.getCurrentInstance().addMessage(null, message);
		}catch (Exception e) {
			FacesMessage message = 
					new FacesMessage("Error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, message);

			e.printStackTrace();
		}
		return null;
	}
	
	// Validate and add a new Head Office
	public String addHeadOffice(HeadOffice headOffice) {
		try {
			if(!(dao.storeExists(headOffice.get_id())))
				throw new SQLException();
		
			try {
				if(mongodao.hasLocation(headOffice.get_id()))
					throw new Exception();
				
				mongodao.addHeadOffice(headOffice);
				
				return "index";
			} catch (Exception e) {
				FacesMessage message = 
				new FacesMessage("Error: Store ID: " + headOffice.get_id() + " already has location");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} catch (SQLException e) {
			FacesMessage message = 
			new FacesMessage("Error: Store ID: " + headOffice.get_id() + " does not exist");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return null;
	}
	
	// Delete store if empty
	public String deleteStore(int storeId) {
		ArrayList<StoreProduct> tempStoreProducts;
		try {
			tempStoreProducts = dao.loadProductsFromStore(storeId);
			if (tempStoreProducts.isEmpty())
				dao.deleteStore(storeId);
			else
				
				throw new Exception();
		} catch (Exception error) {
			FacesMessage message = new FacesMessage("Error: Store has not been deleted from MySQL DB, it contains products");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return null;
	}
	
	// Delete Product
	public String deleteProduct(int productId) {
		try {
			dao.deleteProduct(productId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
