package App.Negocios;

import App.Calendario.Fecha;
import App.Calendario.Horario;
import App.Plano.Lugar;
import App.Estructuras.Grafo;

import java.util.ArrayList;
import java.util.HashMap;

public class Reserva {
    private Cliente cliente;
    private HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> programado;
    private HashMap<Fecha, ArrayList<String>> viajes;

    public Reserva(Cliente c, HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> horario, Grafo g) {
        cliente = c;
        programado = horario;

        /***
         * De Ciudad 1: 12/05/2022 18:15
         * a Ciudad 2: 12/05/2022 23:50
         * Recorrido por bus
         * Recorrido por avion
         * Recorrido maritimo
         */
        viajes = new HashMap<>();
        Lugar aux = null;
        for (Fecha f : horario.keySet()) {
            ArrayList<String> vProgramados = new ArrayList<>();

            for (Lugar l : horario.get(f).keySet()) {
                if (aux != null && aux != l) {
                    vProgramados.add(g.lugarStr(aux, l));
                }
                aux = l;
            }
            viajes.put(f, vProgramados);
        }
    }

    public String getGuia() {
        String reporte = "";

        for (Fecha f : programado.keySet()) {
            reporte += f.toString() + "\n";
            for (Lugar l : programado.get(f).keySet()) {
                reporte += "\t" + l.getNombre() + "\n";
                for (Horario h : programado.get(f).get(l)) {
                    reporte += "\t\t" + h.toString() + "\n";
                }
            }

        }

        return reporte;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<Lugar> getRuta() {
        ArrayList<Lugar> ruta = new ArrayList<>();
        for (Fecha f : programado.keySet()) {
            for (Lugar l : programado.get(f).keySet()) {
                ruta.add(l);
            }
        }
        return ruta;
    }

    public ArrayList<Horario> getActividades() {
        ArrayList<Horario> act = new ArrayList<>();

        for (Fecha f : programado.keySet()) {
            for (Lugar l : programado.get(f).keySet()) {
                for (Horario h : programado.get(f).get(l)) {
                    act.add(h);
                }
            }
        }

        return act;
    }

    public String getPlanViaje() {
        String reporte = "";

        for (Fecha f : programado.keySet()) {
            reporte += "Fecha" + f.toString() + "\n";
            reporte += "VIAJES: \n";
            for (String s: viajes.get(f)) {
                reporte += "\t" + s + "\n";
            }
            reporte += "PROGRAMADO:\n";
            for (Lugar l : programado.get(f).keySet()) {
                reporte += "-" + l.getNombre() + ":\n";
                for (Horario h : programado.get(f).get(l)) {
                    reporte += "\t"+h.toString() + "\n";
                }
            }
        }

        return reporte;
    }


    public boolean contiene(Fecha f) {
        return programado.containsKey(f);
    }

    public HashMap<Lugar, ArrayList<Horario>> getHorario(Fecha f) {
        return programado.get(f);
    }
}