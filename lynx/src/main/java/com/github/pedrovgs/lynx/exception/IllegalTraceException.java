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

package com.github.pedrovgs.lynx.exception;

/**
 * Custom exception created to notify when a Trace object is created with an invalid source. Review
 * Trace.fromString method to know the exact format of a Trace in the String representation needed
 * to avoid this exception.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class IllegalTraceException extends Exception {

  public IllegalTraceException(String detailMessage) {
    super(detailMessage);
  }
}
