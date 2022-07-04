package App.Negocios;

public class Cliente
{
    private String nombre; 
    private String passw;

    public Cliente(String n) {
        nombre = n; 
        passw = n;
    }

    public boolean verificar(String pp) {
        return passw.equals(pp);
    }

    public void setPassw(String p) {
        passw = p;
    }

    public String getNombre() {return nombre;}
    
}