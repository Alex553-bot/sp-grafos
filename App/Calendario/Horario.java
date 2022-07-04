package App.Calendario;

public class Horario {
    private String id;
    private Hora inicio;
    private Hora fin;
    private boolean ocupado;

    public Horario(Hora i, Hora f, String id) {
        this.id = id;
        inicio = i;
        fin = f;
        ocupado = false;
    }

    public void desocupar() {
        ocupado = false;
    }
    
    public void ocupar() {
        ocupado = true;
    }

    public boolean ocupado() {
        return ocupado;
    }

    // para actualizar horarios.
    public void setInicio(Hora i) {
        inicio = i;
    }

    public void setFin(Hora f) {
        fin = f;
    }

    public Hora getFin() {
        return fin;
    }

    public Hora getInicio() {
        return inicio;
    }

    public String getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = o instanceof Horario;
        if (res) {
            Horario aux = (Horario)o;
            res = getID().equals(aux.getID());
            res &= getInicio().equals(aux.getInicio());
            res &= getFin().equals(aux.getFin());
        }
        
        return res;
    }


    /***
     * HORARIO: 15:40 - 16:30 
     *      
     */
    @Override
    public String toString() {
        
        return id + ":"+ inicio.toString() + "-" + fin.toString();
    }
}