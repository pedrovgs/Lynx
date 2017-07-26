Lynx [![Build Status](https://travis-ci.org/pedrovgs/Lynx.svg?branch=master)](https://travis-ci.org/pedrovgs/Lynx) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.pedrovgs/lynx/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.pedrovgs/lynx) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Lynx-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1552)
====

Are you bored of connect your device to your computer to know what's happening inside your app? If you hate it, this is going to be your favorite library. Shake your phone, press a button or add a ``LynxView`` to your layouts and you'll see what Andoird logcat is printing :)

Lynx is an Android library created to show a custom view with all the information logcat is printing, different traces of different levels will be rendererd to show from log messages to your application exceptions. You can filter this traces (using regular expressions if you want), share your logcat to other apps, configure the max number of traces to show or the sampling rate used by the library. The min Api Level supported is 10.

Screenshots
-----------

![Demo Screenshot][1]

Usage
-----

To use Lynx Android library and get your logcat inside your app you **can use different approaches**:

* 1. Start ``LynxActivity`` using a ``LynxConfig`` object.

```java

private void openLynxActivity() {
    LynxConfig lynxConfig = new LynxConfig();
    lynxConfig.setMaxNumberOfTracesToShow(4000)
        .setFilter("WTF");

    Intent lynxActivityIntent = LynxActivity.getIntent(this, lynxConfig);
    startActivity(lynxActivityIntent);
  }

```

* 2. Configure ``LynxShakeDetector`` to start ``LynxActivity`` if you **shake your phone**.

```java

public class YourApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    LynxShakeDetector lynxShakeDetector = new LynxShakeDetector(this);
    lynxShakeDetector.init();
  }
}

```

* 3. Add ``LynxView`` **to your layouts** and configure it as you wish.

```xml

<com.github.pedrovgs.lynx.LynxView
      xmlns:lynx="http://schemas.android.com/apk/res-auto"
      android:layout_width="match_parent"
      android:layout_height="@dimen/lynx_view_height"
      lynx:filter="Lynx"
      lynx:max_traces_to_show="1500"
      lynx:text_size="12sp"/>

```

You can **provide different configurations based on styleable attributes**:

* Filter to apply by default: ``lynx:filter="Lynx"``
* Max number of traces to show inside LynxView: ``lynx:max_traces_to_show=3000``
* Text size used to render traces inside LynxView: ``lynx:text_size="12sp``
* Sampling rate used to read from the application log: ``lynx:sampling_rate=200``

To be able to show LynxActivity shaking your phone or starting it programatically you'll have to add LynxActivity to your AndroidManifest.

```xml

<activity android:name="com.github.pedrovgs.lynx.LynxActivity"/>

```

If you have to support applications based on Android 2.X you'll have to add ``READ_LOG`` permission to your AndroidManifest. **This is not needed for newer Android versions.**

```xml

<uses-permission android:name="android.permission.READ_LOGS"/>

```


Add it to your project
----------------------


Add Lynx dependency to your build.gradle

```groovy

dependencies{
    compile 'com.github.pedrovgs:lynx:1.1.0'
}

```

Or add Lynx as a new dependency inside your pom.xml

```xml

<dependency>
    <groupId>com.github.pedrovgs</groupId>
    <artifactId>lynx</artifactId>
    <version>1.0.7</version>
    <type>aar</type>
</dependency>

```


Do you want to contribute?
--------------------------

I'd like to improve this library with your help, there are some new features to implement waiting for you ;)

* Play/Pause LynxView.
* Provide a custom UI based on styles.
* Any cool feature you can imagine!

Libraries used in this project
------------------------------

* [Renderers] [3]
* [Seismic] [4]
* [Robolectric] [5]
* [JUnit] [6]
* [Mockito] [7]



Developed By
------------

* Pedro Vicente Gómez Sánchez - <pedrovicente.gomez@gmail.com>

<a href="https://twitter.com/pedro_g_s">
  <img alt="Follow me on Twitter" src="https://image.freepik.com/iconos-gratis/twitter-logo_318-40209.jpg" height="60" width="60"/>
</a>
<a href="https://es.linkedin.com/in/pedrovgs">
  <img alt="Add me to Linkedin" src="https://image.freepik.com/iconos-gratis/boton-del-logotipo-linkedin_318-84979.png" height="60" width="60"/>
</a>

License
-------

    Copyright 2015 Pedro Vicente Gómez Sánchez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: ./art/screenshot_demo_1.gif
[2]: https://play.google.com/store/apps/details?id=com.tuenti.messenger
[3]: https://github.com/pedrovgs/Renderers
[4]: https://github.com/square/seismic
[5]: https://github.com/robolectric/robolectric
[6]: https://github.com/junit-team/junit
[7]: https://github.com/mockito/mockito
[8]: https://github.com/pedrovgs
[10]: https://play.google.com/store/apps/details?id=fm.rushmore.mainapp
