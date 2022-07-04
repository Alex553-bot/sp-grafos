package App.Negocios;

import App.Calendario.Horario;

import java.util.ArrayList;

public class Actividad {
    private String nombre;
    //private HashMap<Integer, ArrayList<Horario>> horario;
    
    private ArrayList<Horario> horarios;
    
    
    //public Actividad(String n, ArrayList<Integer> diasSemana) {
    //    nombre = n;
//
    //    horario = new HashMap<>();
//
    //    for (int i=0; i<diasSemana.size(); i++) {
    //        if (diasSemana.get(i)<8 && diasSemana.get(i)>0) {
    //            horario.put(diasSemana.get(i), new ArrayList<Horario>());
    //        }
    //    }
    //}

    public Actividad(String n) {
        nombre = n;
        horarios = new ArrayList<>();
    }

    // debemos remover? 
    //public void actualizarHorario(Hora inicial, Hora fin, int diaSemana) {
    //    if (diaSemana > 0 && diaSemana < 8) {
    //        String id = nombre + horario.get(diaSemana).size();
    //        Horario h = new Horario(inicial, fin, id);
    //        horario.get(diaSemana).add(h);
    //    }
    //}

    public void ocuparHorario(Horario h) {
        //if (
        //        horario.containsKey(dia)
        //        && horario.get(dia).contains(h)
        //) {
            h.ocupar();
        //}
    }

    public void limpiar() {
        //for (Integer i: horario.keySet()) {
            for (Horario h: horarios) {
                h.desocupar();
            }
        //}
    }

    public String getNombre() {
        return nombre;
    }

    //public HashMap<Integer, ArrayList<Horario>> getHorario() {
    //    return horario;
    //}

    public ArrayList<Horario> getHorario() {
        return horarios;
    }

    public ArrayList<Horario> getLibre() {
        ArrayList<Horario> libres = new ArrayList<>();
        for (Horario h: horarios) {
            if (!h.ocupado()) {
                libres.add(h);
            }
        }

        return libres;
    }

    //public Iterator<Integer> getDias() {
    //    return horario.keySet().iterator();
    //}
    public boolean tieneLibre() {
        //for (Integer i: horario.keySet()) {
            for (Horario h: horarios) {
                if (!h.ocupado()) return true;
            }
        //}
        return false;
    }


    /**
     * RECORRIDO: 
     *      + Lunes: 
     *          - RECORRIDO1: 15:20 -> 16:10
     *          - RECORRIDO2: 
     *          - .....
     */
    @Override
    public String toString() {
        String reporte =    nombre + ":\n";
        //for (Integer i: horario.keySet()) {
        //    reporte +=  "+" + Fecha.str(i) + ":\n" + 
        //                "\t\t" + horario.get(i) + "\n\t+";
        //}
        for (Horario h: horarios) {
            reporte += "\t-" + h + "\n";
        }
        return reporte;
    }

    @Override
    public boolean equals(Object o) {
        boolean respuesta = o instanceof Actividad;
        if (respuesta) {
            Actividad aux = (Actividad)o;
            respuesta = aux.getNombre()==getNombre();
        }
        return respuesta;
    }
}