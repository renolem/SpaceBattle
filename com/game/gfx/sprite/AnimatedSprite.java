package com.game.gfx.sprite;

import java.awt.Graphics;

/**
 * Sprite animée
 */
public class AnimatedSprite extends Sprite {
	
	/** Durée d'affichage par defaut de chaque frame */
	public static final long DEFAULT_FRAME_DURATION = 75;

	/** Dernier changement de frame */
	private long lastFrameChange;
	
	/** Index de la frame actuellement affich�e */
	private int frameNumber;
	
	/** Durée d'affichage de chaque frame */
	private long frameDuration;
	
	/** Sprites de l'animation */
	private final FixedSprite[] frames;
	
	/** Animation en boucle */
	private boolean loop;
	
	/**
	 * @return vrai si la sprite s'anime en boucle
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * Marque la sprite à animer en boucle
	 * @param loop - vrai pour que l'animation soit en boucle
	 */
	public void setLoop(final boolean loop) {
		this.loop = loop;
	}
	
	/**
	 * Création d'une nouvelle sprite animée à partir d'un liste de sprite fixe
	 * @param frames
	 */
	public AnimatedSprite(final FixedSprite... frames) {
		super(frames[0].getWidth(), frames[0].getHeight());
		
		this.frames = frames;
		this.frameDuration = DEFAULT_FRAME_DURATION;
		this.frameNumber = 0;
		this.lastFrameChange = 0;
		this.loop = true;
	}

	@Override
	public void draw(final Graphics g, final double x, final double y, final double direction) {
		if (frameNumber < frames.length) {
			frames[frameNumber].draw(g, x, y, direction);
		}
	}
	
	/**
	 * Animation de la sprite
	 * @param delta
	 */
	public void frame(final long delta) {
		lastFrameChange += delta;
		
		if (lastFrameChange > frameDuration) {
			lastFrameChange = 0;
			frameNumber++;
			
			if (frameNumber >= frames.length && loop) {
				frameNumber = 0;
			}
		}
	}

	/**
	 * @return the frameDuration
	 */
	public long getFrameDuration() {
		return frameDuration;
	}

	/**
	 * @param frameDuration the frameDuration to set
	 */
	public void setFrameDuration(final long frameDuration) {
		this.frameDuration = frameDuration;
	}
}