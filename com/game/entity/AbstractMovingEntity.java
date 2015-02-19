package com.game.entity;

import com.game.math2d.Force;

/**
 * Represente une entité ayant la capacité de se déplacer
 */
public abstract class AbstractMovingEntity extends AbstractEntity {
	
    /** Veteur déplacement de l'entité */
    protected final Force velocity;
    
    /** Vitesse maximum de l'entité */
    protected final double maxSpeed;
    
    /**
     * Construit une entité
     * @param width - largeur
     * @param height - hauteur
     * @param maxSpeed - vitesse maximum
     */
    public AbstractMovingEntity(final double width, final double height, final double maxSpeed) {
		super(width, height);
		
		this.maxSpeed = maxSpeed;
		this.velocity = new Force();
	}
 
    /** 
     * Applique une force à l'entité
     * @param f - force à appliquer
     */
    public void applyForce(final Force f) {
    	this.velocity.add(f);
    }
    
    /** 
     * Applique une force à l'entité
     * @param x - abscisse du vecteur force
     * @param y - ordonnée du vecteur force
     */
    public void applyForce(final double x, final double y) {
    	Force f = new Force(x, y);
    	applyForce(f);
    }
    
    /**
     * @return la direction du vecteur deplacement de l'entité
     */
	public double getDirection() {
		return this.velocity.getDirection();
	}
	
	/**
     * Demande à l'entité de se déplacer sur tout les axes
     * @param delta: temps écoulé depuis le dernier déplacement
     */
    public void move(final long delta) {
    	// Vitesse maximum
    	if (this.velocity.getMagnitude() > this.maxSpeed) {
    		this.velocity.setMagnitude(this.maxSpeed);
    	}
    	
    	// Mise à jour de la position
    	setLocation(getLocationX() + this.velocity.getX() * delta, getLocationY() + this.velocity.getY() * delta);
    }
}