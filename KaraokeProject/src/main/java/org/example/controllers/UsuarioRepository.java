package org.example.controllers;

import org.example.Hibernate.Usuarios;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class UsuarioRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("KaraokePU");

    public boolean existeNombreOEmail(String nombre, String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM Usuarios u WHERE u.nombre = :nombre OR u.email = :email",
                    Long.class
            );
            query.setParameter("nombre", nombre);
            query.setParameter("email", email);

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public void guardarUsuario(Usuarios usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}

