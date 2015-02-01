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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity created to show a LynxView with "match_parent" configuration for LynxView
 * "layout_height" and "layout_width". To configure LynxView and all the information to show use
 * Activity extras.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class LynxActivity extends Activity {

  private static final String LYNX_CONFIG_EXTRA = "extra_lynx_config";

  public static Intent getIntent(Context context, LynxConfig lynxConfig) {
    Intent intent = new Intent(context, LynxActivity.class);
    intent.putExtra(LYNX_CONFIG_EXTRA, lynxConfig);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lynx_activity);
    LynxConfig lynxConfig = getLynxConfig();
    configureLynxView(lynxConfig);
  }

  private LynxConfig getLynxConfig() {
    Bundle extras = getIntent().getExtras();
    LynxConfig lynxConfig = new LynxConfig();
    if (extras != null) {
      lynxConfig = (LynxConfig) extras.getSerializable(LYNX_CONFIG_EXTRA);
    }
    return lynxConfig;
  }

  private void configureLynxView(LynxConfig lynxConfig) {
    LynxView lynxView = (LynxView) findViewById(R.id.lynx_view);
    lynxView.setLynxConfig(lynxConfig);
  }
}
