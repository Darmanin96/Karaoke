package org.example.controllers;

import org.example.Hibernate.Canciones;
import org.example.Hibernate.CancionesCantada;
import org.example.Hibernate.Usuarios;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Date;

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

    public boolean existeCancion(String titulo, String artista) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(c) FROM Canciones c WHERE c.titulo = :titulo OR c.artista = :artista",
                    Long.class

            );
            query.setParameter("titulo", titulo);
            query.setParameter("artista", artista);

            return query.getSingleResult() > 0;
        }finally {
            em.close();
        }
    }

    public void guardarCancion(Canciones cancion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cancion);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }

    public boolean existeCancionCantada(String titulo) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(c) FROM CancionesCantada c WHERE c.titulo = :titulo",
                    Long.class
            );
            query.setParameter("titulo", titulo);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public void guardarCancionCantada(String titulo, Date fecha, int vecesCantada) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            CancionesCantada cancion = new CancionesCantada();
            cancion.setTitulo(titulo);
            cancion.setFecha(fecha);
            cancion.setVecesCantada(vecesCantada);
            em.persist(cancion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    }




