package SnakePkg;

//Java program to play an Audio
//file using Clip Object
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {

 // to store current position
 public Long currentFrame;
 public Clip clip;
 
 private int count;
 private Runnable once;
 
 private final static Runnable NOP = new Runnable () {
	    public void run () {
	        // Do nothing
	    }
	};
  
 // current status of clip
 String status;
  
 AudioInputStream audioInputStream;
 static String filePath;

public Audio(int d)throws Exception{
   count = d;
   // create AudioInputStream object
     audioInputStream = AudioSystem.getAudioInputStream(new File("C:/Snake/Sounds/snek.wav").getAbsoluteFile());
      
     // create clip reference
     clip = AudioSystem.getClip();
      
     // open audioInputStream to the clip
     clip.open(audioInputStream);
      
     clip.loop(Clip.LOOP_CONTINUOUSLY);
     
     once = new Runnable () {
         public void run () {
             init();
             once = NOP;
         }
     };
}

 // constructor to initialize streams and clip
 public Audio() throws Exception {
     // create AudioInputStream object
     audioInputStream = AudioSystem.getAudioInputStream(new File("C:/Snake/Sounds/snek.wav").getAbsoluteFile());
      
     // create clip reference
     clip = AudioSystem.getClip();
      
     // open audioInputStream to the clip
     clip.open(audioInputStream);
      
     clip.loop(Clip.LOOP_CONTINUOUSLY);
     
     if (count == 0) {
          count++;
          init();
     }
 }

 public void init() {
     try {
         Audio audioPlayer = new Audio();
          
         audioPlayer.play();
     } 
      
     catch (Exception ex) {
         System.out.println("Error with playing sound.");
         ex.printStackTrace();
      
       }
 }
  
 // Method to play the audio
 public void play() {
     //start the clip
     clip.loop(Clip.LOOP_CONTINUOUSLY);
      
 }
  
 // Method to pause the audio
 public void pause() {
     this.currentFrame = 
     this.clip.getMicrosecondPosition();
     clip.stop();
 }
  
 // Method to resume the audio
 public void resumeAudio() throws Exception {
     this.play();
 }
}