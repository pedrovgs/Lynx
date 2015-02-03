/*
 * Copyright (C) 2015 Pedro Vicente Gomez Sanchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pedrovgs.lynx;

import java.io.Serializable;

/**
 * Lynx configuration parameters used to open main activity. All the configuration library is
 * provided by library clients using this class.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxConfig implements Serializable {
  private static final long serialVersionUID = 293939299388293L;

  private int maxNumberOfTracesToShow = 1000;

  public LynxConfig() {

  }

  public LynxConfig withMaxNumberOfTracesToShow(int maxNumberOfTracesToShow) {
    if (maxNumberOfTracesToShow <= 0) {
      throw new IllegalArgumentException(
          "You can't use a max number of traces equals or lower than zero.");
    }

    this.maxNumberOfTracesToShow = maxNumberOfTracesToShow;
    return this;
  }

  public int getMaxNumberOfTracesToShow() {
    return maxNumberOfTracesToShow;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LynxConfig)) return false;

    LynxConfig that = (LynxConfig) o;

    if (maxNumberOfTracesToShow != that.maxNumberOfTracesToShow) return false;

    return true;
  }

  @Override public int hashCode() {
    return maxNumberOfTracesToShow;
  }

  @Override public String toString() {
    return "LynxConfig{" +
        "maxNumberOfTracesToShow=" + maxNumberOfTracesToShow +
        '}';
  }
}
