package App.Negocios;

import App.Calendario.Fecha;
import App.Calendario.Hora;
import App.Calendario.Horario;
import App.Plano.Lugar;
import App.Estructuras.Grafo;

import java.util.ArrayList;
import java.util.HashMap;

public class Reserva {
    private Cliente cliente;
    private HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> programado;
    private HashMap<Fecha, ArrayList<String>> viajes;
    private Fecha inicial;

    public Reserva(Cliente c, HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> horario, Grafo g) {
        cliente = c;
        programado = horario;
        inicial = new Fecha(1, 1, Integer.MAX_VALUE);
        for (Fecha f: horario.keySet()) {
            if (f.compareTo(inicial)<0) {
                inicial = f;
            }
        }
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
        //Fecha ac = inicial.clone();
        for (Fecha ac: programado.keySet()) {
            reporte += ac.toString() + "\n";
            HashMap<Lugar, ArrayList<Horario>> pp  = programado.get(ac);
            if (pp!=null)
            for (Lugar l : pp.keySet()) {
                reporte += "\t" + l.getNombre() + "\n";
                for (Horario h : programado.get(ac).get(l)) {
                    reporte += "\t\t" + h.toString() + "\n";
                }
            }
            ac.setDiaSemana(ac.getDia()+1);
        }

        return reporte;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<Lugar> getRuta() {
        ArrayList<Lugar> ruta = new ArrayList<>();
        Fecha ac = inicial.clone();
        for (int i=0; i<programado.keySet().size(); i++) {
            HashMap<Lugar, ArrayList<Horario>> pp  = programado.get(ac);
            if (pp!=null)
            for (Lugar l : pp.keySet()) {
                ruta.add(l);
            }
            ac.setDiaSemana(ac.getDia()+1);
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
        //Fecha ac = inicial.clone();

        for (Fecha ac: programado.keySet()) {
            reporte += "Fecha" + ac.toString() + "\n";
            reporte += "VIAJES: \n";
            ArrayList<String> ax = viajes.get(ac);
            if (ax!=null)
            for (String s: ax) {
                reporte += "\t" + s + "\n";
            }
            reporte += "PROGRAMADO:\n";
            HashMap<Lugar, ArrayList<Horario>> pp  = programado.get(ac);
            if (pp!=null)
            
            for (Lugar l : pp.keySet()) {
                reporte += "-" + l.getNombre() + ":\n";
                for (Horario h : programado.get(ac).get(l)) {
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