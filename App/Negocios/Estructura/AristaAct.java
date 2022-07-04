package App.Negocios.Estructura;

import App.Calendario.*;

public class AristaAct 
{
    private Horario actDestino; 
    private Hora peso;
    private Horario actOrigen;
    private int dest;


    // en este grafo tenemos que tener en cuenta que es un arbol de actividades, ciclico, que podemos realizar.
    public AristaAct(Horario h, Horario i, int d) {
        this.dest = d;
        actDestino = h;
        peso = Hora.getDif(h.getFin(), i.getInicio());
        actOrigen = i;
    }

    public Horario getHorarioOrigen() {
        return actOrigen;
    }
    public Horario getHorarioDestino() {
        return actDestino;
    }
    public Hora getPeso() {
        return peso;
    }
    public int getDestino() {
        return dest;
    }


    /**
     * Peso: peso
     * al Vertice: index
     */
    @Override
    public String toString() {
        return "Peso: "+ peso.toString() + ", al vertice: " + dest + "\n";
    }
}