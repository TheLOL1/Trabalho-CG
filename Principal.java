import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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

public class Principal extends Application {

	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	int xInicial=0;
	int yInicial=0;
	int xFinal=0;
	int yFinal=0;
	boolean primeiroclick = false;
	PixelWriter pixelWriter;
	char comando = ' ';

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Canvas canvas = new Canvas(1920, 1080);
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
					}
					else if(comando == 'B')
					{
						BresenhamLinha(xInicial,yInicial,xFinal,yFinal);
					}
					else if(comando == 'C')
					{
						BresenHamCirculo(xInicial,yInicial,xFinal,yFinal);
					}
					else if (comando == 'D')
					{

					}
					else if (comando == 'E')
					{

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
					if(xy.getKey().length() > 0 && xy.getValue().length() > 0)
					System.out.println(xy.getKey() + " " + xy.getValue());
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
						return angulo.getText();
					}
					return null;
				});
				Optional<String> s = dialog.showAndWait();
				s.ifPresent(angulo1 ->
				{
					if(angulo1.length() > 0)
					System.out.println(angulo1);
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
					if(ab.getKey().length() > 0 && ab.getValue().length() > 0)
					System.out.println(ab.getKey() + " " + ab.getValue());
				});
			}
		});
		menuitem5.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				
			}
		});
		menuitem7.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				
			}
		});
		menuitem9.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				
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
				comando = 'A';
			}
		});
		radiomenuitem2.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				comando = 'B';
			}
		});
		radiomenuitem3.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
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
		MenuBar menubar = new MenuBar(menu,menu2,menu3,menu4,menu5);
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

}