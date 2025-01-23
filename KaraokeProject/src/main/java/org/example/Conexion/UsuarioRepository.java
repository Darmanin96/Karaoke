package org.example.Conexion;

import org.example.Hibernate.Usuarios;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import org.example.models.Usuario;
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
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        } finally {
            em.close();
        }
    }


}

