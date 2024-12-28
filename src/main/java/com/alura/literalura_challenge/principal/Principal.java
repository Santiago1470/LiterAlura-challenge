package com.alura.literalura_challenge.principal;

import com.alura.literalura_challenge.model.Autor;
import com.alura.literalura_challenge.model.Datos;
import com.alura.literalura_challenge.model.DatosLibro;
import com.alura.literalura_challenge.model.Libro;
import com.alura.literalura_challenge.repository.LibroRepository;
import com.alura.literalura_challenge.service.ConsumoApi;
import com.alura.literalura_challenge.service.ConvertidorDatos;

import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvertidorDatos convertidorDatos = new ConvertidorDatos();
    private LibroRepository libroRepository;

    public Principal(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void iniciar() {
        byte opcion = 0;
        do {
            System.out.println(mostrarMenu());
            opcion = Byte.parseByte(scanner.nextLine());
            seleccionarOpcion(opcion);
        } while (opcion != 0);
    }

    public String mostrarMenu() {
        return """
                Elija una opción a través de su número:
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                0 - Salir
                """;
    }

    public void seleccionarOpcion(byte opcion) {
        switch (opcion) {
            case 0:
                System.out.println("Gracias por usar nuestros servicios.");

                break;
            case 1:
                buscarLibro();

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                System.out.println("La opción ingresada no existe.");
        }
    }

    public DatosLibro getDatosLibro(String busqueda) {
        String json = consumoApi.obtenerDatosApi(URL_BASE + "/?search=" + busqueda.replace(" ", "+"));
        Datos datos = convertidorDatos.convertirDatos(json, Datos.class);
        return datos.datosLibros().getFirst();
    }

    public void buscarLibro() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        String busqueda = scanner.nextLine();
        DatosLibro datosLibro = getDatosLibro(busqueda);
        System.out.println(datosLibro);
        Libro libro = new Libro(datosLibro.titulo(), datosLibro.idiomas().getFirst(), datosLibro.descargas());
        Autor autor = new Autor(datosLibro.autores().getFirst().nombre(),
                datosLibro.autores().getFirst().year_nacimiento(), datosLibro.autores().getFirst().year_muerte());
        libro.setAutor(autor);
        System.out.println(libro.toString());
        libroRepository.save(libro);
    }
}
