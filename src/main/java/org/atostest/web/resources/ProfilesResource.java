package org.atostest.web.resources;

import org.atostest.services.ProfileService;
import org.atostest.services.dto.ProfileDTO;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import static java.util.Objects.isNull;

@Path("/profiles")
public class ProfilesResource {
    @Inject
    private ProfileService profileService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProfile(@Valid ProfileDTO profileDTO, @Context UriInfo uriInfo) {

        try {

            if (isNull(profileDTO)) {
                return Response.status(400).entity("Bad Request, please provide the profile information")
                        .type("text/plain").build();
            }

            ProfileDTO profile = profileService.create(profileDTO);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(profileDTO.getUsername());

            return Response.created(uriBuilder.build()).entity(profile).build();

        } catch (RuntimeException e) {
            return Response.status(422).entity(e.getMessage())
                    .type("text/plain").build();
        }
    }

}
