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
 * SavitzkyGolay.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of the Savitzky-Golay algorithm.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class SavitzkyGolay
  extends AbstractAlgorithm {

  // The coefficients.
  protected double[] m_Coefficients;

  /**
   * Initializes the algorithm.
   *
   * @param stream the stream to read the setup (eg matrices) from
   */
  protected SavitzkyGolay(InputStream stream) {
    super(stream);
  }

  /**
   * Configures the algorithm with the data read from the stream.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  @Override
  protected void initialize(InputStream stream) {
    // Deserialise a matrix from the state
    double[][] matrix;
    try {
      matrix = PyMADeserialisation.deserialiseMatrix(stream);
    } catch (IOException ioe) {
      throw new RuntimeException("Error initializing from stream", ioe);
    }

    // Make sure we got a matrix with only one row
    if (matrix.length > 1)
      throw new RuntimeException("Coefficient matrix for Savitzky-Golay should only" +
        " have one row, got " + matrix.length + "x" + matrix[0].length);

    // Save the one row as our coefficients
    m_Coefficients = matrix[0];
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
    // Calculate the size of the sliding window
    int windowWidth = m_Coefficients.length;

    // Calculate the number of window positions we can choose from
    int numOutputColumns = data.length - windowWidth + 1;

    // Create a buffer for the results
    double[] result = new double[numOutputColumns];

    // Perform the sliding-window convolution
    for (int i = 0; i < numOutputColumns; i++) {
      result[i] = 0.0;
      for (int c = 0; c < windowWidth; c++) {
        result[i] += m_Coefficients[c] * data[i + c];
      }
    }

    return result;
  }
}
