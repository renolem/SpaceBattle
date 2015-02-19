package com.game.entity;

import java.awt.Graphics;
import java.io.IOException;

import com.game.SpaceBattle;
import com.game.gfx.sprite.AnimatedSprite;
import com.game.gfx.sprite.FixedSprite;
import com.game.gfx.sprite.Sprite;
import com.game.gfx.sprite.SpriteStore;
import com.game.math2d.Force;
import com.game.sfx.Sound;
import com.game.sfx.SoundStore;

public class ShipEntity extends AbstractMovingEntity {

	/** Vitesse (pix/ms) */
	public static final double SPEED = 0.01;
	
	/** Vitesse maximum */
	public static final double MAX_SPEED = 0.3;
	
	/** Vitesse de rotation (rad/ms) */
	public static final double ROTATE_SPEED = Math.PI / 1000;
	
	/** Largeur du vaisseau */
	public static final int WIDTH = 39;
	
	/** Hauteur du vaisseau */
	public static final int HEIGHT = 43;

	/** Direction du vaisseau **/
	private double direction;
	
	// gfx
	private Sprite currentFrame;
	private FixedSprite defaultFrame;
	private AnimatedSprite accelerationFrame;
	private AnimatedSprite rightFrame;
	private AnimatedSprite leftFrame;
	private AnimatedSprite explosionFrame;
	
	//sfx
	private Sound shotSound;
	private Sound engineSound;
	private Sound explosionSound;
	
	/**
	 * Chargement des sprites
	 * @throws IOException 
	 */
	private void initGfx() throws IOException {
		defaultFrame = SpriteStore.getInstance().getSprite("red_ship.png");
		accelerationFrame = SpriteStore.getInstance().getAnimatedSprite("red_ship_acc1.png", "red_ship_acc2.png");
		rightFrame = SpriteStore.getInstance().getAnimatedSprite("red_ship_right_acc1.png", "red_ship_right_acc2.png");
		leftFrame = SpriteStore.getInstance().getAnimatedSprite("red_ship_left_acc1.png", "red_ship_left_acc2.png");
		explosionFrame = SpriteStore.getInstance().getAnimatedSprite(
				"explosion-1.png",
				"explosion-2.png",
				"explosion-3.png",
				"explosion-4.png",
				"explosion-5.png",
				"explosion-6.png",
				"explosion-7.png",
				"explosion-8.png",
				"explosion-9.png",
				"explosion-10.png",
				"explosion-11.png",
				"explosion-12.png"
				);
		explosionFrame.setLoop(false);
		
		currentFrame = defaultFrame;
	}
	
	/**
	 * Chargement des effets sonores
	 * @throws Exception
	 */
	private void initSfx() throws Exception {
		shotSound = SoundStore.getInstance().getSound("laser.wav");
		engineSound = SoundStore.getInstance().getSound("engine.wav");
		explosionSound = SoundStore.getInstance().getSound("explosion.wav");
	}
	
	/**
	 * Constuit une nouvelle entité vaisseau
	 */
	public ShipEntity() {
		super(WIDTH, HEIGHT, MAX_SPEED);
		
		// TODO : chargement des sprites au demarrage du jeu
		try {
			initGfx();
			initSfx();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void renderGfx(Graphics g, int dx, int dy, long delta) {
		if (currentFrame instanceof AnimatedSprite) {
			((AnimatedSprite)currentFrame).frame(delta);
		}
		currentFrame.draw(g, getLocationX() - dx, getLocationY() - dy, direction);
	}
	
	long deathTime = -1;
	
	@Override
	public void renderSfx(long delta) {
		shotSound.render(delta);
		engineSound.render(delta);
		explosionSound.render(delta);
		
		if (explosionSound.isRunning() && deathTime == -1) {
			deathTime = System.currentTimeMillis();		
		}
		
		if (deathTime != -1 && !explosionSound.isRunning()) {
			destroy();
		}
	}
	
	public void move(long delta, boolean up, boolean down, boolean right, boolean left) {
		// pas d'animation par defaut
		currentFrame = defaultFrame;
		
		// direction
		if (right) {
			direction += ROTATE_SPEED*delta;
			currentFrame = rightFrame;
		} else if (left) {
			direction -= ROTATE_SPEED*delta;
			currentFrame = leftFrame;
		}
		
		// vitesse
		if (up) {
			currentFrame = accelerationFrame;

			// appliquer la force de propulsion
			Force propulsion = new Force();
			propulsion.setX(SPEED * Math.cos(this.direction));
			propulsion.setY(SPEED * Math.sin(this.direction));
			applyForce(propulsion);
		} 
		
		// son
		if (up || right || left) {
			engineSound.play(false);
		} else {
			engineSound.stop();
		}
		
		// Limitation dans le deplacement
		double x = getLocationX();
		double y = getLocationY();
		
		if (x <= getWidth()) {
			applyForce(Math.abs(velocity.getX()*1.5), 0);
		}
		
		if (x >= SpaceBattle.GAME_WIDTH-getWidth()) {
			applyForce(-Math.abs(velocity.getX()*1.5), 0);
		}
		
		if (y <= getHeight()) {
			applyForce(0, Math.abs(velocity.getY()*1.5));
		}
		
		if (y >= SpaceBattle.GAME_HEIGHT-getHeight()) {
			applyForce(0, -Math.abs(velocity.getY()*1.5));
		}	
	}
	
	@Override
	public void collidedWith(AbstractEntity other) {
		if (other instanceof ShotEntity) {
			getPlayer().hit();
			other.destroy();
		}
	}
	
	public double getDirection() {
		return direction;
	}


	public void explose() {
		currentFrame = explosionFrame;
		explosionSound.play();
	}
	
	public void shot() {
		shotSound.play(true);
	}
}