package App.Plano;

import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;

import App.Negocios.Actividad;
import App.Negocios.Estructura.AristaAct;
import App.Negocios.Estructura.GrafoAct;
import App.Calendario.Horario;

public class Lugar {
    private Coordenadas coordenadas;
    private String nombre;
    // actividades y la forma de desarrollo de las mismas
    private GrafoAct grafo;

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

    public ArrayList<Horario> componerPrograma(ArrayList<Actividad> camino, ArrayList<Horario> horario) {
        // esto se resuelve por dfs.
        if (camino.size()==1) {
            int i=0;
            Actividad a = camino.get(0);
            
            while (horario.size()==0 && i<a.getHorario().size()) {
                if (!a.getHorario().get(i).ocupado()) {
                    horario.add(a.getHorario().get(i));
                }
                i++;
            }
            return horario;
        }

        Stack<AristaAct> stack = new Stack<>();
        int index = 0;
        for (AristaAct arista : grafo.getAdyacentes(camino.get(0))) {
            index = camino.size()>1? 1:0;
            if (arista.getDestino() == grafo.obtenerIndex(camino.get(index))
                    && !arista.getHorarioOrigen().ocupado()
                    && !arista.getHorarioDestino().ocupado()) {
                stack.add(arista);
                horario.add(arista.getHorarioOrigen());
                
                while (!stack.empty()) {
                    AristaAct actual = stack.pop();
                    index++;
                    horario.add(actual.getHorarioDestino());
                    
                    if (horario.get(index-2).getFin().compareTo(horario.get(index-1).getInicio())==1) {
                        horario.remove(index-1);
                        return horario;
                    }

                    if (camino.get(camino.size() - 1).getHorario().contains(actual.getHorarioDestino())) {
                        return horario;
                    }

                    

                    // aqui agregamos
                    boolean auxiliar = false;
                    for (AristaAct ac : grafo.getAristasEntre(camino.get(index-1), camino.get(index))) {
                        if ( 
                                !ac.getHorarioOrigen().ocupado()
                                && !ac.getHorarioDestino().ocupado()
                            ) {
                            auxiliar = true;
                            stack.push(ac);
                        }
                    }
                    if (!auxiliar) {
                        index--;
                        horario.remove(horario.size() - 1);
                    }

                }

            }
        }
        return null;

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