//Jesus Irais Gonzalez Romero
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Juego extends JFrame implements ActionListener {
	private int ALTURA;
	private GridLayout gridLayout;
	
	private int HUM;
	private int COMP;
	private String NOMBRE;
	private int CUADROS;
	private int NECESITADOS;
	private int PUNTOS;
	private int arr[][][][][][];
	private int tiro[];
	private double score[];
	private double valor[][][][];
	private int necePorNece[][][];
	private Casilla tablero[][];
	private int juegos;
	private int puntuacion[];
	private int turno;
	private boolean puedeMover;
	private boolean cond[];
	private boolean cond2[];
	private int instrucciones[][];
	private boolean segura[][][][];
	private boolean puedeTirar[][][];
	private boolean perteneceASegura[][][][];
	private int lineasDobles[];
	
	public Juego(String titulo, String nombre, int cuadros, int necesitados, int puntos, int hum) {
		super(titulo);
		
		HUM = hum;
		COMP = X(hum);
		NOMBRE = nombre;
		CUADROS = cuadros;
		NECESITADOS = necesitados;
		PUNTOS = puntos;
		
		lineasDobles = new int [2];
		perteneceASegura = new boolean [2][CUADROS][CUADROS][4];
		segura = new boolean [2][CUADROS][CUADROS][4];
		puedeTirar = new boolean [2][CUADROS][CUADROS];
		arr = new int[2][CUADROS][CUADROS][4][NECESITADOS][3];
		tiro = new int[2];
		score = new double[2];
		valor = new double[2][CUADROS][CUADROS][2];
		necePorNece = new int[2][CUADROS][CUADROS];
		tablero = new Casilla[CUADROS][CUADROS];
		juegos = 0;
		puntuacion = new int[2];
		puntuacion[0] = 0;
		puntuacion[1] = 0;
		
		turno = COMP;
		puedeMover = true;
		
		cond = new boolean[4];
		cond2 = new boolean[4];
		instrucciones = new int [4][2];
		instrucciones[0][1]= 1;
		instrucciones[0][0]= 0;
		
		instrucciones[1][1]= 0;
		instrucciones[1][0]= 1;
		
		instrucciones[2][1]= 1;
		instrucciones[2][0]= 1;
		
		instrucciones[3][1]=-1;
		instrucciones[3][0]= 1;
		
		llenaTablero();
		ajustaVentana();
		reinicia();
		
	}
	public void llenaTablero(){
		for(int r=0; r<CUADROS; r++){
			for(int c=0; c<CUADROS; c++){
				tablero[r][c] = new Casilla();
				tablero[r][c].addActionListener(this);
				add(tablero[r][c]);
			}
		}
	}
	
	public void ajustaVentana(){
		gridLayout = new GridLayout(CUADROS, CUADROS);
		setLayout(gridLayout);
		ALTURA = CUADROS*68;
		setSize(ALTURA, ALTURA);	
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);
	}
	public void reinicia(){
		turno=X(turno);
		String msg;
		if(turno==COMP){
			msg="Computadora";
		}else msg=NOMBRE;
		JOptionPane.showMessageDialog(null, msg+" inicia");
		juegos++;
		score[0]=0;
		score[1]=0;
		for(int r=0; r<CUADROS; r++){
			for(int c=0; c<CUADROS; c++){
				tablero[r][c].ficha=-1;
				tablero[r][c].setIcon(null);
				tablero[r][c].setBackground(null);
				for(int i=0; i<4; i++){
					arr[0][r][c][i][0][0]=0;
					arr[1][r][c][i][0][0]=0;
				}
			}
		}
		limpia();
		if(turno==COMP){
			compuMueve();
		}
	}
	
	public void compuMueve(){
		puedeMover=false;
		evaluaTablero();
		despliega();
		tablero[tiro[0]][tiro[1]].ficha=COMP;
		System.out.println("tiro ("+tiro[0]+","+tiro[1]+")");
		tablero[tiro[0]][tiro[1]].setIcon(new ImageIcon(getClass().getResource("imagenes/"+COMP+".png")));
		if(checa(COMP)){
			puntuacion[COMP]++;
			resultado("Perdiste\n" + punt(), "imagenes/Loser.gif");
			return;
		}else if(!posible()){
			resultado("Empate\n" + punt(),"imagenes/Empate.gif");
		}
	}
	public void pertenece(int N){
		for(int f = 0; f<2; f++){
			for(int r = 0; r<CUADROS; r++){
				for(int c = 0; c<CUADROS; c++){
					if(necePorNece[f][r][c]>0){
						for(int i = 0; i<4; i++){
							if(arr[f][r][c][i][0][0]==2){
								for(int n = 0; n<arr[f][r][c][i][0][0]; n++){
									if(necePorNece[f][arr[f][r][c][i][n][1]][arr[f][r][c][i][n][2]]>0&&(r!=arr[f][r][c][i][n][1]||c!=arr[f][r][c][i][n][2])){
										necePorNece[f][arr[f][r][c][i][n][1]][arr[f][r][c][i][n][2]]=-1;
										necePorNece[f][r][c]=-1;
										necePorNece(N-1);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	public void evaluaTablero(){
		raya();
		esLineaSeguraOPertenece();
		boolean puedePasar = true;
		int ajuste = 0;
		for(int f=0; f<2; f++){
			System.out.println(lineasDobles[f]+"lineas dobles");
			if(f==HUM){
				ajuste = 1;
			} else ajuste = 0;
			for(int r=0; r<CUADROS; r++){
				for(int c=0; c<CUADROS; c++){
					for(int i=0; i<4; i++){
						for(int n = 0; n<arr[f][r][c][i][0][0]; n++){
							if(segura[f][arr[f][r][c][i][n][1]][arr[f][r][c][i][n][2]][i]){
								puedePasar = false;
								break;
							}
						}
						if(arr[f][r][c][i][0][0]>0 && !perteneceASegura[f][r][c][i]  && (puedePasar || segura[f][r][c][i]) ){
							for(int n=0; n<arr[f][r][c][i][0][0]; n++){
								if(valor[f][arr[f][r][c][i][n][1]] [arr[f][r][c][i][n][2]] [1]>arr[f][r][c][i][0][0]&&arr[f][r][c][i][0][0]>0 ){
									valor[f][arr[f][r][c][i][n][1]] [arr[f][r][c][i][n][2]] [1]=arr[f][r][c][i][0][0];
								}
								if(!segura[f][arr[f][r][c][i][n][1]][arr[f][r][c][i][n][2]][i] || (double)NECESITADOS/Math.pow((arr[f][r][c][i][0][0]-(1.0/(PUNTOS-score[f]))),3) == Double.POSITIVE_INFINITY
								   ||(f==COMP && score[COMP]>=score[HUM]) || puedeTirar[f][arr[f][r][c][i][n][1]][arr[f][r][c][i][n][2]]){
									valor[f][arr[f][r][c][i][n][1]] [arr[f][r][c][i][n][2]] [0]+=(double)NECESITADOS/Math.pow((arr[f][r][c][i][0][0]-(1.0/(PUNTOS-score[f]))),4)/(Math.pow((arr[f][r][c][i][0][0]+ajuste),4)*lineasDobles[X(f)]);
								}
							}
						}
						puedePasar = true;
					}
				}
			}
		}
		if(!reevaluaTablero()){
			imprimeValor();
		}
	}
	public boolean reevaluaTablero(){
		boolean res=false;
		int cont = 0;
		necePorNece(4);
		for(int r = 0; r<CUADROS; r++){
			for(int c = 0; c<CUADROS; c++){
				if(necePorNece[HUM][r][c]>0){
					//System.out.println("Peligro ("+r+","+c+")");
					cont++;
				}
			}
		}
		if(cont>1){
			double dif=-1;
			for(int r=0; r<CUADROS; r++){
				for(int c=0; c<CUADROS; c++){
					if(valor[COMP][r][c][1]<=2 && valor[HUM][r][c][0]>dif && necePorNece[HUM][r][c]==0){
						dif=valor[HUM][r][c][0];
						tiro[0]=r;
						tiro[1]=c;
						res = true;
						//System.out.println("Defensivo\n"+r+","+c);
					}
				}
			}
		}
		return res;
	}
	public void necePorNece(int N){
		if(N>0){
			for(int f = 0; f<2; f++){
				for(int r = 0; r<CUADROS; r++){
					for(int c = 0; c<CUADROS; c++){
						for(int i = 0; i<4; i++){
							if(arr[f][r][c][i][0][0]==2 && necePorNece[f][r][c]!=-1){
								for(int n = 0; n<arr[f][r][c][i][0][0]; n++){
									necePorNece[f][arr[f][r][c][i][n][1]][arr[f][r][c][i][n][2]]++;
								}
							}
						}
					}
				}
			}
			
			for(int f = 0; f<2; f++){
				for(int r = 0; r<CUADROS; r++){
					for(int c = 0; c<CUADROS; c++){
						if(necePorNece[f][r][c]<2){
							necePorNece[f][r][c]=0;
						}
					}
				}
			}
			pertenece(N);
		}
	}
	
	public void imprimeValor(){
		double mayor=0;
		int f= HUM;
		for(int x=0; x<2; x++){
			f=X(f);
			for(int r=0; r<CUADROS; r++){
				for(int c=0; c<CUADROS; c++){
					if(valor[f][r][c][0] > mayor &&tablero[r][c].ficha==-1){
						mayor=valor[f][r][c][0];
						tiro[0]=r;
						tiro[1]=c;
					}
				}
			}
		}
		//System.out.println("Mejor tiro");
	}
	
	public void esLineaSeguraOPertenece(){
		for(int F = 0; F<2; F++){
			for(int R = 0; R<CUADROS; R++){
				for(int C=0; C<CUADROS; C++){
					for(int I=0; I<4; I++){
						cond[0] = C+(NECESITADOS*instrucciones[I][1])<CUADROS;
						cond[1] = R+(NECESITADOS*instrucciones[I][0])<CUADROS;
						cond[2] = (R+(NECESITADOS*instrucciones[I][0])<CUADROS && C+(NECESITADOS*instrucciones[I][1])<CUADROS);
						cond[3] = (R+(NECESITADOS*instrucciones[I][0])<CUADROS && C+(NECESITADOS*instrucciones[I][1])>=0);
						if(cond[I]){
							if(arr[F][R][C][I][0][0]==1 && arr[F][R][C][I][0][1]==R && arr[F][R][C][I][0][2]==C){
								if(tablero[arr[F][R][C][I][0][1]+(NECESITADOS*instrucciones[I][0])][arr[F][R][C][I][0][2]+(NECESITADOS*instrucciones[I][1])].ficha==-1){
									//System.out.println("A ("+F+","+R+","+C+") le falta uno normal");
									segura[F][R][C][I]  = true;
									segura[F][R+(NECESITADOS*instrucciones[I][0])][C+(NECESITADOS*instrucciones[I][1])][I] = true;
									for(int necesitados = 2; necesitados < NECESITADOS; necesitados++){
										perteneceASegura[F][R+(necesitados*instrucciones[I][0])][C+(necesitados*instrucciones[I][1])][I] = true;
									}
								}
							}
						}
					}
				}
			}
		}
		corrige();
	}
	
	public void corrige(){
		int cont = 0;
		for(int F = 0; F<2; F++){
			for(int R = 0; R<CUADROS; R++){
				for(int C=0; C<CUADROS; C++){
					for(int I=0; I<4; I++){
						if (segura[F][R][C][I] || perteneceASegura[F][R][C][I]){
							cont++;
						}
						if (cont >=2){
							puedeTirar [F][R][C] = true;
							lineasDobles[F] = cont;
							break;
						}
					}
					cont = 0;
				}
			}
		}
	}
	
	public void limpia(){
		for(int f=0; f<2; f++){
			lineasDobles[f] = 1;
			for(int r=0; r<CUADROS; r++){
				for(int c=0; c<CUADROS; c++){
					necePorNece[f][r][c]=0;
					valor[f][r][c][0]=0;
					valor[f][r][c][1]=NECESITADOS;
					for(int i=0; i<4; i++){
						segura[f][r][c][i] = false;
						perteneceASegura[f][r][c][i] = false;
						puedeTirar[f][r][c] = false;
					}
				}
			}
		}
	}
	
	public void marcar(int F, int R, int C, int I){
		switch (I) {
			case 0:
				for(int c=C+1, r=R; c<C+NECESITADOS; c++){
					arr[F][r][c][I][0][0]=-2;
				}
				break;
			case 1:
				for(int r=R+1, c=C; r<R+NECESITADOS; r++){
					arr[F][r][c][I][0][0]=-2;
				}
				break;
			case 2:
				for(int r=R+1, c=C+1; r<R+NECESITADOS; r++, c++){
					arr[F][r][c][I][0][0]=-2;
				}
				break;
			case 3:
				for(int r=R+1, c=C-1; r<R+NECESITADOS; r++, c--){
					arr[F][r][c][I][0][0]=-2;
				}
				break;
		}
	}
	public void raya(){
		limpia();
		for(int F=0; F<2; F++){
			for(int R=0; R<CUADROS; R++){
				for(int C=0; C<CUADROS; C++){
					cond[0]= C<=CUADROS-NECESITADOS||arr[F][R][C][0][0][0]==-2;
					cond[1]= R<=CUADROS-NECESITADOS||arr[F][R][C][1][0][0]==-2;
					cond[2]= (R<=CUADROS-NECESITADOS && C<=CUADROS-NECESITADOS)||arr[F][R][C][2][0][0]==-2;
					cond[3]= (R<=CUADROS-NECESITADOS && C>=NECESITADOS-1)||arr[F][R][C][3][0][0]==-2;
					for(int I=0; I<4; I++){
						if(cond[I]){
							//System.out.println(F+","+R+","+C+","+I+" LLenando raya");
							for(int ne=0, n=0, nB=0, r=R, c=C; ne<NECESITADOS; r+=instrucciones[I][0],c+=instrucciones[I][1],ne++){
								if(arr[F][R][C][I][0][0]>-1){
									if(tablero[r][c].ficha==F && arr[F][r][c][I][0][0]!=-2){
										nB++;
									}
									if(tablero[r][c].ficha!=F){
										if(n==0){
											arr[F][R][C][I][0][0]=0;
										}
										arr[F][R][C][I][0][0]++;
										arr[F][R][C][I][n][1]=r;
										arr[F][R][C][I][n][2]=c;
										n++;
										if(tablero[r][c].ficha!=-1){
											arr[F][R][C][I][0][0]=-1;
										}
									} else if(arr[F][r][c][I][0][0]==-2){
										arr[F][R][C][I][0][0]=-1;
									}
									if(nB==NECESITADOS){
										arr[F][R][C][I][0][0]=-2;
										score[F]++;
										marcar(F,R,C,I);
									}
								}
							}
						} else arr[F][R][C][I][0][0]=-1;
					}
				}
			}
		}
	}
	public boolean posible(){
		raya();
		int cont= 0;
		boolean posible=false;
		for(int f=0; f<2; f++){
			cont=0;;
			for(int r=0; r<CUADROS; r++){
				for(int c=0; c<CUADROS; c++){
					for(int i=0; i<4; i++){
						if(arr[f][r][c][i][0][0]>0){
							cont++;
							if(cont>=PUNTOS){
								posible=true;
							}
						}
						for(int n=0; n<arr[f][r][c][i][0][0]; n++){
						}
					}
				}
			}
		}
		return posible;
	}
	public int X(int f){
		return (f-1)*-1;
	}
	public void resultado(String msg, String imagen){
		JOptionPane.showMessageDialog(null, msg ,msg ,-1,new ImageIcon(getClass().getResource(imagen)));
		reinicia();
	}
	public String punt(){
		return NOMBRE+" "+puntuacion[HUM]+" || Computadora "+puntuacion[COMP]+"\nEmpates "+ (juegos-puntuacion[HUM]-puntuacion[COMP]);
	}
	public boolean checa(int f){
		raya();
		int cont=0;
		for(int r =0; r<CUADROS; r++){
			for(int c = 0; c<CUADROS; c++){
				for(int i = 0; i<4; i++){
					tablero[r][c].setOpaque(true);
					if(arr[f][r][c][i][0][0]==-2){
						if(f==1){
							tablero[r][c].setBackground(new Color(100,30,20));
						} else {
							tablero[r][c].setBackground(new Color(30,200,20));
						}
					}
				}
			}
		}
		if(score[f]>=PUNTOS){
			return true;
		}
		return false;
	}
	public void despliega(){
		for(int f =0; f<2; f++){
			System.out.print("\n");
			System.out.println("--------------------------------------------");
			System.out.print("				Ficha "+ f);
			for(int r =0; r<CUADROS; r++){
				System.out.print("\n");
				for(int c = 0; c<CUADROS; c++){
					System.out.print("("+r+","+c+"): "+ String.valueOf(valor[f][r][c][0]).substring(0,3)+" ");
				}
			}
		}
	}
	
	public void jugadorTira(int r, int c){
		puedeMover=true;
		tablero[r][c].ficha = HUM;
		tablero[r][c].setIcon(new ImageIcon(getClass().getResource("imagenes/" + String.valueOf(HUM)+".png")));
		if(checa(HUM)){
			puntuacion[HUM]++;
			resultado("Ganaste\n" + punt(), "imagenes/GatoConBotas.gif");
			return;
		}else if(!posible()){
			resultado("Empate\n" + punt(),"imagenes/Empate.gif");
		}else{
			if(puedeMover){
				compuMueve();
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		for(int r=0; r<CUADROS; r++){
			for(int c=0; c<CUADROS; c++){
				if(e.getSource().equals(tablero[r][c]) && tablero[r][c].ficha == -1){
					jugadorTira(r, c);
				}
			}
		}
	}
}
