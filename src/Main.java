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
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					running = true;
					int n = 0;
					byte[] buffer = new byte[(int) 1024];

					try {
						while (running) {
							n++;
							int count = 0;
							if (n > 1000)
								break;
							if (count > 0) {
								out.write(buffer, 0, count);
							}
						}

						byte b[] = out.toByteArray();
						for (int i = 0; i < b.length; i++) {
							System.out.println(b[i]);
						}

						try {

							FileWriter fstream = new FileWriter("out.txt");
							BufferedWriter outFile = new BufferedWriter(fstream);

							byte bytes[] = out.toByteArray();
							for (int i = 0; i < b.length; i++) {
								outFile.write("" + b[i] + ";");
							}
							outFile.close();

						} catch (Exception e) {
							System.err.println("Error: " + e.getMessage());
						}

						out.close();
						line.close();
					} catch (IOException e) {
						System.err.println("I/O problems: " + e);
						System.exit(-1);
					}

				}

			});

			listeningThread.start();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		/*OutputStream out = new ByteArrayOutputStream();
		running = true;
		
		try {
			while (running) {
				int count  = line.read(buffer,  0,  buffer.length);
				if (count > 0) {
					out.write(buffer,  0, count);
				}
			}
			out.close();
		} catch (IOException e) {
			System.err.println("I/O problems: " + e);
			System.exit(-1); 
		}*/
		
		
		
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
