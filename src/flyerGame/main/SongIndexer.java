package flyerGame.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;

import osuUtilities.OsuBeatmap;

public class SongIndexer {
	
	public static class Song implements Serializable{
		private static final long serialVersionUID = -279552873668684874L;
		public String songName = "";
		public List<String> beatmapNames = new ArrayList<String>();
		public String folderPath = "";
		
		@Override
		public String toString() {
			return "Song [songName=" + songName + ", beatmapNames="
					+ beatmapNames + ", folderPath=" + folderPath + "]";
		}
	}

	private static List<Song> songs = new ArrayList<SongIndexer.Song>();
	
	public static void main(String[] args) {
		try{
			index();
			System.out.println("Songs indexed successfully");
			FileOutputStream fout = null;
			ObjectOutputStream oos = null;
			try{
				fout = new FileOutputStream("src/res/songs/songIndex.dat");
				oos = new ObjectOutputStream(fout);
				oos.writeObject(songs);
				System.out.println("Index data saved");
			}catch (IOException e){
				System.out.println("Unable to save index data");
				e.printStackTrace();
			}finally{
				if(oos != null)
					oos.close();
				if(fout != null)
					fout.close();
			}
		}catch (IOException e){
			System.out.println("Failed to index songs");
			e.printStackTrace();
		}
	}
	
	private static Map<String, Double> beatmapDiff = new TreeMap<String, Double>();
	
	private static void index() throws IOException{
		File songsDir = new File("src/res/songs");
		if(!songsDir.exists())
			throw new IOException("File doesn't exists");
		for(File songFolder : songsDir.listFiles()){
			if(songFolder.isDirectory()){
				Song song = new Song();
				song.folderPath = songFolder.getName() + "/";
//				System.out.println(song.folderPath);
				for(File file : songFolder.listFiles()){
					String fileName = file.getName();
					if(FilenameUtils.getExtension(fileName).compareTo("osu") == 0){
						OsuBeatmap beatmap = new OsuBeatmap(file);
						String songName = beatmap.data.get("General").get("AudioFilename");
						{
							String[] chunks = songName.split("\\.");
							String correctedSongName = "";
							for(int c=0;c<chunks.length-1; c++){
								correctedSongName += chunks[c]+".";
							}
							correctedSongName += "wav";
//							System.out.println(correctedSongName);
							songName = correctedSongName;
						}
						song.songName = songName;
						if(beatmap.data.get("General").get("Mode").compareTo("0") == 0){
							song.beatmapNames.add(fileName);
							beatmapDiff.put(fileName, Double.parseDouble(beatmap.data.get("Difficulty").get("OverallDifficulty")));
//							System.out.println("Added "+fileName);
						}
					}
				}
				if(song.songName.length() != 0 && song.folderPath.length() != 0 && song.beatmapNames.size() != 0){
					System.out.println(song.songName + " is indexed");
					song.beatmapNames.sort(new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							return beatmapDiff.get(o1).compareTo(beatmapDiff.get(o2));
						}
						
					});
					for(String name : song.beatmapNames){
						System.out.println(name + " " + beatmapDiff.get(name));
					}
					songs.add(song);
				}
			}
		}
	}

}
