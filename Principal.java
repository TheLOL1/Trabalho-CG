import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.util.*;
import javafx.geometry.Insets;
import java.util.*;
import javafx.beans.binding.*;
import java.text.DecimalFormat;

public class Principal extends Application {

	int xInicial=0;
	int yInicial=0;
	int xFinal=0;
	int yFinal=0;
	boolean primeiroclick = false;
	PixelWriter pixelWriter;
	char comando = ' ';
	ArrayList<Integer> pontosxinicialDDA;
	ArrayList<Integer> pontosyinicialDDA;
	ArrayList<Integer> pontosxfinalDDA;
	ArrayList<Integer> pontosyfinalDDA;
	ArrayList<Integer> pontosxinicialBresenham;
	ArrayList<Integer> pontosyinicialBresenham;
	ArrayList<Integer> pontosxfinalBresenham;
	ArrayList<Integer> pontosyfinalBresenham;
	ArrayList<Integer> pontosxinicialCirc;
	ArrayList<Integer> pontosyinicialCirc;
	ArrayList<Integer> pontosxfinalCirc;
	ArrayList<Integer> pontosyfinalCirc;
	Canvas canvas;
	boolean recorte = false;
	double u1 = 0;
	double u2 = 1;

	/**
	 * Inicializar programa.
	 */

	public static void main(String[] args) {
		launch();
	}

	/**
	 * Configura tela,canvas,listeners de click,menu e transformações.
	 */

