package com.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class Map {

	/** Image de background */
    private BufferedImage backgroundImage;
	
	public Map() {
		// chargement du background
        try {
        	URL url = this.getClass().getClassLoader().getResource("com/game/resources/graphics/background.jpg");
        	this.backgroundImage = ImageIO.read(url);
        }
        catch(Exception e) { }
	}

	public void draw(final Graphics g, final int localPlayerX, final int localPlayerY, final int dx, final int dy) {		
		// Affichage du background
		g.drawImage(backgroundImage, 0, 0, SpaceBattle.WIN_WIDTH, SpaceBattle.WIN_HEIGHT, dx, dy, dx + SpaceBattle.WIN_WIDTH, dy + SpaceBattle.WIN_HEIGHT, null);
	}
}