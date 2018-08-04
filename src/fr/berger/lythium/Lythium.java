package fr.berger.lythium;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import fr.berger.enhancedlist.Couple;
import fr.berger.lythium.bundles.pythiumbundle.PythiumBundle;
import fr.berger.lythium.bundles.spotifyobjects.SpotifyBundle;
import fr.berger.lythium.pythium.Pythium;
import fr.berger.lythium.ui.Showcase;
import fr.berger.lythium.ui.Space;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;

// See https://stackoverflow.com/questions/40571199/creating-tray-icon-using-javafx

public class Lythium extends Application {
	
	public enum State {
		INITIALIZING("Initializing", Color.valueOf("#6AC560")),
		RUNNING("OK", Color.valueOf("#72C5BF")),
		CALLING_SCRIPT("Loading...", Color.valueOf("#C5BA63")),
		STOP("Stopping", Color.valueOf("#C55649"));
		
		private String message;
		private Color background;
		
		State(@NotNull String message, @NotNull Color background) {
			setMessage(message);
			setBackground(background);
		}
		
		/* GETTERS & SETTERS */
		
		@NotNull
		public String getMessage() {
			return message;
		}
		
		private void setMessage(@NotNull String message) {
			this.message = message;
		}
		
		@NotNull
		public Color getBackground() {
			return background;
		}
		
		private void setBackground(@NotNull Color background) {
			this.background = background;
		}
	}
	
	private State status;
	
	private Thread th_pythium = null;
	
	
	private java.awt.SystemTray tray;
	private java.awt.TrayIcon trayIcon;
	
	
	private Stage stage;
	
	private JFXDecorator decorator;
	
	private JFXButton bt_refresh;
	
	private Label lb_menu_refresh;
	private Label lb_menu_exit;
	private JFXListView<Label> lv_menu;
	private JFXPopup pp_menu;
	private JFXHamburger hb_menu;
	private JFXRippler rp_menu;
	
	private JFXToolbar tb_toolbar;
	
	private Showcase showcase;
	private Label la_lyrics;
	private Pane pa_lyrics;
	private Pane pa_lyrics_parent;
	private VBox vb_center;
	
	private JFXProgressBar pb_pythium;
	private Label lb_status;
	private StackPane sp_status;
	
	private HBox hb_statusBar;
	
	private BorderPane bp_main;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		setStatus(State.INITIALIZING);
		this.stage = primaryStage;
		
		Platform.setImplicitExit(false);
		javax.swing.SwingUtilities.invokeLater(this::configureTray);
		
		P.initialize();
		
