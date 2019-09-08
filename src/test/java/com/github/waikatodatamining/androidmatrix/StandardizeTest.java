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
 * StandardizeTest.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.waikatodatamining.androidmatrix;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.FileInputStream;

/**
 * Tests the Standardize transformation.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
public class StandardizeTest {

  @Test
  public void apply() throws Exception {
    Standardize standardize = new Standardize(new FileInputStream("src/test/resources/com/github/waikatodatamining/androidmatrix/Standardize.dat"));

    double[][] bolts = PyMADeserialisation.deserialiseMatrix(new FileInputStream("src/test/resources/com/github/waikatodatamining/androidmatrix/bolts.dat"));

    double[][] expectedResponse = PyMADeserialisation.deserialiseMatrix(new FileInputStream("src/test/resources/com/github/waikatodatamining/androidmatrix/Standardize-bolts.dat"));

    for (int i = 0; i < bolts.length; i++)
      Assertions.assertArrayEquals(expectedResponse[i], standardize.apply(bolts[i]));

  }

}
