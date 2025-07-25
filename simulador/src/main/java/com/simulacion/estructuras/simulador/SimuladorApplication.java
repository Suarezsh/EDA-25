package com.simulacion.estructuras.simulador;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
@SpringBootApplication
@RestController
public class SimuladorApplication implements WebMvcConfigurer {
    @GetMapping("/api/hola")
    public ResponseEntity<String> holaMundo() {
        return ResponseEntity.ok("¡Hola desde el backend simplificado!");
    }
    static abstract class Nodo<T extends Comparable<T>> {
        T dato;
        public Nodo(T dato) {
            this.dato = dato;
        }
        public T getDato() {
            return dato;
        }
        public void setDato(T dato) {
            this.dato = dato;
        }
        public abstract String getRepresentacionGrafica();
        public abstract String getExplicacion();
    }
    static class NodoArbolBinario<T extends Comparable<T>> extends Nodo<T> {
        NodoArbolBinario<T> izquierdo;
        NodoArbolBinario<T> derecho;
        public NodoArbolBinario(T dato) {
            super(dato);
            this.izquierdo = null;
            this.derecho = null;
        }
        public NodoArbolBinario<T> getIzquierdo() {
            return izquierdo;
        }
        public void setIzquierdo(NodoArbolBinario<T> izquierdo) {
            this.izquierdo = izquierdo;
        }
        public NodoArbolBinario<T> getDerecho() {
            return derecho;
        }
        public void setDerecho(NodoArbolBinario<T> derecho) {
            this.derecho = derecho;
        }
        @Override
        public String getRepresentacionGrafica() {
            return String.valueOf(dato);
        }
        @Override
        public String getExplicacion() {
            return "Nodo con valor: " + dato;
        }
    }
    static class ArbolBinario<T extends Comparable<T>> {
        private NodoArbolBinario<T> raiz;
        private String ultimaOperacionExplicacion = "Ninguna operación realizada.";
        public ArbolBinario() {
            this.raiz = null;
        }
        public void insertar(T dato) {
            raiz = insertarRec(raiz, dato);
            ultimaOperacionExplicacion = "Insertado el dato: " + dato;
        }
        private NodoArbolBinario<T> insertarRec(NodoArbolBinario<T> nodo, T dato) {
            if (nodo == null) {
                return new NodoArbolBinario<>(dato);
            }
            int comparacion = dato.compareTo(nodo.getDato());
            if (comparacion < 0) {
                nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), dato));
            } else if (comparacion > 0) {
                nodo.setDerecho(insertarRec(nodo.getDerecho(), dato));
            }
            return nodo;
        }
        public void eliminar(T dato) {
            NodoArbolBinario<T> nodoAEliminar = buscarNodo(raiz, dato);
            if (nodoAEliminar != null) {
                raiz = eliminarRec(raiz, dato);
                ultimaOperacionExplicacion = "Eliminado el dato: " + dato;
            } else {
                ultimaOperacionExplicacion = "El dato " + dato + " no se encontró para eliminar.";
            }
        }
        private NodoArbolBinario<T> eliminarRec(NodoArbolBinario<T> nodo, T dato) {
            if (nodo == null) {
                return null;
            }
            int comparacion = dato.compareTo(nodo.getDato());
            if (comparacion < 0) {
                nodo.setIzquierdo(eliminarRec(nodo.getIzquierdo(), dato));
            } else if (comparacion > 0) {
                nodo.setDerecho(eliminarRec(nodo.getDerecho(), dato));
            } else { 
                if (nodo.getIzquierdo() == null) {
                    return nodo.getDerecho();
                } else if (nodo.getDerecho() == null) {
                    return nodo.getIzquierdo();
                }
                NodoArbolBinario<T> sucesor = encontrarMinimo(nodo.getDerecho());
                nodo.setDato(sucesor.getDato()); 
                nodo.setDerecho(eliminarRec(nodo.getDerecho(), sucesor.getDato()));
            }
            return nodo;
        }
        private NodoArbolBinario<T> encontrarMinimo(NodoArbolBinario<T> nodo) {
            while (nodo.getIzquierdo() != null) {
                nodo = nodo.getIzquierdo();
            }
            return nodo;
        }
        private NodoArbolBinario<T> buscarNodo(NodoArbolBinario<T> nodo, T dato) {
            if (nodo == null || nodo.getDato().equals(dato)) {
                return nodo;
            }
            int comparacion = dato.compareTo(nodo.getDato());
            if (comparacion < 0) {
                return buscarNodo(nodo.getIzquierdo(), dato);
            } else {
                return buscarNodo(nodo.getDerecho(), dato);
            }
        }
        public List<String> getRepresentacionCompleta() {
            List<String> representacion = new ArrayList<>();
            if (raiz == null) {
                representacion.add("El árbol está vacío.");
                return representacion;
            }
            obtenerRepresentacionRec(raiz, representacion, 0);
            return representacion;
        }
        private void obtenerRepresentacionRec(NodoArbolBinario<T> nodo, List<String> representacion, int nivel) {
            if (nodo != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < nivel; i++) sb.append("  ");
                sb.append("- ").append(nodo.getDato());
                if (nodo.getIzquierdo() != null || nodo.getDerecho() != null) {
                    sb.append(" (");
                    sb.append(nodo.getIzquierdo() != null ? "Izq: " + nodo.getIzquierdo().getDato() : "Izq: null");
                    sb.append(", ");
                    sb.append(nodo.getDerecho() != null ? "Der: " + nodo.getDerecho().getDato() : "Der: null");
                    sb.append(")");
                }
                representacion.add(sb.toString());
                obtenerRepresentacionRec(nodo.getIzquierdo(), representacion, nivel + 1);
                obtenerRepresentacionRec(nodo.getDerecho(), representacion, nivel + 1);
            }
        }
        public String getUltimaOperacionExplicacion() {
            return ultimaOperacionExplicacion;
        }
    }
    

    public class NodeAVL<E extends Comparable<E>> {
    private E data;
    private NodeAVL<E> left;
    private NodeAVL<E> right;
     int altura;
    private int fe; // Factor de equilibrio
    public NodeAVL(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.fe = 0;
        this.altura = 1;
    }
    public E getData() {
        return data;
    }
    public void setData(E data) {
        this.data = data;
    }
    public int getAltura() {
            return altura;
        }
     public void setAltura(int altura) {
            this.altura = altura;
        }
    public NodeAVL<E> getLeft() {
        return left;
    }
    public void setLeft(NodeAVL<E> left) {
        this.left = left;
    }
    public NodeAVL<E> getRight() {
        return right;
    }
    public void setRight(NodeAVL<E> right) {
        this.right = right;
    }
    public int getFe() {
        return fe;
    }
    public void setFe(int fe) {
        this.fe = fe;
    }
    public String getRepresentacionGrafica() {
            return String.valueOf(data) + "(H:" ;
        }
    public String getExplicacion() {
            return "Nodo con valor: " + data + ", Altura: " + altura;
        }
    @Override
    public String toString() {
        return String.valueOf(data);
    }
}

