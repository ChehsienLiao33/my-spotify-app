package com.example;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

// declares a class for the app
public class SpotifyLikeAppExampleCode {

  // the current audio clip
  private static Clip audioClip;

  /*
   *** IMPORTANT NOTE FOR ALL STUDENTS ***
   * 
   * This next line of code is a "path" that students will need to change in order
   * to play music on their
   * computer. The current path is for my laptop, not yours.
   * 
   * If students who do not understand whre files are located on their computer or
   * how paths work on their computer,
   * should immediately complete the extra credit on "Folders and Directories" in
   * the canvas modules.
   * 
   * Knowing how paths work is fundamental knowledge for using a computer as a
   * technical person.
   * 
   * The play function in this example code plays one song. Once student change
   * the directoryPath variable properly,
   * one of the songs should play. Students should implement their own code for
   * all the functionality in the assignment.
   * 
   * Students can use, and find their own music. You do not have to use or listen
   * to my example music! Have fun!
   * 
   * Students who do not know what a path is on their computers, and how to use a
   * path, are often unable complete
   * this assignment succesfullly. If this is your situation, please complete the
   * extra credit on Folders Path,
   * and Directories. Also, please do google and watch youtube videos if that is
   * helpful too.
   * 
   * Thank you! -Gabriel
   * 
   */

  private static String directoryPath = "/Users/youjia/Documents/GitHub/my-spotify-app/my-spotity-app/src/main/java/com/example/";

  private static Song[] recentPlayedSongs = new Song[0];

  // "main" makes this class a java app that can be executed
  public static void main(final String[] args) {
    // reading audio library from json file
    Song[] library = readAudioLibrary();

    // create a scanner for user input
    Scanner input = new Scanner(System.in);

    String userInput = "";

    while (!userInput.equals("q")) {
      menu();

      // get input
      userInput = input.nextLine();

      // accept upper or lower case commands
      userInput = userInput.toLowerCase();

      // do something
      handleMenu(userInput, library);
    }

    // close the scanner
    input.close();
  }

  /*
   * displays the menu for the app
   */
  public static void menu() {
    System.out.println("---- My Music App ----");
    System.out.println("[H]ome");
    System.out.println("[S]earch by title");
    System.out.println("[L]ibrary");
    System.out.println("[P]lay");
    System.out.println("S[t]op playing");
    System.out.println("[Q]uit");

    System.out.println("");
    System.out.print("Enter q to Quit:");
  }

  /*
   * handles the user input for the app
   */
  public static void handleMenu(String userInput, Song[] library) {
    switch (userInput) {
      case "h":
        System.out.println("-- Songs you have played recently: --");
        printLibrary(recentPlayedSongs);
        System.out.println("-------------------------------------");
        break;
      case "s":

        // Create a new scanner for song name input
        Scanner inputSongNameScanner = new Scanner(System.in);
        System.out.println("Enter the title of the song you would like to play");
        String songName = inputSongNameScanner.nextLine();

        // Get the song number by name
        Integer songNumberByName = searchSongNumberByName(songName, library);

        // Play the selected song
        play(songNumberByName - 1, library);

        // Stop playing if t is entered
        System.out.println("Enter t to stop playing");
        songName = inputSongNameScanner.nextLine();

        while (!"t".equalsIgnoreCase(songName.trim())) {
          System.out.println("Enter t to stop playing");
          songName = inputSongNameScanner.nextLine();
        }
        audioClip.stop();

        break;
      case "l":
        printLibrary(library);

        // Create a new scanner for song number input
        Scanner inputSongNumberScanner = new Scanner(System.in);
        System.out.println("Enter the number of the song you would like to play");
        String songNumberString = inputSongNumberScanner.nextLine();

        // Play the selected song
        Integer songNumber = Integer.valueOf(songNumberString);
        play(songNumber - 1, library);

        // Stop playing if t is entered
        System.out.println("Enter t to stop playing");
        songNumberString = inputSongNumberScanner.nextLine();

        while (!"t".equalsIgnoreCase(songNumberString.trim())) {
          System.out.println("Enter t to stop playing");
          songNumberString = inputSongNumberScanner.nextLine();
        }
        audioClip.stop();

        break;
      case "p":
        System.out.println("-->Play<--");

        // Play the first song in the list
        play(0, library);

        // Stop playing if t is entered
        Scanner inputStopPlaying = new Scanner(System.in);
        System.out.println("Enter t to stop playing");
        String stopPlaying = inputStopPlaying.nextLine();

        while (!"t".equalsIgnoreCase(stopPlaying.trim())) {
          System.out.println("Enter t to stop playing");
          stopPlaying = inputStopPlaying.nextLine();
        }
        audioClip.stop();

        break;
      case "q":
        System.out.println("-->Quit<--");
        break;
      default:
        break;
    }
  }

  /*
   * plays an audio file
   */
  public static void play(final Integer songNumber, Song[] library) {
    // open the audio file

    // get the filePath and open a audio file
    final Song currentSong = library[songNumber];
    final String filename = currentSong.fileName();
    final String filePath = directoryPath + "/wav/" + filename;
    final File file = new File(filePath);

    System.out.println("");
    System.out.println("-- Now playing: --");
    printSong(songNumber + 1, currentSong);
    System.out.println("");

    // stop the current song from playing, before playing the next one
    if (audioClip != null) {
      audioClip.close();
    }

    try {
      // create clip
      audioClip = AudioSystem.getClip();

      // get input stream
      final AudioInputStream in = AudioSystem.getAudioInputStream(file);

      audioClip.open(in);
      audioClip.setMicrosecondPosition(0);
      audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Song[] newArray = new Song[recentPlayedSongs.length + 1];
    for (int i = 0; i < recentPlayedSongs.length; i++) {
      newArray[i] = recentPlayedSongs[i];
    }
    newArray[newArray.length - 1] = currentSong;
    recentPlayedSongs = newArray;
  }

  public static Integer searchSongNumberByName(String songName, Song[] library) {
    Integer songNumber = 0;

    // Search the song name in library and update song number
    for (int i = 0; i < library.length; i++) {
      if (songName.replaceAll("\\s+", "").toLowerCase()
          .equals(library[i].name().replaceAll("\\s+", "").toLowerCase())) {
        songNumber = i + 1;
        break;
      }
    }

    return songNumber;
  }

  // read the audio library of music
  public static Song[] readAudioLibrary() {
    // get the file path
    final String jsonFileName = "audio-library.json";
    final String filePath = directoryPath + "/" + jsonFileName;

    Song[] library = null;
    try {
      System.out.println("Reading the file " + filePath);
      JsonReader reader = new JsonReader(new FileReader(filePath));
      library = new Gson().fromJson(reader, Song[].class);
    } catch (Exception e) {
      System.out.printf("ERROR: unable to read the file %s\n", filePath);
      System.out.println();
    }

    return library;
  }

  public static void printSong(int songId, Song currentSong) {
    System.out
        .println(songId + ". " + currentSong.name() + ", " + currentSong.artist() + ", " + currentSong.fileName());
  }

  public static void printLibrary(Song[] library) {
    for (int i = 0; i < library.length; i++) {
      Song currentSong = library[i];
      printSong(i + 1, currentSong);
    }
  }
}
