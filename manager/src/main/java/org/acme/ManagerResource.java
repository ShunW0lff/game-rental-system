package org.acme;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.acme.model.Game;
import org.acme.model.User;
import org.acme.repository.UserRepository;
import org.acme.restClient.IGame;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// Web Service
@Path("/manager")
public class ManagerResource {
    
    @Inject
    @RestClient
    IGame gameService;

    @Inject
    UserRepository  userRepository;

    private static final String ISSUER = "manager";


    @Inject
    @ConfigProperty(name = "pw2.message", defaultValue = "Olá, mundo!")
    String message;


    @Path("/list")
    @GET
    @RolesAllowed("Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> list(){
        Log.info("Listing games");
        return gameService.getGames();
    }

    @GET
    @Path("/ping")
    @PermitAll
    public String ping(){
        return this.message;
    }


    @POST
    @Path("/jwt")
    @PermitAll
    @Timeout(3000)
    @Retry(maxRetries = 3, delay = 500)
    @CircuitBreaker
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String generate(
        @FormParam("email") final String email, 
        @FormParam("password") final String password) {

            Log.info("Generating JWT for email: " + email);

            User user = userRepository.login(email, password);    
            if (user == null) {
                Log.warn("Invalid email or password for email: " + email);
                throw new WebApplicationException("Invalid email or password", 401);
            }
            return Jwt.issuer(ISSUER)
                .upn(user.getEmail())
                .groups(new HashSet<>(Arrays.asList(user.getRole())))
                .claim(Claims.email, user.getEmail())
                .sign();
    }

    // Lista jogos disponiveis
    @GET
    @Path("/games/availableGames")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(3000)
    @Retry(maxRetries = 5, delay = 500)
    @CircuitBreaker
    public List<Game> listAvailableGames() {
        Log.info("Games from manager service: ");
        return gameService.getAvailableGames();
    }

    // Aluga um jogo
    @POST
    @Path("/rent/{id}")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(3000)
    @Retry(maxRetries = 3, delay = 500)
    @CircuitBreaker
    public Response rent(@PathParam("id") Long id) {
        Log.info("Requesting game rental ID=" + id);
        return gameService.rent(id);
    }

    // Devolve um jogo 
    @POST
    @Path("/return/{id}")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(3000)
    @Retry(maxRetries = 3, delay = 500)
    @CircuitBreaker
    public Response returnGame(@PathParam("id") Long id) {
        Log.info("Requesting game return ID=" + id);
        return gameService.returnGame(id);
    }


}