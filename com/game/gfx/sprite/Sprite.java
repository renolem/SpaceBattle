package com.game.gfx.sprite;

import java.awt.Graphics;

/**
 * Repr�sente une sprite � afficher � l'�cran
 */
public abstract class Sprite {
	
	/** Largeur de la sprite */
	protected int width;
	
	/** Hauteur de la sprite */
	protected int height;
	
	/**
	 * Definition de la taille de la sprite
	 */
	public Sprite(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Retourne la largeur de la sprite
	 * @return int
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retourne la hauteur de la sprite
	 * @return int
	 */
	public int getHeight() {
		return height;
	}

    /**
     * Dessine la sprite sur le graphique donn� en param�tre
     * @param Graphics : Le graphique o� la sprite doit �tre affich�e
     * @param double   : La position (X) o� afficher la sprite
     * @param double   : La position (Y) o� afficher la sprite
     * @param double   : La direction dans laquelle affiche la sprite (radian)
     */
    public abstract void draw(Graphics g, double x, double y, double direction);
}