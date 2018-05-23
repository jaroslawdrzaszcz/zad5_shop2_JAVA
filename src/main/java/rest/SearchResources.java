package rest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import domain.Product;

@Path("/search")
public class SearchResources {

	@PersistenceContext
	EntityManager em;
	
	@GET
	@Path("/price/{from}/{to}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getByPrice(@PathParam("from") int from, @PathParam("to") int to) {
		List<Product> resultByPrice = new ArrayList<Product>();
		
		for (Product p : getAll()) {
			
			double priceOfProduct = p.getPrice();
			
			if (priceOfProduct >= from && priceOfProduct <= to) {
				resultByPrice.add(p);
			}
		}
		
		return resultByPrice;
	}
	
	@GET
	@Path("/name/{byName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getByName(@PathParam("byName") String byName) {
		List<Product> resultByName = new ArrayList<Product>();
		
		for (Product p : getAll()) {
			if (p.getName().contains(byName)) {
				resultByName.add(p);
			}
		}
		
		return resultByName;
	}
	
	@GET
	@Path("/category/{byCategory}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getByCategory(@PathParam("byCategory") String byCategory) {
		List<Product> resultByCategory = new ArrayList<Product>();
		
		for (Product p : getAll()) {
			if (p.getCategory().equals(byCategory)) {
				resultByCategory.add(p);
			}
		}
		
		return resultByCategory;
	}
	
	public List<Product> getAll() {
		return em.createNamedQuery("product.all", Product.class).getResultList();
	}
}
