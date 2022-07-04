package App.Calendario;

/***
 * Clase Hora para poder representar horarios de atencion, viajes, actividades
 * se utiliza las horas 0<=H<24
 * se utiliza los minutos: 0<=m<60
 */
public class Hora {

    private int hora;
    private int minutos;

    public Hora(int h, int m) {
        hora = h % 24;
        minutos = m % 60;
    }

    public static Hora getDif(Hora o, Hora d) {
        int h = d.getHora()-o.getHora();
        int m = d.getMinutos()-o.getMinutos();

        if (m<0) {
            h--;
            m += 60;
        }
        if (h<0) {
            h += 24;
        }

        return new Hora(h, m);
    }

    public int getHora() {
        return hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public int compareTo(Hora h) {
        int r = Integer.compare(getHora(), h.getHora());
        if (r==0) {
            r = Integer.compare(getMinutos(), h.getMinutos());
        }
        return r;
    }

    @Override
    public String toString() {
        return "["+hora + ":" + minutos+"]";
    }
}