		bt_refresh = new JFXButton("Refresh", new ImageView(R.getRefreshIcon(16)));
		bt_refresh.setOnAction(event -> {
			if (th_pythium == null) {
				th_pythium = new Thread(() -> {
					try {
						Platform.runLater(() -> setStatus(State.CALLING_SCRIPT));
						
						Couple<PythiumBundle, SpotifyBundle> results = Pythium.call();
						
						Platform.runLater(() -> {
							if (results != null && results.getX() != null) {
								setStatus(State.RUNNING);
								la_lyrics.setText(results.getX().getLyrics());
								
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
		
		lb_menu_refresh = new Label("Refresh");
		lb_menu_refresh.setOnMouseClicked(event -> bt_refresh.getOnAction().handle(null));
		lb_menu_exit = new Label("Exit");
		lb_menu_exit.setOnMouseClicked(event -> Lythium.this.setStatus(State.STOP));
		lv_menu = new JFXListView<>();
		lv_menu.getItems().addAll(
				lb_menu_refresh,
				lb_menu_exit
		);
		pp_menu = new JFXPopup(lv_menu);
		hb_menu = new JFXHamburger();
		hb_menu.setPadding(new Insets(10));
		rp_menu = new JFXRippler(hb_menu);
		rp_menu.setOnMouseClicked(event -> pp_menu.show(rp_menu, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
		
		tb_toolbar = new JFXToolbar();
		tb_toolbar.setLeft(bt_refresh);
		tb_toolbar.setRight(rp_menu);
		JFXDepthManager.setDepth(tb_toolbar, 0);
		
		showcase = new Showcase();
		
		la_lyrics = new Label("Wanna see your lyrics? Press the \"Refresh\" button to get them!");
		la_lyrics.setWrapText(true);
		
		pa_lyrics = new StackPane(la_lyrics);
		pa_lyrics.setId("pa_lyrics");
		pa_lyrics.setPadding(new Insets(20));
		pa_lyrics.prefWidthProperty().bind(pa_lyrics.widthProperty());
		JFXDepthManager.setDepth(pa_lyrics, 1);
		
		pa_lyrics_parent = new StackPane(pa_lyrics);
		pa_lyrics_parent.setPadding(new Insets(10));
		
		
		vb_center = new VBox(showcase, pa_lyrics_parent);
		vb_center.setPadding(new Insets(10));
		
		pb_pythium = new JFXProgressBar(0.);
		pb_pythium.setId("pb_pythium");
		
		lb_status = new Label();
		sp_status = new StackPane(lb_status);
		sp_status.setId("sp_status");
		sp_status.setPadding(new Insets(5));
		JFXDepthManager.setDepth(sp_status, 1);
		
		hb_statusBar = new HBox(new Space(20, 0), pb_pythium, new Space(20, 0), sp_status);
		hb_statusBar.setAlignment(Pos.CENTER_LEFT);
		hb_statusBar.setPadding(new Insets(10, 10, 10, 0));
		
		bp_main = new BorderPane(vb_center);
		bp_main.setTop(tb_toolbar);
		bp_main.setBottom(hb_statusBar);
		
		decorator = new JFXDecorator(this.stage, bp_main);
		decorator.setTitle("Lythium");
		decorator.setGraphic(new ImageView(R.getLythiumIcon(16, R.ImageType.WHITE)));
		Scene scene = new Scene(decorator);
		scene.getStylesheets().add(R.getLythiumDefaultStylesheet());
		this.stage.setScene(scene);
		this.stage.setTitle(decorator.getTitle());
		this.stage.getIcons().add(R.getLythiumIcon(16, R.ImageType.COLOR));
		this.stage.show();
		
		setStatus(State.RUNNING);
	}
	
	private void configureTray() {
		java.awt.Toolkit.getDefaultToolkit();
		
		if (!java.awt.SystemTray.isSupported()) {
			System.out.println("Your system does not support tray. The application cannot start.");
			Lythium.this.stop();
		}
		
		tray = java.awt.SystemTray.getSystemTray();
		trayIcon = null;
		try {
			trayIcon = new java.awt.TrayIcon(ImageIO.read(Lythium.class.getResourceAsStream("/images/lythium-black16.png")));
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
				public java.awt.Graphics getGraphics() {
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
		mi_refresh.addActionListener(e -> bt_refresh.getOnAction().handle(null));
		
		java.awt.MenuItem mi_exit = new java.awt.MenuItem("Exit");
		mi_exit.addActionListener(e -> Lythium.this.setStatus(State.STOP));
		
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
	
	public void setStatus(@NotNull State status) {
		State oldStatus = this.status;
		this.status = status;
		
		if (lb_status != null && sp_status != null) {
			lb_status.setText(this.status.getMessage());
			sp_status.setStyle("-fx-background-color: " + this.status.getBackground().toString().replaceFirst("0x", "#"));
		}
		
		switch (this.status) {
			case INITIALIZING:
				break;
			case RUNNING:
				pb_pythium.setProgress(0.);
				break;
			case CALLING_SCRIPT:
				pb_pythium.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
				break;
			case STOP:
				stop();
				break;
		}
	}
	
	@Override
	public void stop() {
		Platform.exit();
		
		if (tray != null && trayIcon != null)
			tray.remove(trayIcon);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

/**
 * TODO:
 * Save thumbnails in Lythium-side in .lythium/cache
 * In Pythium, save the lyrics AND the number of time that the music has been fetch from the dictionary, so when the
      file becomes too big, the script can delete the useless ones.
 * In Lythium, make an option to delete the cache files (all .cache-*)
 */
