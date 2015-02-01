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

package com.github.pedrovgs.lynx.model;

/**
 * Logcat trace representation. All traces contains a message and a TraceLevel assigned.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class Trace {

  private final TraceLevel level;
  private final String message;

  public Trace(TraceLevel level, String message) {
    this.level = level;
    this.message = message;
  }

  public TraceLevel getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Trace)) {
      return false;
    }

    Trace trace = (Trace) o;
    return level == trace.level && message.equals(trace.message);
  }

  @Override public int hashCode() {
    int result = level.hashCode();
    result = 31 * result + message.hashCode();
    return result;
  }

  @Override public String toString() {
    return "Trace{" +
        "level=" + level +
        ", message='" + message + '\'' +
        '}';
  }
}
