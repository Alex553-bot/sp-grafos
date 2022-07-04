package BD;

import java.util.ArrayList;

import App.Calendario.*;
import App.Negocios.*;
import App.Negocios.Estructura.*;
import App.Plano.*;

public class Datos {
    public static ArrayList<Lugar> obtenermapa(int n) {
        ArrayList<Lugar> lugares = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Lugar l = new Lugar(new Coordenadas(Math.random() * 500, Math.random() * 500), "Ciudad " + i);

            agregarActividades(l);

            lugares.add(l);
        }

        return lugares;
    }

    private static void agregarActividades(Lugar l) {
        for (int i = 0; i < 6; i++) {
            int x = (int) (Math.random() * 10);
            Actividad a = new Actividad(getActividad(x));
            agregarHorarios(a);
            l.agregarActividad(a);
        }
        GrafoAct grafo = l.getProgramas();
        ArrayList<Actividad> actividades = grafo.getVertices();
        for (int k = 0; k < actividades.size(); k++) {
            for (int j = 0; j < actividades.size(); j++) {
                if (k!=j) {
                    grafo.agregarArista(actividades.get(k), actividades.get(j));
                }
            }
        }
    }

    private static void agregarHorarios(Actividad a) {
        for (int i = 0; i < 6; i++) {
            Hora h1 = new Hora((int) (Math.random() * 17) + 6, (int) (Math.random() * 60));
            Hora h2;
            if (h1.getMinutos() > 29) {
                h2 = new Hora(h1.getHora() + 1, (h1.getMinutos() + 30) % 60);
            } else {
                h2 = new Hora(h1.getHora(), h1.getMinutos() + 30);
            }
            Horario hh = new Horario(h1, h2, a.getNombre() + (a.getHorario().size() + 1));
            a.getHorario().add(hh);
        }
    }

    private static String getActividad(int n) {
        switch (n) {
            case 1:
                return "CICLISMO";
            case 2:
                return "PARACAIDISMO";
            case 3:
                return "FOTOGRAFIA";
            case 4:
                return "TIROLESA";
            case 5:
                return "RECORRIDO POR CIUDAD";
            case 6:
                return "ALPINISMO";
            case 7: 
                return "CAMINATA";
            case 8:
                return "ZOOLOGICO";
            case 9: 
                return "PARQUE ACUATICO";
            default:
                return "VISITAR LUGAR TURISTICO";
        }
    }
}