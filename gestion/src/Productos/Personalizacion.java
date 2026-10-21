package Productos;
import java.util.Scanner;


public class Personalizacion {

    public enum tipoTecnica {
        DTF,
        SUBLIMACION,
        BORDADO,
        SERIGRAFIA;

    }

    public enum tipoContenido {
        DISENO_PROPIO, MENSAJE, LOGO;
    }

    public enum kanban {

        PENDIENTE,
        EN_DISENO,
        ESPERANDO_APROBACION,
        APROBADO;
    }

    public enum TamanoPreferido {
        PEQUENO, MEDIANO, GRANDE;

    }
public enum servicioDiseno {LISTO_DE_CLIENTE, MODIFICACION, DISENO_COMPLETO;
}

    private tipoTecnica tecnica;
    private tipoContenido contenido;
    private kanban estadoDiseno;
    private String tema;
    private double costo;
    private TamanoPreferido tamano;
    private servicioDiseno diseno;
    private String archivoCliente;
private static final double costoDisenoCompleto = 20000;
private static final double costoModificarDiseno = 8000;

    public static Personalizacion crearDesdeConsola(Scanner sc) {

        System.out.println("\n******** PERSONALIZACIÓN **************");

        System.out.println("Técnica de estampado:");
        System.out.println("1. DTF");
        System.out.println("2. SUBLIMACION");
        System.out.println("3. BORDADO");
        System.out.println("4. SERIGRAFIA");

        int opcion = leerOpcion(sc, "Elige la técnica (entre 1 y 4): ", 1, 4);

        tipoTecnica tecnica;

        switch (opcion) {
            case 1:
                tecnica = tipoTecnica.DTF;
                break;
            case 2:
                tecnica = tipoTecnica.SUBLIMACION;
                break;
            case 3:
                tecnica = tipoTecnica.BORDADO;
                break;
            default:
                tecnica = tipoTecnica.SERIGRAFIA;}

        System.out.println("\n Tamaño del diseño:");
        System.out.println("1. Pequeño");
        System.out.println("2. Mediano");
        System.out.println("3. Grande");

        opcion = leerOpcion(sc, "Elige el tamaño (entre 1 y3): ", 1, 3);

        TamanoPreferido tamano;

        switch (opcion) {
            case 1:
                tamano = TamanoPreferido.PEQUENO;
                break;
            case 2:
                tamano = TamanoPreferido.MEDIANO;
                break;
            default:
                tamano = TamanoPreferido.GRANDE;}

        System.out.println("\n Tipo de contenido:");
        System.out.println("1. Mensaje o texto");
        System.out.println("2. Diseño propio o imagen");
        System.out.println("3. Logo");

        opcion = leerOpcion(sc, "Elige el contenido (entre 1 y 3): ", 1, 3);

        tipoContenido contenido;

        switch (opcion) {
            case 1:
                contenido = tipoContenido.MENSAJE;
                break;
            case 2:
                contenido = tipoContenido.DISENO_PROPIO;
                break;
            default:
                contenido = tipoContenido.LOGO;}

        System.out.println("\n Servicio de diseño:");
        System.out.println("1. Diseño listo del cliente");
        System.out.println("2. Modificación de diseño ($8000)");
        System.out.println("3. Diseño completo ($20000)");

        opcion = leerOpcion(sc, "Elige el servicio (entre 1 y 3): ", 1, 3);

        servicioDiseno diseno;

        switch (opcion) {
            case 1:
                diseno = servicioDiseno.LISTO_DE_CLIENTE;
                break;
            case 2:
                diseno = servicioDiseno.MODIFICACION;
                break;
            default:
                diseno = servicioDiseno.DISENO_COMPLETO;
        }

        System.out.println("\n Descripción del diseño (debe tener máximo 40 caracteres):");
        String tema = sc.nextLine();

        while (tema.length() == 0 || tema.length() > 40) {
            System.out.println(
                    "La descripción debe tener entre 1 y 40 caracteres.");
            tema = sc.nextLine();
        }

        System.out.print("Nombre del archivo de referencia (deja en blanco si no hay ninguno): ");
        String archivoCliente = sc.nextLine();

        return new Personalizacion(
                tecnica,
                contenido,
                tema,
                tamano,
                diseno,
                archivoCliente
        );
    }

