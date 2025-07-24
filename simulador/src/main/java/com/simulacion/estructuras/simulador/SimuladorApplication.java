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
            return String.valueOf(data) + "(H:" + altura + ")";
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
                sb.append("- ").append(nodo.getRepresentacionGrafica());
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
    static class NodoArbolB<T extends Comparable<T>> {
        List<T> claves;
        List<NodoArbolB<T>> hijos; 
        boolean esHoja;
        int grado; 
        public NodoArbolB(int grado, boolean esHoja) {
            this.grado = grado;
            this.esHoja = esHoja;
            this.claves = new ArrayList<>(2 * grado - 1);
            this.hijos = new ArrayList<>(2 * grado); 
        }
        public List<T> getClaves() {
            return claves;
        }
        public List<NodoArbolB<T>> getHijos() {
            return hijos;
        }
        public boolean esHoja() {
            return esHoja;
        }
        public int getGrado() {
            return grado;
        }
        public String getRepresentacionGrafica() {
            return claves.toString();
        }
        public String getExplicacion() {
            return "Nodo con claves: " + claves.toString() + (esHoja ? " (Hoja)" : " (Interno)");
        }
        public void insertarClave(T clave) {
            int i = 0;
            while (i < claves.size() && clave.compareTo(claves.get(i)) > 0) {
                i++;
            }
            claves.add(i, clave);
        }
        public void insertarHijo(NodoArbolB<T> hijo, int indice) {
            hijos.add(indice, hijo);
        }
        public void removerClave(int indice) {
            claves.remove(indice);
        }
        public void removerHijo(int indice) {
            hijos.remove(indice);
        }
        public T obtenerClave(int indice) {
            if (indice < 0 || indice >= claves.size()) {
                throw new IndexOutOfBoundsException("Índice de clave fuera de rango: " + indice);
            }
            return claves.get(indice);
        }
        public NodoArbolB<T> obtenerHijo(int indice) {
            if (indice < 0 || indice >= hijos.size()) {
                throw new IndexOutOfBoundsException("Índice de hijo fuera de rango: " + indice + ", tamaño hijos: " + hijos.size());
            }
            return hijos.get(indice);
        }
        public int numeroClaves() {
            return claves.size();
        }
    }
    static class ArbolB<T extends Comparable<T>> {
        private NodoArbolB<T> raiz;
        private int grado; 
        private String ultimaOperacionExplicacion = "Ninguna operación realizada.";
        public ArbolB(int grado) {
            if (grado < 2) throw new IllegalArgumentException("El grado debe ser al menos 2");
            this.grado = grado;
            this.raiz = null;
        }
        public void insertar(T clave) {
            if (raiz == null) {
                raiz = new NodoArbolB<>(grado, true);
                raiz.insertarClave(clave);
                ultimaOperacionExplicacion = "Insertado el dato: " + clave + " (raíz)";
                return;
            }
            if (raiz.numeroClaves() == (2 * grado - 1)) {
                NodoArbolB<T> nuevaRaiz = new NodoArbolB<>(grado, false); 
                nuevaRaiz.insertarHijo(raiz, 0);
                dividirNodo(nuevaRaiz, 0, raiz);
                raiz = nuevaRaiz;
            }
            insertarNoLleno(raiz, clave);
            ultimaOperacionExplicacion = "Insertado el dato: " + clave;
        }
        private void dividirNodo(NodoArbolB<T> padre, int indiceHijo, NodoArbolB<T> hijoLleno) {
            NodoArbolB<T> nuevoHermano = new NodoArbolB<>(grado, hijoLleno.esHoja());
            T claveMediana = hijoLleno.obtenerClave(grado - 1);
            padre.insertarClave(claveMediana);
            padre.insertarHijo(nuevoHermano, indiceHijo + 1);
            for (int i = grado; i < 2 * grado - 1; i++) {
                nuevoHermano.insertarClave(hijoLleno.obtenerClave(i));
            }
            if (!hijoLleno.esHoja()) {
                for (int i = grado; i <= 2 * grado - 1; i++) {
                    nuevoHermano.insertarHijo(hijoLleno.obtenerHijo(i), i - grado); 
                }
            }
            for (int i = 2 * grado - 2; i >= grado; i--) {
                hijoLleno.removerClave(i);
            }
            if (!hijoLleno.esHoja()) {
                for (int i = 2 * grado; i >= grado + 1; i--) {
                    hijoLleno.removerHijo(i);
                }
            }
        }
        private void insertarNoLleno(NodoArbolB<T> nodo, T clave) {
            int i = nodo.numeroClaves() - 1; 
            if (nodo.esHoja()) {
                nodo.insertarClave(clave);
            } else {
                while (i >= 0 && clave.compareTo(nodo.obtenerClave(i)) < 0) {
                    i--;
                }
                i++; 
                if (nodo.obtenerHijo(i).numeroClaves() == (2 * grado - 1)) {
                    dividirNodo(nodo, i, nodo.obtenerHijo(i));
                    if (clave.compareTo(nodo.obtenerClave(i)) > 0) {
                        i++; 
                    }
                }
                insertarNoLleno(nodo.obtenerHijo(i), clave);
            }
        }
        public void eliminar(T clave) {
            if (raiz == null) {
                ultimaOperacionExplicacion = "El árbol B está vacío, no se puede eliminar " + clave;
                return;
            }
            eliminarRec(raiz, clave);
            if (raiz.numeroClaves() == 0 && !raiz.esHoja()) {
                raiz = raiz.obtenerHijo(0);
            }
            ultimaOperacionExplicacion = "Eliminado el dato: " + clave;
        }
        private void eliminarRec(NodoArbolB<T> nodo, T clave) {
            int i = 0;
            while (i < nodo.numeroClaves() && clave.compareTo(nodo.obtenerClave(i)) > 0) {
                i++;
            }
            if (i < nodo.numeroClaves() && clave.compareTo(nodo.obtenerClave(i)) == 0) {
                if (nodo.esHoja()) {
                    nodo.removerClave(i);
                } else {
                    eliminarDeNodoInterno(nodo, i);
                }
            }
            else {
                if (nodo.esHoja()) {
                    ultimaOperacionExplicacion = "El dato " + clave + " no se encontró para eliminar.";
                    return;
                }
                eliminarDelHijo(nodo, i, clave);
                eliminarRec(nodo.obtenerHijo(i), clave);
            }
        }
        private void eliminarDeNodoInterno(NodoArbolB<T> nodo, int indiceClave) {
            T clave = nodo.obtenerClave(indiceClave);
            NodoArbolB<T> hijoIzquierdo = nodo.obtenerHijo(indiceClave);
            NodoArbolB<T> hijoDerecho = nodo.obtenerHijo(indiceClave + 1);
            if (hijoIzquierdo.numeroClaves() >= grado) {
                T predecesor = obtenerPredecesor(hijoIzquierdo);
                nodo.claves.set(indiceClave, predecesor);
                eliminarRec(hijoIzquierdo, predecesor);
            }
            else if (hijoDerecho.numeroClaves() >= grado) {
                T sucesor = obtenerSucesor(hijoDerecho);
                nodo.claves.set(indiceClave, sucesor);
                eliminarRec(hijoDerecho, sucesor);
            }
            else {
                fusionarHijos(nodo, indiceClave);
                eliminarRec(hijoIzquierdo, clave);
            }
        }
        private void eliminarDelHijo(NodoArbolB<T> nodo, int indiceHijo, T clave) {
            NodoArbolB<T> hijo = nodo.obtenerHijo(indiceHijo);
            if (hijo.numeroClaves() < grado) {
                rellenarHijo(nodo, indiceHijo);
            }
            eliminarRec(nodo.obtenerHijo(indiceHijo), clave);
        }
        private void rellenarHijo(NodoArbolB<T> nodo, int indiceHijo) {
            if (indiceHijo != 0 && nodo.obtenerHijo(indiceHijo - 1).numeroClaves() >= grado) {
                prestarDeHermanoIzquierdo(nodo, indiceHijo);
            }
            else if (indiceHijo != nodo.getHijos().size() - 1 && nodo.obtenerHijo(indiceHijo + 1).numeroClaves() >= grado) {
                prestarDeHermanoDerecho(nodo, indiceHijo);
            }
            else {
                if (indiceHijo != nodo.getHijos().size() - 1) {
                    fusionarHijos(nodo, indiceHijo);
                }
                else {
                    fusionarHijos(nodo, indiceHijo - 1);
                }
            }
        }
        private void prestarDeHermanoIzquierdo(NodoArbolB<T> nodo, int indiceHijo) {
            NodoArbolB<T> hijo = nodo.obtenerHijo(indiceHijo);
            NodoArbolB<T> hermanoIzquierdo = nodo.obtenerHijo(indiceHijo - 1);
            T clavePadre = nodo.obtenerClave(indiceHijo - 1);
            hijo.insertarClave(clavePadre);
            nodo.claves.set(indiceHijo - 1, hermanoIzquierdo.obtenerClave(hermanoIzquierdo.numeroClaves() - 1));
            hermanoIzquierdo.removerClave(hermanoIzquierdo.numeroClaves() - 1);
            if (!hermanoIzquierdo.esHoja()) {
                hijo.insertarHijo(hermanoIzquierdo.obtenerHijo(hermanoIzquierdo.getHijos().size() - 1), 0);
                hermanoIzquierdo.removerHijo(hermanoIzquierdo.getHijos().size() - 1);
            }
        }
        private void prestarDeHermanoDerecho(NodoArbolB<T> nodo, int indiceHijo) {
            NodoArbolB<T> hijo = nodo.obtenerHijo(indiceHijo);
            NodoArbolB<T> hermanoDerecho = nodo.obtenerHijo(indiceHijo + 1);
            T clavePadre = nodo.obtenerClave(indiceHijo);
            hijo.insertarClave(clavePadre);
            nodo.claves.set(indiceHijo, hermanoDerecho.obtenerClave(0));
            hermanoDerecho.removerClave(0);
            if (!hermanoDerecho.esHoja()) {
                hijo.insertarHijo(hermanoDerecho.obtenerHijo(0), hijo.numeroClaves());
                hermanoDerecho.removerHijo(0);
            }
        }
        private void fusionarHijos(NodoArbolB<T> nodo, int indiceHijo) {
            NodoArbolB<T> hijo = nodo.obtenerHijo(indiceHijo);
            NodoArbolB<T> hermano = nodo.obtenerHijo(indiceHijo + 1);
            T clavePadre = nodo.obtenerClave(indiceHijo);
            hijo.insertarClave(clavePadre);
            for (int i = 0; i < hermano.numeroClaves(); i++) {
                hijo.insertarClave(hermano.obtenerClave(i));
            }
            if (!hermano.esHoja()) {
                for (int i = 0; i <= hermano.numeroClaves(); i++) { 
                    hijo.insertarHijo(hermano.obtenerHijo(i), hijo.numeroClaves());
                }
            }
            nodo.removerClave(indiceHijo);
            nodo.removerHijo(indiceHijo + 1);
        }
        private T obtenerPredecesor(NodoArbolB<T> nodo) {
            while (!nodo.esHoja()) {
                nodo = nodo.obtenerHijo(nodo.numeroClaves()); 
            }
            return nodo.obtenerClave(nodo.numeroClaves() - 1);
        }
        private T obtenerSucesor(NodoArbolB<T> nodo) {
            while (!nodo.esHoja()) {
                nodo = nodo.obtenerHijo(0);
            }
            return nodo.obtenerClave(0);
        }
        public List<String> getRepresentacionCompleta() {
            List<String> representacion = new ArrayList<>();
            if (raiz == null) {
                representacion.add("El árbol B está vacío.");
                return representacion;
            }
            obtenerRepresentacionRec(raiz, representacion, 0);
            return representacion;
        }
        private void obtenerRepresentacionRec(NodoArbolB<T> nodo, List<String> representacion, int nivel) {
            if (nodo != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < nivel; i++) sb.append("  ");
                sb.append("- ").append(nodo.getRepresentacionGrafica());
                representacion.add(sb.toString());
                if (!nodo.esHoja()) {
                    for (int i = 0; i <= nodo.numeroClaves(); i++) {
                        obtenerRepresentacionRec(nodo.obtenerHijo(i), representacion, nivel + 1);
                    }
                }
            }
        }
        public String getUltimaOperacionExplicacion() {
            return ultimaOperacionExplicacion;
        }
    }
    private ArbolBinario<Integer> simuladorArbolBinario = new ArbolBinario<>();
    private final AVL<Integer> arbol = new AVL<>();
    private SplayTree<Integer> simuladorSplayTree = new SplayTree<>();
    private ArbolB<Integer> simuladorArbolB = new ArbolB<>(3);
    
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
    public ResponseEntity<String> insertarEnArbolB(@RequestBody Integer clave) {
        if (clave == null) return ResponseEntity.badRequest().body("Clave nula.");
        simuladorArbolB.insertar(clave);
        return ResponseEntity.ok(simuladorArbolB.getUltimaOperacionExplicacion());
    }
    @DeleteMapping("/api/arbol-b/eliminar")
    public ResponseEntity<String> eliminarDeArbolB(@RequestBody Integer clave) {
        if (clave == null) return ResponseEntity.badRequest().body("Clave nula.");
        simuladorArbolB.eliminar(clave);
        return ResponseEntity.ok(simuladorArbolB.getUltimaOperacionExplicacion());
    }
    @GetMapping("/api/arbol-b/mostrar")
    public ResponseEntity<List<String>> mostrarArbolB() {
        return ResponseEntity.ok(simuladorArbolB.getRepresentacionCompleta());
    }
    @GetMapping("/api/arbol-b/explicacion")
    public ResponseEntity<String> obtenerExplicacionArbolB() {
        return ResponseEntity.ok(simuladorArbolB.getUltimaOperacionExplicacion());
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }
    public static void main(String[] args) {
        SpringApplication.run(SimuladorApplication.class, args);
    }
}