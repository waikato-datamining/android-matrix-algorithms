/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * PyMADeserialisationTest.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Tests the PyMADeserialisation class.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class PyMADeserialisationTest{

  @Test
  public void deserialiseBytes() throws IOException {
    byte[] orig = toBytes(0x12, 0x13, 0x24, 0xaf, 0xbb, 0x3c);

    InputStream input = byteStream(orig);

    byte[] bytes = PyMADeserialisation.deserialiseBytes(input, orig.length);

    Assertions.assertArrayEquals(orig, bytes);
  }

  @Test
  public void deserialiseInts() throws IOException {
    int[] orig = new int[]{123, 65436, -222333, 1000000000, -2332211};

    InputStream input = intStream(orig);

    int[] ints = PyMADeserialisation.deserialiseInts(input, orig.length);

    Assertions.assertArrayEquals(orig, ints);
  }

  @Test
  public void deserialiseDoubles() throws IOException {
    double[] orig = new double[]{1.0, 1.1, 1234556.434, -13.000002, 101.1110010101};

    InputStream input = doubleStream(orig);

    double[] doubles = PyMADeserialisation.deserialiseDoubles(input, orig.length);

    Assertions.assertArrayEquals(orig, doubles);
  }

  @Test
  public void deserialiseMatrix() throws IOException {
    FileInputStream f = new FileInputStream("src/test/resources/com/github/waikatodatamining/androidmatrix/bolts.dat");

    double[][] bolts = PyMADeserialisation.deserialiseMatrix(f);

    Assertions.assertEquals(40, bolts.length);
    Assertions.assertEquals(7, bolts[0].length);
    Assertions.assertEquals(2.5, bolts[6][3]);
    Assertions.assertEquals(35.61, bolts[21][6]);
  }

  /**
   * Serialises some ints to a byte array.
   *
   * @param ints  The ints to serialise.
   * @return      The byte array.
   */
  public static byte[] serialiseInts(int ... ints) {
    int size = Integer.BYTES * ints.length;

    ByteBuffer b = ByteBuffer.allocate(size);
    b.order(ByteOrder.LITTLE_ENDIAN);

    for (int i : ints)
      b.putInt(i);

    return b.array();
  }

  /**
   * Serialises some doubles to a byte array.
   *
   * @param doubles The doubles to serialise.
   * @return        The byte array.
   */
  public static byte[] serialiseDoubles(double ... doubles) {
    int size = Double.BYTES * doubles.length;

    ByteBuffer b = ByteBuffer.allocate(size);
    b.order(ByteOrder.LITTLE_ENDIAN);

    for (double d : doubles)
      b.putDouble(d);

    return b.array();
  }

  /**
   * Creates a byte stream from some bytes.
   *
   * @param bytes Byte values as integers.
   * @return      A byte input stream.
   */
  public static InputStream byteStream(int ... bytes) {
    return byteStream(toBytes(bytes));
  }

  /**
   * Creates an input stream of the given byte array.
   *
   * @param bytes   The bytes to turn into an input stream.
   * @return        The byte input stream.
   */
  public static InputStream byteStream(byte[] bytes) {
    return new ByteArrayInputStream(bytes);
  }

  /**
   * Creates a byte input stream of the serialised ints.
   *
   * @param ints  The ints to stream.
   * @return      The input stream.
   */
  public static InputStream intStream(int ... ints) {
    return byteStream(serialiseInts(ints));
  }

  /**
   * Creates a byte input stream of the serialised doubles.
   *
   * @param doubles The doubles to stream.
   * @return        The input stream.
   */
  public static InputStream doubleStream(double ... doubles) {
    return byteStream(serialiseDoubles(doubles));
  }

  /**
   * Helper method for turning integers into a byte array.
   *
   * @param bytes   The byte values (0 <= b <= 255)
   * @return        A byte array containing the values.
   */
  public static byte[] toBytes(int ... bytes) {
    byte[] result = new byte[bytes.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = (byte) bytes[i];
    }

    return result;
  }

}
