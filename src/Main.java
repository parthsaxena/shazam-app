import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Main {

	static boolean running = false;
	
	public static void main(String[] args) {

		final AudioFormat format = getFormat();                                                                                                                                                                                                                                                                                                              
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		final TargetDataLine line;
			try {
				line = (TargetDataLine) AudioSystem.getLine(info);
				line.open(format);
				line.start();
				
				Thread listeningThread = new Thread(new Runnable() {
					public void run() {
						OutputStream out = new ByteArrayOutputStream();
						running = true;
						byte[] buffer = new byte[(int) 1024];
						
						try {
						    while (running) {
						        int count = line.read(buffer, 0, buffer.length);
						        if (count > 0) {
						            out.write(buffer, 0, count);
						        }
						    }
						    out.close();
						} catch (IOException e) {
						    System.err.println("I/O problems: " + e);
						    System.exit(-1);
						}
					}
				});	
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error Attaching Line...");
			}
	}
		
	private static AudioFormat getFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
}
