package App.Plano;

import java.util.Iterator;
import java.util.ArrayList;

import App.Negocios.Actividad;
import App.Negocios.Estructura.*;
import BD.Datos;
import App.Calendario.Horario;

public class Lugar {
    private Coordenadas coordenadas;
    private String nombre;
    // actividades y la forma de desarrollo de las mismas
    private GrafoAct grafo;

    public Integer value = Integer.MAX_VALUE-1;

    public static void main(String[] args) {
        Lugar l = new Lugar(new Coordenadas(5, 5), "jj");
    
        Datos.agregarActividades(l);
        System.out.println(l.toString());
        
        Actividad a1 = l.buscarActividad("ALPINISMO");
        Actividad a2 = l.buscarActividad("CAMINATA");
        
        if (a1!=null && a2!=null) {
            ArrayList<ArrayList<Actividad>> ll = l.getProgramas().getRutas(a1, a2);
            ArrayList<Actividad> ac = ll.get(ll.size()-1);
            ArrayList<Horario> hh = new ArrayList<>();
            System.out.println(hh.toString());
            for (int i=0; i<ac.size()-1; i++) {
                l.componerProgramaMinimo(ac.get(i), ac.get(i+1), hh);
            }
            System.out.println(hh.toString());
        }
    }

    public Lugar(Coordenadas p, String n) {
        grafo = new GrafoAct();
        coordenadas = p;
        nombre = n;
    }

    public Actividad buscarActividad(String n) {
        Iterator<Actividad> x = grafo.getVertices().iterator();
        Actividad act = null;
        while (x.hasNext()) {
            act = x.next();
            if (act.getNombre().equals(n)) {
                return act;
            }
        }
        return null;
    }

    public void agregarActividad(Actividad a) {
        grafo.agregarActividad(a);
    }

    public GrafoAct getProgramas() {
        return grafo;
    }

    public Coordenadas getCoordenadas() {
        return coordenadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        nombre = n;
    }

    public void setCoordenadas(Coordenadas p) {
        coordenadas = p;
    }

    @Override
    public boolean equals(Object o) {
        boolean respuesta = o instanceof Lugar;
        if (respuesta) {
            respuesta = ((Lugar) o).getNombre().equals(getNombre());
            respuesta &= ((Lugar) o).getCoordenadas().equals(getCoordenadas());
        }
        return respuesta;
    }

    public static String recorridoStr(Lugar o, Lugar d, double distancia) {
        String respuesta = "De:" + o.getNombre() + "\n";

        return respuesta;
    }

    public void limpiar() {
        for (Actividad a : grafo.getVertices()) {
            a.limpiar();
        }
    }

    // debe tener un metodo donde devuelve un lista: Horarios de 1 dia (es decir el
    // ultimo horario debe ser <23:59)
    // como entrada debemos pasar una lista: Actividades(en el orden a realizar)

    public ArrayList<Horario> componerProgramaMinimo(Actividad origen, Actividad dest, ArrayList<Horario> h) {
        // actualizamos el ultimo.
        Horario ant = null;
        if (h.isEmpty()) {
            ArrayList<Horario> horariosOrigen = origen.getHorario();
            for (Horario oo: horariosOrigen) {
                if (ant == null) {
                    ant = oo;
                } else if (oo.getInicio().compareTo(ant.getInicio())<0) {
                    ant = oo;
                }
            }
            h.add(ant);
        } else {
            ant = h.get(h.size() - 1);
        }

        // encontramos ruta minima
        AristaAct minimo = null;

        int d = grafo.obtenerIndex(dest);

        for (AristaAct a : grafo.getAristasEntre(origen, dest)) {
            if (a.getDestino() == d && ant.equals(a.getHorarioOrigen())
                    && !a.getHorarioDestino().ocupado() 
                    && ant.getFin().compareTo(a.getHorarioDestino().getInicio())<0) {
                if (minimo == null) {
                    minimo = a;
                } else if (a.getPeso().compareTo(minimo.getPeso()) < 0) {
                    minimo = a;
                }
            }
        }
        
        if (minimo!=null)
        h.add(minimo.getHorarioDestino());
        
        return h;
    }

    public String verActs() {
        String res = "";
        for (Actividad a: grafo.getVertices()) {
            res += a.getNombre() + " , ";
        }
        return res;
    }

    public void imprimirHorariosDisponibles() {
        System.out.println(nombre);
        for (Actividad a: grafo.getVertices()) {
            System.out.println(a.getLibre());
        }
    }

    /***
     * "COCHABAMBA:
     * - Coordenadas:(x, y)
     * - Actividades:
     * + Recorrido por la ciudad:
     * Lunes:
     * Horarios: 18:00 --> 18:45
     * ...
     * + ...
     * "
     */
    @Override
    public String toString() {
        String reporte = nombre + ":\n";

        reporte += "Coordenadas: " + coordenadas.toString() + "\n" +
                "Actividades: \n" +
                grafo.getVertices().toString();

        return reporte;
    }
}