public class AVL<E extends Comparable<E>> {
    private NodeAVL<E> root;
    private String ultimaOperacionExplicacion = "Ninguna operación realizada.";
    private boolean height;

    public AVL() {
        this.root = null;
    }

    public NodeAVL<E> getRootAVL() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(E x) throws ExceptionItemDuplicate {
        root = insertRec(x, root);
        ultimaOperacionExplicacion = "Insertado el dato: " + x;
    }

    private NodeAVL<E> insertRec(E x, NodeAVL<E> node) throws ExceptionItemDuplicate {
        NodeAVL<E> res = node;

        if (node == null) {
            res = new NodeAVL<>(x);
            height = true;
        } else {
            int cmp = x.compareTo(node.getData());

            if (cmp == 0)
                throw new ExceptionItemDuplicate("El elemento " + x + " ya está en el árbol.");

            if (cmp < 0) {
                res.setLeft(insertRec(x, node.getLeft()));

                if (height) {
                    switch (res.getFe()) {
                        case 1: res.setFe(0); height = false; break;
                        case 0: res.setFe(-1); height = true; break;
                        case -1: res = balanceToRight(res); height = false; break;
                    }
                }
            } else {
                res.setRight(insertRec(x, node.getRight()));

                if (height) {
                    switch (res.getFe()) {
                        case -1: res.setFe(0); height = false; break;
                        case 0: res.setFe(1); height = true; break;
                        case 1: res = balanceToLeft(res); height = false; break;
                    }
                }
            }
        }

        return res;
    }

    private NodeAVL<E> balanceToRight(NodeAVL<E> node) {
        NodeAVL<E> hijo = node.getLeft();

        if (hijo.getFe() == -1) {
            node.setFe(0);
            hijo.setFe(0);
            node = rotateRight(node);
        } else {
            NodeAVL<E> nieto = hijo.getRight();

            switch (nieto.getFe()) {
                case -1: node.setFe(1); hijo.setFe(0); break;
                case 0:  node.setFe(0); hijo.setFe(0); break;
                case 1:  node.setFe(0); hijo.setFe(-1); break;
            }

            nieto.setFe(0);
            node.setLeft(rotateLeft(hijo));
            node = rotateRight(node);
        }

        return node;
    }

    private NodeAVL<E> balanceToLeft(NodeAVL<E> node) {
        NodeAVL<E> hijo = node.getRight();

        if (hijo.getFe() == 1) {
            node.setFe(0);
            hijo.setFe(0);
            node = rotateLeft(node);
        } else {
            NodeAVL<E> nieto = hijo.getLeft();

            switch (nieto.getFe()) {
                case 1:  node.setFe(-1); hijo.setFe(0); break;
                case 0:  node.setFe(0);  hijo.setFe(0); break;
                case -1: node.setFe(0);  hijo.setFe(1); break;
            }

            nieto.setFe(0);
            node.setRight(rotateRight(hijo));
            node = rotateLeft(node);
        }

        return node;
    }

    private NodeAVL<E> rotateRight(NodeAVL<E> node) {
        NodeAVL<E> hijo = node.getLeft();
        node.setLeft(hijo.getRight());
        hijo.setRight(node);
        return hijo;
    }

    private NodeAVL<E> rotateLeft(NodeAVL<E> node) {
        NodeAVL<E> hijo = node.getRight();
        node.setRight(hijo.getLeft());
        hijo.setLeft(node);
        return hijo;
    }

    public void inOrden() {
        if (isEmpty()) System.out.println("Árbol vacío");
        else inOrden(root);
        System.out.println();
    }

    private void inOrden(NodeAVL<E> node) {
        if (node.getLeft() != null) inOrden(node.getLeft());
        System.out.print(node + ", ");
        if (node.getRight() != null) inOrden(node.getRight());
    }

    public void preOrden() {
        if (isEmpty()) System.out.println("Árbol vacío");
        else preOrden(root);
        System.out.println();
    }

    private void preOrden(NodeAVL<E> node) {
        if (node != null) {
            System.out.print(node + ", ");
            preOrden(node.getLeft());
            preOrden(node.getRight());
        }
    }

    public void postOrden() {
        if (isEmpty()) System.out.println("Árbol vacío");
        else postOrden(root);
        System.out.println();
    }

    private void postOrden(NodeAVL<E> node) {
        if (node != null) {
            postOrden(node.getLeft());
            postOrden(node.getRight());
            System.out.print(node + ", ");
        }
    }

    public String postOrdenIter() {
        StringBuilder sb = new StringBuilder();
        if (root == null) return "";

        Stack<NodeAVL<E>> stack1 = new Stack<>();
        Stack<NodeAVL<E>> stack2 = new Stack<>();
        stack1.push(root);

        while (!stack1.isEmpty()) {
            NodeAVL<E> node = stack1.pop();
            stack2.push(node);

            if (node.getLeft() != null) stack1.push(node.getLeft());
            if (node.getRight() != null) stack1.push(node.getRight());
        }

        while (!stack2.isEmpty()) {
            sb.append(stack2.pop()).append(", ");
        }

        return sb.toString();
    }

