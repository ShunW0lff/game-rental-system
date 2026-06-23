package org.acme.data;


import java.util.ArrayList;
import java.util.List;

import org.acme.model.Game;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemoryDataBase {

    private List<Game> data;

    // Construtor
    public MemoryDataBase(){
        this.data = new ArrayList<>();

        Game game1 = new Game();
        game1.setName("Resident Evil 4");
        game1.setYear("2005");
        game1.setGenre("Horror");
        game1.setStock(10);

        Game game2 = new Game();
        game2.setName("Devil May Cry 5");
        game2.setYear("2019");
        game2.setGenre("Hack 'n' Slash");
        game2.setStock(5);

        // Inserção dos jogos dentro do array
        data.add(game1);
        data.add(game2);

    }

    public List<Game> getGames(){
        return this.data;
    }


    
}
