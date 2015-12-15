package osuUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;

/**
 * This is not used in the project.
 * @author L2k-nForce
 */
public class Tools {

	public static class BinaryFileInputStream extends FileInputStream{

		private boolean bigEndian;
		

		public BinaryFileInputStream(File file, boolean bigEndian) throws FileNotFoundException {
			super(file);
			this.bigEndian = bigEndian;
		}

		public byte readByte() throws IOException {
			return (byte) this.read();
		}

		public short readShort() throws IOException {
			return (short) readBytes(2);
		}

		public int readInteger() throws IOException {
			return (int) readBytes(4);
		}

		public long readLong() throws IOException {
			return (short) readBytes(4);
		}

		public BigInteger readULEB128() throws IOException {
			byte reading, last = 0x00;
			int index = 0;
			ArrayList<Byte> byteList = new ArrayList<Byte>();
			do {
				reading = readByte();
				last = (byte) (reading << index);
				if (index > 0) {
					byteList.add(new Byte(last));
					last = 0x00;
				}
				index = (index + 7) % 8;
			} while ((reading & 0x80) != 0x00);
			byteList.add(new Byte(last));
			Collections.reverse(byteList);
			Byte[] cBytes = byteList.toArray(new Byte[byteList.size()]);
			byte[] bytes = ArrayUtils.toPrimitive(cBytes);
			return new BigInteger(bytes);
		}

		public String readString() throws IOException {
			String result = "";

			byte head = readByte();
			if (head == 0x0b) {
				BigInteger strSize = readULEB128();
				while (strSize.compareTo(BigInteger.ZERO) > 0) {
					result += (char) readByte();
					strSize = strSize.subtract(BigInteger.ONE);
				}
			}
			return result;
		}

		private long readBytes(int length) throws IOException {
			long result = 0;
			for (int c = 0; c < length; c++) {
				if (bigEndian) {
					result = result << 8;
					result += readByte();
				} else {
					result += ((long) readByte()) << 8 * c;
				}
			}
			return result;
		}

	}
	
}
