package org.example.models;

public class Usuario {
    private String nombre;
    private String email;
    private byte[] contrasena; // Cambiado de String a byte[]

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getContrasena() { // Devuelve byte[]
        return contrasena;
    }

    public void setContrasena(byte[] contrasena) { // Acepta byte[]
        this.contrasena = contrasena;
    }
}
