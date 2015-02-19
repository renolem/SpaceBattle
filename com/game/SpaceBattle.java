package com.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.game.entity.AbstractEntity;
import com.game.entity.AbstractMovingEntity;
import com.game.entity.ShotEntity;
import com.game.player.AbstractPlayer;
import com.game.player.BotPlayer;
import com.game.player.LocalPlayer;

@SuppressWarnings("serial")
public class SpaceBattle extends JFrame {
	
	/** Nom du jeux */
	public static final String GAME_NAME = "Bataille Spaciale";
    
    /** Largeur de la fenêtre */
    public static final int WIN_WIDTH  = 1024;
    
    /** Hauteur de le fenêtre */
    public static final int WIN_HEIGHT = 768;
    
    /** Largeur de la zone de jeux */
    public static final int GAME_WIDTH  = 1920;
    
    /** Hauteur de la zone de jeux */
    public static final int GAME_HEIGHT = 1200;
    
    /** Position (axe X) du joueur pendant le scrolling */
    public static final double PLAYER_DISPLAY_X = WIN_WIDTH * 0.5;
    
    /** Position (axe Y) du joueur pendant le scrolling */
    public static final double PLAYER_DISPLAY_Y = WIN_HEIGHT * 0.5;
    
    /** Variable permettant d'utiliser la mémoire VRAM */
    private final BufferStrategy strategy; 
    
    /** Buffer mémoire où les opérations graphiques sont appliquées */
    private final Graphics2D buffer; 
    
    /** Carte du jeux */
    private final Map map;
    
    /** HUD */
    private final HUD hud;
   
    /** Joueur local */
    private final AbstractPlayer local_player;
    
    /** Liste des joueurs */
	private final Set<AbstractPlayer> players = new HashSet<AbstractPlayer>();

	
	private Set<AbstractEntity> entities = new HashSet<AbstractEntity>();
	private Set<AbstractEntity> entitiesToRemove = new HashSet<AbstractEntity>();
	
