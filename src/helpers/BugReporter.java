package helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.prefs.Preferences;

import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.main.AnimationState;
import animal.main.AnimationWindow;

public class BugReporter {
  
  private File fileBugReportStream = null;

  public BugReporter(File fileBugReportStream) {
    this.fileBugReportStream = fileBugReportStream;
  }
  
  public String getContentOfOutputStreamFile() {
    String text = "";
    if(fileBugReportStream!=null) {
      try {
        FileInputStream fis = new FileInputStream(fileBugReportStream);
        byte[] data = new byte[(int) fileBugReportStream.length()];
        fis.read(data);
        fis.close();
        text = new String(data, "UTF-8");
      } catch (IOException e) {}
    }
    return text;
  }
  
  public void writeBugReportFileContent(File f) {
    try {
      BufferedWriter outStream = new BufferedWriter(new FileWriter(f));
      
      writeNewSection(outStream, "Generel Informations");
      writeTextWithNewLine(outStream, "Current Timestamp: "+new Date());
      writeTextWithNewLine(outStream, "Java Version: "+System.getProperty("java.version"));
      writeTextWithNewLine(outStream, "OS Name: "+System.getProperty("os.name"));
      writeTextWithNewLine(outStream, "OS Arch: "+System.getProperty("os.arch"));
      writeTextWithNewLine(outStream, "OS Version: "+System.getProperty("os.version"));
      writeTextWithNewLine(outStream, "Run as Admin: "+isAdmin());
      writeEndSection(outStream);
      
      writeNewSection(outStream, "Animal Informations");
      writeTextWithNewLine(outStream, "Animal VersionDate: "+AnimalConfiguration.getDefaultConfiguration().getVersionDate());
      writeTextWithNewLine(outStream, "Animal VersionNumber: "+AnimalConfiguration.getDefaultConfiguration().getVersionNumber());
      writeTextWithNewLine(outStream, "Animal.jar Name: "+AnimalReader.getNameOfJar());
      writeTextWithNewLine(outStream, "Animal.jar LastModifiedDate: "+AnimalReader.getLastModifiedDateFromJar());
      JarEntry latestEntry = getLatestEntry();
      if(latestEntry!=null) {
        writeTextWithNewLine(outStream, "Animal.jar LatestEntry: "+latestEntry.getName()+" {"+new Date(latestEntry.getTime())+"}");
      } else {
        writeTextWithNewLine(outStream, "Animal.jar LatestEntry: null");
      }
      writeEndSection(outStream);
      
      writeNewSection(outStream, "Player Informations");
      AnimationWindow aw = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false);
      writeTextWithNewLine(outStream, "Player Frame Title: "+aw.getAnimationWindowView().getTitle());
      writeTextWithNewLine(outStream, "Player isAnimationPlayerHooked: "+aw.isAnimationPlayerHooked());
      writeTextWithNewLine(outStream, "Player Settings getMagnification: "+aw.getMagnification());
      writeTextWithNewLine(outStream, "Player Settings getSpeed: "+aw.getSpeed());
      writeTextWithNewLine(outStream, "Player Settings isShowQuestions: "+aw.isShowQuestions());
      writeTextWithNewLine(outStream, "Player Settings isSettingsVisible: "+aw.getAnimationWindowView().isSettingsVisible());
      writeEndSection(outStream);

      writeNewSection(outStream, "Animation Informations");
      AnimationState as = aw.getAnimationState();
      Animation ani = as.getAnimation();
      String animationFrameTitle= aw.getAnimationWindowView().getTitle();
      boolean firstLoad = animationFrameTitle.equals("unnamed");
      boolean loadedFromASIP = animationFrameTitle.equals("Animal Animation: localBuffer");
      writeTextWithNewLine(outStream, "Animation loaded from AnimalScript Input Pane: "+(!firstLoad&&loadedFromASIP));
      writeTextWithNewLine(outStream, "Animation loaded as Generator: "+(!firstLoad&&!loadedFromASIP));
      writeTextWithNewLine(outStream, "Animation Title: "+ani.getTitle());
      writeTextWithNewLine(outStream, "Animation Author: "+ani.getAuthor());
      writeTextWithNewLine(outStream, "Animation NrOfSteps: "+ani.getStepCount());
      writeTextWithNewLine(outStream, "Animation CurrentStep: "+as.getStep());
      writeTextWithNewLine(outStream, "Animation AnimalScriptCode: Added it to the end of this file!");
      writeEndSection(outStream);
      
      writeNewSection(outStream, "Console Output (Only Animal)");
      writeTextWithNewLine(outStream, AnimalMainWindow.getOutputArea().getText());
      writeEndSection(outStream);

      writeNewSection(outStream, "Console Output (Systems)");
      writeTextWithNewLine(outStream, getContentOfOutputStreamFile());
      writeEndSection(outStream);
      
      writeNewSection(outStream, "Animal.jar Entries");
      LinkedList<JarEntry> list = AnimalReader.getAllEntriesInJar();
      Collections.sort(list, new Comparator<JarEntry>() {
        @Override
        public int compare(JarEntry o1, JarEntry o2) {
            return Collator.getInstance().compare(o1.getName(), o2.getName());
        }
      });
      if(!list.isEmpty()) {
        for(JarEntry entry : list) {
          writeTextWithNewLine(outStream, entry.getName());
        }
      } else {
        writeTextWithNewLine(outStream, "Entry list is empty!");
      }
      writeEndSection(outStream);
      
      writeNewSection(outStream, "Animation AnimalScriptCode");
      writeTextWithNewLine(outStream, Animal.get().getScriptInputWindow().getScriptingContent());
      writeEndSection(outStream);
      
      outStream.close();
    } catch (IOException e) {}
  }
  
  private void writeTextWithNewLine(BufferedWriter outStream, String text) throws IOException {
    outStream.write(text);
    outStream.newLine();
  }
  
  private void writeEndSection(BufferedWriter outStream) throws IOException {
    outStream.newLine();
    outStream.newLine();
    outStream.newLine();
    outStream.newLine();
  }
  
  private void writeNewSection(BufferedWriter outStream, String name) throws IOException {
    outStream.write("--------------------"+name);
    outStream.newLine();
    outStream.newLine();
  }
  
  private JarEntry getLatestEntry() {
    LinkedList<JarEntry> list = AnimalReader.getAllEntriesInJar();
    JarEntry latest = null;
    for(JarEntry entry : list) {
      if(latest==null || latest.getTime()<entry.getTime()) {
        latest = entry;
      }
    }
    return latest;
  }
  
  public static boolean isAdmin(){
    PrintStream systemErr = System.err;
    synchronized(systemErr){    // better synchroize to avoid problems with other threads that access System.err
        System.setErr(new PrintStream(new OutputStream() { @Override public void write(int i) throws IOException { } }));
        try{
            Preferences prefs = Preferences.systemRoot();
            prefs.put("foo", "bar"); // SecurityException on Windows
            prefs.remove("foo");
            prefs.flush(); // BackingStoreException on Linux
            return true;
        }catch(Exception e){
            return false;
        }finally{
            System.setErr(systemErr);
        }
    }
  }

}
