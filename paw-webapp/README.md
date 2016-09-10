#PowerUp! 
This is the PAW project, PowerUp!

##Setting up project with IntelliJ IDEA
1. Clone the repo
2. Start up IntelliJ IDEA 
3. If you already have a project open, go to ```File -> New -> Project from existing sources...```. Otherwise select ```Import project```
4. Navigate to where you cloned the repo and choose the ```paw-webapp``` subfolder (at the same level as ```doc```)
5. Choose to ```Import project from external model``` and choose Maven
6. Leave all the options as is, also check ```Import Maven projects automatically``` and ```Create module groups for multi-Module Maven projects```
    * Optionally, select to automatically download sources and documentation (highly recommended)
7. Click next through all the following windows
8. IDEA may come up with balloons for VCS (Git), Maven auto-import, and other project features. Enable them all, there should be no errors. IDEA should automatically detect all source folders, there's no need to go to module settings for each module anymore.
9. Build the project. Either in the command line invoking ```mvn package``` or ```Build -> Make project``` in IDEA (you can also use the icon on the top right later). IDEA should automatically use JDK 8, or at least ask you if you want to set up a JDK and have version 8 preselected.

##Running the project with IDEA's Jetty runner
1. If you don't have the Jetty Runner plugin installed (or if you don't know), go to ```Help -> Find Action``` and look for Plugins.
2. Select ```Browse repositories...``` and find Jetty Runner. Jetty Integration/Jetty Server is **not** what you're looking for. Restart IDEA if necessary.
3. Go to ```Run -> Edit configurations...```. Expand Defaults and find Jetty Runner. Click the green + icon on the top left to create a new configuration of this type.
4. Specify the classes folder. This is usually the same as web app folder, changing ```.../webapp/src/main/webapp``` by ```.../webapp/target/classes```
5. Click OK. You may have to rebuild the project thie one time.
6. Go to ```Run -> Run``` or use the icon or keyboard shortcut.
7. Visit <http://localhost:8080/paw-webapp> to see the web app in action!
8. When you make changes, recompile (if needed) re-run. Tell IDEA to automatically stop and restart the server for you instead of asking every time, or you manually stopping and restarting the server.
9. PROFIT