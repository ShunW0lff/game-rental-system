package org.acme;

import java.util.List;

import org.acme.data.MemoryDataBase;
import org.acme.model.Game;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.logging.Log;


    @Path("/games")
public class GameResource {

    @Inject
MemoryDataBase data;

    @GET
    @Path("/add/{name}/{year}/{genre}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Game addGame(@PathParam("name") String name,
                    @PathParam("year") String year, @PathParam("genre") String genre){

        Game game = new Game();
        game.setName(name);
        game.setYear(year);
        game.setGenre(genre);

        return game;
    }

    @POST
    @Transactional
    @Path("/addGameForm")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Game addBookForm(@FormParam("name") String name,
                    @FormParam("year") String year, @FormParam("genre") String genre){

        Game game = new Game();
        game.setName(name);
        game.setYear(year);
        game.setGenre(genre);

        game.persist();
        return game;
    }

    @POST
    @Path("/addGameJSON")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Game addGameJSON(Game game){
        return game;
    }

    @Path("/remove")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String removeGame(){
        return "removed";
    }

    @GET
    @Path("/listBooks")
    //@RolesAllowed("Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Game> list(){
        Log.info("Listing Games");
        return data.getGames();
    }
}
