package rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Comment;
import domain.Product;

@Path("/products")
@Stateless
public class ProductsResources {

	@PersistenceContext
	EntityManager em;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getAll() {
		return em.createNamedQuery("product.all", Product.class).getResultList();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(Product product) {
		em.persist(product);
		return Response.ok(product.getId()).build();
	}
	
	@GET
	@Path("/{productId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("productId") int productId){
		Product result = productById(productId);
		
		if(result==null){
			return Response.status(404).build();
		}
		return Response.ok(result).build();
	}
	
	@PUT
	@Path("/{productId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("productId") int productId, Product p){
		Product result = productById(productId);
		
		if(result==null){
			return Response.status(404).build();
		}
		result.setName(p.getName());
		result.setPrice(p.getPrice());
		result.setCategory(p.getCategory());
		em.persist(result);
		return Response.ok().build();
	}
	

	
	private Product productById(int id) {
		return em.createNamedQuery("product.id", Product.class)
				.setParameter("productId", id)
				.getSingleResult();
	}
	
	@GET
	@Path("/{productId}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getComments(@PathParam("productId") int productId) {
		Product result = productById(productId);
		if(result==null)
			return null;
		
		return result.getComments();
	}
	@POST
	@Path("/{productId}/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(@PathParam("productId") int productId, Comment comment) {
		Product result = productById(productId);
		if(result==null)
			return Response.status(404).build();
		result.getComments().add(comment);
		comment.setProduct(result);
		em.persist(comment);

		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{productId}/comments/{commentId}")
	public Response deleteComment(@PathParam("productId") int productId, @PathParam("commentId") int commentId) {
		Product result = productById(productId);
		if (result == null) {
			return Response.status(404).build();
		}
		em.remove(result.getComments().get(commentId));
		result.getComments().remove(commentId);
		return Response.ok().build();
	}
	
	@GET
	@Path("/{productId}/comments/{commentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommentById(@PathParam("productId") int productId, @PathParam("commentId") int commentId){
		
		Product product = productById(productId);
		Comment result = product.getComments().get(commentId);
		return Response.ok(result).build();
	}
}
