//package App.Negocios.Estructura;
package Backup;
import java.util.HashMap;
import java.util.ArrayList;

import App.Calendario.*;
import App.Negocios.*;
import App.Negocios.Estructura.*;

public class GrafoAct {
    // sera un multigrafo
    private HashMap<Integer, ArrayList<AristaAct>> grafo;
    private ArrayList<Actividad> vertices;

    public GrafoAct() {
        grafo = new HashMap<>();
        vertices = new ArrayList<>();
    }

    public ArrayList<ArrayList<AristaAct>> getRutas(Actividad a1, Actividad a2) {
        ArrayList<ArrayList<Actividad>> rutas = new ArrayList<>();
        int o = obtenerIndex(a1);
        int d = obtenerIndex(a2);
        ArrayList<ArrayList<AristaAct>> paths = encontrarRutas(o, d);
        // for (ArrayList<Integer> lista : paths) {
        // rutas.add(convertir(lista));
        // }

        return paths;
    }

    public ArrayList<ArrayList<AristaAct>> encontrarRutas(int s, int d) {
        int n = vertices.size();
        boolean[] visitados = new boolean[n];
        ArrayList<AristaAct> lista = new ArrayList<>();

        ArrayList<ArrayList<AristaAct>> todos = new ArrayList<>();

        // lista.add(s);

        for (AristaAct a : grafo.get(s)) {
            if (!a.getHorarioDestino().ocupado()) {
                lista.add(a);
                obtenerRutas(s, d, visitados, lista, todos);
                lista = new ArrayList<>();
            }
        }
        return todos;
    }

    private void obtenerRutas(int u, int d,
            boolean[] visitados,
            ArrayList<AristaAct> rutaLocal,
            ArrayList<ArrayList<AristaAct>> todasRutas) {

        if (u == d) {
            todasRutas.add((ArrayList) rutaLocal.clone());
            return;
        }

        visitados[u] = true;

        for (AristaAct a : grafo.get(u)) {
            int i = a.getDestino();
            if (!visitados[i] && !a.getHorarioDestino().ocupado()) {
                rutaLocal.add(a);
                obtenerRutas(i, d, visitados, rutaLocal, todasRutas);
                a.getHorarioDestino().ocupar();
                rutaLocal.remove(new Integer(i));
            }
        }

        visitados[u] = false;
    }

    private ArrayList<Actividad> convertir(ArrayList<Integer> i) {
        ArrayList<Actividad> resultado = new ArrayList<>();
        for (int x : i) {
            resultado.add(vertices.get(x));
        }
        return resultado;
    }

    public void agregarActividad(Actividad a) {
        if (!vertices.contains(a)) {
            grafo.put(vertices.size(), new ArrayList<>());
            vertices.add(a);
        }
    }

    public Hora obtenerHorario(Actividad a1, Actividad a2, Horario h1, Horario h2) {
        int index = obtenerIndex(a1);
        for (AristaAct arista : grafo.get(index)) {
            if (a2.equals(vertices.get(arista.getDestino())) &&
                    h1.equals(arista.getHorarioOrigen()) &&
                    h2.equals(arista.getHorarioDestino())) {
                return arista.getPeso();
            }

        }
        return null;
    }

    // crea todas las aristas correspondientes por DIAS.
    public void agregarArista(Actividad o, Actividad d) {
        int index = obtenerIndex(o);
        // for (Integer dia : o.getHorario().keySet()) {
        for (Horario h1 : o.getHorario()) {
            for (Horario h2 : d.getHorario()) {
                grafo.get(index).add(new AristaAct(h2, h1, obtenerIndex(d)));
            }
        }
        // }
    }

    public boolean existeAct(String act) {
        for (Actividad a : vertices) {
            if (a.getNombre().equals(act)) {
                return true;
            }
        }
        return false;
    }

    public int obtenerIndex(Actividad a) {
        int respuesta = 0;
        while (respuesta < vertices.size() && !vertices.get(respuesta).equals(a)) {
            respuesta++;
        }
        return respuesta;
    }

    public ArrayList<Actividad> getVertices() {
        return vertices;
    }

    public void dibujarGrafo() {
        for (int i = 0; i < grafo.size(); i++) {
            System.out.println("vertice: " + i + ":" + grafo.get(i).toString());
        }
    }
}