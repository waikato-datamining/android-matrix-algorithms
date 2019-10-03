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
 * SIMPLS.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of the SIMPLS algorithm.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class SIMPLS
  extends AbstractAlgorithm {

  // The loadings.
  protected double[][] m_W;

  /**
   * Initializes the algorithm.
   *
   * @param stream the stream to read the setup (eg matrices) from
   */
  public SIMPLS(InputStream stream) {
    super(stream);
  }

  /**
   * Configures the algorithm with the data read from the stream.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  @Override
  protected void initialize(InputStream stream) {
    try {
      m_W = PyMADeserialisation.deserialiseMatrix(stream);
    } catch (IOException ioe) {
      throw new RuntimeException("Error initializing from stream", ioe);
    }
  }

  /**
   * Applies the algorithm to the data.
   *
   * @param data	the data to convert
   * @return		the converted data
   * @throws Exception	if conversion fails
   */
  @Override
  public double[] apply(double[] data) throws Exception {
    // Must have compatible size with matrix
    if (data.length != m_W.length)
      throw new RuntimeException("Data size does not match loadings size");

    // Create the result buffer
    int resultLength = m_W[0].length;
    double[] result = new double[resultLength];

    // Perform the matrix multiplication
    for (int i = 0; i < resultLength; i++) {
      result[i] = 0.0;
      for (int j = 0; j < data.length; j++) {
        result[i] += data[j] * m_W[j][i];
      }
    }

    return result;
  }
}