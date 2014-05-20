package com.guit.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GJsonObject {

	public String name = "GJsonObject";
	public String data = "empty";

	public GJsonObject parent = null;

	private List<GJsonObject> children = new ArrayList<GJsonObject>();

	public GJsonObject() {
	}

	public GJsonObject(String name, String data) {
		this.name = name;
		this.data = data;
	}

	public GJsonObject findNode(String node) {

		if (name.equals(node)) return this;

		for (GJsonObject child : children) {
			GJsonObject findNode = child.findNode(node);
			if (findNode != null) return findNode;
		}
		return null;
	}

	public GJsonObject getNode(String node) {
		for (GJsonObject child : children) {
			if (child.name.equals(node)) return child;
		}
		return null;
	}

	public void add(GJsonObject json) {
		json.parent = this;
		children.add(json);
	}

	public GJsonObject set(byte b) {
		data = Byte.toString(b);
		return this;
	}

	public GJsonObject set(short s) {
		data = Short.toString(s);
		return this;
	}

	public GJsonObject set(int i) {
		data = Integer.toString(i);
		return this;
	}

	public GJsonObject set(float f) {
		data = Float.toString(f);
		return this;
	}

	public GJsonObject set(double d) {
		data = Double.toString(d);
		return this;
	}

	public GJsonObject set(long l) {
		data = Long.toString(l);
		return this;
	}

	public GJsonObject set(boolean bool) {
		data = Boolean.toString(bool);
		return this;
	}

	public byte getByte() {
		return Byte.parseByte(data);
	}

	public short getShort() {
		return Short.parseShort(data);
	}

	public int getInt() {
		return Integer.parseInt(data);
	}

	public float getFloat() {
		return Float.parseFloat(data);
	}

	public double getDouble() {
		return Double.parseDouble(data);
	}

	public long getLong() {
		return Long.parseLong(data);
	}

	public boolean getBoolean() {
		return Boolean.parseBoolean(data);
	}

	public char getChar() {
		return data.charAt(0);
	}

	public byte[] toByteArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		byte[] bytes = new byte[pieces.length];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = Byte.parseByte(pieces[i]);
		}

		return bytes;
	}

	public short[] toShortArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		short[] shorts = new short[pieces.length];

		for (int i = 0; i < shorts.length; i++) {
			shorts[i] = Short.parseShort(pieces[i]);
		}

		return shorts;
	}

	public int[] toIntArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		int[] ints = new int[pieces.length];

		for (int i = 0; i < ints.length; i++) {
			ints[i] = Integer.parseInt(pieces[i]);
		}

		return ints;
	}

	public float[] toFloatArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		float[] floats = new float[pieces.length];

		for (int i = 0; i < floats.length; i++) {
			floats[i] = Float.parseFloat(pieces[i]);
		}

		return floats;
	}

	public double[] toDoubleArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		double[] doubles = new double[pieces.length];

		for (int i = 0; i < doubles.length; i++) {
			doubles[i] = Double.parseDouble(pieces[i]);
		}

		return doubles;
	}

	public long[] toLongArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		long[] longs = new long[pieces.length];

		for (int i = 0; i < longs.length; i++) {
			longs[i] = Long.parseLong(pieces[i]);
		}

		return longs;
	}

	public boolean[] toBooleanArray() {
		String data = this.data.substring(1, this.data.length() - 1);

		String[] pieces = data.split(",");

		boolean[] booleans = new boolean[pieces.length];

		for (int i = 0; i < booleans.length; i++) {
			booleans[i] = Boolean.parseBoolean(pieces[i]);
		}

		return booleans;
	}

	public List<GJsonObject> getChildren() {
		return children;
	}

	public static GJsonObject parse(File file) {
		try {
			return GJsonObject.parse(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static GJsonObject parse(InputStream stream) {

		StringBuilder builder = new StringBuilder();

		int integer = -1;
		try {
			while ((integer = stream.read()) != -1) {
				builder.append((char) integer);
			}

			return GJsonObject.parse(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static GJsonObject parse(String strJson) {

		StringReader reader = new StringReader(strJson);

		GJsonObject lastNode = new GJsonObject();

		try {

			char ch = ' ';

			boolean parsingString = false;
			String string = "";
			String dataString = "";
			boolean readingData = false;
			boolean ignoreComma = false;

			List<Character> expectedChars = new ArrayList<Character>();
			List<Character> notExpectedChars = new ArrayList<Character>();

			StringBuilder builder = new StringBuilder();

			int braces = 0;

			while (true) {
				int integer = reader.read();
				ch = (char) integer;
				builder.append(ch);
				if (integer == -1) break;

				if (!parsingString) {
					// NOT parsing string

					// remove \n
					while (true) {
						if (integer == 13) {
							reader.mark(1);
							if (reader.read() == 10) {
								integer = reader.read();
								ch = (char) integer;
								builder.append(ch);
							} else reader.reset();
						} else break;
					}

					// remove white space / tab
					while (true) {
						if (ch == ' ' || ch == '	') {
							integer = reader.read();
							ch = (char) integer;
							builder.append(ch);
						} else break;
					}

					// check for expected chars
					{
						boolean isExpected = false;
						if (expectedChars.size() != 0) {
							for (int i = 0; i < expectedChars.size(); i++) {
								if (ch == expectedChars.get(i)) {
									isExpected = true;
									break;
								}
							}
						} else isExpected = true;

						if (!isExpected) {
							System.out.println("Json so far: \n" + builder.toString() + "<^error");

							String expectedArray = "[";
							for (int i = 0; i < expectedChars.size(); i++) {
								if (i != expectedChars.size() - 1) expectedArray += expectedChars.get(i) + ", ";
								else expectedArray += expectedChars.get(i);
							}
							expectedArray += "]";
							throw new IllegalArgumentException("Didn't meet expected chars currentchar(" + integer + ")='" + ch + "'" + ", expected chars " + expectedArray);
						}

					}

					{
						// check for not expected chars
						boolean isNotExpected = false;
						if (notExpectedChars.size() != 0) {
							for (int i = 0; i < notExpectedChars.size(); i++) {
								if (ch != notExpectedChars.get(i)) {
									isNotExpected = true;
									break;
								}
							}
						} else isNotExpected = true;

						if (!isNotExpected) {
							System.out.println("Json so far: \n" + builder.toString() + "<--error");

							String notExpectedArray = "[";
							for (int i = 0; i < expectedChars.size(); i++) {
								if (i != expectedChars.size() - 1) notExpectedArray += expectedChars.get(i) + ", ";
								else notExpectedArray += expectedChars.get(i);
							}
							notExpectedArray += "]";
							throw new IllegalArgumentException("Encountered not expected char. currentchar='" + ch + "'" + ", did not expect " + notExpectedArray);
						}
					}

					if (ch == '[') {
						ignoreComma = true;
						notExpectedChars.add('{');
						notExpectedChars.add('}');
						notExpectedChars.add(':');
					}
					if (ch == ']') {
						ignoreComma = false;
						notExpectedChars.clear();
					}

					if (ch == '"') {
						// start parsing string
						parsingString = true;

						expectedChars.clear();
					}

					if (!ignoreComma && ch == ',') {
						if (readingData) {
							lastNode.data = dataString;
							readingData = false;
						}
						// go back to previous node
						lastNode = lastNode.parent;
						readingData = false;

						expectedChars.clear();
						expectedChars.add('"');
					}

					if (ch == '{') {
						// not parsing data, parsing new nodes
						readingData = false;
						dataString = "";
						expectedChars.add('"');
						braces++;
					}

					if (ch == '}') {
						if (readingData) {
							lastNode.data = dataString;
							readingData = false;
						}

						// go back 2 nodes if there are no further arguments or
						// 1 node if there are further arguments

						int amt = 2;
						reader.mark(1);
						if (reader.read() == ',') amt = 1;
						reader.reset();

						for (int i = 0; i < amt; i++) {
							if (lastNode.parent != null) {
								lastNode = lastNode.parent;
							} else {
								// parent is null, done parsing the file
								break;
							}
						}

						braces--;
					}

					if (readingData) {
						if (integer != 9 && integer != 32) {
							dataString += ch;
						}
					}

					if (ch == ':') {
						// create new node and load data to it
						GJsonObject newNode = new GJsonObject(string, "");
						lastNode.add(newNode);
						lastNode = newNode;
						readingData = true;
						string = "";
						dataString = "";
						expectedChars.clear();
					}

				} else {
					// parsing string
					if (ch == '"') {
						// done parsing string
						parsingString = false;

						if (readingData) {
							// was reading data, so store it to last node
							readingData = false;
							lastNode.data = string;
							string = "";
							expectedChars.add(',');
							expectedChars.add('}');
						} else {
							expectedChars.add(':');
						}
					} else {
						// parse the string
						string += ch;
					}
				}

			}

			reader.close();

			if (braces != 0) throw new IllegalArgumentException("Did not read string properly. Braces diff: " + braces + "(Should be 0) " + "Check ',' and '}' are set properly");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return lastNode;
	}
}
