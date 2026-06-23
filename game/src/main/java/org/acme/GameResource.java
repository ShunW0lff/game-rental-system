package org.acme;

import java.util.List;

import org.acme.model.Game;

import io.quarkus.logging.Log;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
    @Path("/games")
public class GameResource {

    //@Inject
    //MemoryDataBase database;

    //Adiciona um jogo no banco de dados (somente admin)
    @POST
    @Transactional
    @Path("/addGameJSON")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Game game) {
        if (game.getStock() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Inventory cannot be negative").build();
        }
        game.persist();
        Log.info("Game added: " + game.getName());
        return Response.status(Response.Status.CREATED).entity(game).build();
    }

    //Retorna os jogos cadastrados no banco de dados
    @GET
    @Path("/listGames")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> list() {
        Log.info("Listando jogos");
        return Game.listAll();
    }

  // Aluga um jogo (somente admin e user)
    @POST
    @Transactional
    @Path("/rentGame/{id}")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_JSON)
    public Response alugar(@PathParam("id") Long id) {
        Game game = Game.findById(id);
        if (game == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Game not found").build();
        }
        if (game.getStock() <= 0) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("No copies available for rent").build();
        }
        game.setStock(game.getStock() - 1);
        Log.info("Rented Game: " + game.getName() + " | Stock remaining: " + game.getStock());
        return Response.ok(game).build();
    }

      // Devolve um jogo (somente admin e user)
    @POST
    @Transactional
    @Path("/returnGame/{id}")
    @RolesAllowed({ "Admin", "User" })
    @Produces(MediaType.APPLICATION_JSON)
    public Response devolver(@PathParam("id") Long id) {
        Game game = Game.findById(id);
        if (game == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Game not found").build();
        }
        game.setStock(game.getStock() + 1);
        Log.info("Game returned: " + game.getName() + " | Stock remaining: " + game.getStock());
        return Response.ok(game).build();
    }

    // Remove um jogo (somente admin)
@DELETE
@Transactional
@Path("/delete/{id}")
@RolesAllowed("Admin")
@Produces(MediaType.APPLICATION_JSON)
public Response delete(@PathParam("id") Long id) {
    Game game = Game.findById(id);
    if (game == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Game not found").build();
    }
    game.delete();
    Log.info("Game deleted: " + game.getName());
    return Response.noContent().build();
}
    }