    private static int leerOpcion(
            Scanner sc, String mensaje, int minimo, int maximo) {

        int opcion = 0;

        while (opcion < minimo || opcion > maximo) {
            System.out.print(mensaje);
            // ese hasNextint es un metodo del escaner pa que sepa si tiene un nextint acumulado despues de un numero para que no falle
            if (sc.hasNextInt()) {
                opcion = sc.nextInt();
                sc.nextLine();
                if (opcion < minimo || opcion > maximo) {
                    System.out.println("Opción no válida.");}
            } else {
                System.out.println("Debes ingresar un número.");
                sc.nextLine();}
        }

        return opcion;
    }


    public Personalizacion(tipoTecnica tecnica, tipoContenido contenido, String tema, TamanoPreferido tamano, servicioDiseno diseno, String archivoCliente)  {
        this.tecnica = tecnica;
        this.contenido = contenido;
        this.tema = tema;
        this.estadoDiseno = kanban.PENDIENTE;
        this.tamano = tamano;
        this.diseno = diseno;
        this.archivoCliente = archivoCliente;
        this.costo = costoTotal();

    }
    public void establecerDiseno(servicioDiseno diseno) {
        this.diseno = diseno;
        this.costo = costoTotal();
    }

    private double calcularCostoTecnica() {
        double costo = 0;
        if (tecnica == tipoTecnica.DTF) {
            if (tamano == TamanoPreferido.PEQUENO) {
                costo = 12000;
            } else if (tamano == TamanoPreferido.MEDIANO) {
                costo = 18000;
            } else {
                costo = 25000;
            }
        } else if (tecnica == tipoTecnica.SUBLIMACION) {

            if (tamano == TamanoPreferido.GRANDE) {
                costo = 15000;
            } else {
                costo = 9000;
            }

        } else if (tecnica == tipoTecnica.BORDADO) {

            if (tamano == TamanoPreferido.GRANDE) {
                costo = 35000;
            } else if (tamano == TamanoPreferido.MEDIANO) {
                costo = 26000;
            } else {
                costo = 16000;
            }
        } else if (tecnica == tipoTecnica.SERIGRAFIA) {

            if (tamano == TamanoPreferido.GRANDE) {
                costo = 20000;
            } else if (tamano == TamanoPreferido.MEDIANO) {
                costo = 14000;
            } else {
                costo = 8000;
            }
        }return costo;
    }
    public double obtenerCosto(){
        return this.costo;
    }

    public tipoTecnica obtenerTecnica(){
        return tecnica;
    }
    public void establecerTecnica(tipoTecnica tecnica){
        this.tecnica = tecnica;
        this.costo = costoTotal();

    }
    public void establecerTamano(TamanoPreferido tamano){
        this.tamano = tamano;
        this.costo = costoTotal();

    }

    public void establecerEstadoDiseno(kanban estado){this.estadoDiseno = estado;}
private double costoDiseno(){

        if(diseno == servicioDiseno.DISENO_COMPLETO) {
            return costoDisenoCompleto;
        }else if(diseno == servicioDiseno.MODIFICACION){
            return costoModificarDiseno;
        }
        return 0;
}
private double costoTotal(){
        return calcularCostoTecnica() + costoDiseno();
}
public void cambioKanban(){
       if(estadoDiseno == kanban.PENDIENTE) {
           if (diseno == servicioDiseno.LISTO_DE_CLIENTE) {
               estadoDiseno = kanban.APROBADO;
           } else {
               estadoDiseno = kanban.EN_DISENO;
           }}
else if (estadoDiseno == kanban.EN_DISENO) {
estadoDiseno = kanban.ESPERANDO_APROBACION;
           } else if (estadoDiseno == kanban.APROBADO){

       }else if (estadoDiseno == kanban.ESPERANDO_APROBACION){
    estadoDiseno = kanban.APROBADO;
       }
}

    public tipoTecnica getTecnica() {
        return tecnica;
    }
    public double getCosto() {
        return costo;
    }
    public String getTema() {
        return tema;
    }
    public TamanoPreferido getTamano() {
        return tamano;
    }
    public servicioDiseno getDiseno() {
        return diseno;
    }
    public kanban getEstadoDiseno() {
        return estadoDiseno;
    }
    public String getArchivoCliente() {
        return archivoCliente;
    }
    public tipoContenido getContenido(){ return contenido; }

}