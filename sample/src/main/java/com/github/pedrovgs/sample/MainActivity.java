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

package com.github.pedrovgs.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import com.github.pedrovgs.lynx.LynxActivity;
import com.github.pedrovgs.lynx.LynxConfig;

public class MainActivity extends ActionBarActivity {

  private static final int MAX_TRACES_TO_SHOW = 200;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button bt_show_logcat_view = (Button) findViewById(R.id.bt_show_lynx_view);
    bt_show_logcat_view.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        Context context = MainActivity.this;
        LynxConfig lynxConfig = new LynxConfig();
        lynxConfig.withMaxNumberOfTracesToShow(MAX_TRACES_TO_SHOW);
        Intent lynxActivityIntent = LynxActivity.getIntent(context, lynxConfig);
        startActivity(lynxActivityIntent);
      }
    });
  }
}
