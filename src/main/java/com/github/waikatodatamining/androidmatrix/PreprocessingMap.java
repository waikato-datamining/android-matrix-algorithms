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
 * PreprocessingMap.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that applies preprocessing to a map of input features. Can
 * optionally specify an ordering to the inputs, and get the preprocessed
 * features in that order. Has a similar interface to {@link AbstractAlgorithm},
 * but is technically distinct as it applies to a set of inputs, not a single
 * input.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class PreprocessingMap {

  /** The map from input name to preprocessors. */
  protected Map<String, PreprocessingStages> m_Stages = new HashMap<>();

  /** The ordering of the inputs. */
  protected String[] m_Ordering;

  /**
   * Initializes the preprocessing map.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  public PreprocessingMap(InputStream stream) {
    initialize(stream);
  }

  /**
   * Configures the algorithm with the data read from the stream.
   *
   * @param stream	the stream to read the setup (eg matrices) from
   */
  protected void initialize(InputStream stream) {
    try {
      // Deserialise the number of inputs this map covers
      int numInputs = PyMADeserialisation.deserialiseInts(stream, 1)[0];

      // Create the ordering array
      m_Ordering = new String[numInputs];

      // Deserialise each input's preprocessing
      for (int i = 0; i < numInputs; i++) {
        // Deserialise the input name
        String inputName = PyMADeserialisation.deserialiseString(stream);

        // Put it in the ordering
        m_Ordering[i] = inputName;

        // Deserialise the preprocessing
        PreprocessingStages stages = new PreprocessingStages(stream);

        // Put the stages in the map
        m_Stages.put(inputName, stages);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error initializing from stream", e);
    }
  }

  /**
   * Applies the preprocessing to the data.
   *
   * @param data	the data to convert
   * @return		the converted data
   * @throws Exception	if conversion fails
   */
  public Map<String, double[]> apply(Map<String, double[]> data) throws Exception {
    return apply(data, false);
  }

  /**
   * Applies the preprocessing to the data.
   *
   * @param data	the data to convert
   * @param inverse whether to inverse-apply the preprocessing
   * @return		the converted data
   * @throws Exception	if conversion fails
   */
  public Map<String, double[]> apply(Map<String, double[]> data, boolean inverse) throws Exception {
    // Create the results map
    Map<String, double[]> result = new HashMap<>();

    // Process each input in turn
    for (String name : m_Stages.keySet()) {
      // Get the preprocessing stages for this input
      PreprocessingStages stages = m_Stages.get(name);

      // Get the data for this input
      double[] inputData = data.get(name);

      // Apply the preprocessing to the input data
      double[] resultData = inverse ? stages.applyInverse(inputData) : stages.apply(inputData);

      // Add the result to the results map
      result.put(name, resultData);
    }

    return result;
  }

  /**
   * Applies the preprocessing to the data.
   *
   * @param data	the data to convert
   * @return		the converted data
   * @throws Exception	if conversion fails
   */
  public double[][] applyOrdered(Map<String, double[]> data) throws Exception {
    return applyOrdered(data, false);
  }

    /**
     * Applies the preprocessing to the data.
     *
     * @param data	the data to convert
     * @param inverse whether to inverse-apply the preprocessing
     * @return		the converted data
     * @throws Exception	if conversion fails
     */
    public double[][] applyOrdered(Map<String, double[]> data, boolean inverse) throws Exception {
    // Apply unordered
    Map<String, double[]> unordered = apply(data, inverse);

    // Create the result array
    double[][] result = new double[m_Ordering.length][];

    // Add each result in order
    for (int i = 0; i < m_Ordering.length; i++) {
      result[i] = unordered.get(m_Ordering[i]);
    }

    return result;
  }

}
