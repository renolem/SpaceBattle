package com.game.player;

import java.awt.Color;
import java.awt.Graphics;

import com.game.GamePad;
import com.game.entity.ShipEntity;
import com.game.entity.ShotEntity;

public abstract class AbstractPlayer {
	
	/** Nom du joueur */
	private String name;
	
	/** Point de vie du joueur */
	private int health;
	
	/** Entité representant le joueur */
	private final ShipEntity entity;

	/** Etat des touches du joueurs */
	private final GamePad gamepad;
	
	/** Date du dernier tir */
	private long lastShot;
	
	/** Delai entre deux tirs */
	public final long DELAY_BETWEEN_SHOT = 150;
	
	/** Etat du joueur */
	private boolean dead;
	
	/**
	 * Construit un nouveau joueur
	 */
	public AbstractPlayer() {
		this.entity = new ShipEntity();
		this.entity.setPlayer(this);
		this.gamepad = new GamePad();
		
		this.health = 75;
		this.dead = true;
	}
	
	/**
	 * Fait apparaitre le joueur dans le jeu à la position données
	 * @param x - position X
	 * @param y - position Y
	 */
	public void spawn(double x, double y) {
		this.entity.setLocation(x, y);
		this.dead = false;
	}
	
	/**
	 * Affiche les détails autour du joueur
	 */
	public void drawDetails(Graphics g, int dx, int dy) {
		// Recuperation des coordonnées du vaisseau
		int x = new Double(getLocationX() - dx - getWidth()/2).intValue();
		int y = new Double(getLocationY() - dy - getHeight()/2).intValue();
		
		// Affichage du nom
		g.setColor(Color.YELLOW);
		g.drawString(name, x, y-15);
		
		// Affichage de la vie
		g.setColor(Color.RED);
		g.fillRect(x, y-11, ShipEntity.WIDTH, 3);
		g.setColor(Color.GREEN);
		g.fillRect(x, y-11, health*ShipEntity.WIDTH/100, 3);
	}
	
	public void move(long delta) {
		entity.move(delta, gamepad.isUp(), gamepad.isDown(), gamepad.isRight(), gamepad.isLeft());
	}
	
	public ShotEntity shot(long delta) {
		if (gamepad.isShot() && System.currentTimeMillis() - lastShot > DELAY_BETWEEN_SHOT) {
			lastShot = System.currentTimeMillis();
			
			double x1 = getLocationX();
			double y1 = getLocationY();
			
			double x2 = getGamePad().getMouseLocationX();
			double y2 = getGamePad().getMouseLocationY();
			
			double direction = Math.atan((y2 - y1) / (x2 - x1));
			
			if (x2 < x1) {
				direction += Math.PI;
			}
			
			double x = getLocationX() + Math.cos(direction)*getWidth();
			double y = getLocationY() + Math.sin(direction)*getHeight();
			
			ShotEntity shot = new ShotEntity((int)x, (int)y, direction);
			
			entity.shot();
			
			return shot;
		}
		return null;
	}
	
	public GamePad getGamePad() {
		return this.gamepad;
	}
	
	public ShipEntity getEntity() {
		return this.entity;
	}

	public String getName() {
		return name;
	}
	
	public double getLocationX() {
		return this.entity.getLocationX();
	}
	
	public double getLocationY() {
		return this.entity.getLocationY();
	}
	
	public double getWidth() {
		return this.entity.getWidth();
	}
	
	public double getHeight() {
		return this.entity.getHeight();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void hit() {
		this.health -= 5;
		
		if (this.health <= 0) {
			this.health = 0;
			
			this.entity.explose();
			this.dead = true;
		}
	}
	
	public boolean isDead() {
		return this.dead;
	}
}