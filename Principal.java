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

public class Principal extends Application {

	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	int xInicial=0;
	int yInicial=0;
	int xFinal=0;
	int yFinal=0;
	boolean primeiroclick = false;
	PixelWriter pixelWriter;

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
					//DDA(xInicial,yInicial,xFinal,yFinal);
					BresenHamCirculo(xInicial,yInicial,xFinal);
					//BresenhamLinha(xInicial,yInicial,xFinal,yFinal);
					primeiroclick = false;
				}
			}
		});
		Menu menu = new Menu("Transformacoes geometricas");
		MenuItem menuitem = new MenuItem("Translacao");
		MenuItem menuitem2 = new MenuItem("Rotacao");
		MenuItem menuitem3 = new MenuItem("Escala");
		MenuItem menuitem4 = new MenuItem("Reflexao");
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
		Menu menu2 = new Menu("Selecionar algoritmo de rasterizacao");
		RadioMenuItem radiomenuitem = new RadioMenuItem("DDA - Reta");
		RadioMenuItem radiomenuitem2 = new RadioMenuItem("Bresenham - Reta");
		RadioMenuItem radiomenuitem3 = new RadioMenuItem("Bresenham - Circunferencia");
		ToggleGroup togglegroup = new ToggleGroup();
		SeparatorMenuItem separator4 = new SeparatorMenuItem();
		SeparatorMenuItem separator5 = new SeparatorMenuItem();
		togglegroup.getToggles().add(radiomenuitem);
		togglegroup.getToggles().add(radiomenuitem2);
		togglegroup.getToggles().add(radiomenuitem3);
		menu2.getItems().add(radiomenuitem);
		menu2.getItems().add(separator4);
		menu2.getItems().add(radiomenuitem2);
		menu2.getItems().add(separator5);
		menu2.getItems().add(radiomenuitem3);
		Menu menu3 = new Menu("Selecionar algoritmo de recorte");
		RadioMenuItem radiomenuitem4 = new RadioMenuItem("Cohen-Sutherland");
		RadioMenuItem radiomenuitem5 = new RadioMenuItem("Liang-Barsky");
		ToggleGroup togglegroup2 = new ToggleGroup();
		SeparatorMenuItem separator6 = new SeparatorMenuItem();
		togglegroup2.getToggles().add(radiomenuitem4);
		togglegroup2.getToggles().add(radiomenuitem5);
		menu3.getItems().add(radiomenuitem4);
		menu3.getItems().add(separator6);
		menu3.getItems().add(radiomenuitem5);
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
				alert.setContentText("O primeiro click do mouse no canvas define coordenada inicial e o segundo click coordenada final");
				alert.showAndWait();
			}
		});
		Menu menu5 = new Menu();
		menu5.setGraphic(ajuda);
		MenuBar menubar = new MenuBar(menu,menu2,menu3,menu4,menu5);



		BorderPane pane = new BorderPane();
		pane.setCenter(canvas);
		pane.setTop(menubar);
		Scene scene = new Scene(pane,WIDTH,HEIGHT,Color.WHITE);
		scene.getStylesheets().add("teste.css"); 
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
		System.out.println(Math.round(x)+","+Math.round(y));
		pixelWriter.setColor(Math.round(x),Math.round(y),Color.BLUE);
		for (k = 0; k < passos; k++)
		{
			x += xincremento;
			y += yincremento;
			pixelWriter.setColor(Math.round(x),Math.round(y),Color.BLUE);
			System.out.println(Math.round(x)+","+Math.round(y));
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


	public void BresenHamCirculo (int xcentro,int ycentro,int raio)
	{
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
		pixelWriter.setColor(xcentro+x,ycentro+y,Color.BLUE);
		pixelWriter.setColor(xcentro-x,ycentro+y,Color.BLUE);
		pixelWriter.setColor(xcentro+x,ycentro-y,Color.BLUE);
		pixelWriter.setColor(xcentro-x,ycentro-y,Color.BLUE);
		pixelWriter.setColor(xcentro+y,ycentro+x,Color.BLUE);
		pixelWriter.setColor(xcentro-y,ycentro+x,Color.BLUE);
		pixelWriter.setColor(xcentro+y,ycentro-x,Color.BLUE);
		pixelWriter.setColor(xcentro-y,ycentro-x,Color.BLUE);
	}

}