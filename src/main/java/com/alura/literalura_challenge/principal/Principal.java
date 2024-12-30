package com.alura.literalura_challenge.principal;

import com.alura.literalura_challenge.model.Autor;
import com.alura.literalura_challenge.model.Datos;
import com.alura.literalura_challenge.model.DatosLibro;
import com.alura.literalura_challenge.model.Libro;
import com.alura.literalura_challenge.repository.AutorRepository;
import com.alura.literalura_challenge.service.ConsumoApi;
import com.alura.literalura_challenge.service.ConvertidorDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvertidorDatos convertidorDatos = new ConvertidorDatos();
    private AutorRepository repository;

    public Principal(AutorRepository repository) {
        this.repository = repository;
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
                ------------------------------------------------------
                Elija una opción a través de su número:
                1 - Buscar libro por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                6 - Top 10 libros más descargados
                7 - Buscar autor por nombre
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
                listarLibrosRegistrados();

                break;
            case 3:
                listarAutoresRegistrados();

                break;
            case 4:
                listarAutoresVivosByYear();

                break;
            case 5:
                listarLibrosPorIdioma();

                break;
            case 6:
                mostrarTop10Libros();

                break;
            case 7:
                buscarAutorPorNombre();

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

        try {
            DatosLibro datosLibro = getDatosLibro(busqueda);
            Optional<Libro> libroBuscado = repository.buscarLibroPorTitulo(datosLibro.titulo());

            if(libroBuscado.isPresent()) {
                System.out.println("\nNo se puede registrar el mismo libro más de una vez.\n");
            } else {
                Libro libro = new Libro(datosLibro.titulo(), datosLibro.idiomas().getFirst(), datosLibro.descargas());
                Optional<Autor> autorBuscado = repository.buscarAutorPorNombre(datosLibro.autores().getFirst().nombre());

                Autor autor = autorBuscado.orElseGet(() -> new Autor(
                        datosLibro.autores().getFirst().nombre(), datosLibro.autores().getFirst().yearNacimiento(),
                        datosLibro.autores().getFirst().yearMuerte(), new ArrayList<>()));
                libro.setAutor(autor);
                autor.getLibros().add(libro);
                repository.save(autor);
                System.out.println(libro.toString());
            }
        } catch (NoSuchElementException e) {
            System.out.println("Libro no encontrado\n");
        }
    }

    public void listarLibrosRegistrados() {
        List<Libro> librosRegistrados = repository.listarLibrosRegistrados();
        librosRegistrados.forEach(l -> System.out.println(l.toString()));
        calcularEstadisticasLibros(librosRegistrados);
    }

    public void listarAutoresRegistrados() {
        List<Autor> autoresRegistrados = repository.findAll();
        autoresRegistrados.forEach(a -> System.out.println(a.toString()));
    }

    public void listarAutoresVivosByYear() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar:");
        int year = Integer.parseInt(scanner.nextLine());
//        List<Autor> autores = repository.findAll();
//        autores.stream()
//                .filter(a -> year >= a.getYearNacimiento() && year <= a.getYearMuerte())
//                .forEach(a -> System.out.println(a.toString()));

        List<Autor> autores = repository.findByYearNacimientoLessThanEqualAndYearMuerteGreaterThanEqualOrYearMuerteIsNull(year, year);
        autores.forEach(a -> System.out.println(a.toString()));
    }

    public void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - fránces
                pt - portugués
                """);
        String idioma = scanner.nextLine();
        List<Libro> libros = repository.listarLibrosPorIdioma(idioma);
        libros.forEach(l -> System.out.println(l.toString()));
        calcularEstadisticasLibros(libros);
    }

    public void calcularEstadisticasLibros(List<Libro> libros) {
        DoubleSummaryStatistics estadisticas = libros.stream()
                .collect(Collectors.summarizingDouble(Libro::getDescargas));
        System.out.println("-------- Estadísticas de los libros encontrados --------");
        System.out.println("Cantidad de libros: " + estadisticas.getCount());
        System.out.println("Cantidad mínima de descargas: " + estadisticas.getMin());
        System.out.println("Promedio de descargas: " + estadisticas.getAverage());
        System.out.println("Cantidad máxima de descargas: " + estadisticas.getMax());
        System.out.println("Cantidad total de descargas: " + estadisticas.getSum());
    }

    public void mostrarTop10Libros() {
        List<Libro> top10Libros = repository.listarTop10LibrosDescargados();
        System.out.println("-------- Top 10 libros más descargados --------");
        top10Libros.forEach(l -> System.out.println(l.toString()));
    }

    public void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        String nombreAutor = scanner.nextLine();
        Optional<Autor> autorBuscado = repository.findFirstByNombreContainsIgnoreCase(nombreAutor);
        if (autorBuscado.isPresent()) {
            Autor autor = autorBuscado.get();
            System.out.println(autor.toString());
            autor.getLibros().forEach(l -> System.out.println(l.toString()));
            calcularEstadisticasLibros(autor.getLibros());
        } else {
            System.out.println("No se encontró ningún autor con el nombre ingresado");
        }

    }

}
