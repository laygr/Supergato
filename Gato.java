// Jesus Irais Gonzalez Romero
import javax.swing.*;

public class Gato{
	private static int hum = 1;
	private static String nombre;
	private static int cuadros = 3;
	private static int necesitados = 3;
	private static int puntos = 1;
	private static final String TITULO = "Super Gato. Hecho Por Lay";
	
	public static void main (String[] args) {
		Menu menu = new Menu(TITULO);
	}
	public static void iniciaJuego(){
		Juego juego = new Juego(TITULO, nombre, cuadros, necesitados, puntos, hum);
	}
	public static void setHum(int huM){
		hum = huM;
	}
	public static void setNombre(String nombrE){
		nombre = nombrE;
	}
	public static void setCuadros(int cuadroS){
		cuadros = cuadroS;
	}
	public static void setNecesitados(int necesitadoS){
		necesitados = necesitadoS;
	}
	public static void setPuntos(int puntoS){
		puntos = puntoS;
	}
}
	