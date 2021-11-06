package helix.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import helix.Constants;

/**
 * Read in binary from a location
 * Stores an array of bytes read in from a file
 * @author Sly
 *
 * @see {@link Byte}
 */
public class BinaryReader {
	private static final Logger log = Logger.getLogger(BinaryReader.class.getName());

	/**
	 * Reference to the path of the file being read in
	 */
	public final String filePath;

	private InputStream is;
	
	private final ArrayList<Byte> bytes;
	
	/**
	 * Read in the bytes from this file (relative to absolute directory)
	 * @param filePath - Path of the file to read in
	 * 
	 * @see {@link helix.Constants#ABS_PATH}
	 */
	public BinaryReader(String filePath) {
		this(filePath, true);
	}
	
	/**
	 * Read in the bytes from this file
	 * @param filePath - Path of the file to read in
	 * @param relative - whether or not the file path is relative to the absolute directory
	 * 
	 * @see {@link helix.Constants#ABS_PATH}
	 */
	public BinaryReader(String filePath, boolean relative) {
		this.bytes = new ArrayList<Byte>();
		
		if(relative)
			filePath = Constants.ABS_PATH + filePath;
		if(!new File(filePath).exists())
			System.err.println("File doesn't exist: " + filePath);
		
		this.filePath = filePath;
		this.readBytes();
		
		if(this.bytes.size() == 0)
			System.out.println("BAD FILE");
		System.out.println("Read in " + this.bytes.size() + " Bytes from " + this.filePath);
	}
	
	/**
	 * Fetch a string from a position in the {@link BinaryReader#bytes} array
	 * @param position - position to read from in the byte array
	 * @param length - length to read in the byte array
	 * @return - A string containing data from the byte array at the specified position, up to the length requested
	 */
	public String getString(int position, int length) {
		// start at specified position
		byte[] data = new byte[length];
		for(int i = 0; i < data.length - 1; i++) {
			data[i] = bytes.get(position + i);
		}

		return new String(data).trim();
	}
	
	/**
	 * Return a boolean value from a byte at a specified position
	 * @param bytePosition - Position of the byte in the {@link bytes} to look at
	 * @param bitPosition - Position of the bit in the byte to look at
	 * @return - boolean value of the bit requested
	 * 
	 * @see {@link BinaryReader#getBoolean}
	 */
	public boolean getBool(int bytePosition, int bitPosition) {
		return this.getBoolean(bytePosition, bitPosition);
	}

	/**
	 * Return a boolean value from a byte at a specified position
	 * @param bytePosition - Position of the byte in the {@link bytes} to look at
	 * @param bitPosition - Position of the bit in the byte to look at
	 * @return - boolean value of the bit requested
	 * 
	 * @see {@link BinaryReader#getBool}
	 */
	public boolean getBoolean(int bytePosition, int bitPosition) {
		int block = 0x00;
		
		block = bytes.get(bytePosition) | block;
		
		int convertedBitPosition = (int)Math.pow(2, bitPosition);
		return ((block & convertedBitPosition) != 0);
	}
	
	/**
	 * Fetch bytes from a given position up to a length
	 * @param position - position in the {@link BinaryReader#bytes} to read from
	 * @param len - amount of bytes to fetch
	 * @return - an array of bytes containing all the bytes fetched
	 */
	public byte[] getBytes(int position, int len) {
		byte[] bytes = new byte[len];
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = this.bytes.get(position + i);
		}
		
		return bytes;
	}
	
	/**
	 * Fetch a (single byte) integer from the {@link BinaryReader#bytes}
	 * @param position - position to get the integer from
	 */
	public int getInt(int position) {
		return (int)bytes.get(position);
	}
	
	/**
	 * Reading byte array helper function
	 */
	private void readBytes() {
		bytes.removeAll(bytes);
		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			log.severe("FAILED TO FIND FILE: " + filePath);
			e.printStackTrace();
		}
		
		byte byteRead;
		int numBytes = 0;
		// read data
		try {
			while((byteRead = (byte)is.read()) != -1) {
				bytes.add(byteRead);
				numBytes++;
			}
		} catch (IOException e) {
			log.severe("Failed to read byte at: " + numBytes);
			e.printStackTrace();
		}

		//try {
		//  is.close();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
	}

	public ArrayList<Byte> getBytes() {
		return bytes;
	}
	
	

}
