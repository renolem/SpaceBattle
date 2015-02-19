package com.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;

import com.game.player.AbstractPlayer;

/**
 * Permet d'afficher à l'écran les informations d'un joueur
 */
public class HUD {
	
	/** Largeur de la mini cate */
	private static final int MINIMAP_WIDTH = 100;
	
	/** Longueur de la mini carte */
	private static final int MINIMAP_HEIGHT = 100;
	
	/** Position (X) de la mini carte*/
	private static final int MINIMAP_X = SpaceBattle.WIN_WIDTH - MINIMAP_WIDTH - 5;
	
	/** Position (Y) de la mini carte */
	private static final int MINIMAP_Y = 5;
	
	/**
	 * Affiche à l'écran les informations sur le joueur
	 * @param g - graphique
	 * @param p - joueur
	 */
	public void draw(final Graphics g, final AbstractPlayer player, final Set<AbstractPlayer> players) {	
		g.setColor(Color.RED);
		g.drawRect(MINIMAP_X, MINIMAP_Y, MINIMAP_WIDTH, MINIMAP_HEIGHT);
		
		int x = (int)player.getLocationX() * MINIMAP_WIDTH / SpaceBattle.GAME_WIDTH;
		int y = (int)player.getLocationY() * MINIMAP_HEIGHT / SpaceBattle.GAME_HEIGHT;

		g.setColor(Color.YELLOW);
		g.fillOval(MINIMAP_X + x, MINIMAP_Y + y, 4, 4);
		
		g.setColor(Color.BLUE);
		for (AbstractPlayer p : players) {
			if (p == player || p.isDead()) {
				continue;
			}
			
			x = (int)p.getLocationX() * MINIMAP_WIDTH / SpaceBattle.GAME_WIDTH;
			y = (int)p.getLocationY() * MINIMAP_HEIGHT / SpaceBattle.GAME_HEIGHT;
			
			g.fillOval(MINIMAP_X + x, MINIMAP_Y + y, 4, 4);
		}
	}
}