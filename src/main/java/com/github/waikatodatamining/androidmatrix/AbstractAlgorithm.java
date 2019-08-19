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
 * AbstractAlgorithm.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Ancestor for matrix algorithms.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractAlgorithm
  implements Serializable {

  /**
   * Initializes the algorithm.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  protected AbstractAlgorithm(InputStream stream) {
    initialize(stream);
  }

  /**
   * Configures the algorithm with the data read from the stream.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  protected abstract void initialize(InputStream stream);

  /**
   * Applies the algorithm to the data.
   *
   * @param data	the data to convert
   * @return		the converted data
   * @throws Exception	if conversion fails
   */
  public abstract double[] apply(double[] data) throws Exception;
}
