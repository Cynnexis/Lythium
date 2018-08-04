package fr.berger.lythium.ui;

import javafx.scene.layout.Pane;

public class Space extends Pane {
	
	private double _width;
	private double _height;
	
	public Space(double width, double height) {
		super();
		set_width(width);
		set_height(height);
	}
	public Space(double widthHeight) {
		this(widthHeight, widthHeight);
	}
	public Space() {
		this(10., 10.);
	}
	
	public double get_width() {
		return _width;
	}
	
	public void set_width(double _width) {
		this._width = _width;
		this.setWidth(this._width);
		this.setMaxWidth(this._width);
		this.setMinWidth(this._width);
		this.setPrefWidth(this._width);
	}
	
	public double get_height() {
		return _height;
	}
	
	public void set_height(double _height) {
		this._height = _height;
		this.setHeight(this._height);
		this.setMaxHeight(this._height);
		this.setMinHeight(this._height);
		this.setPrefHeight(this._height);
	}
}
