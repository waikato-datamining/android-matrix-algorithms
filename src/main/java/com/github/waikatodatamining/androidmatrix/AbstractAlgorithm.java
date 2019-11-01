/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
  implements Algorithm, Serializable {

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
}
