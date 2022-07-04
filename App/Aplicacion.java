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
        ArrayList<Lugar> lugares = Datos.obtenermapa(5);
        Aplicacion aplicacion = new Aplicacion(lugares);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Opcion 1: imprimir lugares y su informacion");
            System.out.println("Opcion 2: crear Reserva");
            System.out.println("Opcion 3: imprimir Reservas");
            System.out.println("Opcion 0: para salir");
            int opcion = (sc.nextInt());
            if (opcion==0) {
                break;
            }
            switch (opcion) {
                case 1: aplicacion.imprimirLugares();
                    break;
                case 3: aplicacion.imprimirReservas();
                    break;
                default:
                    System.out.print("Cliente: ");
                    String cliente = sc.nextLine();
                    System.out.println("Lugar Origen: ");
                    Lugar origen = aplicacion.buscarLugar(sc.nextLine());
                    System.out.println("Lugar Destino");
                    Lugar destino = aplicacion.buscarLugar(sc.nextLine());
                    System.out.println("Actividades a realizar: ");
                    ArrayList<String> acts = new ArrayList<>();
                    while (true) {
                        if (sc.nextLine().isEmpty()) {
                            break;
                        }
                        acts.add(sc.nextLine());
                    }
                    System.out.println("Fecha inicio: \nDia:");
                    int dia = sc.nextInt();
                    System.out.println("Mes:");
                    int mes = sc.nextInt();
                    System.out.println("Anio:");
                    int anio = sc.nextInt();
                    Fecha inicio = new Fecha(dia, mes, anio);
                    System.out.println("Fecha fin: \nDia:");
                    dia = sc.nextInt();
                    System.out.println("Mes:");
                    mes = sc.nextInt();
                    System.out.println("Anio:");
                    anio = sc.nextInt();
                    Fecha fin = new Fecha(dia, mes, anio);
                    aplicacion.crearReserva(cliente, inicio, fin, origen, destino, acts);
            }
        }

        ArrayList<String> act = new ArrayList<>();
        act.add("PARACAIDISMO");
        act.add("VISITAR LUGAR TURISTICO");

        aplicacion.crearReserva(
                "Sr. Lopez", 
                new Fecha(17, 06, 2022), 
                new Fecha(29, 06, 2022),
                lugares.get(0), lugares.get(1), 
                act);

        System.out.println(aplicacion.getReservas().get(0).getGuia());
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
        for (ArrayList<Lugar> camino : caminos) {
            inicial = aux.clone();

            // ordenamos las actividades,
            HashMap<Lugar, ArrayList<Actividad>> actsAux = convertirActividades(actividades, camino);
            // debemos saber si existen lugarse vacios
            if (actsAux != null) {
                HashMap<Fecha, HashMap<Lugar, ArrayList<Horario>>> cronograma = llenarCronograma(actsAux, inicial, fin);
                // System.out.println("llego");
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

        Hora aux = new Hora(0, 0);
        Fecha clon = inicial.clone();
        crono.put(clon, new HashMap<>());
        for (Lugar l : actividades.keySet()) {
            crono.get(clon).put(l, new ArrayList<>());
            int index = 0;
            cargarDia(inicial);
            ArrayList<Actividad> acts = actividades.get(l);
            while (index < acts.size()) {
                ArrayList<Horario> cronogramaDia = crearCronogramas(acts, l, index);

                if (aux.compareTo(cronogramaDia.get(0).getInicio()) > 0) {
                    inicial.setDiaSemana(inicial.getDia() + 1);
                    clon = inicial.clone();
                    crono.put(clon, new HashMap<>());
                    crono.get(clon).put(l, new ArrayList<>());
                }

                for (Horario h : cronogramaDia) {
                    crono.get(clon).get(l).add(h);
                    aux = h.getFin();
                }

                inicial.setDiaSemana(inicial.getDia() + 1);
                cargarDia(inicial);
                if (inicial.compareTo(fin) > 0) {
                    return null;
                }

                index += cronogramaDia.size();
            }
            aux = new Hora(0, 0);
            inicial.setDiaSemana(inicial.getDia() + 1);
            clon = inicial.clone();
            crono.put(clon, new HashMap<>());
        }

        return crono;
    }

    private ArrayList<Horario> crearCronogramas(
            ArrayList<Actividad> actividades,
            Lugar lugar, int index) {

        ArrayList<ArrayList<Actividad>> caminos = new ArrayList<>();
        ArrayList<Actividad> acts = new ArrayList<>();
        for (int x = index; x < actividades.size(); x++) {
            acts.add(actividades.get(x));
        }

        for (int i = 0; i < acts.size(); i++) {
            for (int j = 0; j < acts.size(); j++) {
                caminos = lugar.getProgramas().getRutas(acts.get(i),
                        acts.get(j));

                for (ArrayList<Actividad> aux : caminos) {
                    if (!contiene(aux, acts)) {
                        // caminos.remove(aux);
                    } else {
                        ArrayList<Horario> horarios = lugar.componerPrograma(aux, new ArrayList<>());
                        if (horarios != null && horarios.size() > 0) {
                            // System.out.println("Pudo agregar horarios");
                            return horarios;
                        }

                    }
                }
            }
        }

        return null;
    }

    private boolean contiene(ArrayList<Actividad> l1, ArrayList<Actividad> l2) {
        for (Actividad a : l2) {
            if (!l1.contains(a)) {
                return false;
            }
        }
        return true;
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
            //l.getProgramas().dibujarGrafo();
        }
    }
    public void imprimirReservas() {
        for (Reserva r: reservas) {
            System.out.println("RESERVA: \nGUIA:");
            System.out.println(r.getGuia());
            System.out.println("PLAN DE VIAJE:");
            System.out.println(r.getPlanViaje());
        }
    }

    public Lugar buscarLugar(String x) {
        for (Lugar l: grafo.getVertices()) {
            if (l.getNombre().equals(x)) {
                return l;
            }
        }
        return null;
    }
}