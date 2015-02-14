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
 * Abstraction created to represent the application main thread. This interface is used to send
 * messages from a background thread to the UI thread. The usage of interfaces to abstract the
 * execution context is really useful for testing.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public interface MainThread {

  void post(Runnable runnable);
}
