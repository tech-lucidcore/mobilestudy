package kr.testuser.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@Path("/test")
public class TestServlet {
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/getTest/{pathParam}")
	public Response testFunction(@Context HttpServletRequest request,
								 @PathParam("pathParam") String pathParam,
								 @QueryParam("queryParam") String queryParam) {
		try {
			ResponseBuilder builder = Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Tet")
					.type(MediaType.APPLICATION_JSON)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true");
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			ResponseBuilder builder = Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage())
					.type(MediaType.APPLICATION_JSON)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Credentials", "true");
			return builder.build();
		}
	}
}
