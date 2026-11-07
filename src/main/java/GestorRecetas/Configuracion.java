package GestorRecetas;

public class Configuracion {
    private int ultimaOpcionMenu;
    private int totalRecetas;

    public Configuracion() {
        this.ultimaOpcionMenu = 0;
        this.totalRecetas = 0;
    }

    public Configuracion(int ultimaOpcionMenu, int totalRecetas) {
        this.ultimaOpcionMenu = ultimaOpcionMenu;
        this.totalRecetas = totalRecetas;
    }

    public int getUltimaOpcionMenu() {
        return ultimaOpcionMenu;
    }

    public void setUltimaOpcionMenu(int ultimaOpcionMenu) {
        this.ultimaOpcionMenu = ultimaOpcionMenu;
    }

    public int getTotalRecetas() {
        return totalRecetas;
    }

    public void setTotalRecetas(int totalRecetas) {
        this.totalRecetas = totalRecetas;
    }
}