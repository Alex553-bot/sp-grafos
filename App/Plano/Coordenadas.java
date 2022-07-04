package App.Plano;

public class Coordenadas {

    private double x; 
    private double y; 

    public Coordenadas(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {return x;}
    public double getY() {return y;}

    public void setX(double nx) {
        x = nx;
    }
    public void setY(double ny) {
        y = ny;
    }

    public static double calcularDistancia(Coordenadas actual, Coordenadas dest) {
        double distancia = 0;
        
        double x = actual.getX();
        double y = actual.getY();

        x = x-dest.getX();
        y = y-dest.getY();
        
        distancia = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        
        return distancia;
    }

    public static double calcularRelacion(Coordenadas p1, Coordenadas p2) {
        double m = 0;

        m = p2.getX()-p1.getX();
        m = m/(p2.getY()-p1.getY());

        return m;
    }

    public void mover(Coordenadas nuevo, double relacion) {
        this.x = (getX() + (1/relacion)*nuevo.getX())/(1+(1/relacion));
        this.y = (getY() + (1/relacion)*nuevo.getY())/(1+(1/relacion));
    }

    @Override
    public boolean equals(Object o) {
        boolean respuesta = o instanceof Coordenadas;
        if (respuesta) {
            Coordenadas p = (Coordenadas)o;
            respuesta = Math.abs(p.getX()-getX())<0.01;
            respuesta &= (Math.abs(p.getY()-getY())<0.01);
        }
        
        return respuesta;
    }
    @Override
    public String toString() {
        return "coordenadas: " + x + ", " + y; 
    }

}