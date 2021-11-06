package helix.game;

import helix.utils.io.BinaryReader;
import helix.utils.io.BinaryWriter;

/**
 * Serializable interface to enable data redaing/writing
 * @author bmeachem
 *
 */
public interface Serializable {

	/**
	 * Write object through a specified {@link BinaryWriter}
	 * at a given position
	 * @param writer - Writer to write object with
	 * @param pos - Position in the file to write to
	 * @return - Whether or not writing was successful
	 */
	public boolean write(BinaryWriter writer, int pos);
	
	/**
	 * Read an object from a specified {@link BinaryReader}
	 * at a given position
	 * @param reader - Reader to read the object data with
	 * @param pos - Position of the file to read from
	 * @return - Whether or not reading was successful
	 */
	public boolean parse(BinaryReader reader, int pos);
}
