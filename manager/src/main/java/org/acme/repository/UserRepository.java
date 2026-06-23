package org.acme.repository;

import org.acme.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    /**
     * Realiza o login do usuário verificando o email e a senha.
     * @param email Email do usuário.
     * @param password Senha do usuário.
     * @return O usuário correspondente ao email e senha fornecidos, ou null se não encontrado.
     */
    public User login(String email, String password) {
        return find("email = ?1 and password = ?2", email, password).firstResult();
    }
    
}