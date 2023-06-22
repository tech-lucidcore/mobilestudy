package kr.testuser.realtest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.ibatis.io.Resources;

@Path("/realtest")
public class RealTestServlet {
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/fire")
	public Response testFunction(@Context HttpServletRequest request,
								 @QueryParam("sleep") long sleep,
								 @QueryParam("count") long count,
								 @QueryParam("postfix") String postfix) {
		long current = 0;
		try {
			List<byte[]> list = loadDataFromLogFile("RealTest_" + postfix + ".log");
			if (list != null) {
				while(true) {
					for (int ii=0; ii<list.size(); ii++) {
						RealTesterListener.__realTester.sendData(list.get(ii));
						Thread.sleep(sleep);
						current++;
						if (current >= count) {
							break;
						}
					}
					if (current >= count) {
						break;
					}
				}
				return getResponse(Status.OK, "OK!!!" + current);
			}
			return getResponse(Status.OK, "LOAD FILE ERROR!!!");
		} catch (Exception e) {
			e.printStackTrace();
			return getResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	protected List<byte[]> loadDataFromLogFile(String fileName) {
		List<byte[]> ret = new ArrayList<>();
		try {
			InputStream is = Resources.getResourceAsStream(fileName);
			byte[] lenData = new byte[4];
			int read;
			while (true) {
				read = is.read(lenData);
				if (read != 4) {
					break;
				}
				int len = parseInt(lenData);
				byte[] data = new byte[len];
				read = is.read(data);
				if (read != len) {
					break;
				}
				byte[] newData = new byte[4 + len];
				System.arraycopy(lenData, 0, newData, 0, 4);
				System.arraycopy(data, 0, newData, 4, len);
				ret.add(newData);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public int parseInt(byte[] bytes) {
		int ret = 0;
		int multi = 1;
		for (int ii=bytes.length-1; ii>=0; ii--) {
			if ('1' <= bytes[ii] && bytes[ii] <= '9') {
				ret += (bytes[ii] - '0') * multi;
			}
			multi *= 10;
		}
		return ret;
	}
	
	public Response getResponse(Status status, String message) {
		ResponseBuilder builder = Response
				.status(status)
				.entity(message)
				.type(MediaType.APPLICATION_JSON)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true");
			return builder.build();
	}
}
