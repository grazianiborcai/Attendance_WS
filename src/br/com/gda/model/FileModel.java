package br.com.gda.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.google.gson.JsonObject;
import com.sun.jersey.core.header.FormDataContentDisposition;

public class FileModel extends JsonBuilder {

//	private static final String PATH = "D:/Users/Microcity/Desktop/Upload_Files/";
	private static final String PATH = "/opt/tomcat-latest/webapps/Agenda_img/";

	public Response uploadFile(String path, InputStream fileInputStream,
			FormDataContentDisposition contentDispositionHeader) {
		String filePath = path + "." + JPG;

		JsonObject jsonObject = getJsonObjectUpdate(saveCompressedFile(fileInputStream, filePath));

		return response(jsonObject);
	}

	// save uploaded file with lower size
	public SQLException saveCompressedFile(InputStream is, String serverLocation) {
		
		SQLException exception = new SQLException(FILE_UPLOADED, null, 200);
		
		try {
//			String extention = serverLocation.substring(serverLocation.indexOf(".") + 1, serverLocation.length());
			String newServerLocation = PATH + serverLocation;
//			if (!extention.equals(JPG))
//				newServerLocation = serverLocation.replaceAll(extention, JPG);
			File file = new File(newServerLocation);
			file.getParentFile().mkdirs();
			OutputStream os = new FileOutputStream(file);
			
			float quality = 0.8f;

			// create a BufferedImage as the result of decoding the supplied
			// InputStream
			BufferedImage image = ImageIO.read(is);

			// write image
			ImageIO.write(image, JPG, os);
			
			// get all image writers for JPG format

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

			if (!writers.hasNext())
				throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);

			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(os);

			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			// compress to a given quality

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

			param.setCompressionQuality(quality);

			// appends a complete image stream containing a single image and

			// associated stream and image metadata and thumbnails to the output

			writer.write(null, new IIOImage(image, null, null), param);
			
			// close all streams
			is.close();
			os.close();
			ios.close();
			writer.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exception = new SQLException(e.getMessage(), null, 4001);
		}

		return exception;

	}

	public Response downloadFile(String codOwner, String codMaterial) {
		// set file (and path) to be download
		File f = new File(PATH + codOwner + BAR + codMaterial + "." + JPG);

		if (!f.exists()) {
			SQLException exception = new SQLException(FILE_NOT_FOUND, null, 4002);
			return response(getJsonObjectUpdate(exception));
		}

		ResponseBuilder responseBuilder = Response.ok((Object) f);
		responseBuilder.header("Content-Disposition", "attachment; filename=\"" + codMaterial + "\"");
		return responseBuilder.build();
	}

}
