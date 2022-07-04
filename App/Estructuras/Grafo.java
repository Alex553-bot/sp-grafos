package App.Estructuras;

import App.Plano.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

public class Grafo {

    private LinkedList<Arista>[] grafo;
    private Lugar[] vertices;

    public static void main(String[] args) {
        Lugar l1 = new Lugar(new Coordenadas(0, 0), "l1");
        Lugar l2 = new Lugar(new Coordenadas(1, 4), "l2");
        Lugar l3 = new Lugar(new Coordenadas(2, 3), "l3");
        Lugar l4 = new Lugar(new Coordenadas(3, 2), "l4");
        Lugar l5 = new Lugar(new Coordenadas(4, 1), "l5");

        ArrayList<Lugar> lugares = new ArrayList<>();
        lugares.add(l1);
        lugares.add(l2);
        lugares.add(l3);
        lugares.add(l4);
        lugares.add(l5);

        Grafo g = new Grafo(5, lugares.iterator());
        g.insertarArista(l1, l2);
        g.insertarArista(l1, l4);
        g.insertarArista(l1, l5);
        g.insertarArista(l2, l1);
        g.insertarArista(l2, l4);
        g.insertarArista(l3, l4);
        g.insertarArista(l4, l2);
        g.insertarArista(l4, l5);
        g.insertarArista(l4, l1);
        g.insertarArista(l5, l1);
        g.insertarArista(l5, l4);

        g.dibujarGrafo();

        ArrayList<ArrayList<Integer>> paths = g.encontrarRutas(0, 1);

        paths = g.encontrarRutas(0, 3);
        for (ArrayList<Integer> l : paths) {
            System.out.println("CAMINO:" + l);
            ArrayList<Lugar> lug = g.convertir(l);
            for (Lugar x: lug) {
                System.out.print(x.getNombre());
            }
            System.out.println();

        }
         
        
    }

    public String lugarStr(Lugar o, Lugar d) {
        String reporte = "De: " + o.getNombre() + 
                            "\n A: " + d.getNombre() + 
                            "\nRECORRIDO: ";
        double p = obtenerPeso(o, d);
        
        if (p<10) {
            // maritimo
            reporte += "MARITIMO";
        } else if (p<40) {
            // aereo
            reporte += "AEREO";
        } else {
            // terrestre
            reporte += "TERRESTRE";
        }

        return reporte;
    }

    public Grafo(int n, Iterator<Lugar> lugares) {
        inicializar(n);
        int i = 0;
        vertices = new Lugar[n];
        while (lugares.hasNext()) {
            vertices[i] = lugares.next();
            i++;
        }
    }

    public Grafo(int n) {
        inicializar(n);
    }

    private void inicializar(int n) {
        grafo = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            grafo[i] = new LinkedList<>();
        }
    }

    public void insertarArista(Lugar origen, Lugar destino) {
        int o = buscar(origen);
        int d = buscar(destino);

        double peso = Coordenadas.calcularDistancia(origen.getCoordenadas(),
                destino.getCoordenadas());

        Arista arista = new Arista(d, (float) peso);

        if (!contiene(grafo[o], d)) {
            grafo[o].add(arista);
        }
    }

    private int buscar(Lugar l) {
        int index = 0;
        while (index < vertices.length && !vertices[index].equals(l)) {
            index++;
        }
        return index;
    }

    public void eliminarArista(Lugar origen, Lugar destino) {
        int o = buscar(origen);
        int d = buscar(destino);

        LinkedList<Arista> aristas = grafo[o];
        Arista aEliminar = encontrarArista(d, aristas);
        if (aEliminar != null) {
            aristas.remove(aEliminar);
        }
    }

    private Arista encontrarArista(int destino, LinkedList<Arista> lista) {
        int i = 0;
        Arista actual = lista.poll();
        while ((actual.gesDestino() != destino) && (i < lista.size())) {
            lista.add(actual);
            actual = lista.poll();
            i++;
        }
        if (i == lista.size()) {
            return null;
        }
        return actual;
    }

    public double obtenerPeso(Lugar origen, Lugar destino) {
        int o = buscar(origen);
        int d = buscar(destino);
        LinkedList<Arista> aristas = grafo[o];

        Arista arista = encontrarArista(d, aristas);
        if (arista == null) {
            return 0;
        }
        return arista.getPeso();
    }

    public void dibujarGrafo() {
        for (int i = 0; i < grafo.length; i++) {
            System.out.println("vertice: " + i + ":" + grafo[i].toString());
        }
    }

    public ArrayList<ArrayList<Lugar>> getRutas(Lugar l1, Lugar l2) {
        ArrayList<ArrayList<Lugar>> rutas = new ArrayList<>();
        int o = buscar(l1);
        int d = buscar(l2);
        ArrayList<ArrayList<Integer>> paths = encontrarRutas(o, d);
        for (ArrayList<Integer> lista : paths) {
            rutas.add(convertir(lista));
        }

        return rutas;
    }

    public ArrayList<ArrayList<Integer>> encontrarRutas(int s, int d) {
        int n = vertices.length;
        boolean[] visitados = new boolean[n];
        ArrayList<Integer> lista = new ArrayList<>();

        ArrayList<ArrayList<Integer>> todos = new ArrayList<>();

        lista.add(s);

        obtenerRutas(s, d, visitados, lista, todos);
        return todos;
    }

    private void obtenerRutas(int u, int d,
            boolean[] visitados,
            ArrayList<Integer> rutaLocal,
            ArrayList<ArrayList<Integer>> todasRutas) 
    {

        if (u == d) {
            todasRutas.add((ArrayList) rutaLocal.clone());
            return;
        }

        visitados[u] = true;

        for (Arista a : grafo[u]) {
            int i = a.gesDestino();
            if (!visitados[i]) {
                rutaLocal.add(i);
                obtenerRutas(i, d, visitados, rutaLocal, todasRutas);

                rutaLocal.remove(new Integer(i));
            }
        }

        visitados[u] = false;
    }

    public boolean existe(int d, long camino) {
        long n = camino;
        boolean respuesta = true;

        while (n > 0 && respuesta) {
            respuesta = (n % 10 != d);
            n /= 10;
        }

        respuesta &= n != 0;

        return respuesta;
    }

    public ArrayList<Lugar> convertir(ArrayList<Integer> l) {
        ArrayList<Lugar> lista = new ArrayList<>();
        for (int actual : l) {
            lista.add(vertices[actual]);
        }
        return lista;
    }

    private boolean contiene(LinkedList<Arista> lista, int index) {
        int i = 0;
        while (i < lista.size()) {
            if (index == lista.get(i).gesDestino()) {
                return true;
            }
            i++;
        }
        return false;
    }

    public Lugar[] getVertices() {
        return vertices;
    }
}