package com.game.entity;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.game.player.AbstractPlayer;

/**
 * Represente une entité fixe
 */
public abstract class AbstractEntity {
    
    /** Rectangle autour de l'entité pour les collisions */
    private final Rectangle2D rect;
    
    /** Entité detruite */
    private boolean destroyed;
    
    /** Lien vers le joueur controllant l'entité */
    private AbstractPlayer player;

	/**
     * Construit une entité
     * @param width - largeur
     * @param height - hauteur
     */
    public AbstractEntity(final double width, final double height) {
    	this.destroyed = false;
    	this.rect = new Rectangle2D.Double(0, 0, width, height);
    }
    
    /**
     * @return vrai si l'entité est détruite
     */
	public boolean isDestroyed() {
		return destroyed;
	}
	
	/**
	 * Détruit l'entité
	 */
	public void destroy() {
		this.destroyed = true;
	}
	
	/**
	 * @return le joueur contrôlant l'entité. Null si aucun joueur ne la contrôle
	 */
	public AbstractPlayer getPlayer() {
		return player;
	}

	/**
	 * Définie le joueur contrôlant l'entité
	 * @param player - joueur contrôlant l'entité
	 */
	public void setPlayer(final AbstractPlayer player) {
		this.player = player;
	}
	
	/**
	 * @return la position (X) de l'entité
	 */
	public double getLocationX() {
		return this.rect.getCenterX();
	}
	
	/**
	 * @return la position (Y) de l'entité
	 */
	public double getLocationY() {
		return this.rect.getCenterY();
	}
	
	/**
	 * 
	 * @return
	 */
	public Rectangle2D getRectangle() {
		return this.rect;
	}
	
	/**
	 * Définie la position de l'entité
	 * @param x - position (X)
	 * @param y - position (Y)
	 */
	public void setLocation(double x, double y) {
		this.rect.setRect(x - rect.getWidth()/2, y - rect.getHeight()/2, rect.getWidth(), rect.getHeight());
	}
	
	/**
	 * @return la largeur de l'entité
	 */
	public double getWidth() {
		return this.rect.getWidth();
	}
	
	/**
	 * @return la hauter de l'entité
	 */
	public double getHeight() {
		return this.rect.getHeight();
	}

	/**
     * Rendu graphique de l'entité
     * @param g - Graphics où afficher l'entité
     * @param dx - decallage sur l'axe X
     * @param dy - decallage sur l'axe Y
     * @param delta - 
     */
    public abstract void renderGfx(final Graphics g, int dx, int dy, long delta);
    
    /**
     * Rendu sonore de l'entité. Par defaut, l'entité ne produit aucun son.
     * @param delta - 
     */
    public void renderSfx(long delta) {
    	// Par défaut, une entité n'emet pas de son
    }
    
    /**
     * @return la position de l'entité
     */
    public Point2D getLocation() {
    	return new Point2D.Double(this.rect.getCenterX(), this.rect.getCenterY());
    }
    
    /**
     * @param entity - entité avec laquelle mesurer la distance
     * @return la distance entre les deux entités
     */
    public double distanceTo(final AbstractEntity entity) {
    	return this.getLocation().distance(entity.getLocation());
    }
    
    /**
     * Notifie une collision avec une autre entité
     * @param entity - entité avec laquelle a eu lieu la collision
     */
    public void collidedWith(final AbstractEntity entity) {
    	// Par défaut, pas de collision
    }
    
    /**
     * Regarde si l'entité est en colision avec une autre
     * @param Entity: entité avec laquelle verifier s'il y a collision
     * @return vrai s'il y a collision
     */
    public boolean collidesWith(final AbstractEntity other) {
        return rect.intersects(other.rect);
    }
    
    /**
     * Interraction entre deux entités
     * @param entity - entité avec laquelle effectuer l'interaction
     */
    public void interactWith(final AbstractEntity entity) {
    	// Par défaut, pas d'interaction
    }
}