	@Override
	public void start(Stage stage) throws Exception {
		pontosxinicialDDA = new ArrayList<>();
		pontosyinicialDDA = new ArrayList<>();
		pontosxfinalDDA = new ArrayList<>();
		pontosyfinalDDA = new ArrayList<>();
		pontosxinicialBresenham = new ArrayList<>();
		pontosyinicialBresenham = new ArrayList<>();
		pontosxfinalBresenham = new ArrayList<>();
		pontosyfinalBresenham = new ArrayList<>();
		pontosxinicialCirc = new ArrayList<>();
		pontosyinicialCirc = new ArrayList<>();
		pontosxfinalCirc = new ArrayList<>();
		pontosyfinalCirc = new ArrayList<>();
		canvas = new Canvas(1920, 1080);
		pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if (!primeiroclick)
				{
					xInicial = (int) event.getX();
					yInicial = (int) event.getY();
					primeiroclick = true;
				}
				else
				{
					xFinal = (int) event.getX();
					yFinal = (int) event.getY();
					primeiroclick = false;
					if (comando == 'A')
					{
						DDA(xInicial,yInicial,xFinal,yFinal);
						pontosxinicialDDA.add(xInicial);
						pontosyinicialDDA.add(yInicial);
						pontosxfinalDDA.add(xFinal);
						pontosyfinalDDA.add(yFinal);
					}
					else if(comando == 'B')
					{
						BresenhamLinha(xInicial,yInicial,xFinal,yFinal);
						pontosxinicialBresenham.add(xInicial);
						pontosyinicialBresenham.add(yInicial);
						pontosxfinalBresenham.add(xFinal);
						pontosyfinalBresenham.add(yFinal);
					}
					else if(comando == 'C')
					{
						BresenHamCirculo(xInicial,yInicial,xFinal,yFinal);
						pontosxinicialCirc.add(xInicial);
						pontosyinicialCirc.add(yInicial);
						pontosxfinalCirc.add(xFinal);
						pontosyfinalCirc.add(yFinal);
					}
					else if (comando == 'D')
					{
						cohenSutherland(xInicial,yInicial,xFinal,yFinal);
					}
					else if (comando == 'E')
					{
						LiangBarsky(xInicial,yInicial,xFinal,yFinal);
					}
				}
			}
		});
		Menu menu = new Menu("Transformacoes geometricas");
		MenuItem menuitem = new MenuItem("Translacao");
		MenuItem menuitem2 = new MenuItem("Rotacao");
		MenuItem menuitem3 = new MenuItem("Escala");
		Menu menuitem4 = new Menu("Reflexao");
		MenuItem menuitem5 = new MenuItem("Em X");
		MenuItem menuitem7 = new MenuItem("Em Y");
		MenuItem menuitem9 = new MenuItem("Em X/Y");
		menuitem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Dialog <Pair<String,String>> dialog = new Dialog<>();
				dialog.setTitle("Translacao");
				dialog.setHeaderText(null);
				ButtonType ok = new ButtonType("OK",ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(ok,ButtonType.CANCEL);
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20,150,10,10));
				TextField x = new TextField();
				TextField y = new TextField();
				grid.add(new Label("Eixo X:"),0,0);
				grid.add(x,1,0);
				grid.add(new Label("Eixo Y:"),0,1);
				grid.add(y,1,1);
				Node ok1 = dialog.getDialogPane().lookupButton(ok);
				x.textProperty().addListener((observable,oldValue,newValue) ->
				{
					ok1.setDisable(newValue.trim().isEmpty());
				});
				y.textProperty().addListener((observable,oldValue,newValue) ->
				{
					ok1.setDisable(newValue.trim().isEmpty());
				});
				dialog.getDialogPane().setContent(grid);
				dialog.setResultConverter(dialogButton ->
				{
					if (dialogButton == ok)
					{
						return new Pair<>(x.getText(),y.getText());
					}
					return null;
				});
				Optional<Pair<String,String>> s = dialog.showAndWait();
				s.ifPresent(xy ->
				{
					if(xy.getKey().length() > 0 && xy.getValue().length() > 0 )
					{
						int i,j;
						try
						{
							i = Integer.parseInt(xy.getKey());
							j = Integer.parseInt(xy.getValue());
							limparcanvas();
							if (!pontosxinicialDDA.isEmpty())
							{
								for (int k = 0; k < pontosxinicialDDA.size();k++)
								{
									int novopontoxinicial = pontosxinicialDDA.get(k) + i;
									int novopontoyinicial = pontosyinicialDDA.get(k) + j;
									int novopontoxfinal = pontosxfinalDDA.get(k) + i;
									int novopontoyfinal = pontosyfinalDDA.get(k) + j;
									DDA(novopontoxinicial,novopontoyinicial,novopontoxfinal,novopontoyfinal);
									pontosxinicialDDA.set(k,novopontoxinicial);
									pontosyinicialDDA.set(k,novopontoyinicial);
									pontosxfinalDDA.set(k,novopontoxfinal);
									pontosyfinalDDA.set(k,novopontoyfinal);
								}
							}
							if (!pontosxinicialBresenham.isEmpty())
							{
								for (int k = 0; k < pontosxinicialBresenham.size();k++)
								{
									int novopontoxinicial = pontosxinicialBresenham.get(k) + i;
									int novopontoyinicial = pontosyinicialBresenham.get(k) + j;
									int novopontoxfinal = pontosxfinalBresenham.get(k) + i;
									int novopontoyfinal = pontosyfinalBresenham.get(k) + j;
									BresenhamLinha(novopontoxinicial,novopontoyinicial,novopontoxfinal,novopontoyfinal);
									pontosxinicialBresenham.set(k,novopontoxinicial);
									pontosyinicialBresenham.set(k,novopontoyinicial);
									pontosxfinalBresenham.set(k,novopontoxfinal);
									pontosyfinalBresenham.set(k,novopontoyfinal);
								}
							}
							if (recorte)
							{
								cohenSutherland(xInicial,yInicial,xFinal,yFinal);
							}
							if (!pontosxinicialCirc.isEmpty() && !recorte)
							{
								for (int k = 0; k < pontosxinicialCirc.size();k++)
								{
									int novopontoxinicial = pontosxinicialCirc.get(k) + i;
									int novopontoyinicial = pontosyinicialCirc.get(k) + j;
									int novopontoxfinal = pontosxfinalCirc.get(k) + i;
									int novopontoyfinal = pontosyfinalCirc.get(k) + j;
									BresenHamCirculo(novopontoxinicial,novopontoyinicial,novopontoxfinal,novopontoyfinal);
									pontosxinicialCirc.set(k,novopontoxinicial);
									pontosyinicialCirc.set(k,novopontoyinicial);
									pontosxfinalCirc.set(k,novopontoxfinal);
									pontosyfinalCirc.set(k,novopontoyfinal);
								}
							}
						}
						catch (Exception e)
						{
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERRO");
							alert.setHeaderText("Valor Invalido!");
							alert.setContentText("Insira valores inteiros!");
							alert.showAndWait();
						}
					}
				});
			}
		});
		menuitem2.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Dialog <String> dialog = new Dialog<>();
				dialog.setTitle("Rotacao");
				dialog.setHeaderText(null);
				ButtonType ok = new ButtonType("OK",ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(ok,ButtonType.CANCEL);
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20,150,10,10));
				TextField angulo = new TextField();
				grid.add(new Label("Angulo:"),0,0);
				grid.add(angulo,1,0);
				Node ok1 = dialog.getDialogPane().lookupButton(ok);
				angulo.textProperty().addListener((observable,oldValue,newValue) ->
				{
					ok1.setDisable(newValue.trim().isEmpty());
				});
				dialog.getDialogPane().setContent(grid);
				dialog.setResultConverter(dialogButton ->
				{
					if (dialogButton == ok )
					{
						return (angulo.getText());
					}
					return null;
				});
				Optional<String> s = dialog.showAndWait();
				s.ifPresent(angulo1 ->
				{
					if(angulo1.length() > 0 )
					{
						int i;
						try
						{

							i = Integer.parseInt(angulo1);
							double seno = Math.sin(i);
							double cosseno = Math.cos(i);
							limparcanvas();
							if (!pontosxinicialDDA.isEmpty())
							{
								for (int k = 0; k < pontosxinicialDDA.size();k++)
								{
									int novopontoxfinal = (int)(((double)pontosxinicialDDA.get(k)*cosseno)-((double)pontosyinicialDDA.get(k)*seno));
									int novopontoyfinal = (int)(((double)pontosxinicialDDA.get(k)*seno)+((double)pontosyinicialDDA.get(k)*cosseno));
									DDA(pontosxinicialDDA.get(k),pontosyinicialDDA.get(k),novopontoxfinal,novopontoyfinal);
									pontosxfinalDDA.set(k,novopontoxfinal);
									pontosyfinalDDA.set(k,novopontoyfinal);
								}
							}
							if (!pontosxinicialBresenham.isEmpty())
							{
								for (int k = 0; k < pontosxinicialBresenham.size();k++)
								{
									int novopontoxfinal = (int)(((double)pontosxinicialBresenham.get(k)*cosseno)-((double)pontosyinicialBresenham.get(k)*seno));
									int novopontoyfinal = (int)(((double)pontosxinicialBresenham.get(k)*seno)+((double)pontosyinicialBresenham.get(k)*cosseno));
									BresenhamLinha(pontosxinicialBresenham.get(k),pontosyinicialBresenham.get(k),novopontoxfinal,novopontoyfinal);
									pontosxfinalBresenham.set(k,novopontoxfinal);
									pontosyfinalBresenham.set(k,novopontoyfinal);
								}
							}
							if (recorte)
							{
								cohenSutherland(xInicial,yInicial,xFinal,yFinal);
							}
							if (!pontosxinicialCirc.isEmpty() && !recorte)
							{
								for (int k = 0; k < pontosxinicialCirc.size();k++)
								{
									BresenHamCirculo(pontosxinicialCirc.get(k),pontosyinicialCirc.get(k),pontosxfinalCirc.get(k),pontosyfinalCirc.get(k));
								}
							}
						}
						catch (Exception e)
						{
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERRO");
							alert.setHeaderText("Valor Invalido!");
							alert.setContentText("Insira valores inteiros!");
							alert.showAndWait();
						}
					}
				});
			}
		});
		menuitem3.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Dialog <Pair<String,String>> dialog = new Dialog<>();
				dialog.setTitle("Escala");
				dialog.setHeaderText(null);
				ButtonType ok = new ButtonType("OK",ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(ok,ButtonType.CANCEL);
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20,150,10,10));
				TextField a = new TextField();
				TextField b = new TextField();
				grid.add(new Label("Valor A:"),0,0);
				grid.add(a,1,0);
				grid.add(new Label("Valor B:"),0,1);
				grid.add(b,1,1);
				Node ok1 = dialog.getDialogPane().lookupButton(ok);
				a.textProperty().addListener((observable,oldValue,newValue) ->
				{
					ok1.setDisable(newValue.trim().isEmpty());
				});
				b.textProperty().addListener((observable,oldValue,newValue) ->
				{
					ok1.setDisable(newValue.trim().isEmpty());
				});
				dialog.getDialogPane().setContent(grid);
				dialog.setResultConverter(dialogButton ->
				{
					if (dialogButton == ok)
					{
						return new Pair<>(a.getText(),b.getText());
					}
					return null;
				});
				Optional<Pair<String,String>> s = dialog.showAndWait();
				s.ifPresent(ab ->
				{
					if(ab.getKey().length() > 0 && ab.getValue().length() > 0 )
					{
						int i,j;
						try
						{
							i = Integer.parseInt(ab.getKey());
							j = Integer.parseInt(ab.getValue());
							limparcanvas();
							if (!pontosxinicialDDA.isEmpty())
							{
								for (int k = 0; k < pontosxinicialDDA.size();k++)
								{
									int x1 = pontosxinicialDDA.get(k);
									int y1 = pontosyinicialDDA.get(k);
									int x2 = pontosxfinalDDA.get(k);
									int y2 = pontosyfinalDDA.get(k);
									int novopontoxinicial = ((x1-x2) * i)+x2;
									int novopontoyinicial = ((y1-y2) * i)+y2;
									int novopontoxfinal = ((x2-x1) * i)+x1;
									int novopontoyfinal = ((y2-y1) * i)+y1;
									DDA(novopontoxinicial,novopontoyinicial,novopontoxfinal,novopontoyfinal);
									pontosxinicialDDA.set(k,novopontoxinicial);
									pontosyinicialDDA.set(k,novopontoyinicial);
									pontosxfinalDDA.set(k,novopontoxfinal);
									pontosyfinalDDA.set(k,novopontoyfinal);
								}
							}
							if (!pontosxinicialBresenham.isEmpty())
							{
								for (int k = 0; k < pontosxinicialBresenham.size();k++)
								{
									int novopontoxinicial = pontosxinicialBresenham.get(k);
									int novopontoyinicial = pontosyinicialBresenham.get(k);
									int novopontoxfinal = pontosxfinalBresenham.get(k) * i;
									int novopontoyfinal = pontosyfinalBresenham.get(k) * j;
									BresenhamLinha(novopontoxinicial,novopontoyinicial,novopontoxfinal,novopontoyfinal);
									pontosxinicialBresenham.set(k,novopontoxinicial);
									pontosyinicialBresenham.set(k,novopontoyinicial);
									pontosxfinalBresenham.set(k,novopontoxfinal);
									pontosyfinalBresenham.set(k,novopontoyfinal);
								}
							}
							if (recorte)
							{
								cohenSutherland(xInicial,yInicial,xFinal,yFinal);
							}
							if (!pontosxinicialCirc.isEmpty() && !recorte)
							{
								for (int k = 0; k < pontosxinicialCirc.size();k++)
								{
									int novopontoxinicial = pontosxinicialCirc.get(k);
									int novopontoyinicial = pontosyinicialCirc.get(k);
									int novopontoxfinal = pontosxfinalCirc.get(k) * i;
									int novopontoyfinal = pontosyfinalCirc.get(k) * j;
									BresenHamCirculo(novopontoxinicial,novopontoyinicial,novopontoxfinal,novopontoyfinal);
									pontosxinicialCirc.set(k,novopontoxinicial);
									pontosyinicialCirc.set(k,novopontoyinicial);
									pontosxfinalCirc.set(k,novopontoxfinal);
									pontosyfinalCirc.set(k,novopontoyfinal);
								}
							}
						}
						catch (Exception e)
						{
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("ERRO");
							alert.setHeaderText("Valor Invalido!");
							alert.setContentText("Insira valores inteiros!");
							alert.showAndWait();
						}
					}
				});
			}
		});
		menuitem5.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				reflexao(1,-1);
				if (recorte)
				{
					cohenSutherland(xInicial,yInicial,xFinal,yFinal);
				}	
			}
		});
		menuitem7.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				reflexao(-1,1);
				if (recorte)
				{
					cohenSutherland(xInicial,yInicial,xFinal,yFinal);
				}		
			}
		});
		menuitem9.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				reflexao(-1,-1);
				if (recorte)
				{
					cohenSutherland(xInicial,yInicial,xFinal,yFinal);
				}		
			}
		});
		menuitem4.getItems().add(menuitem5);
		menuitem4.getItems().add(menuitem7);
		menuitem4.getItems().add(menuitem9);
		SeparatorMenuItem separator = new SeparatorMenuItem();
		SeparatorMenuItem separator2 = new SeparatorMenuItem();
		SeparatorMenuItem separator3 = new SeparatorMenuItem();
		menu.getItems().add(menuitem);
		menu.getItems().add(separator);
		menu.getItems().add(menuitem2);
		menu.getItems().add(separator2);
		menu.getItems().add(menuitem3);
		menu.getItems().add(separator3);
		menu.getItems().add(menuitem4);
		Menu menu2 = new Menu("Rasterizacao");
		MenuItem radiomenuitem = new MenuItem("DDA - Reta");
		MenuItem radiomenuitem2 = new MenuItem("Bresenham - Reta");
		MenuItem radiomenuitem3 = new MenuItem("Bresenham - Circunferencia");
		SeparatorMenuItem separator4 = new SeparatorMenuItem();
		SeparatorMenuItem separator5 = new SeparatorMenuItem();
		menu2.getItems().add(radiomenuitem);
		menu2.getItems().add(separator4);
		menu2.getItems().add(radiomenuitem2);
		menu2.getItems().add(separator5);
		menu2.getItems().add(radiomenuitem3);
		Menu menu3 = new Menu("Recorte");
		MenuItem radiomenuitem4 = new MenuItem("Cohen-Sutherland");
		MenuItem radiomenuitem5 = new MenuItem("Liang-Barsky");
		SeparatorMenuItem separator6 = new SeparatorMenuItem();
		menu3.getItems().add(radiomenuitem4);
		menu3.getItems().add(separator6);
		menu3.getItems().add(radiomenuitem5);
		radiomenuitem.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (recorte)
				{
					limparcanvas();
					for (int k = 0; k < pontosxinicialDDA.size();k++)
					{
						DDA(pontosxinicialDDA.get(k),pontosyinicialDDA.get(k),pontosxfinalDDA.get(k),pontosyfinalDDA.get(k));
					}
					for (int k = 0; k < pontosxinicialBresenham.size();k++)
					{
						BresenhamLinha(pontosxinicialBresenham.get(k),pontosyinicialBresenham.get(k),pontosxfinalBresenham.get(k),pontosyfinalBresenham.get(k));
					}
					for (int k = 0; k < pontosxinicialCirc.size();k++)
					{
						BresenHamCirculo(pontosxinicialCirc.get(k),pontosyinicialCirc.get(k),pontosxfinalCirc.get(k),pontosyfinalCirc.get(k));
					}
					recorte = false;
				}	
				comando = 'A';
			}
		});
		radiomenuitem2.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (recorte)
				{
					limparcanvas();
					for (int k = 0; k < pontosxinicialDDA.size();k++)
					{
						DDA(pontosxinicialDDA.get(k),pontosyinicialDDA.get(k),pontosxfinalDDA.get(k),pontosyfinalDDA.get(k));
					}
					for (int k = 0; k < pontosxinicialBresenham.size();k++)
					{
						BresenhamLinha(pontosxinicialBresenham.get(k),pontosyinicialBresenham.get(k),pontosxfinalBresenham.get(k),pontosyfinalBresenham.get(k));
					}
					for (int k = 0; k < pontosxinicialCirc.size();k++)
					{
						BresenHamCirculo(pontosxinicialCirc.get(k),pontosyinicialCirc.get(k),pontosxfinalCirc.get(k),pontosyfinalCirc.get(k));
					}
					recorte = false;
				}
				comando = 'B';
			}
		});
		radiomenuitem3.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (recorte)
				{
					limparcanvas();
					for (int k = 0; k < pontosxinicialDDA.size();k++)
					{
						DDA(pontosxinicialDDA.get(k),pontosyinicialDDA.get(k),pontosxfinalDDA.get(k),pontosyfinalDDA.get(k));
					}
					for (int k = 0; k < pontosxinicialBresenham.size();k++)
					{
						BresenhamLinha(pontosxinicialBresenham.get(k),pontosyinicialBresenham.get(k),pontosxfinalBresenham.get(k),pontosyfinalBresenham.get(k));
					}
					for (int k = 0; k < pontosxinicialCirc.size();k++)
					{
						BresenHamCirculo(pontosxinicialCirc.get(k),pontosyinicialCirc.get(k),pontosxfinalCirc.get(k),pontosyfinalCirc.get(k));
					}
					recorte = false;
				}
				comando = 'C';
			}
		});
		radiomenuitem4.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				comando = 'D';
			}
		});
		radiomenuitem5.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				comando = 'E';
			}
		});
		Label sobre = new Label("Sobre");
		sobre.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Sobre");
				alert.setHeaderText("Trabalho Computacao Grafica");
				alert.setContentText("Feito por: Gabriel de Lima Correa Ferreira");
				alert.showAndWait();
			}
		});
		Menu menu4 = new Menu();
		menu4.setGraphic(sobre);
		Label ajuda = new Label ("Ajuda");
		ajuda.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				Alert alert = new Alert (AlertType.INFORMATION);
				alert.setTitle("Ajuda");
				alert.setHeaderText(null);
				alert.setContentText("Escolha primeiro o comando que deseja executar apos isso para rasterizacao e recorte o primeiro click do mouse no canvas define coordenada inicial e o segundo click coordenada final");
				alert.showAndWait();
			}
		});
		Menu menu5 = new Menu();
		menu5.setGraphic(ajuda);
		Label limpar = new Label("Limpar Canvas");
		limpar.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				limparcanvas();
				pontosxinicialDDA.clear();
				pontosyinicialDDA.clear();
				pontosxfinalDDA.clear();
				pontosyfinalDDA.clear();
				pontosxinicialBresenham.clear();
				pontosyinicialBresenham.clear();
				pontosxfinalBresenham.clear();
				pontosyfinalBresenham.clear();
				pontosxinicialCirc.clear();
				pontosyinicialCirc.clear();
				pontosxfinalCirc.clear();
				pontosyfinalCirc.clear();
			}
		});
		Menu menu6 = new Menu();
		menu6.setGraphic(limpar);
		MenuBar menubar = new MenuBar(menu,menu2,menu3,menu6,menu4,menu5);
		BorderPane pane = new BorderPane();
		pane.setCenter(canvas);
		pane.setTop(menubar);
		Scene scene = new Scene(pane,1920,1080,Color.WHITE);
		scene.getStylesheets().add("Style.css"); 
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.setTitle("Trabalho CG");
		stage.show();
	}

	/**
	 * Rasterização de linha usando DDA.
	 * @param xa - x1
	 * @param ya - y1
	 * @param xb - x2
	 * @param yb - y2
	 */

	public void DDA(int xa, int ya, int xb, int yb)
	{
		int dx = xb - xa;
		int dy = yb - ya;
		int passos;
		int k;
		float xincremento;
		float yincremento;
		float x = xa;
		float y = ya;
		if (Math.abs(dx) > Math.abs(dy))
		{
			passos = Math.abs(dx);
		}
		else
		{
			passos = Math.abs(dy);
		}
		xincremento = dx / (float) passos;
		yincremento = dy / (float) passos;
		pixelWriter.setColor(Math.round(x),Math.round(y),Color.BLUE);
		for (k = 0; k < passos; k++)
		{
			x += xincremento;
			y += yincremento;
			pixelWriter.setColor(Math.round(x),Math.round(y),Color.BLUE);
		}
	}

	/**
	 * Rasterização de linha usando Bresenham.
	 * @param x0 - x1
	 * @param y0 - y1
	 * @param x1 - x2
	 * @param y1 - y2
	 */

	public void BresenhamLinha (int x0,int y0,int x1,int y1)
	{
		int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
 
        int sx = x0 < x1 ? 1 : -1; 
        int sy = y0 < y1 ? 1 : -1; 
 
        int err = dx-dy;
        int e2;
 
        while (true) 
        {
            pixelWriter.setColor(x0,y0,Color.RED);
 
            if (x0 == x1 && y0 == y1) 
                break;
 
            e2 = 2 * err;
            if (e2 > -dy) 
            {
                err = err - dy;
                x0 = x0 + sx;
            }
 
            if (e2 < dx) 
            {
                err = err + dx;
                y0 = y0 + sy;
            }
        }                               
	}

	/**
	 * Rasterização de circulo usando Bresenham.
	 */


	public void BresenHamCirculo (int xcentro,int ycentro,int x2,int y2)
	{
		int raio = (int) Math.sqrt((Math.pow(x2-xcentro,2))+(Math.pow(y2-ycentro,2)));
		int x = 0;
		int y = raio;
		int p = 1 - raio;
		BresenHamPlotarPontos(xcentro,ycentro,x,y);
		while (x < y)
		{
			x++;
			if (p < 0)
			{
				p += 2 * x + 1;
			}
			else
			{
				y--;
				p += 2 * (x-y) + 1;
			}
			BresenHamPlotarPontos(xcentro,ycentro,x,y);
		}
	}

	/**
	 * Seta o pixel com a cor verde a partir da posicao definida no metodo anterior.
	 */

	public void BresenHamPlotarPontos(int xcentro,int ycentro,int x,int y)
	{
		pixelWriter.setColor(xcentro+x,ycentro+y,Color.GREEN);
		pixelWriter.setColor(xcentro-x,ycentro+y,Color.GREEN);
		pixelWriter.setColor(xcentro+x,ycentro-y,Color.GREEN);
		pixelWriter.setColor(xcentro-x,ycentro-y,Color.GREEN);
		pixelWriter.setColor(xcentro+y,ycentro+x,Color.GREEN);
		pixelWriter.setColor(xcentro-y,ycentro+x,Color.GREEN);
		pixelWriter.setColor(xcentro+y,ycentro-x,Color.GREEN);
		pixelWriter.setColor(xcentro-y,ycentro-x,Color.GREEN);
	}

	/**
	 * Limpa completamente a região do Canvas.
	 */

	public void limparcanvas()
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0,0,1920,1080);
	}

	/**
	 * Transformação - Reflexão.
	 * @param xr - reflexão aplicada no eixo x
	 * @param yr - reflexão aplicada no eixo y
	 */

	public void reflexao(int xr,int yr)
	{
		limparcanvas();
		if (!pontosxinicialDDA.isEmpty())
		{
			for (int k = 0; k < pontosxinicialDDA.size();k++)
			{
				int novopontoxfinal = (((pontosxfinalDDA.get(k)-pontosxinicialDDA.get(k))*xr)+pontosxinicialDDA.get(k));
				int novopontoyfinal = (((pontosyfinalDDA.get(k)-pontosyinicialDDA.get(k))*yr)+pontosyinicialDDA.get(k));
				DDA(pontosxinicialDDA.get(k),pontosyinicialDDA.get(k),novopontoxfinal,novopontoyfinal);
				pontosxfinalDDA.set(k,novopontoxfinal);
				pontosyfinalDDA.set(k,novopontoyfinal);
			}
		}
		if (!pontosxinicialBresenham.isEmpty())
		{
			for (int k = 0; k < pontosxinicialBresenham.size();k++)
			{
				int novopontoxfinal = (((pontosxfinalBresenham.get(k)-pontosxinicialBresenham.get(k))*xr)+pontosxinicialBresenham.get(k));
				int novopontoyfinal = (((pontosyfinalBresenham.get(k)-pontosyinicialBresenham.get(k))*yr)+pontosyinicialBresenham.get(k));
				BresenhamLinha(pontosxinicialBresenham.get(k),pontosyinicialBresenham.get(k),novopontoxfinal,novopontoyfinal);
				pontosxfinalBresenham.set(k,novopontoxfinal);
				pontosyfinalBresenham.set(k,novopontoyfinal);
			}
		}
		if (!pontosxinicialCirc.isEmpty() && !recorte)
		{
			for (int k = 0; k < pontosxinicialCirc.size();k++)
			{
				int novopontoxfinal = (((pontosxfinalCirc.get(k)-pontosxinicialCirc.get(k))*xr)+pontosxinicialCirc.get(k));
				int novopontoyfinal = (((pontosyfinalCirc.get(k)-pontosyinicialCirc.get(k))*yr)+pontosyinicialCirc.get(k));
				BresenHamCirculo(pontosxinicialCirc.get(k),pontosyinicialCirc.get(k),novopontoxfinal,novopontoyfinal);
				pontosxfinalCirc.set(k,novopontoxfinal);
				pontosyfinalCirc.set(k,novopontoyfinal);
			}
		}
	}

	/**
	 * Calcula o codigo que é utilizado no algoritmo de recorte Cohen Sutherland.
	 * @param x1p x1 da janela
	 * @param y1p y1 da janela
	 * @param x2p x2 da janela
	 * @param y2p y2 da janela
	 * @param x valor do x da reta
	 * @param y valor do y da reta
	 */

	public int codigoCohenSutherland(int x1p,int y1p,int x2p,int y2p,int x,int y)
	{
		int codigo = 0;
		int [] limites = limitesJanela(x1p,y1p,x2p,y2p);
		int xmin,ymin,xmax,ymax;
		xmin = limites[0];
		ymin = limites[1];
		xmax = limites[2];
		ymax = limites[3];
		if (x < xmin)
		{
			codigo+=1;
		}
		else if(x > xmax)
		{
			codigo+=2;
		}
		if (y<ymin)
		{
			codigo+=4;
		}
		else if(y>ymax)
		{
			codigo+=8;
		}
		return (codigo);
	}

	/**
	 * Define os limites da janela.
	 * @param x1 da janela
	 * @param y1 da janela
	 * @param x2 da janela
	 * @param y2 da janela
	 */

	public int [ ] limitesJanela(int x1,int y1,int x2,int y2)
	{
		int [] array = new int[4];
		if (x1>x2)
		{
			array[2] = x1;
			array[0] = x2;
		}
		else
		{
			array[2] = x2;
			array[0] = x1;
		}
		if (y1>y2)
		{
			array[3] = y1;
			array[1] = y2;
		}
		else
		{
			array[3] = y2;
			array[1] = y1;
		}
		return (array);
	}

	/**
	 * Algoritmo de Recorte de Linha - Cohen Sutherland
	 * @param x1 da janela
	 * @param y1 da janela
	 * @param x2 da janela
	 * @param y2 da janela
	 */

	public void cohenSutherland(int x1,int y1,int x2,int y2)
	{
		limparcanvas();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.strokeLine(x1,y1,x2,y1);
		gc.strokeLine(x1,y1,x1,y2);
		gc.strokeLine(x1,y2,x2,y2);
		gc.strokeLine(x2,y2,x2,y1);
		if (!pontosxinicialDDA.isEmpty())
		{
			int [ ] limites = limitesJanela(x1,y1,x2,y2);
			int xmin,ymin,xmax,ymax;
			xmin = limites[0];
			ymin = limites[1];
			xmax = limites[2];
			ymax = limites[3];
			for (int k = 0; k < pontosxinicialDDA.size();k++)
			{
				boolean aceito = false;
				boolean feito = false;
				int cfora=0,xint=0,yint=0,nx1=0,ny1=0,nx2=0,ny2=0;
				nx1 = pontosxinicialDDA.get(k);
				ny1 = pontosyinicialDDA.get(k);
				nx2 = pontosxfinalDDA.get(k);
				ny2 = pontosyfinalDDA.get(k);
				while (!feito)
				{
					int codigo = codigoCohenSutherland(x1,y1,x2,y2,nx1,ny1);
					int codigo2 = codigoCohenSutherland(x1,y1,x2,y2,nx2,ny2);
					if (codigo == 0 && codigo2 == 0)
					{
						aceito = true;
						feito = true;
					}
					else if ((codigo & codigo2) != 0)
					{
						feito = true;
						//DDA(nx1,ny1,nx2,ny2);
					}
					else
					{
						if (codigo != 0)
						{
							cfora = codigo;
						}
						else
						{
							cfora = codigo2;
						}
						if ((cfora & 1) == 1)
						{
							xint = xmin;
							yint = ny1 + (ny2-ny1) * (xmin-nx1) / (nx2-nx1);
						}
						else if((cfora & 2) == 2)
						{
							xint = xmax;
							yint = ny1 + (ny2-ny1) * (xmax-nx1) / (nx2-nx1);
						}
						else if((cfora & 4) == 4 )
						{
							yint = ymin;
							xint = nx1 + (nx2-nx1) * (ymin-ny1) / (ny2-ny1);
						}
						else if((cfora & 8)==8)
						{
							yint = ymax;
							xint = nx1 + (nx2-nx1) * (ymax-ny1) / (ny2-ny1);
						}
						if (cfora == codigo)
						{
							nx1 = xint;
							ny1 = yint;
						}
						else
						{
							nx2 = xint;
							ny2 = yint;
						}
					}
					if (aceito)
					{
						DDA(nx1,ny1,nx2,ny2);
					}
				}
			}
		}
		if (!pontosxinicialBresenham.isEmpty())
		{
			int [ ] limites = limitesJanela(x1,y1,x2,y2);
			int xmin,ymin,xmax,ymax;
			xmin = limites[0];
			ymin = limites[1];
			xmax = limites[2];
			ymax = limites[3];
			for (int k = 0; k < pontosxinicialBresenham.size();k++)
			{
				boolean aceito = false;
				boolean feito = false;
				int cfora=0,xint=0,yint=0,nx1=0,ny1=0,nx2=0,ny2=0;
				nx1 = pontosxinicialBresenham.get(k);
				ny1 = pontosyinicialBresenham.get(k);
				nx2 = pontosxfinalBresenham.get(k);
				ny2 = pontosyfinalBresenham.get(k);
				while (!feito)
				{
					int codigo = codigoCohenSutherland(x1,y1,x2,y2,nx1,ny1);
					int codigo2 = codigoCohenSutherland(x1,y1,x2,y2,nx2,ny2);
					if (codigo == 0 && codigo2 == 0)
					{
						aceito = true;
						feito = true;
					}
					else if ((codigo & codigo2) != 0)
					{
						feito = true;
						//BresenhamLinha(nx1,ny1,nx2,ny2);
					}
					else
					{
						if (codigo != 0)
						{
							cfora = codigo;
						}
						else
						{
							cfora = codigo2;
						}
						if ((cfora & 1) == 1)
						{
							xint = xmin;
							yint = ny1 + (ny2-ny1) * (xmin-nx1) / (nx2-nx1);
						}
						else if((cfora & 2) == 2)
						{
							xint = xmax;
							yint = ny1 + (ny2-ny1) * (xmax-nx1) / (nx2-nx1);
						}
						else if((cfora & 4) == 4 )
						{
							yint = ymin;
							xint = nx1 + (nx2-nx1) * (ymin-ny1) / (ny2-ny1);
						}
						else if((cfora & 8)==8)
						{
							yint = ymax;
							xint = nx1 + (nx2-nx1) * (ymax-ny1) / (ny2-ny1);
						}
						if (cfora == codigo)
						{
							nx1 = xint;
							ny1 = yint;
						}
						else
						{
							nx2 = xint;
							ny2 = yint;
						}
					}
					if (aceito)
					{
						BresenhamLinha(nx1,ny1,nx2,ny2);
					}
				}
			}
		}
		recorte = true;
	}

	/**
	 * Clip test utlizado no algoritmo de Liang Barsky
	 */

	public boolean ClipTest(double p,double q,double u1aux,double u2aux)
	{
		boolean resultado = true;
		double r;
		if (p < 0)
		{
			r = q/p;
			if (r > u2aux)
			{
				resultado = false; //fora da janela
			}
			else if (r > u1aux)
			{
				u1 = r;
			}
		}
		else if (p > 0)
		{
			r = q/p;
			if (r < u1aux)
			{
				resultado = false; //fora da janela
			}
			else if (r < u2aux)
			{
				u2 = r;
			}
		}
		else if (q < 0)
		{
			resultado = false; //fora da janela
		}
		return (resultado);
	}

	/**
	 * Algoritmo de Recorte de Linha - Liang Barsky
	 * @param x1 da janela
	 * @param y1 da janela
	 * @param x2 da janela
	 * @param y2 da janela
	 */

	public void LiangBarsky (int x1,int y1,int x2,int y2)
	{
		limparcanvas();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.strokeLine(x1,y1,x2,y1);
		gc.strokeLine(x1,y1,x1,y2);
		gc.strokeLine(x1,y2,x2,y2);
		gc.strokeLine(x2,y2,x2,y1);
		if (!pontosxinicialDDA.isEmpty())
		{
			u1 = 0;
			u2 = 1;
			double dx = 0;
			double dy = 0;
			int xmin,ymin,xmax,ymax;
			int [ ] limites = limitesJanela(x1,y1,x2,y2);
			xmin = limites [0];
			ymin = limites [1];
			xmax = limites [2];
			ymax = limites [3];
			for (int k = 0; k < pontosxinicialDDA.size();k++)
			{
				u1 = 0;
				u2 = 1;
				int x1n = pontosxinicialDDA.get(k);
				int y1n = pontosyinicialDDA.get(k);
				int x2n = pontosxfinalDDA.get(k);
				int y2n = pontosyfinalDDA.get(k);
				dx = x2n - x1n;
				dy = y2n - y1n;
				if (ClipTest(-dx,x1n-xmin,u1,u2)) //esq
				if (ClipTest(dx,xmax-x1n,u1,u2)) //dir
				if (ClipTest(-dy,y1n-ymin,u1,u2)) //inf
				if (ClipTest(dy,ymax-y1n,u1,u2)) //sup
				{
					if (u2 < 1.0)
					{
						x2n = x1n + (int)(dx * u2);
						y2n = y1n + (int)(dy * u2);
					}
					if (u1 > 0.0)
					{
						x1n = x1n + (int) (dx * u1);
						y1n = y1n + (int) (dy * u1);
					}
					DDA(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				/*
				else
				{
					DDA(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				else
				{
					DDA(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				else
				{
					DDA(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				else
				{
					DDA(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				*/
			}
		}
		if (!pontosxinicialBresenham.isEmpty())
		{
			u1 = 0;
			u2 = 1;
			double dx = 0;
			double dy = 0;
			int xmin,ymin,xmax,ymax;
			int [ ] limites = limitesJanela(x1,y1,x2,y2);
			xmin = limites [0];
			ymin = limites [1];
			xmax = limites [2];
			ymax = limites [3];
			for (int k = 0; k < pontosxinicialBresenham.size();k++)
			{
				u1 = 0;
				u2 = 1;
				int x1n = pontosxinicialBresenham.get(k);
				int y1n = pontosyinicialBresenham.get(k);
				int x2n = pontosxfinalBresenham.get(k);
				int y2n = pontosyfinalBresenham.get(k);
				dx = x2n - x1n;
				dy = y2n - y1n;
				if (ClipTest(-dx,x1n-xmin,u1,u2)) //esq
				if (ClipTest(dx,xmax-x1n,u1,u2)) //dir
				if (ClipTest(-dy,y1n-ymin,u1,u2)) //inf
				if (ClipTest(dy,ymax-y1n,u1,u2)) //sup
				{
					if (u2 < 1.0)
					{
						x2n = x1n + (int)(dx * u2);
						y2n = y1n + (int)(dy * u2);
					}
					if (u1 > 0.0)
					{
						x1n = x1n + (int) (dx * u1);
						y1n = y1n + (int) (dy * u1);
					}
					BresenhamLinha(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				/*
				else
				{
					BresenhamLinha(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				else
				{
					BresenhamLinha(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				else
				{
					BresenhamLinha(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				else
				{
					BresenhamLinha(Math.round(x1n),Math.round(y1n),Math.round(x2n),Math.round(y2n));
				}
				*/
			}
		}
		recorte = true;
	}
}