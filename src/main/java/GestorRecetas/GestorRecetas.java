package GestorRecetas;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorRecetas {
    private static final String RUTA_FICHERO = "src/main/resources/";
    private static final String FICHERO_RECETAS = "recetas.dat";
    private static final String FICHERO_CONFIG = "configuracion.json";
    private static final String FICHERO_XML = "recetas.xml";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        crearCatalogo();

        recuperarJSON();


        boolean menu = true;
        do {

            System.out.println("--- Menu gestor recetas ---");
            System.out.println("1- Mostrar recetas almacenadas");
            System.out.println("2- Buscar recetas por id");
            System.out.println("3- Modificar tiempo de preparación de una receta");
            System.out.println("4- Añadir una receta nueva");
            System.out.println("5- Generar un XML con las recetas");
            System.out.println("6- Finalizar y crear JSON");
            System.out.println("Elige una opción: ");

            int opcion = sc.nextInt();

            switch(opcion){
                case 1:
                    mostrarRecetas();
                    break;

                case 2:
                    buscarRecetaId();
                    break;

                case 3:
                    modificarTiempoPreparacion();
                    break;

                case 4:
                    anadirReceta();
                    break;

                case 5:
                    generarXML();
                    break;

                case 6:
                    crearJSON();
                    menu = false;
                    break;

            }

        }while(menu == true);

    }

    public static void crearCatalogo(){
        File ficheroRecetas = new File (RUTA_FICHERO+FICHERO_RECETAS); // uso file para poder comprobar después si el fichero existe

        if (!ficheroRecetas.exists()){
            System.out.println("El fichero recetas.dat no existe");

            // si no existe, creo la lista catálogo y meto dentro de esa lista la información de las primeras recetas
            List <Receta> catalogo = new ArrayList<>();
            catalogo.add(new Receta(1,"Croquetas",120,"Huevo, bechamel, pan rallado"));
            catalogo.add(new Receta(2,"Ensalada César",15,"Lechuga, tomate, pollo, cebolla, salsa"));

            // uso object outputstream para poder escribir objetos dentro del fichero
            try(ObjectOutputStream escribirFichero = new ObjectOutputStream(new FileOutputStream(RUTA_FICHERO + FICHERO_RECETAS))){
                for (int i = 0; i< catalogo.size(); i++){
                    escribirFichero.writeObject(catalogo.get(i));
                }
                System.out.println("Se ha creado un catálogo con dos recetas iniciales");
            }catch (IOException e){
                System.out.println("Error al crear el catalogo");
            }
        }
    }

    public static void mostrarRecetas(){

        // leo el fichero con un bucle para mostrar todas las recetas
        try (ObjectInputStream leerFichero =new ObjectInputStream(new FileInputStream(RUTA_FICHERO + FICHERO_RECETAS))) {

            while(true){
                Receta recetaLeida = (Receta) leerFichero.readObject();
                System.out.println(recetaLeida);
            }

        } catch (EOFException e){

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero recetas.dat");
        }
    }

    public static void buscarRecetaId(){
        Scanner sc = new Scanner(System.in);

        File fichero = new File(RUTA_FICHERO + FICHERO_RECETAS); //compruebo primero si existe el fichero
        if (!fichero.exists()) {
            System.out.println("No existe el fichero recetas.dat");
            return; //salir del método
        }

        System.out.println("Dime el id de la receta que quieras buscar: ");
        int idBuscado = sc.nextInt();

        // leo el fichero y durante el bucle busca la receta con el mismo id pedido y lo muestra por pantalla
        try (ObjectInputStream leerFichero =new ObjectInputStream(new FileInputStream(RUTA_FICHERO + FICHERO_RECETAS))) {

            while(true){
                Receta recetaLeida = (Receta) leerFichero.readObject();
                if (recetaLeida.getId() == idBuscado){
                    System.out.println(recetaLeida);
                }
            }

        } catch (EOFException e){

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero recetas.dat"+ e.getMessage());
        }
    }

    public static void modificarTiempoPreparacion(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Dime el id de la receta a la que le quieras cambiar el tiempo de preparación: ");
        int idBuscadoTiempo = sc.nextInt();
        System.out.println("Dime a cuánto tiempo (minutos) quieres cambiarlo: ");
        int nuevoTiempoPreparacion = sc.nextInt();

        // creo una lista para almacenar las recetas de nuevo más la que vamos a modificar
        List<Receta> listaRecetas = new ArrayList<>();

        // leo el fichero como en buscarRecetaId() y voy almacenando todas en listaRecetas. En el bucle le cambiamos el tiempo a la receta seleccionada y también se guarda
        try (ObjectInputStream leerFichero = new ObjectInputStream(new FileInputStream(RUTA_FICHERO + FICHERO_RECETAS))) {

            while(true){
                Receta recetaLeida = (Receta) leerFichero.readObject();
                if (recetaLeida.getId() == idBuscadoTiempo){
                    recetaLeida.setTiempoPreparacion(nuevoTiempoPreparacion);
                }
                listaRecetas.add(recetaLeida);
            }

        } catch (EOFException e){

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero recetas.dat");
        }


        // reescribimos en el fichero recetas.dat las recetas que hemos almacenado en listaRecetas pero actualizadas con el nuevo tiempo de preparación
        try (ObjectOutputStream escribirFichero = new ObjectOutputStream(new FileOutputStream(RUTA_FICHERO + FICHERO_RECETAS))) {
            for (Receta r : listaRecetas) {
                escribirFichero.writeObject(r);
            }
            System.out.println("Tiempo de preparación modificado y guardado");
        } catch (IOException e) {
            System.out.println("Error al escribir el fichero recetas.dat");
        }
    }

    public static void anadirReceta(){
        Scanner sc = new Scanner(System.in);

        //pido los datos de la receta
        System.out.println("Introduce el id: ");
        int id = sc.nextInt();
        sc.nextLine(); // al ejecutar el programa se saltaba "String nombre = sc.nextLine();" así que he añadido esta línea para que al pulsar intro lo detecte y ya pueda escribir el nombre

        System.out.println("Introduce el nombre: ");
        String nombre = sc.nextLine();

        System.out.println("Introduce el tiempo de preparación (en minutos): ");
        int tiempoPreparacion = sc.nextInt();
        sc.nextLine();

        System.out.println("Introduce los ingredientes (en una misma linea): ");
        String ingredientes = sc.nextLine();

        // almaceno los datos en la lista recetaNueva
        List <Receta> recetaNueva = new ArrayList<>();
        recetaNueva.add(new Receta(id,nombre,tiempoPreparacion,ingredientes));

        // Escribimos la receta nueva en el fichero de recetas. append true para seguir escribiendo en el fichero sin borrar lo que ya había
        try(ObjectOutputStream escribirFichero = new ObjectOutputStream(new FileOutputStream(RUTA_FICHERO + FICHERO_RECETAS, true))){
            for (int i = 0; i< recetaNueva.size(); i++){
                escribirFichero.writeObject(recetaNueva.get(i));
            }
            System.out.println("Se ha añadido la receta a recetas.dat");

        } catch (FileNotFoundException e) {
            System.out.println("No se ha encontrado el fichero");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo");
        }
    }

    public static void recuperarJSON (){
        Configuracion configuracion; //creamos objeto configuración
        Gson gson = new Gson();
        File ficheroJson = new File (RUTA_FICHERO+FICHERO_CONFIG); // creamos file para comprobar si existe

        if (ficheroJson.exists()) {
            try (FileReader reader = new FileReader(ficheroJson)) {
                configuracion = gson.fromJson(reader, Configuracion.class); // si existe lee el fichero configuracion.json y lo mete en el objeto configuracion con los datos de la clase Configuracion

                System.out.println("Última opción del menu utilizada: " + configuracion.getUltimaOpcionMenu()); // mostramos los datos
                System.out.println("Total de recetas almacenadas: " + configuracion.getTotalRecetas());

            } catch (IOException e) {
                System.out.println("Error al leer el documento JSON");
            }
        } else {
            System.out.println("No existe el fichero JSON"); // si no existe se muestra este mensaje por pantalla y el programa sigue con normalidad
        }
    }

    public static void crearJSON () {
        int totalRecetas = 0; // variable para contar todas las recetas

        // leemos el fichero recetas.dat y vamos sumando 1 a la variable por cada receta
        try (ObjectInputStream leerFichero =new ObjectInputStream(new FileInputStream(RUTA_FICHERO + FICHERO_RECETAS))) {

            while(true){
                leerFichero.readObject();
                totalRecetas++;
            }

        } catch (EOFException e){

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // creamos un nuevo objeto de Configuración y actualizamos el total de recetas
        Configuracion configuracion = new Configuracion();
        configuracion.setTotalRecetas(totalRecetas);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // escribimos el fichero configuracion.json con los datos del objeto configuracion actualizados
        try (FileWriter escribirfichero = new FileWriter((RUTA_FICHERO + FICHERO_CONFIG))) {
            gson.toJson(configuracion, escribirfichero);
            System.out.println("Documento JSON creado/actualizado");

        } catch (IOException e) {
            System.out.println("Error al escribir el documento JSON.");

        }
    }

    public static void generarXML (){
        // creo una lista para almacenar todas las recetas de recetas.dat
        List<Receta> listaRecetas = new ArrayList<>();

        // leemos recetas.dat y añadimos cada receta a la lista listaRecetas
        try (ObjectInputStream leerFichero = new ObjectInputStream(new FileInputStream(RUTA_FICHERO + FICHERO_RECETAS))) {
            while (true) {
                Receta receta = (Receta) leerFichero.readObject();
                listaRecetas.add(receta);
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // hago la estructura del XML, la idea es que quede así:
        // <catalogo>
        //      <recetas>
        //          <receta>
        //              <id></id>
        //              <nombre></nombre>
        //              <tiempoPreparacion></tiempoPreparacion>
        //              <ingredientes></ingredientes>
        //          </receta>
        //          ... más recetas...
        //      </recetas>
        // </catalogo>
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();

            // elemento raiz
            Element catalogo = documento.createElement("catalogo");
            documento.appendChild(catalogo);

            // elemento recetas
            Element recetas = documento.createElement("recetas");
            catalogo.appendChild(recetas);

            // bucle para añadir todas las recetas que se hayan añadido
            for (int i = 0; i< listaRecetas.size(); i++){
                Receta r = listaRecetas.get(i); // selecciono el objeto según la posicion

                // elemento receta
                Element receta = documento.createElement("receta");
                recetas.appendChild(receta);

                // elemento id
                Element id = documento.createElement("id");
                id.setTextContent(String.valueOf(r.getId())); // setTextContent para agregar texto dentro del elemento
                receta.appendChild(id);

                // elemento nombre
                Element nombre = documento.createElement("nombre");
                nombre.setTextContent(String.valueOf(r.getNombre()));
                receta.appendChild(nombre);

                // elemento tiempoPreparacion
                Element tiempoPreparacion = documento.createElement("tiempoPreparacion");
                tiempoPreparacion.setTextContent(String.valueOf(r.getTiempoPreparacion()));
                receta.appendChild(tiempoPreparacion);

                // elemento ingredientes
                Element ingredientes = documento.createElement("ingredientes");
                ingredientes.setTextContent(String.valueOf(r.getIngredientes()));
                receta.appendChild(ingredientes);

            }

            // lo guardamos en el fichero XML
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(documento),new StreamResult(new File(RUTA_FICHERO + FICHERO_XML)));

            System.out.println("Documento XML creado correctamente.");

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}