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

import com.github.pedrovgs.lynx.exception.IllegalTraceException;

/**
 * Logcat trace representation. All traces contains a message and a TraceLevel assigned.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class Trace {

  private static final char TRACE_LEVEL_SEPARATOR = '/';
  private static final int END_OF_DATE_INDEX = 18;
  private static final int START_OF_MESSAGE_INDEX = 21;
  public static final int MIN_TRACE_SIZE = 21;
  public static final int TRACE_LEVEL_INDEX = 19;

  private final TraceLevel level;
  private final String message;

  public Trace(TraceLevel level, String message) {
    this.level = level;
    this.message = message;
  }

  /**
   * Factory method used to create a Trace instance from a String. The format of the input string
   * have to be something like: "02-07 17:45:33.014 D/Any debug trace"
   *
   * @param logcatTrace the logcat string
   * @return a new Trace instance
   * @throws IllegalTraceException if the string argument is an invalid string
   */
  public static Trace fromString(String logcatTrace) throws IllegalTraceException {
    if (logcatTrace == null
        || logcatTrace.length() < MIN_TRACE_SIZE
        || logcatTrace.charAt(20) != TRACE_LEVEL_SEPARATOR) {
      throw new IllegalTraceException(
          "You are trying to create a Trace object from a invalid String. Your trace have to be "
              + "something like: '02-07 17:45:33.014 D/Any debug trace'.");
    }
    TraceLevel level = TraceLevel.getTraceLevel(logcatTrace.charAt(TRACE_LEVEL_INDEX));
    String date = logcatTrace.substring(0, END_OF_DATE_INDEX);
    String message = logcatTrace.substring(START_OF_MESSAGE_INDEX, logcatTrace.length());
    return new Trace(level, date + " " + message);
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
    return "Trace{" + "level=" + level + ", message='" + message + '\'' + '}';
  }
}
