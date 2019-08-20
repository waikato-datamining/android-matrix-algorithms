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
 * PyMADeserialisation.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Static class which provides utilities for deserialising
 * state from py-matrix-algorithms.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class PyMADeserialisation {

  /**
   * Reads a number of bytes from the stream.
   *
   * @param stream		The stream to read from.
   * @param count		The number of bytes to read.
   * @return			The array of bytes.
   * @throws IOException	If there is an error reading from the stream.
   * @throws RuntimeException	If there isn't enough data in the stream to fill the request.
   */
  public static byte[] deserialiseBytes(InputStream stream, int count) throws IOException, RuntimeException {
    // Create a buffer to store the raw data in
    byte[] buffer = new byte[count];

    // Read the data from the stream
    int bytesRead = stream.read(buffer);

    // Throw if not enough data was read
    if (bytesRead != count)
      throw new RuntimeException("Failed to read enough data from the stream");

    return buffer;
  }

  /**
   * Deserialises a number of ints from the stream.
   *
   * @param stream		The stream to read from.
   * @param count		The number of ints to read.
   * @return			The array of ints.
   * @throws IOException	If there is an error reading from the stream.
   * @throws RuntimeException	If there isn't enough data in the stream to fill the request.
   */
  public static int[] deserialiseInts(InputStream stream, int count) throws IOException, RuntimeException {
    // Calculate the number of bytes to read
    int bytesToRead = Integer.BYTES * count;

    // Deserialise the raw bytes
    byte[] intBuffer = deserialiseBytes(stream, bytesToRead);

    // Wrap the data in a ByteBuffer
    ByteBuffer converter = wrap(intBuffer);

    // Create an array to hold the ints
    int[] ints = new int[count];

    // Convert the ints into the array
    for (int i = 0; i < count; i++)
      ints[i] = converter.getInt();

    return ints;
  }

  /**
   * Deserialises a number of doubles from the stream.
   *
   * @param stream		The stream to read from.
   * @param count		The number of doubles to read.
   * @return			The array of doubles.
   * @throws IOException	If there is an error reading from the stream.
   * @throws RuntimeException	If there isn't enough data in the stream to fill the request.
   */
  public static double[] deserialiseDoubles(InputStream stream, int count) throws IOException, RuntimeException {
    // Calculate the number of bytes to read
    int bytesToRead = Double.BYTES * count;

    // Deserialise the raw bytes
    byte[] doubleBuffer = deserialiseBytes(stream, bytesToRead);

    // Wrap the data in a ByteBuffer
    ByteBuffer converter = wrap(doubleBuffer);

    // Create an array to hold the doubles
    double[] doubles = new double[count];

    // Convert the doubles into the array
    for (int i = 0; i < count; i++)
      doubles[i] = converter.getDouble();

    return doubles;
  }

  /**
   * Deserialises a matrix from the given stream.
   *
   * @param stream	The stream of serialised data.
   * @return		The matrix.
   */
  public static double[][] deserialiseMatrix(InputStream stream) throws IOException, RuntimeException {
    // Get the size of the matrix
    int[] dimensions = deserialiseInts(stream, 2);
    int numRows = dimensions[0];
    int numColumns = dimensions[1];

    // Create an array to hold the matrix data
    double[][] matrix = new double[numRows][numColumns];

    // Deserialise the matrix data
    double[] matrixData = deserialiseDoubles(stream, numRows * numColumns);

    // Format the data into a matrix
    for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
      System.arraycopy(matrixData, rowIndex * numColumns, matrix[rowIndex], 0, numColumns);
    }

    return matrix;
  }

  /**
   * Creates a wrapper for the given byte array.
   *
   * @param bytes   The array to wrap.
   * @return        The ByteBuffer wrapper.
   */
  public static ByteBuffer wrap(byte[] bytes) {
    // Wrap the array
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

    // Enforce little-endianness as standard
    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

    return byteBuffer;
  }

}
