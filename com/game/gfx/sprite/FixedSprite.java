package com.game.gfx.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

/**
 * Classe représentant une sprite fixe à afficher à l'écran
 */
public class FixedSprite extends Sprite {
    
    /** L'image à afficher */
    private Image image;
    
    /**
     * Création d'une nouvelle sprite à partir de l'image en paramètre
     * @param image - image à utiliser pour la sprite
     */
    public FixedSprite(Image image) {
    	super(image.getWidth(null), image.getHeight(null));
        this.image = image;
    }

    @Override
    public void draw(Graphics g, double x, double y, double direction) {	
    	AffineTransform affineTransform = new AffineTransform(); 
		affineTransform.setToTranslation(x-width/2, y-height/2); 
		affineTransform.rotate(direction+Math.toRadians(90), width/2, height/2); 
		((Graphics2D)g).drawImage(image, affineTransform, null);
    }
}