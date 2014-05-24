// Jesus Irais Gonzalez Romero
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JFrame implements ActionListener {
	private static final int WINDOW_WIDTH = 265;	
	private static final int WINDOW_HEIGHT = 400;	
	
	JTextArea nombre;
	JTextArea menu;
	JLabel ficha;
	JButton fichas[];
	JButton start;
	JTextField eleccion;
	JButton tipos[];
	String descripciones[];
	FlowLayout flowLayout;
	JLabel intNombre;
	
	public Menu(String titulo){
		super(titulo);
		
		llenaTodo();
		agregaAVentana();
		ajustaVentana();
	}
	
	public void llenaTodo(){
		flowLayout = new FlowLayout();
		
		intNombre = new JLabel("Introduce tu nombre:");
		nombre= new JTextArea("Jugador");
		
		//fichas
		ficha = new JLabel("Escoge tu ficha");
		fichas= new JButton[2];
		fichas[0]= new JButton();
		fichas[0].setIcon(new ImageIcon(getClass().getResource("imagenes/0.png")));
		fichas[0].addActionListener(this);
		fichas[1]= new JButton();
		fichas[1].setIcon(new ImageIcon(getClass().getResource("imagenes/1.png")));
		fichas[1].addActionListener(this);
		
		//menu
		menu = new JTextArea(2,1);
		descripciones= new String[4];
		descripciones[0] = "1)   3x3        1             3";
		descripciones[1] = "2)   7x7        2             4";
		descripciones[2] = "3)   11x11     3             4";
		descripciones[3] = "4)   13x13     3             5";
		menu.setText("\n --Escoge un tipo de juego--\n\n  Tablero   Rayas    Casillas\n	     por raya");
		menu.setEditable(false);
		menu.setBackground(null);
		
		//botones
		tipos= new JButton[4];
		for(int i=0; i<4; i++){
			tipos[i]= new JButton(descripciones[i]);
			tipos[i].addActionListener(this);
		}
		
		eleccion= new JTextField("1");
		eleccion.setEditable(false);
		
		start= new JButton("Iniciar");
		start.addActionListener(this);
	}
	
	public void ajustaVentana(){
		setLayout(flowLayout);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);	
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void agregaAVentana(){
		add(intNombre);
		add(nombre);
		add(ficha);
		for(int i=1; i>=0; i--){
			add(fichas[i]);
		}
		add(menu);
		for(int i=0; i<4; i++){
			add(tipos[i]);
		}
		add(eleccion);
		add(start);
		setVisible(true);
	}
	
	public void cambiaFicha(int n){
		Gato.setHum(n);
	}
	
	public void cambiaTipo(int n){
		eleccion.setText(String.valueOf(n+1));
		switch (n) {
			case 0:
				Gato.setCuadros(3);
				Gato.setPuntos(1);
				Gato.setNecesitados(3);
				break;
			case 1:
				Gato.setCuadros(7);
				Gato.setPuntos(2);
				Gato.setNecesitados(4);
				break;
			case 2:
				Gato.setCuadros(11);
				Gato.setPuntos(3);
				Gato.setNecesitados(4);
				break;
			case 3:
				Gato.setCuadros(13);
				Gato.setPuntos(3);
				Gato.setNecesitados(5);
				break;
		}
	}
	
	public void actionPerformed(ActionEvent e){
		for(int i=0; i<2; i++){
			if(e.getSource().equals(fichas[i])){
				cambiaFicha(i);
			}
		}
		for(int i=0; i<4; i++){
			if(e.getSource().equals(tipos[i])){
				cambiaTipo(i);
			}
		}
		if(e.getSource()==start){
			Gato.setNombre(nombre.getText());
			Gato.iniciaJuego();
		}
	}
}
