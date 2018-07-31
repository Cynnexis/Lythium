package fr.berger.lythium;

import fr.berger.enhancedlist.Couple;
import fr.berger.lythium.bundles.pythiumbundle.PythiumBundle;
import fr.berger.lythium.bundles.spotifyobjects.SpotifyBundle;
import fr.berger.lythium.pythium.Pythium;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

// See https://stackoverflow.com/questions/40571199/creating-tray-icon-using-javafx

public class Lythium extends Application {
	
	private Stage stage;
	
	private Button bt_refresh;
	
	private ToolBar tb_tools;
	private TextArea ta_lyrics;
	
	private VBox vb_main;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		
		Platform.setImplicitExit(false);
		javax.swing.SwingUtilities.invokeLater(this::configureTray);
		
		bt_refresh = new Button("Refresh");
		bt_refresh.setOnAction(event -> {
			try {
				Couple<PythiumBundle, SpotifyBundle> results = Pythium.call();
				if (results != null && results.getX() != null)
					ta_lyrics.setText(results.getX().getLyrics());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		tb_tools = new ToolBar(bt_refresh);
		
		ta_lyrics = new TextArea();
		ta_lyrics.setEditable(false);
		
		vb_main = new VBox(5, tb_tools, ta_lyrics);
		
		Scene scene = new Scene(vb_main);
		this.stage.setScene(scene);
		this.stage.show();
	}
	
	private void configureTray() {
		java.awt.Toolkit.getDefaultToolkit();
		
		if (!java.awt.SystemTray.isSupported()) {
			System.out.println("Your system does not support tray. The application cannot start.");
			Platform.exit();
		}
		
		java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
		java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(new java.awt.Image() {
			@Override
			public int getWidth(ImageObserver observer) {
				return 0;
			}
			
			@Override
			public int getHeight(ImageObserver observer) {
				return 0;
			}
			
			@Override
			public ImageProducer getSource() {
				return null;
			}
			
			@Override
			public java.awt.Graphics getGraphics() {
				return null;
			}
			
			@Override
			public Object getProperty(String name, java.awt.image.ImageObserver observer) {
				return null;
			}
		});
		
		trayIcon.addActionListener(e -> Platform.runLater(this::showFrame));
		
		java.awt.MenuItem mi_refresh = new java.awt.MenuItem("Refresh");
		mi_refresh.addActionListener(e -> {
			bt_refresh.getOnAction().handle(null);
		});
		
		java.awt.MenuItem mi_exit = new java.awt.MenuItem("Exit");
		mi_exit.addActionListener(e -> {
			Platform.exit();
			tray.remove(trayIcon);
		});
		
		java.awt.PopupMenu menu = new java.awt.PopupMenu();
		menu.add(mi_refresh);
		menu.add(mi_exit);
		trayIcon.setPopupMenu(menu);
		
		try {
			tray.add(trayIcon);
		} catch (java.awt.AWTException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	
	private void showFrame() {
		if (stage != null) {
			stage.show();
			stage.toFront();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
