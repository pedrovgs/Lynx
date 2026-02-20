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

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.github.pedrovgs.lynx.LynxActivity;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.model.TraceLevel;

/**
 * Activity created to show how to use Lynx.
 *
 * @author Pedro Vicente Gomez Sanchez.
 */
public class MainActivity extends AppCompatActivity {

  private static final int MAX_TRACES_TO_SHOW = 3000;
  private static final String LYNX_FILTER = "Lynx";
  private static final int SAMPLING_RATE = 200;

  private Thread logGeneratorThread;
  private boolean continueReading = true;
  private int traceCounter = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button bt_show_lynx_activity = (Button) findViewById(R.id.bt_show_lynx_activity);
    bt_show_lynx_activity.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        openLynxActivity();
      }
    });

    final Button bt_show_lynx_view = (Button) findViewById(R.id.bt_show_lynx_view);
    bt_show_lynx_view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        final View lynxView = findViewById(R.id.lynx_view);
        lynxView.setVisibility(View.VISIBLE);
      }
    });

    generateFiveRandomTracesPerSecond();
  }

  private void openLynxActivity() {
    LynxConfig lynxConfig = new LynxConfig();
    lynxConfig.setMaxNumberOfTracesToShow(MAX_TRACES_TO_SHOW)
        .setFilter(LYNX_FILTER)
        .setFilterTraceLevel(TraceLevel.DEBUG)
        .setSamplingRate(SAMPLING_RATE);

    Intent lynxActivityIntent = LynxActivity.getIntent(this, lynxConfig);
    startActivity(lynxActivityIntent);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    continueReading = false;
  }

  /**
   * Random traces generator used just for this demo application.
   */
  private void generateFiveRandomTracesPerSecond() {
    logGeneratorThread = new Thread(new Runnable() {
      @Override public void run() {
        while (continueReading) {
          int traceLevel = traceCounter % 6;
          switch (traceLevel) {
            case 0:
              Log.d("Lynx", traceCounter + " - Debug trace generated automatically");
              break;
            case 1:
              Log.w("Lynx", traceCounter + " - Warning trace generated automatically");
              break;
            case 2:
              Log.e("Lynx", traceCounter + " - Error trace generated automatically");
              break;
            case 3:
              Log.wtf("Lynx", traceCounter + " - WTF trace generated automatically");
              break;
            case 4:
              Log.i("Lynx", traceCounter + " - Info trace generated automatically");
            default:
              Log.v("Lynx", traceCounter + " - Verbose trace generated automatically");
          }
          traceCounter++;
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });
    logGeneratorThread.start();
  }
}
