package View;

import javafx.scene.media.AudioClip;

public class musicThread extends Thread {

    public musicThread(String s) {
        super(s);
//        public void run(){
        int s1 = 99999;
        AudioClip audio = new AudioClip(getClass().getResource(s + ".mp3").toExternalForm());
        audio.setVolume(0.5f);
        audio.setCycleCount(s1);
        audio.play();

    }
}