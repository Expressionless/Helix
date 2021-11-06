package helix.utils.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import helix.Constants;

/**
 * Basic Binary file writer
 * @author Sly
 *
 */
public class BinaryWriter {
	private static final Logger log = Logger.getLogger(BinaryReader.class.getName());

	private OutputStream out;
	
	/**
	 * Path to the output file
	 */
	private String outputPath;

	/**
	 * Create a new BinaryWriter and open up the file at the specified path, relative to
	 * the absolute directory
	 * 
	 * @param filePath
	 * 
	 * @see {@link Constants#ABS_PATH}
	 */
	public BinaryWriter(String filePath) {
		this(filePath, true);
	}

	/**
	 * Create a new BinaryWriter and open up the file at the specified path
	 * 
	 * @param filePath
	 * @param relative
	 * 
	 * @see {@link Constants#ABS_PATH}
	 */
	public BinaryWriter(String outputDir, boolean relative) {
		if (relative)
			outputDir = Constants.ABS_PATH + outputDir;

		this.outputPath = outputDir;

		File outputFile = new File(outputDir);
		try {
			outputFile.createNewFile();
		} catch (IOException e1) {
			log.severe("Failed to create new file: " + outputDir);
			e1.printStackTrace();
		}

		try {
			out = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write bytes to the end of the file
	 * @param bytes - bytes to write
	 */
	public void write(byte... bytes) {
		try {
			System.out.println("wrote: " + bytes.length + " bytes");
			out.write(bytes);
		} catch (IOException e) {
			log.severe("Failed to write data to file: " + outputPath);
			e.printStackTrace();
		}
	}

	/**
	 * Write ints to the end of the file
	 * @param bytes - bytes to write
	 */
	public void write(int... intBytes) {
		byte[] bytes = new byte[intBytes.length];
		for (int i = 0; i < intBytes.length; i++) {
			bytes[i] = (byte) intBytes[i];
		}
		this.write(bytes);
	}

	/**
	 * Write bytes to the end of the file
	 * @param bytes - bytes to write
	 * Same as: {@link BinaryWriter#write(int... ints)}
	 */
	public void writeInts(int... intBytes) {
		byte[] bytes = new byte[intBytes.length];
		for (int i = 0; i < intBytes.length; i++) {
			bytes[i] = (byte) intBytes[i];
		}
		this.write(bytes);
	}

	/**
	 * Write a string to data. Will convert to binary first
	 * 
	 * @param string - String to write
	 */
	public void write(String string, int allocation) {
		while (string.length() < allocation) {
			string += " ";
		}
		this.write(string.getBytes());
	}

	/**
	 * Write a block of booleans to the end of the file
	 * @param flags - booleans to write (sets of 4)
	 * @param numBlocks - number of blocks to write (bytes)
	 */
	public void writeBools(boolean[] flags, int numBlocks) {
		int[] blocks = new int[numBlocks];
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < flags.length; j++) {
				if (flags[j]) {
					blocks[i] = (blocks[i] ^ (int) Math.pow(2, j));
				}
			}

			this.write(blocks);
		}
	}

	/**
	 * Write a single block of booleans to the end of the file
	 * @param flags - booleans to write
	 */
	public void writeBools(boolean[] flags) {
		if (flags.length != 4)
			return;
		this.writeBools(flags, 1);
	}

	/**
	 * Flush the output and close the file
	 */
	public void close() {
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert a long to a byte array
	 * @param long
	 * @return 2 bytes consisting of the long
	 */
	public byte[] longToByte(long x) {
		byte[] result = new byte[8];
	    for (int i = 7; i >= 0; i--) {
	        result[i] = (byte)(x & 0xFF);
	        x >>= 8;
	    }
	    
	    return result;
	}
	
	/**
	 * Write a long
	 * @param num
	 */
	public void write(long num) {
		byte[] bytes = this.longToByte(num);
		this.write(bytes);
	}
}