    public E recover(E x) throws ExceptionItemNoFound {
        NodeAVL<E> res = recover(x, root);
        if (res == null)
            throw new ExceptionItemNoFound("El dato " + x + " no está");
        return res.getData();
    }

    private NodeAVL<E> recover(E x, NodeAVL<E> node) {
        if (node == null) return null;

        int cmp = x.compareTo(node.getData());
        if (cmp < 0) return recover(x, node.getLeft());
        else if (cmp > 0) return recover(x, node.getRight());
        else return node;
    }

    public int altura() {
        return altura(root);
    }

    private int altura(NodeAVL<E> node) {
        if (node == null) return -1;
        return 1 + Math.max(altura(node.getLeft()), altura(node.getRight()));
    }

    public boolean isSimilary(AVL<E> otherTree) {
        return isSimilary(this.root, otherTree.root);
    }

    private boolean isSimilary(NodeAVL<E> node1, NodeAVL<E> node2) {
        if (node1 == null && node2 == null) return true;
        if (node1 == null || node2 == null) return false;

        return isSimilary(node1.getLeft(), node2.getLeft()) &&
               isSimilary(node1.getRight(), node2.getRight());
    }

    public void remove(E x) throws ExceptionItemNoFound {
    height = false; // Usamos height para saber si el árbol cambió de altura
    root = removeRec(x, root);
    ultimaOperacionExplicacion = "Eliminado el dato: " + x;
}

    private NodeAVL<E> removeRec(E x, NodeAVL<E> node) throws ExceptionItemNoFound {
    if (node == null)
        throw new ExceptionItemNoFound("El dato " + x + " no está");

    int cmp = x.compareTo(node.getData());

    if (cmp < 0) {
        node.setLeft(removeRec(x, node.getLeft()));
        if (height) {
            node = balanceAfterRightDelete(node);
        }
    } else if (cmp > 0) {
        node.setRight(removeRec(x, node.getRight()));
        if (height) {
            node = balanceAfterLeftDelete(node);
        }
    } else {
        height = true;
        if (node.getLeft() == null) {
            return node.getRight();
        } else if (node.getRight() == null) {
            return node.getLeft();
        } else {
            NodeAVL<E> successor = min(node.getRight());
            node.setData(successor.getData());
            node.setRight(removeRec(successor.getData(), node.getRight()));
            if (height) {
                node = balanceAfterLeftDelete(node);
            }
        }
    }
    
 
    return node;
}

    private NodeAVL<E> balanceAfterRightDelete(NodeAVL<E> node) {
    switch (node.getFe()) {
        case -1:
            node.setFe(0);
            break;
        case 0:
            node.setFe(1);
            height = false;
            break;
        case 1:
            NodeAVL<E> right = node.getRight();
            if (right.getFe() >= 0) {
                node = rotateLeft(node);
                if (right.getFe() == 0) {
                    node.setFe(-1);
                    node.getLeft().setFe(1);
                    height = false;
                } else {
                    node.setFe(0);
                    node.getLeft().setFe(0);
                }
            } else {
                node.setRight(rotateRight(right));
                node = rotateLeft(node);
                updateFEAfterDoubleRotation(node);
            }
            break;
    }
    return node;
}

private NodeAVL<E> balanceAfterLeftDelete(NodeAVL<E> node) {
    switch (node.getFe()) {
        case 1:
            node.setFe(0);
            break;
        case 0:
            node.setFe(-1);
            height = false;
            break;
        case -1:
            NodeAVL<E> left = node.getLeft();
            if (left.getFe() <= 0) {
                node = rotateRight(node);
                if (left.getFe() == 0) {
                    node.setFe(1);
                    node.getRight().setFe(-1);
                    height = false;
                } else {
                    node.setFe(0);
                    node.getRight().setFe(0);
                }
            } else {
                node.setLeft(rotateLeft(left));
                node = rotateRight(node);
                updateFEAfterDoubleRotation(node);
            }
            break;
    }
    return node;
}

private void updateFEAfterDoubleRotation(NodeAVL<E> node) {
    int fe = node.getFe();
    if (fe == 1) {
        node.getLeft().setFe(0);
        node.getRight().setFe(-1);
    } else if (fe == -1) {
        node.getLeft().setFe(1);
        node.getRight().setFe(0);
    } else {
        node.getLeft().setFe(0);
        node.getRight().setFe(0);
    }
    node.setFe(0);
}

    private NodeAVL<E> min(NodeAVL<E> node) {
        while (node.getLeft() != null) node = node.getLeft();
        return node;
    }

    public String toString() {
        return (root == null) ? "Árbol vacío" : root.toString();
    }

    public List<String> getRepresentacionCompleta() {
            List<String> representacion = new ArrayList<>();
            if (root == null) {
                representacion.add("El árbol AVL está vacío.");
                return representacion;
            }
            obtenerRepresentacionRec(root, representacion, 0);
            return representacion;
        }
        private void obtenerRepresentacionRec(NodeAVL<E> nodo, List<String> representacion, int nivel) {
            if (nodo != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < nivel; i++) sb.append("  ");
                sb.append("- ").append(nodo.getRepresentacionGrafica()).append(nivel).append(")");
                if (nodo.getLeft() != null || nodo.getRight() != null) {
                    sb.append(" (");
                    sb.append(nodo.getLeft() != null ? "Izq: " + nodo.getLeft().getData() : "Izq: null");
                    sb.append(", ");
                    sb.append(nodo.getRight() != null ? "Der: " + nodo.getRight().getData() : "Der: null");
                    sb.append(")");
                }
                representacion.add(sb.toString());
                obtenerRepresentacionRec(nodo.getLeft(), representacion, nivel + 1);
                obtenerRepresentacionRec(nodo.getRight(), representacion, nivel + 1);
            }
        }
    
    public String getUltimaOperacionExplicacion() {
            return ultimaOperacionExplicacion;
        }
}

