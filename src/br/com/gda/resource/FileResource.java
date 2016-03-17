package br.com.gda.resource;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import br.com.gda.model.FileModel;

@Path("/File")
public class FileResource {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("path") String path, @FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

		return new FileModel().uploadFile(path, fileInputStream, contentDispositionHeader);
	}

	@GET
	@Path("/download")
	@Produces({ "image/png", "image/jpg", "image/gif" })
	public Response downloadFile(@QueryParam("codOwner") String codOwner,
			@QueryParam("codMaterial") String codMaterial) {

		return new FileModel().downloadFile(codOwner, codMaterial);
	}

}
