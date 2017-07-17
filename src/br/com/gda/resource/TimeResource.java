package br.com.gda.resource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;

import br.com.mind5.helper.Weekday;

@Path("/Time")
public class TimeResource {

	private static final String GET_TIME = "getTime";

	@GET
	@Path(GET_TIME)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectTime() {
		
//		Class<?> c = Weekday.class;
////	    Object t = c.newInstance();
//		Method[] methods = c.getMethods();
//		for (Method method : methods) {
//			String mname = method.getName();
//			if (!mname.startsWith("set")) {
//			    continue;
//			}
//			String aname = mname.substring(3);
//			aname = aname.replace(aname.charAt(0), aname.toLowerCase().charAt(0));
//			System.out.println(aname);
//			
//			
////			Parameter[] parameters = method.getParameters();
////			for (Parameter parameter : parameters) {
////				System.out.print(parameter.getType().getName());
////			}
//			
////			String text = null;
////			System.out.println(text);
//		}
		

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("zoneId", "Z");
		jsonObject.addProperty("time", ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime().toString());

		return Response.status(Response.Status.OK).entity(jsonObject.toString()).type(MediaType.APPLICATION_JSON)
				.build();

	}

}