//exceptions
public class ExceptionItemDuplicate extends Exception {
    public ExceptionItemDuplicate(String message) {
        super(message);
    }
}
public class ExceptionItemNoFound extends Exception {
    public ExceptionItemNoFound() {
        super();
    }
    public ExceptionItemNoFound(String msg) {
        super(msg);
    }
}


    static class NodoSplayTree<T extends Comparable<T>> extends Nodo<T> {
        NodoSplayTree<T> padre;
        NodoSplayTree<T> izquierdo;
        NodoSplayTree<T> derecho;
        public NodoSplayTree(T dato) {
            super(dato);
            this.padre = null;
            this.izquierdo = null;
            this.derecho = null;
        }
        public NodoSplayTree<T> getPadre() {
            return padre;
        }
        public void setPadre(NodoSplayTree<T> padre) {
            this.padre = padre;
        }
        public NodoSplayTree<T> getIzquierdo() {
            return izquierdo;
        }
        public void setIzquierdo(NodoSplayTree<T> izquierdo) {
            this.izquierdo = izquierdo;
        }
        public NodoSplayTree<T> getDerecho() {
            return derecho;
        }
        public void setDerecho(NodoSplayTree<T> derecho) {
            this.derecho = derecho;
        }
        @Override
        public String getRepresentacionGrafica() {
            return String.valueOf(dato);
        }
        @Override
        public String getExplicacion() {
            return "Nodo Splay con valor: " + dato;
        }
    }
    static class SplayTree<T extends Comparable<T>> {
        private NodoSplayTree<T> raiz;
        private String ultimaOperacionExplicacion = "Ninguna operación realizada.";
        public SplayTree() {
            this.raiz = null;
        }
        private void setPadre(NodoSplayTree<T> nodo, NodoSplayTree<T> padre) {
            if (nodo != null) {
                nodo.setPadre(padre);
            }
        }
        private void rotarDerecha(NodoSplayTree<T> nodo) {
            NodoSplayTree<T> p = nodo.getPadre();
            NodoSplayTree<T> q = nodo;
            NodoSplayTree<T> beta = q.getIzquierdo();
            if (p != null) {
                if (p.getIzquierdo() == q) p.setIzquierdo(beta);
                else p.setDerecho(beta);
            } else {
                this.raiz = beta;
            }
            setPadre(beta, p);
            q.setIzquierdo(beta.getDerecho());
            setPadre(beta.getDerecho(), q);
            beta.setDerecho(q);
            setPadre(q, beta);
        }
        private void rotarIzquierda(NodoSplayTree<T> nodo) {
            NodoSplayTree<T> p = nodo.getPadre();
            NodoSplayTree<T> q = nodo;
            NodoSplayTree<T> beta = q.getDerecho();
            if (p != null) {
                if (p.getIzquierdo() == q) p.setIzquierdo(beta);
                else p.setDerecho(beta);
            } else {
                this.raiz = beta;
            }
            setPadre(beta, p);
            q.setDerecho(beta.getIzquierdo());
            setPadre(beta.getIzquierdo(), q);
            beta.setIzquierdo(q);
            setPadre(q, beta);
        }
        private void splay(NodoSplayTree<T> nodo) {
            while (nodo.getPadre() != null) {
                NodoSplayTree<T> p = nodo.getPadre();
                NodoSplayTree<T> g = p.getPadre();
                if (g == null) { 
                    if (p.getIzquierdo() == nodo) rotarDerecha(p);
                    else rotarIzquierda(p);
                } else {
                    if (p.getIzquierdo() == nodo && g.getIzquierdo() == p) { 
                        rotarDerecha(g);
                        rotarDerecha(p);
                    } else if (p.getDerecho() == nodo && g.getDerecho() == p) { 
                        rotarIzquierda(g);
                        rotarIzquierda(p);
                    } else if (p.getIzquierdo() == nodo && g.getDerecho() == p) { 
                        rotarDerecha(p);
                        rotarIzquierda(g);
                    } else { 
                        rotarIzquierda(p);
                        rotarDerecha(g);
                    }
                }
            }
            this.raiz = nodo;
        }
        public void insertar(T dato) {
            if (raiz == null) {
                raiz = new NodoSplayTree<>(dato);
                ultimaOperacionExplicacion = "Insertado el dato: " + dato + " (raíz)";
                return;
            }
            NodoSplayTree<T> actual = raiz;
            NodoSplayTree<T> padre = null;
            int comparacion = 0;
            while (actual != null) {
                padre = actual;
                comparacion = dato.compareTo(actual.getDato());
                if (comparacion < 0) actual = actual.getIzquierdo();
                else if (comparacion > 0) actual = actual.getDerecho();
                else { 
                    splay(actual);
                    ultimaOperacionExplicacion = "El dato " + dato + " ya existe. Se ha splayeado el nodo existente.";
                    return;
                }
            }
            NodoSplayTree<T> nuevoNodo = new NodoSplayTree<>(dato);
            nuevoNodo.setPadre(padre);
            if (comparacion < 0) padre.setIzquierdo(nuevoNodo);
            else padre.setDerecho(nuevoNodo);
            splay(nuevoNodo);
            ultimaOperacionExplicacion = "Insertado el dato: " + dato;
        }
        public void eliminar(T dato) {
            if (raiz == null) {
                ultimaOperacionExplicacion = "El árbol Splay está vacío, no se puede eliminar " + dato;
                return;
            }
            NodoSplayTree<T> nodoAEliminar = buscarRec(raiz, dato);
            if (nodoAEliminar == null) {
                ultimaOperacionExplicacion = "El dato " + dato + " no se encontró para eliminar.";
                return;
            }
            splay(nodoAEliminar); 
            NodoSplayTree<T> izquierdo = raiz.getIzquierdo();
            NodoSplayTree<T> derecho = raiz.getDerecho();
            if (izquierdo != null) {
                izquierdo.setPadre(null); 
                raiz = izquierdo; 
            }
            if (derecho != null) {
                derecho.setPadre(null); 
                if (izquierdo != null) {
                    NodoSplayTree<T> maxIzquierdo = encontrarMax(izquierdo);
                    maxIzquierdo.setDerecho(derecho);
                    setPadre(derecho, maxIzquierdo);
                } else {
                    raiz = derecho;
                }
            } else {
                if (izquierdo == null) {
                    raiz = null;
                }
            }
            ultimaOperacionExplicacion = "Eliminado el dato: " + dato;
        }
        private NodoSplayTree<T> buscarRec(NodoSplayTree<T> nodo, T dato) {
            if (nodo == null) return null;
            int comparacion = dato.compareTo(nodo.getDato());
            if (comparacion == 0) return nodo;
            else if (comparacion < 0) return buscarRec(nodo.getIzquierdo(), dato);
            else return buscarRec(nodo.getDerecho(), dato);
        }
        private NodoSplayTree<T> encontrarMax(NodoSplayTree<T> nodo) {
            while (nodo != null && nodo.getDerecho() != null) {
                nodo = nodo.getDerecho();
            }
            return nodo;
        }
        public List<String> getRepresentacionCompleta() {
            List<String> representacion = new ArrayList<>();
            if (raiz == null) {
                representacion.add("El árbol Splay está vacío.");
                return representacion;
            }
            obtenerRepresentacionRec(raiz, representacion, 0);
            return representacion;
        }
        private void obtenerRepresentacionRec(NodoSplayTree<T> nodo, List<String> representacion, int nivel) {
            if (nodo != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < nivel; i++) sb.append("  ");
                sb.append("- ").append(nodo.getRepresentacionGrafica());
                if (nodo.getIzquierdo() != null || nodo.getDerecho() != null) {
                    sb.append(" (");
                    sb.append(nodo.getIzquierdo() != null ? "Izq: " + nodo.getIzquierdo().getDato() : "Izq: null");
                    sb.append(", ");
                    sb.append(nodo.getDerecho() != null ? "Der: " + nodo.getDerecho().getDato() : "Der: null");
                    sb.append(")");
                }
                representacion.add(sb.toString());
                obtenerRepresentacionRec(nodo.getIzquierdo(), representacion, nivel + 1);
                obtenerRepresentacionRec(nodo.getDerecho(), representacion, nivel + 1);
            }
        }
        public String getUltimaOperacionExplicacion() {
            return ultimaOperacionExplicacion;
        }
    }
  
