import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaBiblioteca {
    static class Libro {
        int id;
        String titulo;
        String autor;
        boolean prestado;

        public Libro(int id, String titulo, String autor) {
            this.id = id;
            this.titulo = titulo;
            this.autor = autor;
            this.prestado = false;
        }
    }

    static class NodoAVL {
        Libro libro;
        NodoAVL izquierda;
        NodoAVL derecha;
        int altura;

        public NodoAVL(Libro libro) {
            this.libro = libro;
            this.altura = 1;
        }
    }

    static class ArbolAVL {
        NodoAVL raiz;

        int altura(NodoAVL nodo) {
            if (nodo == null)
                return 0;
            return nodo.altura;
        }

        int maximo(int a, int b) {
            return Math.max(a, b);
        }

        NodoAVL rotarDerecha(NodoAVL nodoDesbalanceado) {
            NodoAVL nuevoPadre = nodoDesbalanceado.izquierda;
            NodoAVL nuevoDerecha = nuevoPadre.derecha;

            nodoDesbalanceado.izquierda = nuevoDerecha;
            nuevoPadre.derecha = nodoDesbalanceado;

            nodoDesbalanceado.altura = maximo(altura(nodoDesbalanceado.izquierda), altura(nodoDesbalanceado.derecha)) + 1;
            nuevoPadre.altura = maximo(altura(nuevoPadre.izquierda), altura(nuevoPadre.derecha)) + 1;

            return nuevoPadre;
        }

        NodoAVL rotarIzquierda(NodoAVL nodoDesbalanceado) {
            NodoAVL nuevoPadre = nodoDesbalanceado.derecha;
            NodoAVL nuevoIzquierda = nuevoPadre.izquierda;

            nuevoPadre.izquierda = nodoDesbalanceado;
            nodoDesbalanceado.derecha = nuevoIzquierda;

            nodoDesbalanceado.altura = maximo(altura(nodoDesbalanceado.izquierda), altura(nodoDesbalanceado.derecha)) + 1;
            nuevoPadre.altura = maximo(altura(nuevoPadre.izquierda), altura(nuevoPadre.derecha)) + 1;

            return nuevoPadre;
        }

        int obtenerBalance(NodoAVL nodo) {
            if (nodo == null)
                return 0;
            return altura(nodo.izquierda) - altura(nodo.derecha);
        }

        NodoAVL insertar(NodoAVL nodo, Libro libro) {
            if (nodo == null)
                return new NodoAVL(libro);

            if (libro.id < nodo.libro.id)
                nodo.izquierda = insertar(nodo.izquierda, libro);
            else if (libro.id > nodo.libro.id)
                nodo.derecha = insertar(nodo.derecha, libro);
            else
                return nodo;

            nodo.altura = 1 + maximo(altura(nodo.izquierda), altura(nodo.derecha));

            int balance = obtenerBalance(nodo);

            if (balance > 1 && libro.id < nodo.izquierda.libro.id)
                return rotarDerecha(nodo);

            if (balance < -1 && libro.id > nodo.derecha.libro.id)
                return rotarIzquierda(nodo);

            if (balance > 1 && libro.id > nodo.izquierda.libro.id) {
                nodo.izquierda = rotarIzquierda(nodo.izquierda);
                return rotarDerecha(nodo);
            }

            if (balance < -1 && libro.id < nodo.derecha.libro.id) {
                nodo.derecha = rotarDerecha(nodo.derecha);
                return rotarIzquierda(nodo);
            }

            return nodo;
        }

        NodoAVL eliminar(NodoAVL nodo, int id) {
            if (nodo == null)
                return nodo;

            if (id < nodo.libro.id)
                nodo.izquierda = eliminar(nodo.izquierda, id);
            else if (id > nodo.libro.id)
                nodo.derecha = eliminar(nodo.derecha, id);
            else {
                if ((nodo.izquierda == null) || (nodo.derecha == null)) {
                    NodoAVL temp = null;
                    if (temp == nodo.izquierda)
                        temp = nodo.derecha;
                    else
                        temp = nodo.izquierda;

                    if (temp == null) {
                        temp = nodo;
                        nodo = null;
                    } else
                        nodo = temp;
                } else {
                    NodoAVL temp = encontrarMinimo(nodo.derecha);
                    nodo.libro = temp.libro;
                    nodo.derecha = eliminar(nodo.derecha, temp.libro.id);
                }
            }

            if (nodo == null)
                return nodo;

            nodo.altura = maximo(altura(nodo.izquierda), altura(nodo.derecha)) + 1;

            int balance = obtenerBalance(nodo);

            if (balance > 1 && obtenerBalance(nodo.izquierda) >= 0)
                return rotarDerecha(nodo);

            if (balance > 1 && obtenerBalance(nodo.izquierda) < 0) {
                nodo.izquierda = rotarIzquierda(nodo.izquierda);
                return rotarDerecha(nodo);
            }

            if (balance < -1 && obtenerBalance(nodo.derecha) <= 0)
                return rotarIzquierda(nodo);

            if (balance < -1 && obtenerBalance(nodo.derecha) > 0) {
                nodo.derecha = rotarDerecha(nodo.derecha);
                return rotarIzquierda(nodo);
            }

            return nodo;
        }

        NodoAVL encontrarMinimo(NodoAVL nodo) {
            NodoAVL actual = nodo;
            while (actual.izquierda != null)
                actual = actual.izquierda;
            return actual;
        }

        void preOrden(NodoAVL nodo) {
            if (nodo != null) {
                System.out.println("ID: " + nodo.libro.id + ", Título: " + nodo.libro.titulo + ", Autor: " + nodo.libro.autor);
                preOrden(nodo.izquierda);
                preOrden(nodo.derecha);
            }
        }
        void inOrden(NodoAVL nodo) {
            if (nodo != null) {
                inOrden(nodo.izquierda);
                System.out.println("ID: " + nodo.libro.id + ", Título: " + nodo.libro.titulo + ", Autor: " + nodo.libro.autor);
                inOrden(nodo.derecha);
            }
        }
        void postOrden(NodoAVL nodo) {
            if (nodo != null) {
                postOrden(nodo.izquierda);
                postOrden(nodo.derecha);
                System.out.println("ID: " + nodo.libro.id + ", Título: " + nodo.libro.titulo + ", Autor: " + nodo.libro.autor);
            }
        }

        NodoAVL buscar(NodoAVL nodo, int id) {
            if (nodo == null || nodo.libro.id == id)
                return nodo;

            if (id < nodo.libro.id)
                return buscar(nodo.izquierda, id);

            return buscar(nodo.derecha, id);
        }

        void buscarPorAutor(NodoAVL nodo, String autor, List<Libro> librosEncontrados) {
            if (nodo == null)
                return;

            if (nodo.libro.autor.equals(autor))
                librosEncontrados.add(nodo.libro);

            buscarPorAutor(nodo.izquierda, autor, librosEncontrados);
            buscarPorAutor(nodo.derecha, autor, librosEncontrados);

        }

        void buscarPorTitulo(NodoAVL nodo, String titulo, List<Libro> librosEncontrados) {
            if (nodo == null)
                return;

            if (nodo.libro.titulo.equals(titulo))
                librosEncontrados.add(nodo.libro);

            buscarPorTitulo(nodo.izquierda, titulo, librosEncontrados);
            buscarPorTitulo(nodo.derecha, titulo, librosEncontrados);

        }

        void prestarLibro(NodoAVL nodo, int id) {
            if (nodo == null)
                return;

            if (nodo.libro.id == id) {
                if (!nodo.libro.prestado) {
                    nodo.libro.prestado = true;
                    System.out.println("El libro '" + nodo.libro.titulo + "' ha sido prestado.");
                } else {
                    System.out.println("El libro '" + nodo.libro.titulo + "' ya está prestado.");
                }
                return;
            }

            if (id < nodo.libro.id)
                prestarLibro(nodo.izquierda, id);
            else
                prestarLibro(nodo.derecha, id);
        }
        void devolverLibro(NodoAVL nodo, int id) {
            if (nodo == null)
                return;

            if (nodo.libro.id == id) {
                if (nodo.libro.prestado) {
                    nodo.libro.prestado = false;
                    System.out.println("El libro '" + nodo.libro.titulo + "' ha sido devuelto.");
                } else {
                    System.out.println("El libro '" + nodo.libro.titulo + "' no está prestado.");
                }
                return;
            }

            if (id < nodo.libro.id)
                devolverLibro(nodo.izquierda, id);
            else
                devolverLibro(nodo.derecha, id);
        }
    }

    public static void main(String[] args) {
        ArbolAVL arbol = new ArbolAVL();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println(" Menu");
            System.out.println("1. Cargar libros desde archivo");
            System.out.println("2. Insertar libro");
            System.out.println("3. Eliminar libro");
            System.out.println("4. Buscar libros por ID");
            System.out.println("5. Buscar libros por Autor");
            System.out.println("6. Buscar libros por Título");
            System.out.println("7. Mostrar todos los libros (pre orden)");
            System.out.println("8. Mostrar todos los libros (in orden)");
            System.out.println("9. Mostrar todos los libros (post orden)");
            System.out.println("10. Prestar libro");
            System.out.println("11. Devolver libro");
            System.out.println("12. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    cargarLibros(arbol);
                    break;
                case 2:
                    System.out.print("Ingrese el ID del libro: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el título del libro: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Ingrese el autor del libro: ");
                    String autor = scanner.nextLine();
                    Libro nuevoLibro = new Libro(id, titulo, autor);
                    arbol.raiz = arbol.insertar(arbol.raiz, nuevoLibro);
                    break;
                case 3:
                    System.out.print("Ingrese el ID del libro a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    arbol.raiz = arbol.eliminar(arbol.raiz, idEliminar);
                    System.out.println("Libro eliminado correctamente.");
                    break;
                case 4:
                    System.out.print("Ingrese el ID del libro a buscar: ");
                    int idBuscar = scanner.nextInt();
                    NodoAVL resultado = arbol.buscar(arbol.raiz, idBuscar);
                    if (resultado != null) {
                        System.out.println("Libro encontrado:");
                        System.out.println("ID: " + resultado.libro.id + ", Título: " + resultado.libro.titulo + ", Autor: " + resultado.libro.autor);
                    } else {
                        System.out.println("Libro no encontrado.");
                    }
                    break;
                case 5:
                    scanner.nextLine();
                    System.out.print("Ingrese el nombre completo del autor: ");
                    String autorBuscar = scanner.nextLine();
                    List<Libro> librosAutor = new ArrayList<>();
                    arbol.buscarPorAutor(arbol.raiz, autorBuscar, librosAutor);
                    if (!librosAutor.isEmpty()) {
                        System.out.println("Libros encontrados para el autor " + autorBuscar + ":");
                        for (Libro libro : librosAutor) {
                            System.out.println("ID: " + libro.id + ", Título: " + libro.titulo + ", Autor: " + libro.autor);
                        }
                    } else {
                        System.out.println("No se encontraron libros para el autor " + autorBuscar);
                    }
                    break;
                case 6:
                    scanner.nextLine();
                    System.out.print("Ingrese el título del libro: ");
                    String tituloBuscar = scanner.nextLine();
                    List<Libro> librosTitulo = new ArrayList<>();
                    arbol.buscarPorTitulo(arbol.raiz, tituloBuscar, librosTitulo);
                    if (!librosTitulo.isEmpty()) {
                        System.out.println("Libros encontrados con el título " + tituloBuscar + ":");
                        for (Libro libro : librosTitulo) {
                            System.out.println("ID: " + libro.id + ", Título: " + libro.titulo + ", Autor: " + libro.autor);
                        }
                    } else {
                        System.out.println("No se encontraron libros con el título " + tituloBuscar);
                    }
                    break;
                case 7:
                    System.out.println("Lista de libros (preorden):");
                    arbol.preOrden(arbol.raiz);
                    break;
                case 8:
                    System.out.println("Lista de libros (in orden):");
                    arbol.inOrden(arbol.raiz);
                    break;
                case 9:
                    System.out.println("Lista de libros (post orden):");
                    arbol.postOrden(arbol.raiz);
                    break;
                case 10:
                    System.out.print("Ingrese el ID del libro a prestar: ");
                    int idPrestar = scanner.nextInt();
                    arbol.prestarLibro(arbol.raiz, idPrestar);
                    break;
                case 11:
                    System.out.print("Ingrese el ID del libro a devolver: ");
                    int idDevolver = scanner.nextInt();
                    arbol.devolverLibro(arbol.raiz, idDevolver);
                    break;
                case 12:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                    break;
            }
        } while (opcion != 12);

        scanner.close();
    }


    static void cargarLibros(ArbolAVL arbol) {
        try {
            String rutaArchivo = "C:/Users/samue/IdeaProjects/Libreria SDG/src/libros.txt";
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] datosLibro = linea.split(",");
                if (datosLibro.length != 3) {
                    System.out.println("Error: formato incorrecto en la línea: " + linea);
                    continue;
                }
                int id = Integer.parseInt(datosLibro[0]);
                String titulo = datosLibro[1];
                String autor = datosLibro[2];
                Libro libro = new Libro(id, titulo, autor);
                arbol.raiz = arbol.insertar(arbol.raiz, libro);
            }
            reader.close();
            System.out.println("Libros cargados correctamente desde el archivo.");
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de libros.");
        } catch (NumberFormatException e) {
            System.out.println("Error: formato numérico incorrecto en el archivo de libros.");
        }
    }

}
