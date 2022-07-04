package App.Estructuras;

public class Arista 
{
    private int destino;
    private float peso;

    public Arista(int d, float p) {
        destino = d;
        peso = p;
    }

    @Override
    public boolean equals(Object o) {
        boolean respuesta = o instanceof Arista;
        if (respuesta) {
            respuesta &= ((Arista)o).gesDestino()==destino;
        }
        return respuesta;
    }

    @Override
    public String toString() {
        String resultado = "(" + destino + "," +peso+")";

        return resultado;
    }

    public int gesDestino() {return destino;}
    public double getPeso() {return peso;}
}