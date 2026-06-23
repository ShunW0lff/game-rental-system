package org.acme.restClient;

import java.util.List;

import org.acme.model.Game;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(baseUri = "https://localhost:8443/games")
@AccessToken
public interface IGame {

    @GET
    @Path("/listGames")
    @Produces(MediaType.APPLICATION_JSON)
    List<Game> getGames();

    @GET
    @Path("/availableGames")
    @Produces(MediaType.APPLICATION_JSON)
    List<Game> getAvailableGames();

    @POST
    @Path("/rent/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response rent(@PathParam("id") Long id);

    @POST
    @Path("/return/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response returnGame(@PathParam("id") Long id);
}