//Btree
    public class NodeB<E extends Comparable<E>> {
	 public ArrayList<E> keys;
	 public ArrayList<NodeB<E>> childs;
	 public int count;
		public NodeB(int orden){
			 this.keys = new ArrayList<E>(orden);
			 this.childs = new ArrayList<NodeB<E>>(orden);
			 this.count = 0;
			 for(int i=0; i<orden; i++){
			 this.keys.add(null);
			 this.childs.add(null);
			 }
	 }
	/*Método que verifica la regla del máximo tomando en cuenta el orden del árbol al que
	pertenece */   
	 public boolean nodeFull(int orden){
		 return count == orden - 1;
	 }
	/*Método que verifica la regla del mínimo tomando en cuenta el orden del árbol al que
	 Pertenece el nodo*/
	 public boolean nodeEmpty(int orden) {
	        return count < (orden-1)/2;
	 }
	/*Método que busca de manera secuencial el elemento ‘cl’ en el conjunto de claves del nodo. Si no está, el
	método retorna False y en pos[0] la posición del desciende por donde se debe descender en el árbol. En caso
	contrario, retorna True y la posición en que la clave está en el nodo al que pertenece nodo*/
	public boolean searchNode(E cl, int pos[]){
        pos[0]=0;
        while (pos[0] < this.count && this.keys.get(pos[0]).compareTo(cl)<0) 
           pos[0]++;
        return(pos[0]<this.count && this.keys.get(pos[0]).equals(cl));
        	   
    }

    public ArrayList<E> getKeys() {
		return keys;
	}
	public void setKeys(ArrayList<E> keys) {
		this.keys = keys;
	}
	public ArrayList<NodeB<E>> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<NodeB<E>> childs) {
		this.childs = childs;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	/*Método que retorna el conjunto de claves contenidas en el átbol*/
	public String toString(){
		String result = "(";
	    for(int i = 0; i < count; i++){
	        if(i > 0) result += ", ";
	        result += this.keys.get(i);
	    }
	    result += ")";
	    return result;
	 }
	
	public void add (E cl,int orden) {
		if (!nodeFull(orden)) {
			this.keys.set(this.count, cl);
		count++;
		}
		else
			System.out.println ("Nodo esta en ele maximo...");
			
	}
    public boolean isLeaf() {
    return this.childs.get(0) == null;
}
	}


    public class BTree<E extends Comparable<E>> {
    private NodeB<E> root;
    private int orden; // orden del BTree

    private boolean up;
    private NodeB<E> nDes;
   
    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }
    
 // Inserta una clave, si no existe
    public void insert(E cl) {
       up=false;
       E mediana;
       
       mediana =push(this.root,cl);
       
       if(up) {
    	 
    	        NodeB<E> nuevo = new NodeB<>(orden);
    	        nuevo.count = 1;
    	        nuevo.keys.set(0, mediana);
    	        nuevo.childs.set(0, this.root);
    	        nuevo.childs.set(1, nDes);
    	        this.root = nuevo;
                ultimaOperacionExplicacion = "Insertado " + cl + ". Se creó nueva raíz por división.";
    	     }
        else {
                 ultimaOperacionExplicacion = "Insertado " + cl + " exitosamente.";
             }
    }

    private E push(NodeB<E> current, E cl) {
    	int[] pos = new int[1];
    	E mediana;
    	if (current == null) {
            up = true;
            nDes = null;
            return cl;
        }
    	else {
	        //caso base 
    		 boolean f1 = current.searchNode(cl, pos);
        	 if(f1) {
        		 System.out.println("Key duplicandose...");
        		 return null;
        	 }
        	 mediana= push (current.childs.get(pos[0]),cl );
        	 if  (up) {
        		 if(current.nodeFull(orden))
        			 return divideNode(current, mediana,pos[0]);
        		 else {
        			 putNode(current, mediana, nDes,pos[0]);
        			 up = false;
        		 }
        	 }
        	 return mediana;
        }
    	
    }

    // Inserta una clave ordenadamente en un nodo no lleno
    private void putNode(NodeB<E> current, E cl, NodeB<E> rd, int k) {
    	 int i;
    	 for(i = current.count-1; i>=k;i--) {
    		 current.keys.set(i+1,  current.keys.get(i));
    		 current.childs.set(i+2,  current.childs.get(i+1));
    	 }
        
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count ++;
    }
    
    
    private E divideNode(NodeB<E> current, E cl, int k) {
        NodeB<E> rd = nDes;
        nDes = new NodeB<E>(this.orden);
        int i;

        int posMdna = (k <= this.orden / 2) ? this.orden / 2 : this.orden  / 2 + 1;
        
        for (i = posMdna; i < orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }

        nDes.count = (orden - 1) - posMdna;
        current.count = posMdna;

        if (k <= this.orden / 2) {
            putNode(current, cl, rd, k);
        } else {
            putNode(nDes, cl, rd, k - posMdna);
        }

        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));
        current.count--;

        up = true;
        return median;
    }
    
    public E search(E x)throws ExceptionItemNoFound{
    	E element = searchRec(x,root);
    	if (element==null)
    		throw new ExceptionItemNoFound("La clave no existe en el arbol..");
    	return element;
    }
    
    public E search (E x, NodeB<E> current)throws ExceptionItemNoFound{
    	int[] pos = new int[1];
    	
    	if (current == null)
            return null;
    	else {
	        //caso base 
    		 boolean f1 = current.searchNode(x, pos);
        	 if(f1) {
        		 return current.keys.get(pos[0]);
        	 }
        	return searchRec(x,current.childs.get(pos[0])); 
        }
    }
    
    private E searchRec(E x , NodeB<E> current) throws ExceptionItemNoFound{
    	int pos[]=new int[1];
    	
    	if(current ==null) {
    		return null;
    	}
    	else {
    		boolean fl;
    		fl = current.searchNode(x, pos);
    		if (fl){
    				return current.keys.get(pos[0]);
    		}
    		return searchRec(x,current.childs.get(pos[0]));
    	}
    }
    
    public String toString() {
        String s = "";
        if (isEmpty())
            s += "BTree is empty...";
        else
            s = writeTree(this.root);
        return s;
    }

    private String writeTree(NodeB<E> current) {
    	int i;
    	String s = "";
        if (current != null) {
         s +=current.toString()+ "\n";
       
        for ( i = 0; i <= current.count; i++)        	
           s +=writeTree(current.childs.get(i));           
        }
        return s;
    }
     
    public void remove(E cl) {
        if (isEmpty()) {
            System.out.println("Árbol vacío");
            return;
        }
        delete(this.root, cl);
        
        //en caso quede vacia
        if (this.root.count == 0 && !isLeaf(this.root)) {
            this.root = this.root.childs.get(0);
        }
        ultimaOperacionExplicacion = "Eliminado " + cl + " del árbol B.";
    }

    // Elimina si está en hoja o reemplaza con sucesor si está en nodo interno
    private void delete(NodeB<E> node, E cl) {
        if (node == null) return;

        int[] pos = new int[1];
        boolean found = node.searchNode(cl, pos);

        if (found) {
        	 deleteFromNode(node, pos[0]);
            
        } else {
        	if (!isLeaf(node)) {
                delete(node.childs.get(pos[0]), cl);
                
                if (node.childs.get(pos[0]) != null && 
                        node.childs.get(pos[0]).nodeEmpty(orden)) {
                        fixUnderflow(node, pos[0]);
                    }
            }
        }      
    }
    
    private void deleteFromNode(NodeB<E> node, int pos) {
        if (isLeaf(node)) {
            // Caso 1: Eliminación de hoja
            removeFromLeaf(node, pos);
        } else {
            // Caso 2: Eliminación de nodo interno
            // Opción A: Reemplazar con predecesor
            if (EnoughKeys(node.childs.get(pos))) {
                E predecessor = getPredecessor(node.childs.get(pos));
                node.keys.set(pos, predecessor);
                delete(node.childs.get(pos), predecessor);
            }
            // Opción B: Reemplazar con sucesor
            else if (EnoughKeys(node.childs.get(pos + 1))) {
                E successor = getSuccessor(node.childs.get(pos + 1));
                node.keys.set(pos, successor);
                delete(node.childs.get(pos + 1), successor);
            }
            // Opción C: Fusionar hijos y eliminar
            else {
                mergeChildren(node, pos);
                delete(node.childs.get(pos), node.keys.get(pos));
            }
        }
    }
    // Corregir underflow 
    private void fixUnderflow(NodeB<E> parent, int childIndex) {
        NodeB<E> child = parent.childs.get(childIndex);
        
   
        if (childIndex > 0 && 
            parent.childs.get(childIndex - 1).count > (orden - 1) / 2) {
            redistributeFromLeft(parent, childIndex);
        }
       
        else if (childIndex < parent.count && 
                 parent.childs.get(childIndex + 1).count > (orden - 1) / 2) {
            redistributeFromRight(parent, childIndex);
        }
        // Fusionar con hermano izquierdo
        else if (childIndex > 0) {
            mergeWithLeft(parent, childIndex);
        }
        // Fusionar con hermano derecho
        else if (childIndex < parent.count) {
            mergeWithRight(parent, childIndex);
        }
    }
    
    private void redistributeFromLeft(NodeB<E> parent, int childIndex) {
        NodeB<E> child = parent.childs.get(childIndex);
        NodeB<E> leftSibling = parent.childs.get(childIndex - 1);
        
        for (int i = child.count; i > 0; i--) {
            child.keys.set(i, child.keys.get(i - 1));
            child.childs.set(i + 1, child.childs.get(i));
        }
        child.childs.set(1, child.childs.get(0));
        
        child.keys.set(0, parent.keys.get(childIndex - 1));
        child.childs.set(0, leftSibling.childs.get(leftSibling.count));
        child.count++;
       
        parent.keys.set(childIndex - 1, leftSibling.keys.get(leftSibling.count - 1));
        leftSibling.keys.set(leftSibling.count - 1, null);
        leftSibling.childs.set(leftSibling.count, null);
        leftSibling.count--;
    }
    
    private void redistributeFromRight(NodeB<E> parent, int childIndex) {
        NodeB<E> child = parent.childs.get(childIndex);
        NodeB<E> rightSibling = parent.childs.get(childIndex + 1);
      
        child.keys.set(child.count, parent.keys.get(childIndex));
        child.childs.set(child.count + 1, rightSibling.childs.get(0));
        child.count++;
        
        parent.keys.set(childIndex, rightSibling.keys.get(0));
 
        for (int i = 0; i < rightSibling.count - 1; i++) {
            rightSibling.keys.set(i, rightSibling.keys.get(i + 1));
            rightSibling.childs.set(i, rightSibling.childs.get(i + 1));
        }
        rightSibling.childs.set(rightSibling.count - 1, rightSibling.childs.get(rightSibling.count));
        rightSibling.keys.set(rightSibling.count - 1, null);
        rightSibling.childs.set(rightSibling.count, null);
        rightSibling.count--;
    }
    
    private void mergeWithLeft(NodeB<E> parent, int childIndex) {
        NodeB<E> child = parent.childs.get(childIndex);
        NodeB<E> leftSibling = parent.childs.get(childIndex - 1);
        
        // clave del padre al hermano izquierdo
        leftSibling.keys.set(leftSibling.count, parent.keys.get(childIndex - 1));
        leftSibling.count++;
        
        // Mover  al hermano izquierdo
        for (int i = 0; i < child.count; i++) {
            leftSibling.keys.set(leftSibling.count, child.keys.get(i));
            leftSibling.childs.set(leftSibling.count, child.childs.get(i));
            leftSibling.count++;
        }
        leftSibling.childs.set(leftSibling.count, child.childs.get(child.count));
        
        removeFromParent(parent, childIndex - 1);
    }
       
    private void mergeWithRight(NodeB<E> parent, int childIndice) {
    	NodeB<E> child = parent.childs.get(childIndice);
        NodeB<E> rightSibling = parent.childs.get(childIndice + 1);
        
       
        child.keys.set(child.count, parent.keys.get(childIndice));
        child.count++;
        //  del hermano derecho al hijo
        for (int i = 0; i < rightSibling.count; i++) {
            child.keys.set(child.count, rightSibling.keys.get(i));
            child.childs.set(child.count, rightSibling.childs.get(i));
            child.count++;
        }
        child.childs.set(child.count, rightSibling.childs.get(rightSibling.count));
        
        removeFromParent(parent, childIndice);
    }
    
    private void mergeChildren(NodeB<E> parent, int pos) {
    	NodeB<E> leftChild = parent.childs.get(pos);
        NodeB<E> rightChild = parent.childs.get(pos + 1);
        
        //  padre al hijo izquierdo
        leftChild.keys.set(leftChild.count, parent.keys.get(pos));
        leftChild.count++;
        
        // claves e hijos del hijo derecho al izquierdo
        for (int i = 0; i < rightChild.count; i++) {
            leftChild.keys.set(leftChild.count, rightChild.keys.get(i));
            leftChild.childs.set(leftChild.count, rightChild.childs.get(i));
            leftChild.count++;
        }
        leftChild.childs.set(leftChild.count, rightChild.childs.get(rightChild.count));
        
        removeFromParent(parent, pos);
    }
    
    private void removeFromParent(NodeB<E> parent, int pos) {
    	for (int i = pos; i < parent.count - 1; i++) {
            parent.keys.set(i, parent.keys.get(i + 1));
            parent.childs.set(i + 1, parent.childs.get(i + 2));
        }
        parent.keys.set(parent.count - 1, null);
        parent.childs.set(parent.count, null);
        parent.count--;
    }
    
    private boolean isLeaf(NodeB<E> node) {
        return node.childs.get(0) == null;
    }
    
    private boolean EnoughKeys(NodeB<E> node) {
        return node != null && node.count > (orden - 1) / 2;
    }

    private void removeFromLeaf(NodeB<E> node, int pos) {
        for (int i = pos; i < node.count - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
        }
        node.keys.set(node.count - 1, null);
        node.count--;
    }

    private E getSuccessor(NodeB<E> node) {
        while (!isLeaf(node)) {
            node = node.childs.get(0);
        }
        return node.keys.get(0);
    }

    private E getPredecessor(NodeB<E> node) {
        while (!isLeaf(node)) {
            node = node.childs.get(node.count);
        }
        return node.keys.get(node.count - 1);
    }
    
    public NodeB<E> getRoot() {
        return this.root;
    }
    public List<String> getRepresentacionCompleta() {
    List<String> representacion = new ArrayList<>();
    if (isEmpty()) {
        representacion.add("BTree vacío");
    } else {
        getRepresentacionRec(this.root, representacion, 0);
    }
    return representacion;
}

