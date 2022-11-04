package org.atostest.web.resources;

import org.atostest.services.DocumentService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.isNull;

@Path("/documents")
public class DocumentsResource {

    @Inject
    private DocumentService documentService;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") InputStream uploadedInputStream,
                           @FormDataParam("file") FormDataContentDisposition fileDetail, @Context HttpHeaders httpHeaders) {

        try {
            String username = httpHeaders.getHeaderString("username");
            String filename = httpHeaders.getHeaderString("document-name");
            if (verifyBadRequest(username, filename))
                return Response.status(400).entity("Bad Request, please provide the username or the document name.")
                        .type("text/plain").build();

            String uploadedFileLocation = documentService.upload(username, filename, uploadedInputStream);

            String output = "File uploaded to : " + uploadedFileLocation;

            return Response.status(200).entity(output).build();
        } catch (RuntimeException e) {
            return Response.status(422).entity(e.getMessage())
                    .type("text/plain").build();
        } catch (IOException ioException) {
            return Response.status(500).entity("An unexpected error occurred when saving the file!")
                    .type("text/plain").build();
        }

    }

    @GET
    @Path("/retrieve")
    public Response retrieve(@Context HttpHeaders httpHeaders) {
        try {
            String username = httpHeaders.getHeaderString("username");
            String filename = httpHeaders.getHeaderString("document-name");
            if (verifyBadRequest(username, filename))
                return Response.status(400).entity("Bad Request, please provide the username or the document name.")
                        .type("text/plain").build();

            byte[] bytes = documentService.retrieve(username, filename);

            return Response.ok(bytes).build();

        } catch (RuntimeException e) {
            return Response.status(422).entity(e.getMessage())
                    .type("text/plain").build();
        } catch (IOException ioException) {
            return Response.status(500).entity("An unexpected error occurred when retrieving the file!")
                    .type("text/plain").build();
        }
    }

    @DELETE
    @Path("/delete/{documentName}")
    public Response delete(@PathParam("documentName") String document, @Context HttpHeaders httpHeaders) {
        try {
            String username = httpHeaders.getHeaderString("username");
            if (verifyBadRequest(username, document))
                return Response.status(400).entity("Bad Request, please provide the username or the document name.")
                        .type("text/plain").build();

            documentService.delete(username, document);

            return Response.noContent().build();

        } catch (RuntimeException e) {
            return Response.status(422).entity(e.getMessage())
                    .type("text/plain").build();
        } catch (IOException ioException) {
            return Response.status(500).entity("An unexpected error occurred when deleting the file!")
                    .type("text/plain").build();
        }
    }

    private static boolean verifyBadRequest(String username, String document) {
        return isNull(document) || isNull(username);
    }

}
