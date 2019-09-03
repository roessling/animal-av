package htdptl.facade;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The batch modus produces animations for each scm, rkt or ss file in given
 * source directory and stores the animations in the given target directory
 * 
 */
public class Batch implements Runnable {

  public int              i;
  public int              n;
  private File            in;
  private File            out;
  private volatile Thread thread;
  private String          currentFileName = "";
  private int[]           steps;
  private File[]          fileArray;

  public Batch(File in, File out) {
    this.in = in;
    this.out = out;
  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop() {
    thread = null;
  }

  public void process(File file, File out) {
    currentFileName = file.getName();
    String key = file.getName();
    key = key.substring(0, key.lastIndexOf('.'));
    String content = Util.getFileContents(file);
    Facade facade = new Facade(key);

    try {
      facade.input(content);
      if (steps != null) {
        steps[i] = facade.animate();
      } else {
        facade.animate();
      }
      File outFile = new File(out + "/" + key + ".asu");
      System.out.println(outFile);
      FileWriter fw = new FileWriter(outFile);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(facade.getScriptCode());
      bw.close();
    } catch (NoExpressionsException e) {
      e.printStackTrace();
    } catch (TraceTooLargeException e) {
      steps[i] = -1;
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void run() {
    i = 0;
    if (in.isFile()) {
      process(in, out);
    } else {

      fileArray = in.listFiles(new FileFilter() {
        public boolean accept(File f) {
          return f.getName().toLowerCase().endsWith(".scm")
              || f.getName().toLowerCase().endsWith(".rkt")
              || f.getName().toLowerCase().endsWith(".ss");
        }
      });

      n = fileArray.length;

      steps = new int[n];

      for (i = 0; i < fileArray.length; i++) {
        Thread thisThread = Thread.currentThread();
        if (thread != thisThread) {
          return;
        }
        if (fileArray[i].isDirectory()) {
          continue;
        }
        process(fileArray[i], out);
      }
    }

  }

  public int totalFiles() {
    return n;
  }

  public int processedFiles() {
    return i;
  }

  public String getCurrentFileName() {
    return currentFileName;
  }

  public int[] getLog() {
    return steps;
  }

  public File[] getFiles() {
    return fileArray;
  }

}
