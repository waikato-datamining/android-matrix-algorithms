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
 * PreprocessingStages.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Class representing an ordered series of preprocessing stages that
 * get applied to an input feature-set in turn.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class PreprocessingStages
  extends AbstractAlgorithm {

    /** The stages of preprocessing to apply. */
    protected AbstractAlgorithm[] m_Stages;

    /**
     * Initializes the algorithm.
     *
     * @param stream the stream to read the setup (eg matrices) from
     */
    public PreprocessingStages(InputStream stream) {
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
        // Deserialise the number of stages
        int numStages = PyMADeserialisation.deserialiseInts(stream, 1)[0];

        // Create the stages array
        m_Stages = new AbstractAlgorithm[numStages];

        // Deserialise each stage
        for (int i = 0; i < numStages; i++) {
          // Read the stage name
          String stageName = PyMADeserialisation.deserialiseString(stream);

          // Reflect the class for the stage
          Class<AbstractAlgorithm> klass = (Class<AbstractAlgorithm>)
                this.getClass().getClassLoader().loadClass(
                      this.getClass().getPackage().getName() + "." + stageName
                );

          // Instantiate the stage
          m_Stages[i] = klass.getConstructor(InputStream.class).newInstance(stream);
        }
      } catch (IOException | ClassNotFoundException | NoSuchMethodException |
            InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Error initializing from stream", e);
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
      // Apply the data to each stage in turn
      for (AbstractAlgorithm stage : m_Stages)
        data = stage.apply(data);

      return data;
    }
}
