package fr.berger.lythium;

import fr.berger.enhancedlist.Couple;
import fr.berger.lythium.bundles.pythiumbundle.PythiumBundle;
import fr.berger.lythium.bundles.spotifyobjects.SpotifyBundle;
import fr.berger.lythium.pythium.Pythium;
import fr.berger.lythium.ui.Showcase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;

// See https://stackoverflow.com/questions/40571199/creating-tray-icon-using-javafx

public class Lythium extends Application {
	
	private Thread th_pythium = null;
	
	
	private java.awt.SystemTray tray;
	private java.awt.TrayIcon trayIcon;
	
	
	private Stage stage;
	
	private Button bt_refresh;
	
	private ToolBar tb_tools;
	private Showcase showcase;
	private TextArea ta_lyrics;
	private ProgressBar pb_pythium;
	
	private VBox vb_main;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		
		Platform.setImplicitExit(false);
		javax.swing.SwingUtilities.invokeLater(this::configureTray);
		
		P.initialize();
		
		bt_refresh = new Button("Refresh", new ImageView(new Image(getClass().getResourceAsStream("/images/refresh16.png"))));
		bt_refresh.setOnAction(event -> {
			if (th_pythium == null) {
				th_pythium = new Thread(() -> {
					try {
						Platform.runLater(() -> pb_pythium.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS));
						
						Couple<PythiumBundle, SpotifyBundle> results = Pythium.call();
						
						Platform.runLater(() -> {
							if (results != null && results.getX() != null) {
								pb_pythium.setProgress(0);
								ta_lyrics.setText(results.getX().getLyrics());
								
								if (showcase != null)
									showcase.update(results);
							}
						});
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						th_pythium = null;
					}
				});
				th_pythium.start();
			}
		});
		
		tb_tools = new ToolBar(bt_refresh);
		
		showcase = new Showcase();
		
		ta_lyrics = new TextArea();
		ta_lyrics.setEditable(false);
		
		pb_pythium = new ProgressBar();
		pb_pythium.setProgress(0.0);
		
		vb_main = new VBox(0, tb_tools, showcase, ta_lyrics, pb_pythium);
		
		Scene scene = new Scene(vb_main);
		new JMetro(JMetro.Style.LIGHT).applyTheme(scene);
		this.stage.setScene(scene);
		this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/lythium128.png")));
		this.stage.show();
	}
	
	private void configureTray() {
		java.awt.Toolkit.getDefaultToolkit();
		
		if (!java.awt.SystemTray.isSupported()) {
			System.out.println("Your system does not support tray. The application cannot start.");
			Platform.exit();
		}
		
		tray = java.awt.SystemTray.getSystemTray();
		trayIcon = null;
		try {
			trayIcon = new java.awt.TrayIcon(ImageIO.read(getClass().getResourceAsStream("/images/lythium128.png")));
		} catch (IOException e) {
			trayIcon = new java.awt.TrayIcon(new java.awt.Image() {
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
				public Graphics getGraphics() {
					return null;
				}
				
				@Override
				public Object getProperty(String name, ImageObserver observer) {
					return null;
				}
			});
		}
		
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
		menu.addSeparator();
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

/**
 * TODO:
 * Save thumbnails in Lythium-side in .lythium/cache
 * In Pythium, save the lyrics AND the number of time that the music has been fetch from the dictionary, so when the file becomes too big, the script can delete the useless ones.
 * In Lythium, make an option to delete the cache files (all .cache-*)
 */