	private Rectangle2D visible = new Rectangle2D.Double();
	
	
	public SpaceBattle() {
		super(GAME_NAME);
		
		// Création de la fenetre
		setLayout(new BorderLayout());
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(GAME_NAME);
		
		// Suppression du curseur
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		
	
		Canvas can = new Canvas();
		can.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		add(can, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
		
		can.setIgnoreRepaint(true);
		can.createBufferStrategy(2);
		
		
		// Initialisation du buffer d'affichage
		strategy = can.getBufferStrategy(); 
	    buffer = (Graphics2D)strategy.getDrawGraphics();
	    
	   
		map = new Map();
		hud = new HUD();
		local_player = new LocalPlayer();
		
		can.addKeyListener(new GameKeyListener());
		can.addMouseListener(new GameMouseListener());
		
		// simulation joueur
		for (int i=1; i<=5; i++) {
		    final BotPlayer p1 = new BotPlayer();
		    p1.setName("Player-" + i);
		    addPlayerToGame(p1);
		    
		    // Pseudo IA...
		    new Thread() {
		    	public void run() {
		    		while(true) {
		    			
		    			int i = (int)(Math.random() * 100) % 4;
		    			
		    			switch (i) {
		    			case 0: p1.getGamePad().setLeft(!p1.getGamePad().isLeft()); break;
		    			case 1: p1.getGamePad().setRight(!p1.getGamePad().isRight()); break;
		    			case 2: p1.getGamePad().setUp(!p1.getGamePad().isUp()); break;
		    			case 3: p1.getGamePad().setShot(!p1.getGamePad().isShot()); break;
		    			}

		    			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    		}
		    	}
		    }.start();
		}
		
		// Initialisation du joueur local
	    local_player.setName("Renaud");
	    addPlayerToGame(local_player);
		
		// boucle principale
		long lastLoopTime = System.currentTimeMillis();
		
	    while(true) {
	    	long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            
            frame(delta);

			try {
				long loopDuration = System.currentTimeMillis() - lastLoopTime;
				long l = 1000/60 - loopDuration;
				
				Thread.sleep(l < 0 ? 0 : l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}
	

	private int dx, dy;
	
	private void frame(long delta) {
		// Calcul du décalage entre la position réelle et l'affichage	
		if (local_player.getEntity().getLocationX() < SpaceBattle.PLAYER_DISPLAY_X) {
			dx = 0;
		} else if (local_player.getEntity().getLocationX() > SpaceBattle.GAME_WIDTH - SpaceBattle.PLAYER_DISPLAY_X) {
			dx = SpaceBattle.GAME_WIDTH - SpaceBattle.WIN_WIDTH;
		} else {
			dx = (int)(local_player.getEntity().getLocationX() - SpaceBattle.PLAYER_DISPLAY_X);
		}
		
		if (local_player.getEntity().getLocationY() < SpaceBattle.PLAYER_DISPLAY_Y) {
			dy = 0;
		} else if (local_player.getEntity().getLocationY() > SpaceBattle.GAME_HEIGHT - SpaceBattle.PLAYER_DISPLAY_Y) {
			dy = SpaceBattle.GAME_HEIGHT - SpaceBattle.WIN_HEIGHT;
		} else {
			dy = (int)(local_player.getEntity().getLocationY() - SpaceBattle.PLAYER_DISPLAY_Y);
		}
		
		visible.setRect(dx, dy, WIN_WIDTH, WIN_HEIGHT);
		
		// Affichage de la map
		map.draw(buffer, (int)local_player.getEntity().getLocationX(), (int)local_player.getEntity().getLocationY(), dx, dy);
		
		// Affichage du HUD
		hud.draw(buffer, local_player, players);
		
		// Gestion des entités
        for(AbstractEntity entity : entities) {
        	// Destruction de l'entité si elle est marquée à detruire
        	if (entity.isDestroyed() || isOutOfGameArea(entity)) {
        		entitiesToRemove.add(entity);
        		continue;
        	}
        	
        	if (visible.intersects(entity.getRectangle())) {
        		entity.renderGfx(buffer, dx, dy, delta);	
        	}
        	
        	entity.renderSfx(delta);
        	
        	if (entity instanceof AbstractMovingEntity) {
        		((AbstractMovingEntity)entity).move(delta);
        	}
        	
        	// collision et interaction
        	for(AbstractEntity other : entities) {
        		if (other == entity) continue;
    			other.interactWith(entity);
    			if (entity.collidesWith(other)) {
        			other.collidedWith(entity);
        		}
        	}
        }
        
        // Nettoyage des entités
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();

        // Gestion des joueurs
        for (AbstractPlayer player : players) {
        	if (player.isDead()) {
        		continue;
        	}
        	
        	// Deplacement
        	player.move(delta);
        	
        	// affichage details
        	player.drawDetails(buffer, dx, dy);
        	
        	// Tir
        	ShotEntity shot = player.shot(delta);
        	if (shot != null) {
        		entities.add(shot);
        	}
        }
        
        // curseur
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
     	local_player.getGamePad().setMouseLocation(mouseLocation.getX() + dx - this.getX(), mouseLocation.getY() + dy - this.getY());
    	buffer.setColor(Color.YELLOW);
    	buffer.fillRect((int)local_player.getGamePad().getMouseLocationX()-dx-5, (int)local_player.getGamePad().getMouseLocationY()-dy-5, 10, 10);
    	
    	// Fin de partie
    	if (this.local_player.isDead()) {
    		endGame(false);
    	} else {
    		boolean allDead = true;
    		for (AbstractPlayer p : players) {
    			if (p == local_player) {
    				continue;
    			}
    			
    			allDead &= p.isDead();
    			if (!allDead) {
    				break;
    			}
    		}
    		if (allDead) {
    			endGame(true);
    		}
		}
    	
		strategy.show();
	}
	
	/**
	 * Fin de partie
	 */
	private void endGame(boolean victory) {
		// TODO
		JOptionPane.showMessageDialog(this, victory ? "Vous avez gagné !" : "Vous avez perdu !");
		System.exit(0);
	}
	
	/**
	 * Ajoute un joueur à la partie
	 * @param player - joueur à ajouter
	 */
	private void addPlayerToGame(AbstractPlayer player) {
		double x = Math.random() * GAME_WIDTH;
		double y = Math.random() * GAME_HEIGHT;
		
		player.spawn(x, y);
		
		this.players.add(player);
		this.entities.add(player.getEntity());
	}
	
	/**
	 * Determine si une entité est sortie de l'aire de jeu
	 * @param entity - entité à tester
	 * @return vrai si l'entité n'est plus dans l'aire de jeu
	 */
	private boolean isOutOfGameArea(final AbstractEntity entity) {
		Rectangle area = new Rectangle(0, 0, SpaceBattle.GAME_WIDTH, SpaceBattle.GAME_HEIGHT);
		return !area.intersects(entity.getRectangle());
	}
	
	/**
	 * Gestion des evenements de la souris pour le joueur local
	 */
	private class GameMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			local_player.getGamePad().setShot(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			local_player.getGamePad().setShot(false);
		}		
	}
	
	/**
	 * Gestion des evenements du clavier pour le joueur local
	 */
	private class GameKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				local_player.getGamePad().setUp(true);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				local_player.getGamePad().setDown(true);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				local_player.getGamePad().setLeft(true);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				local_player.getGamePad().setRight(true);
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				local_player.getGamePad().setShot(true);
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				local_player.getGamePad().setUp(false);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				local_player.getGamePad().setDown(false);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				local_player.getGamePad().setLeft(false);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				local_player.getGamePad().setRight(false);
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				local_player.getGamePad().setShot(false);
			}
		}
	}
	
	public static void main(String arg[]) {
		new SpaceBattle();
	}
}