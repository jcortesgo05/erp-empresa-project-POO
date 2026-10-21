package Entidades;
import Productos.*;
import Inventario.Inventario;
import Productos.Personalizacion;

import java.util.Scanner;

public class Tienda {
    private Scanner sc;
    private Inventario inventario;
    private RegaloPersonalizado ultimoRegalo;
    private Cierre cierre;

    public Tienda() {
        sc = new Scanner(System.in);
        inventario = new Inventario();
        ultimoRegalo = null;
        cierre = new Cierre();
        inventario.agregarProducto(new Producto("Taza", 18000, 40));
        inventario.agregarProducto(new Producto("Agenda", 35000, 20));
    }

    public void iniciar() {
        int opcion = 0;
        while (opcion != 7) {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");
            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    consultarProductos();
                    break;
                case 3:
                    registrarProveedor();
                    break;
                case 4:
                    consultarProveedor();
                    break;
                case 5:
                    hacerRegalo();
                    break;
                case 6:
                    verUltimoRegalo();
                    break;
                case 7:
                    System.out.println("Cerrando la jornada...");
                    break;
                default:
                    System.out.println("Opción no válida. Elige un número del 1 al 7.");
            }
        }
        System.out.println(cierre);
    }

    private void mostrarMenu() {
        System.out.println("\n======= REGALO DE GRACIA =======");
        System.out.println("1. Registrar producto");
        System.out.println("2. Consultar productos");
        System.out.println("3. Registrar proveedor");
        System.out.println("4. Consultar proveedor");
        System.out.println("5. Crear regalo personalizado");
        System.out.println("6. Ver último regalo");
        System.out.println("7. Salir y cerrar el día");
        System.out.println("================================");
    }


    private void registrarProducto() {
        if (inventario.hayEspacio()) {
            String nombre = leerTexto("Nombre del producto: ");
            int precio = leerEntero("Precio base (pesos, sin decimales): ");
            while (precio <= 0) {
                System.out.println("El precio debe ser mayor que 0.");
                precio = leerEntero("Precio base (pesos, sin decimales): ");
            }
            int stock = leerEntero("Unidades disponibles: ");
            while (stock < 0) {
                System.out.println("Las unidades no pueden ser negativas.");
                stock = leerEntero("Unidades disponibles: ");
            }
            Producto nuevo = new Producto(nombre, precio, stock);
            inventario.agregarProducto(nuevo);
            System.out.println("Producto registrado: " + nuevo);
        } else {
            System.out.println("El inventario está lleno (máximo 4 productos).");
        }
    }

    private void consultarProductos() {
        if (inventario.getCantidadProductos() == 0) {
            System.out.println("Todavía no hay productos registrados.");
        } else {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println(inventario);
        }
    }


    private void registrarProveedor() {
        String nombre = leerTexto("Nombre del proveedor: ");
        String telefono = leerTexto("Teléfono: ");
        while (telefono.length() < 7) {
            System.out.println("El teléfono debe tener al menos 7 caracteres.");
            telefono = leerTexto("Teléfono: ");
        }
        String correo = leerTexto("Correo: ");
        String suministra = leerTexto("¿Qué suministra? ");
        inventario.setProveedor(new Proveedor(nombre, telefono, correo, suministra));
        System.out.println("Proveedor registrado.");
    }

    private void consultarProveedor() {
        if (inventario.getProveedor() == null) {
            System.out.println("Todavía no hay un proveedor registrado.");
        } else {
            System.out.println("\n--- PROVEEDOR ---");
            System.out.println(inventario.getProveedor());
        }
    }


    private void hacerRegalo() {
        if (inventario.getCantidadProductos() == 0) {
            System.out.println("Primero registra un producto (opción 1).");
        } else {
            int numero = elegirNumeroProducto();
            Producto elegido = inventario.obtenerProducto(numero);
            if (elegido.estaDisponible()) {
                procesarRegalo(numero, elegido);
            } else {
                System.out.println("Ese producto está agotado.");
            }
        }
    }

    private int elegirNumeroProducto() {
        System.out.println("\n--- PRODUCTOS ---");
        System.out.println(inventario);
        int numero = leerEntero("Número del producto: ");
        while (numero < 1 || numero > inventario.getCantidadProductos()) {
            System.out.println("Producto no válido. Elige un número de la lista.");
            numero = leerEntero("Número del producto: ");
        }
        return numero;
    }

    private void procesarRegalo(int numero, Producto elegido) {
        // Validación de disponibilidad: no se puede pedir más de lo que hay
        int cantidad = leerEntero("Cantidad (disponibles: " + elegido.getStock() + "): ");
        while (cantidad < 1 || cantidad > elegido.getStock()) {
            System.out.println("La cantidad debe estar entre 1 y " + elegido.getStock() + ".");
            cantidad = leerEntero("Cantidad: ");
        }

        Personalizacion pers = crearPersonalizacion();
        Empaque empaque = crearEmpaque();

        RegaloPersonalizado regalo = new RegaloPersonalizado(elegido, pers, empaque, cantidad);
        regalo.calcularPrecio();
        System.out.println(regalo);

        if (leerSiNo("¿Desea continuar con el pedido? (s/n): ")) {
            // Regla: se pide un abono antes de registrar la venta
            double minimo = regalo.calcularAbonoMinimo();
            System.out.println("Para registrar la venta se requiere un abono mínimo del 30%: $" + minimo);
            int abono = leerEntero("Valor del abono (pesos): ");
            while (abono < minimo || abono > regalo.getTotal()) {
                System.out.println("El abono debe estar entre $" + minimo + " y $" + regalo.getTotal() + ".");
                abono = leerEntero("Valor del abono (pesos): ");
            }

            if (inventario.hayDisponibilidad(numero, cantidad)) {
                regalo.registrarAbono(abono);
                inventario.actualizarExistencias(numero, cantidad);
                ultimoRegalo = regalo;
                cierre.registrar(regalo);
                System.out.println("Venta registrada. Abono recibido: $" + abono);
                System.out.println("Saldo pendiente: $" + regalo.getSaldoPendiente());
            } else {
                System.out.println("Ya no hay unidades suficientes. Pedido cancelado.");
            }
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    private Personalizacion crearPersonalizacion() {
        return Personalizacion.crearDesdeConsola(sc);
    }


    private Empaque crearEmpaque() {
        String material = elegirMaterial();
        String tamano = "N/A";
        if (!material.equals("Sin empaque")) {
            tamano = elegirTamanoEmpaque();
        }
        return new Empaque(material, tamano);
    }



    private String elegirMaterial() {
        System.out.println("\nMaterial del empaque:");
        System.out.println("1. Caja de cartón (+$3000)");
        System.out.println("2. Bolsa de papel (+$1500)");
        System.out.println("3. Caja de madera (+$8000)");
        System.out.println("4. Sin empaque (+$0)");
        String material = "";
        while (material.length() == 0) {
            int op = leerEntero("Elige (1-4): ");
            switch (op) {
                case 1: material = "Caja de cartón"; break;
                case 2: material = "Bolsa de papel"; break;
                case 3: material = "Caja de madera"; break;
                case 4: material = "Sin empaque"; break;
                default: System.out.println("Opción no válida.");
            }
        }
        return material;
    }

    private String elegirTamanoEmpaque() {
        System.out.println("\nTamaño del empaque:");
        System.out.println("1. Pequeño (+$0)");
        System.out.println("2. Mediano (+$1000)");
        System.out.println("3. Grande (+$2000)");
        String tamano = "";
        while (tamano.length() == 0) {
            int op = leerEntero("Elige (1-3): ");
            switch (op) {
                case 1: tamano = "Pequeño"; break;
                case 2: tamano = "Mediano"; break;
                case 3: tamano = "Grande"; break;
                default: System.out.println("Opción no válida.");
            }
        }
        return tamano;
    }

    private void verUltimoRegalo() {
        if (ultimoRegalo == null) {
            System.out.println("Todavía no hay regalos confirmados.");
        } else {
            System.out.println(ultimoRegalo);
        }
    }

    // ---- Lectura de datos ----
    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String texto = sc.nextLine();
        while (texto.length() == 0) {
            System.out.println("Este dato no puede estar vacío.");
            System.out.print(mensaje);
            texto = sc.nextLine();
        }
        return texto;
    }

    private boolean leerSiNo(String mensaje) {
        boolean valido = false;
        boolean respuesta = false;
        while (!valido) {
            System.out.print(mensaje);
            String r = sc.nextLine();
            if (r.equals("s") || r.equals("S")) {
                respuesta = true;
                valido = true;
            } else if (r.equals("n") || r.equals("N")) {
                respuesta = false;
                valido = true;
            } else {
                System.out.println("Responde con s o n.");
            }
        }
        return respuesta;
    }
}
