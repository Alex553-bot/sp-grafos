package App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import App.Calendario.*;

import App.Estructuras.Grafo;

import App.Negocios.*;
import App.Plano.*;

import BD.Datos;

public class Aplicacion {

    public static void main(String[] args) {
        ArrayList<Lugar> lugares = Datos.obtenermapa(8);
        Aplicacion aplicacion = new Aplicacion(lugares);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Opcion 1: imprimir lugares y su informacion");
            System.out.println("Opcion 2: crear Reserva");
            System.out.println("Opcion 3: imprimir Reservas");
            System.out.println("Opcion 4: imprimir Actividades de 1 lugar");
            System.out.println("Opcion 5: imprimir horarios disponibles de una fecha");
            System.out.println("Opcion 6: calcular costo minimo");
            System.out.println("Opcion 0: para salir");
            int opcion = (sc.nextInt());
            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1:
                    aplicacion.imprimirLugares();
                    break;
                case 3:
                    aplicacion.imprimirReservas();
                    break;
                case 4:
                    sc.nextLine();
                    System.out.println("CIUDAD : ");

                    System.out.println((aplicacion.buscarLugar(sc.nextLine())).verActs());
                    break;
                case 5:
                    sc.nextLine();
                    System.out.println("Dia");
                    int dia = sc.nextInt();
                    System.out.println("Mes:");
                    int mes = sc.nextInt();
                    System.out.println("Anio:");
                    int anio = sc.nextInt();
                    Fecha inicio = new Fecha(dia, mes, anio);
                    aplicacion.cargarDia(inicio);
                    aplicacion.imprimirVacios();
                    break;
                case 6: 
                    sc.nextLine();
                    Lugar l1 = aplicacion.buscarLugar(sc.nextLine());
                    Lugar l2 = aplicacion.buscarLugar(sc.nextLine());
                    aplicacion.getGrafo().dibujarGrafo();
                    System.out.println("MINIMO:" +aplicacion.getGrafo().getMinimo(l1, l2));
                    break;
                default:
                    // System.out.print("Cliente: ");
                    String cliente = sc.nextLine();
                    System.out.println("Lugar Origen: ");
                    Lugar origen = aplicacion.buscarLugar(sc.nextLine());
                    System.out.println(origen == null);
                    System.out.println("Lugar Destino");
                    Lugar destino = aplicacion.buscarLugar(sc.nextLine());
                    System.out.println("Dias minimos es:" +(aplicacion.getGrafo().getMinimo(origen, destino)/240+1)
                                        + "dias");
                    System.out.println("Actividades a realizar: ");
                    ArrayList<String> acts = new ArrayList<>();
                    while (true) {
                        String act = sc.nextLine();
                        if (act.isEmpty()) {
                            break;
                        }
                        acts.add(act);
                    }
                    System.out.println("Fecha inicio: \nDia:");
                    dia = sc.nextInt();
                    System.out.println("Mes:");
                    mes = sc.nextInt();
                    System.out.println("Anio:");
                    anio = sc.nextInt();
                    inicio = new Fecha(dia, mes, anio);
                    System.out.println("Fecha fin: \nDia:");
                    dia = sc.nextInt();
                    System.out.println("Mes:");
                    mes = sc.nextInt();
                    System.out.println("Anio:");
                    anio = sc.nextInt();
                    Fecha fin = new Fecha(dia, mes, anio);
                    System.out.println("Actividades guardadas:" + acts.toString());
                    aplicacion.crearReserva(cliente, inicio, fin, origen, destino, acts);
            }
        }
        sc.close();

        ArrayList<String> act = new ArrayList<>();
        act.add("PARACAIDISMO");
        act.add("VISITAR LUGAR TURISTICO");

        aplicacion.crearReserva(
                "Sr. Lopez",
                new Fecha(17, 06, 2022),
                new Fecha(29, 06, 2022),
                lugares.get(0), lugares.get(1),
                act);

        System.out.println(aplicacion.getReservas().get(0).getPlanViaje());
        System.out.println();

        act.clear();
        act.add("FOTOGRAFIA");
        act.add("PARACAIDISMO");
        act.add("VISITAR LUGAR TURISTICO");
        aplicacion.crearReserva(
                "Sr.X",
                new Fecha(20, 06, 2022),
                new Fecha(29, 06, 2022),
                lugares.get(0), lugares.get(3),
                act);
        System.out.println(aplicacion.getReservas().get(1).getPlanViaje());
    }

    private Grafo grafo;
    private ArrayList<Reserva> reservas;

    public Aplicacion(ArrayList<Lugar> lugares) {
        grafo = new Grafo(lugares.size(), lugares.iterator());
        for (int i = 0; i < lugares.size(); i++) {
            for (int j = 0; j < lugares.size(); j++) {
                if (i != j) {
                    grafo.insertarArista(lugares.get(i), lugares.get(j));
                }
            }
        }
        reservas = new ArrayList<>();
    }

    public Reserva crearReserva(
            String cliente,
            Fecha inicial, Fecha fin,
            Lugar origen, Lugar dest,
            ArrayList<String> actividades) {

        ArrayList<ArrayList<Lugar>> caminos = grafo.getRutas(origen, dest);
        Fecha aux = inicial.clone();

        for (int i=caminos.size()-1; i>-1; i--) {
            ArrayList<Lugar> camino = caminos.get(i);
            inicial = aux.clone();

            // ordenamos las actividades,
            HashMap<Lugar, ArrayList<Actividad>> actsAux = convertirActividades(actividades, camino);
            // debemos saber si existen lugarse vacios
            if (actsAux != null) {
                HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> cronograma = llenarCronograma(actsAux, inicial, fin);
                if (cronograma != null) {
                    Reserva x = new Reserva(new Cliente(cliente), cronograma, grafo);
                    reservas.add(x);
                    return x;
                }
            }
        }
        return null;
    }

    private HashMap<Lugar, ArrayList<Actividad>> convertirActividades(ArrayList<String> cadenas,
            ArrayList<Lugar> lugares) {
        HashMap<Lugar, ArrayList<Actividad>> acts = new HashMap<>();
        for (Lugar l : lugares) {
            ArrayList<Actividad> actsAux = new ArrayList<>();
            for (String s : cadenas) {
                Actividad a = l.buscarActividad(s);
                if (a != null) {
                    actsAux.add(a);
                }
            }
            if (actsAux.isEmpty()) {
                return null;
            }
            acts.put(l, actsAux);
        }
        return acts;
    }

    public HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> llenarCronograma(
            HashMap<Lugar, ArrayList<Actividad>> actividades,
            Fecha inicial, Fecha fin) {
        HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> crono = new HashMap<>();

        Fecha clon = inicial.clone();
        crono.put(clon, new HashMap<>());
        for (Lugar l : actividades.keySet()) {
            crono.get(clon).put(l, new ArrayList<>());
            int index = 0;
            cargarDia(inicial);
            ArrayList<Actividad> acts = actividades.get(l);
            while (index < acts.size()) {
                ArrayList<Horario> cronogramaDia = crearCronograma(acts, l, index);

                //if (aux.compareTo(cronogramaDia.get(0).getInicio()) > 0) {
                //    System.out.println("Cambiando de dia, por act" + inicial.toString());
                //    inicial.setDiaSemana(inicial.getDia() + 1);
                //    clon = inicial.clone();
                //    crono.put(clon, new HashMap<>());
                //    crono.get(clon).put(l, new ArrayList<>());
                //}

                for (Horario h : cronogramaDia) {
                    crono.get(clon).get(l).add(h);
                }

                inicial.setDiaSemana(inicial.getDia() + 1);
                clon = inicial.clone();
                crono.put(clon, new HashMap<>());
                crono.get(clon).put(l, new ArrayList<>());
                cargarDia(inicial);
                if (inicial.compareTo(fin) > 0) {
                    return null;
                }

                index += cronogramaDia.size();
            }
            //System.out.println("Cambiando dia por lugar" + inicial.toString());
            inicial.setDiaSemana(inicial.getDia() + 1);
            clon = inicial.clone();
            crono.put(clon, new HashMap<>());
        }

        return crono;
    }

    private ArrayList<Horario> crearCronograma(
            ArrayList<Actividad> actividades,
            Lugar lugar, int index) {
        ArrayList<Actividad> acts = new ArrayList<>();

        for (int i = index; i < actividades.size(); i++) {
            acts.add(actividades.get(i));
        }
        ArrayList<Horario> horarioMin = new ArrayList<>();
        if (acts.size() == 1) {
            lugar.componerProgramaMinimo(actividades.get(0), actividades.get(0), horarioMin);
        } else {
            for (int i = 0; i < actividades.size() - 1; i++) {
                int aux = horarioMin.size();
                lugar.componerProgramaMinimo(actividades.get(i), actividades.get(i + 1), horarioMin);
                if (aux == horarioMin.size())
                    break;
            }
        }
        if (horarioMin.isEmpty())
            return null;
        return horarioMin;
    }

    // metodo donde vemos el estado de nuestros lugares y sus disponibilidades.
    public void cargarDia(Fecha f) {
        // debemos vaciar el grafo y luego llenar el dia
        vaciarHorario();

        for (Reserva r : reservas) {
            if (r.contiene(f)) {
                HashMap<Lugar, ArrayList<Horario>> mapa = r.getHorario(f);
                for (Lugar l : mapa.keySet()) {
                    ArrayList<Horario> horario = mapa.get(l);
                    for (Horario h : horario) {
                        Actividad a = l.buscarActividad(h.getID());
                        a.ocuparHorario(h);
                    }
                }
            }
        }
    }
    public void imprimirVacios() {
        System.out.println();
        System.out.println("VACANTES EN TODOS LOS LUGARES");
        for (Lugar l: grafo.getVertices()) {
            l.imprimirHorariosDisponibles();
        }
    }

    public void vaciarHorario() {
        // tenemos que vaciar cada Lugar
        Lugar[] lugares = grafo.getVertices();
        for (Lugar l : lugares) {
            l.limpiar();
        }
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public void imprimirLugares() {
        for (Lugar l : grafo.getVertices()) {
            System.out.println(l.toString());
            // l.getProgramas().dibujarGrafo();
        }
    }

    public void imprimirReservas() {
        for (Reserva r : reservas) {
            System.out.println("RESERVA: \nGUIA:");
            System.out.println(r.getGuia());
            System.out.println("PLAN DE VIAJE:");
            System.out.println(r.getPlanViaje());
        }
    }

    public Lugar buscarLugar(String x) {
        for (Lugar l : grafo.getVertices()) {
            if (l.getNombre().equals(x)) {
                return l;
            }
        }
        return null;
    }
}