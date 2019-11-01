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
 * Log.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;

/**
 * Returns the logarithm (of a given base) to the offset data.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class Log
  extends AbstractAlgorithm {

  // The base of the logarithm
  protected double m_Base;

  // The base conversion factor (1 / ln(m_Base))
  protected double m_BaseConversionFactor;

  // The offset
  protected double m_Offset;

  public Log(InputStream stream) {
    super(stream);
  }

  @Override
  protected void initialize(InputStream stream) {
    try {
      // Log's state is 3 doubles
      double[] state = PyMADeserialisation.deserialiseDoubles(stream, 3);

      // Unpack the state
      m_Base = state[0];
      m_BaseConversionFactor = state[1];
      m_Offset = state[2];
    } catch (IOException ioe) {
      throw new RuntimeException("Error initializing from stream", ioe);
    }
  }

  @Override
  public double[] apply(double[] data) throws Exception {
    // Create the result buffer
    double[] result = new double[data.length];

    // Apply the algorithm to each element
    for (int i = 0; i < data.length; i++) {
      // Apply the offset
      result[i] = data[i] + m_Offset;

      // Log is undefined if the value is zero/negative
      if (result[i] <= 0)
        throw new RuntimeException("Logarithm is undefined for zero/negative values");

      // Apply the logarithm
      result[i] = StrictMath.log(result[i]) * m_BaseConversionFactor;
    }

    return result;
  }
}
