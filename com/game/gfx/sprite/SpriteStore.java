package com.game.gfx.sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Manager (singleton) de ressources pour les sprites du jeux
 */
public class SpriteStore {
	
	/** Chemin vers les ressources graphiques */
	private final String RESSOURCES_PATH = "com/game/resources/graphics/";
	
    /** La seule instance de la classe */
    private static SpriteStore single = new SpriteStore();
        
    /**
     * Retourne la seule instance de la classe
     * @return SpriteStore : la seule instance de la classe
     */
    public static SpriteStore getInstance() {
        return single;
    }
    
    /** Map des sprites mises en cache */
    private HashMap<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
    
    private BufferedImage getSourceImage(String ref) throws IOException {
    	BufferedImage sourceImage = null;
    	
    	URL url = this.getClass().getClassLoader().getResource(RESSOURCES_PATH + ref);
    	sourceImage = ImageIO.read(url);
    	
    	sprites.put(ref, sourceImage);
      
    	return sourceImage;
    }
    
    /**
     * Recupere une sprite en cache ou bien la charge si ce n'est pas encore fait
     * @param String: reference de l'image à utiliser pour la sprite
     * @return Sprite
     */
    public FixedSprite getSprite(String ref) throws IOException {
        
        // la sprite est en cache
        if (sprites.get(ref) != null) {
            return new FixedSprite(sprites.get(ref));
        }
        
        getSourceImage(ref);
        
        return getSprite(ref);
    }
    
 
    
    public AnimatedSprite getAnimatedSprite(String... ref) throws IOException {
    	
    	FixedSprite sprites[] = new FixedSprite[ref.length];
    	
    	for (int i=0; i<ref.length; i++) {
    		sprites[i] = getSprite(ref[i]);
    	}
    	
    	return new AnimatedSprite(sprites);
    }
}