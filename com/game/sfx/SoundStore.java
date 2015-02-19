package com.game.sfx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Manager (singleton) de ressources pour les sprites du jeux
 */
public class SoundStore {
	
	/** Chemin vers les ressources audio */
	private final String RESSOURCES_PATH = "com/game/resources/sounds/";
    
    /** La seule instance de la classe */
    private static SoundStore single = new SoundStore();
        
    /**
     * Retourne la seule instance de la classe
     * @return SpriteStore : la seule instance de la classe
     */
    public static SoundStore getInstance() {
        return single;
    }
    
    /** Map des effets sonores mis en cache */
    private HashMap<String, SoundData> soundData = new HashMap<String, SoundData>();

    
	private SoundData getSoundData(String ref) {
		try {
			URL url = this.getClass().getClassLoader().getResource(RESSOURCES_PATH + ref);
	        AudioInputStream in = AudioSystem.getAudioInputStream(url);
			
			AudioFormat format = in.getFormat();
		
		
			byte[] data = new byte[format.getFrameSize()];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int length;
			

			while ((length = in.read(data)) >= 0)
				out.write(data, 0, length);

			in.close();
			
			data = out.toByteArray();
			
			SoundData sd = new SoundData(data, format);
			soundData.put(ref, sd);
			
			return sd;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
    }
	

    public Sound getSound(String ref) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        
        // le son est en cache
        if (soundData.get(ref) != null) {
        	return new Sound(soundData.get(ref));
        }
        
        getSoundData(ref);
         
        return getSound(ref);
    }
}