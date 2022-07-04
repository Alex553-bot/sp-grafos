package App.Calendario;

public class Fecha implements Comparable
{
    private int diaSemana;
    private int dia;
    private int mes;
    private int anio;

    public Fecha(int d, int m, int a) {
        dia = d;
        mes = m;
        anio = a;
    }

    public static int getDiferencia(Fecha f1, Fecha f2) {
        int diferencia = 0;
        
        Fecha f3 = f1.clone();
        if (f1.compareTo(f2)<0) {
            f3 = f2.clone();
            f2 = f1;
        }
        while (!f3.equals(f2)) {
            f3.setDiaSemana(f3.getDia() + 1);
            diferencia++;
        }

        f3 = null;
        return diferencia;
    }

    public static String str(int d) {
        switch (d) {
            case 1:
                return "LUNES";
            case 2:
                return "MARTES";
            case 3:
                return "MIERCOLES";
            case 4:
                return "JUEVES";
            case 5:
                return "VIERNES";
            case 6:
                return "SABADO";
            default:
                return "DOMINGO";
        }
    }

    @Override
    public int compareTo(Object o) {
        Fecha f = (Fecha)o;
        int comp = Integer.compare(getAnio(), f.getAnio());
        if (comp == 0) {
            comp = Integer.compare(getMes(), f.getMes());
        }
        if (comp==0) {
            comp = Integer.compare(getDia(), f.getDia());
        }
        return comp;
    }

    public int calcularDiaSemana() {
        int respuesta = 6;

        switch (mes) {
            case 12:
                respuesta += 5;
                break;
            case 11:
                respuesta += 3;
                break;
            case 10:
                respuesta += 0;
                break;
            case 9:
                respuesta += 5;
                break;
            case 8:
                respuesta += 2;
                break;
            case 7:
                respuesta += 6;
                break;
            case 6:
                respuesta += 4;
                break;
            case 5:
                respuesta += 1;
                break;
            case 4:
                respuesta += 6;
                break;
            case 3:
            case 2:
                respuesta += 3;
                break;
            default:
                break;
        }

        respuesta += dia;

        return respuesta%7==0? 7: respuesta%7;
    }

    public void setDiaSemana(int dia) {
        if (mes == 2) {
            if (dia > 28) {
                this.dia = dia - 28;
                mes++;
            } else {
                this.dia = dia;
            }
        } else if (mes % 2 == 0) {
            if (dia > 30) {
                this.dia = dia - 30;
                this.mes = (this.mes + 1) % 12;
                if (this.mes == 0) {
                    this.anio++;
                }
            } else {
                this.dia = dia;
            }
        } else {
            if (dia > 31) {
                this.dia = dia - 31;
                this.mes = (this.mes + 1) % 12;
                if (this.mes == 0) {
                    this.anio++;
                }
            } else {
                this.dia = dia;
            }
        }
    }

    public void setMes(int mes) {
        this.mes = mes % 12 + 1;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    @Override
    public String toString() {
        return dia + "/" + mes + "/" + anio;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = o instanceof Fecha;
        if (res) {
            Fecha f = (Fecha) o;
            res = f.getDia() == getDia();
            res &= f.getMes() == getMes();
            res &= f.getAnio() == getAnio();
        }
        return res;
    }

    @Override
    public Fecha clone() {
        return new Fecha(dia, mes, anio);
    }
}