private void getRepresentacionRec(NodeB<E> nodo, List<String> rep, int nivel) {
    if (nodo != null) {
        String indent = "  ".repeat(nivel);
        rep.add(indent + "Nivel " + nivel + ": " + nodo.toString());
        
        for (int i = 0; i <= nodo.count; i++) {
            if (nodo.childs.get(i) != null) {
                getRepresentacionRec(nodo.childs.get(i), rep, nivel + 1);
            }
        }
    }
}

private String ultimaOperacionExplicacion = "No se han realizado operaciones aún.";

public String getUltimaOperacionExplicacion() {
    return ultimaOperacionExplicacion;
}
}


    private ArbolBinario<Integer> simuladorArbolBinario = new ArbolBinario<>();
    private final AVL<Integer> arbol = new AVL<>();
    private SplayTree<Integer> simuladorSplayTree = new SplayTree<>();
    private BTree<Integer> btree = new BTree<>(3);
    
    @GetMapping("/api/estructuras/hola")
    public ResponseEntity<String> holaEstructuras() {
        return ResponseEntity.ok("¡Hola desde el simulador de estructuras!");
    }
    @PostMapping("/api/arbol-binario/insertar")
    public ResponseEntity<String> insertarEnArbolBinario(@RequestBody Integer dato) {
        if (dato == null) return ResponseEntity.badRequest().body("Dato nulo.");
        simuladorArbolBinario.insertar(dato);
        return ResponseEntity.ok(simuladorArbolBinario.getUltimaOperacionExplicacion());
    }
    @DeleteMapping("/api/arbol-binario/eliminar")
    public ResponseEntity<String> eliminarDeArbolBinario(@RequestBody Integer dato) {
        if (dato == null) return ResponseEntity.badRequest().body("Dato nulo.");
        simuladorArbolBinario.eliminar(dato);
        return ResponseEntity.ok(simuladorArbolBinario.getUltimaOperacionExplicacion());
    }
    @GetMapping("/api/arbol-binario/mostrar")
    public ResponseEntity<List<String>> mostrarArbolBinario() {
        return ResponseEntity.ok(simuladorArbolBinario.getRepresentacionCompleta());
    }
    @GetMapping("/api/arbol-binario/explicacion")
    public ResponseEntity<String> obtenerExplicacionArbolBinario() {
        return ResponseEntity.ok(simuladorArbolBinario.getUltimaOperacionExplicacion());
    }
    @PostMapping("/api/arbol-avl/insertar")
    public ResponseEntity<Map<String, Object>> insertar(@RequestParam int valor) {
        try {
            arbol.insert(valor);
        } catch (ExceptionItemDuplicate e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.ok(convertirArbol(arbol.getRootAVL()));
    }

    @DeleteMapping("/api/arbol-avl/eliminar")
    public ResponseEntity<Map<String, Object>> eliminar(@RequestParam int valor) {
        try {
            arbol.remove(valor);
        } catch (ExceptionItemNoFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(convertirArbol(arbol.getRootAVL()));
    }

    @GetMapping("/api/arbol-avl/mostrar")
    public ResponseEntity<List<String>> mostrarArbolAVL() {
        return ResponseEntity.ok(arbol.getRepresentacionCompleta());
    }

    @GetMapping("/api/arbol-avl/explicacion")
    public ResponseEntity<String> obtenerExplicacionArbolAVL() {
        return ResponseEntity.ok(arbol.getUltimaOperacionExplicacion());
    }

    private Map<String, Object> convertirArbol(NodeAVL<Integer> nodo) {
        if (nodo == null) return null;

        Map<String, Object> map = new HashMap<>();
        map.put("id", System.identityHashCode(nodo));
        map.put("valor", nodo.getData());
        map.put("fe", nodo.getFe());
        map.put("izquierda", convertirArbol(nodo.getLeft()));
        map.put("derecha", convertirArbol(nodo.getRight()));
        return map;
    }
    @PostMapping("/api/splay-tree/insertar")
    public ResponseEntity<String> insertarEnSplayTree(@RequestBody Integer dato) {
        if (dato == null) return ResponseEntity.badRequest().body("Dato nulo.");
        simuladorSplayTree.insertar(dato);
        return ResponseEntity.ok(simuladorSplayTree.getUltimaOperacionExplicacion());
    }
    @DeleteMapping("/api/splay-tree/eliminar")
    public ResponseEntity<String> eliminarDeSplayTree(@RequestBody Integer dato) {
        if (dato == null) return ResponseEntity.badRequest().body("Dato nulo.");
        simuladorSplayTree.eliminar(dato);
        return ResponseEntity.ok(simuladorSplayTree.getUltimaOperacionExplicacion());
    }
    @GetMapping("/api/splay-tree/mostrar")
    public ResponseEntity<List<String>> mostrarSplayTree() {
        return ResponseEntity.ok(simuladorSplayTree.getRepresentacionCompleta());
    }
    @GetMapping("/api/splay-tree/explicacion")
    public ResponseEntity<String> obtenerExplicacionSplayTree() {
        return ResponseEntity.ok(simuladorSplayTree.getUltimaOperacionExplicacion());
    }
       @PostMapping("/api/arbol-b/insertar")
    public ResponseEntity<Map<String, Object>> insertarb(@RequestParam int valor) {
        btree.insert(valor);
        return ResponseEntity.ok(convertirArbol(btree.getRoot()));
    }
    
    @DeleteMapping("/api/arbol-b/eliminar") 
    public ResponseEntity<Map<String, Object>> eliminarb(@RequestParam int valor) {
        btree.remove(valor);
        return ResponseEntity.ok(convertirArbol(btree.getRoot()));
    }
    
    @GetMapping("/api/arbol-b/mostrar")
    public ResponseEntity<List<String>> mostrarArbolBTree() {
        return ResponseEntity.ok(btree.getRepresentacionCompleta());
    }
    
    @GetMapping("/api/arbol-b/explicacion")
    public ResponseEntity<String> obtenerExplicacionArbolBTree() {
        return ResponseEntity.ok(btree.getUltimaOperacionExplicacion());
    }
    
    private Map<String, Object> convertirArbol(NodeB<Integer> nodo) {
        if (nodo == null) return null;
        
        Map<String, Object> map = new HashMap<>();
        map.put("id", System.identityHashCode(nodo));
        map.put("keys", nodo.keys.subList(0, nodo.count));
        
        List<Map<String, Object>> children = new ArrayList<>();
        for (int i = 0; i <= nodo.count; i++) {
            children.add(convertirArbol(nodo.childs.get(i)));
        }
        map.put("children", children);
        
        return map;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }
    public static void main(String[] args) {
        SpringApplication.run(SimuladorApplication.class, args);
    }
}