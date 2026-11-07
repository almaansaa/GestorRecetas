package GestorRecetas;

import java.io.Serializable;

public class Receta implements Serializable {
    private int id;
    private String nombre;
    private int tiempoPreparacion; // en minutos
    private String ingredientes;

    public Receta(int id, String nombre, int tiempoPreparacion, String ingredientes) {
        this.id = id;
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.ingredientes = ingredientes;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    @Override
    public String toString() {
        return "ID: " + id +" // Nombre: " + nombre + " // Tiempo de preparación: " + tiempoPreparacion + " // Ingredientes: " + ingredientes;
    }
}