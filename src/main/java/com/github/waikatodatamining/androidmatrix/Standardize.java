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
 * Standardize.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;

/**
 * Standardizes the attributes of the data to mean 0 and standard
 * deviation 1.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class Standardize
  extends AbstractAlgorithm
  implements InvertibleAlgorithm {

  /** The means of the columns to standardize to. */
  protected double[] m_ColumnMeans;

  /** The standard deviations of the columns to standardize to. */
  protected double[] m_ColumnStdDevs;

  /**
   * Initializes the algorithm.
   *
   * @param stream the stream to read the setup (eg matrices) from
   */
  public Standardize(InputStream stream) {
    super(stream);
  }

  /**
   * Configures the algorithm with the data read from the stream.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  @Override
  protected void initialize(InputStream stream) {
    // Deserialise the matrices from the state
    try {
      m_ColumnMeans = PyMADeserialisation.deserialiseOneDimensionalMatrix(stream);
      m_ColumnStdDevs = PyMADeserialisation.deserialiseOneDimensionalMatrix(stream);
    } catch (IOException ioe) {
      throw new RuntimeException("Error initializing from stream", ioe);
    }

    // Make sure the two matrices are the same length
    if (m_ColumnMeans.length != m_ColumnStdDevs.length)
      throw new RuntimeException("Received matrices of different lengths " +
        "(" + m_ColumnMeans.length + " means, " +
        m_ColumnStdDevs.length + " standard deviations)");
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
    // Check the data
    ensureDataLength(data);

    // Generate the result data
    double[] result = new double[data.length];
    for (int i = 0; i < data.length; i++)
      result[i] = (data[i] - m_ColumnMeans[i]) / m_ColumnStdDevs[i];

    return result;
  }

  @Override
  public double[] applyInverse(double[] data) throws Exception {
    // Check the data
    ensureDataLength(data);

    // Generate the result data
    double[] result = new double[data.length];
    for (int i = 0; i < data.length; i++)
      result[i] = data[i] * m_ColumnStdDevs[i] + m_ColumnMeans[i];

    return result;
  }

  /**
   * Makes sure the data to apply and applyInverse is the right size
   * for the state matrices.
   *
   * @param data                The data to check.
   * @throws RuntimeException   If the data is the wrong size.
   */
  protected void ensureDataLength(double[] data) throws RuntimeException {
    // Make sure the data is the same size as the state matrices
    if (data.length != m_ColumnMeans.length)
      throw new RuntimeException("Data size doesn't match state size " +
            "(" + data.length + " columns for " +
            m_ColumnMeans.length + " state columns)");
  }
}
