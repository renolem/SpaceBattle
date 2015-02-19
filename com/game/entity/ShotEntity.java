package com.game.entity;

import java.awt.Graphics;
import java.io.IOException;

import com.game.gfx.sprite.AnimatedSprite;
import com.game.gfx.sprite.Sprite;
import com.game.gfx.sprite.SpriteStore;

/**
 * Represente un missile
 */
public class ShotEntity extends AbstractMovingEntity {
	
	/** Vitesse du missile (pix/ms) */
	private static final double SPEED = 0.6;
	
	/** Sprite courante */
	private Sprite currentFrame;
	
	/** Sprite du missile quand explosion */
	private transient AnimatedSprite explodeFrame;

	/**
	 * Construit une nouvelle entité missile
	 */
	public ShotEntity(final int x, final int y, final double direction) {
		super(10, 10, SPEED);
		setLocation(x, y);
		
		// TODO : chargement des sprites au demarrage du jeu
		try {
			this.currentFrame = SpriteStore.getInstance().getSprite("greenLaserRay.png");
			this.explodeFrame = SpriteStore.getInstance().getAnimatedSprite("greenLaserRay.png");
		} catch(IOException ioe) { }
		
		velocity.setX(SPEED * Math.cos(direction));
		velocity.setY(SPEED * Math.sin(direction));
	}
	
	@Override
	public void renderGfx(final Graphics g, final int dx, final int dy, final long delta) {
		this.currentFrame.draw(g, getLocationX() - dx, getLocationY() - dy, this.velocity.getDirection());
	}

	@Override
	public void collidedWith(final AbstractEntity other) {
		// La gestion de la collision se fait avec l'entité qui a recu le missile
		this.currentFrame = this.explodeFrame;
	}
}