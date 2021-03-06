# Dragon Boat Racing 2021

![Dragon Boat Racing 2021 Logo](https://github.com/hardgforgifs/game/raw/master/core/assets/Title.png)
<h1 align="center">
<img src="https://img.shields.io/github/issues/hardgforgifs/Game?color=green&style=flat-square">
<img src="https://img.shields.io/github/issues-pr/hardgforgifs/Game?color=yellow&style=flat-square">
<img src="https://img.shields.io/github/stars/hardgforgifs/Game?color=red&style=flat-square">
<img src="https://img.shields.io/github/forks/hardgforgifs/Game?color=blue&style=flat-square">
</h1>

The latest and greatest racing game!

## Instructions for cloning repository
1. Download git at https://git-scm.com/downloads
2. Open cmd and go to the folder in which you want to clone the project
3. Type git clone https://github.com/hardgforgifs/Game.git

## Instructions for running tests
1. Open terminal in root directory
2. Run ``gradlew tests:test``

## Integrated testing
1. Add new running configuration in IDE
2. Set main class to ``com.randomlygenerated.tests.IntegratedTesting``
3. Run and perform testing as preferred

## Dependencies

* openjdk 8
* JUnit
* Gson

## Releasing
To release, run the following at the root directory:
``gradlew desktop:dist``

## Developing (IntelliJ IDEA)

After cloning the repo you will want to open it in the IDE.

Then you will want to open `Edit Run/Debug Configurations` available on the top bar.
Add a new `application` then you will want to configure it with the following options:

Main class: `com.hardgforgif.dragonboatracing.desktop.DesktopLauncher`  
Working directory: `complete path to cloned repo\game\desktop\src\assets`  
Use classpath of module: `game.desktop.main`  

Then select the new configuration, and you will be able to run it.

## Music used

Love Drama (Race Music) Music by <a href="/users/lymzpodcast-18852086/?tab=audio&amp;utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=audio&amp;utm_content=1333">lymzpodcast</a> from <a href="https://pixabay.com/music/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=music&amp;utm_content=1333">Pixabay</a>

Wataboi - Vibin (Menu Music) Music by <a href="/users/wataboi-12344345/?tab=audio&amp;utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=audio&amp;utm_content=1168">Wataboi</a> from <a href="https://pixabay.com/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=music&amp;utm_content=1168">Pixabay</a>

## Credits

``com.randomlygenerated.tests.UnitTests.GdxTestRunner`` is licensed under the Apache-2 License, the original repository can be found [here](https://github.com/TomGrill/gdx-testing). The original license remains commented at the top, and the source remains unchanged.