package osuUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OsuBeatmap {
	
	/**
	 * @deprecated <br>
	 * This main function is used for testing the Class
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("res/APG550 - NEVERLAND (Natsu) [Advanced].osu");
		
		OsuBeatmap beatmap = null;
		
		try {
			beatmap = new OsuBeatmap(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(beatmap != null){
			for(String key : beatmap.data.keySet()){
				System.out.println(key);
				Map<String,String> map = beatmap.data.get(key);
				if(map != null){
					for(String subKey : map.keySet()){
						System.out.println("\t"+subKey+":"+map.get(subKey));
					}
				}
			}
		}
	}
	
	/**
	 * Stores the beats of the song. <br>
	 * Contains mpb (millisecond per beat)
	 * and offset (startong time in which
	 * the mpb applies to.
	 * @author L2k-nForce
	 */
	public static class Beats {
		public double mpb, offset;

		public Beats(double mpb, double offset) {
			super();
			this.mpb = mpb;
			this.offset = offset;
		}
	}
	
	/**
	 * Stores HitCircle data <br>
	 * Contains x, y, time
	 * @author L2k-nForce
	 */
	public static class HitCircle {
		public int x, y, time;

		public HitCircle(int x, int y, int time) {
			super();
			this.x = x;
			this.y = y;
			this.time = time;
		}

		@Override
		public String toString() {
			return "HitCircle [x=" + x + ", y=" + y + ", time=" + time + "]";
		}
		
	}
	
	public ArrayList<HitCircle> hitCircles = new ArrayList<HitCircle>();
	public ArrayList<Beats> beats = new ArrayList<OsuBeatmap.Beats>();
	public Map<String,Map<String,String>> data = new TreeMap<String, Map<String,String>>();
	
	/**
	 * Open Osu Beatmap file and stores the file data
	 * inside the object.
	 * @param beatmapFile
	 * @throws IOException
	 */
	public OsuBeatmap(File beatmapFile) throws IOException {
		this(new FileInputStream(beatmapFile));
	}
	
	/**
	 * Read Osu Beatmap (file)inputStream and stores the
	 * data inside the object.
	 * @param inputStream
	 * @throws IOException
	 */
	public OsuBeatmap(InputStream inputStream) throws IOException {
		String fileContent = "";
		int b;
		try {
			while((b = inputStream.read()) != -1){
				fileContent += (char)b;
			}
		} catch (IOException e) {
			throw e;
		} finally{
			inputStream.close();
		}

		fileContent = fileContent.replaceAll("\r\n", "\n");
		String beforeReplace;
		do{
			beforeReplace = fileContent;
			fileContent = fileContent.replaceAll("\n\n\n", "\n\n");
		}while(fileContent.length() != beforeReplace.length());
		String[] blocks = fileContent.split("\n\n");
		for(String block : blocks){
			
//			System.out.println(block.replaceAll("\n", "*"));
			
			String[] lines = block.split("\n");
			
			lines[0] = lines[0].replaceAll("\\[", "").replaceAll("\\]","");
			
			if(lines[0].compareTo("HitObjects") == 0){
				for(int c=1; c<lines.length; c++){
					String[] parts = lines[c].split(","); 
					if(parts.length > 2){
						hitCircles.add(
								new HitCircle(
										Integer.parseInt(parts[0]), 
										Integer.parseInt(parts[1]), 
										Integer.parseInt(parts[2])
										));
					}
				}
			} else if(lines[0].compareTo("TimingPoints") == 0){
				for(int c=1; c<lines.length; c++){
					String[] parts = lines[c].split(","); 
					if(parts.length > 2){
						beats.add(
								new Beats(
										Double.parseDouble(parts[0]), 
										Double.parseDouble(parts[1])));
					}
				}
			} else if(lines[0].compareTo("Events") == 0){
				Map<String, String> map = new TreeMap<String, String>();
				map.put("background", lines[2].split(",")[2].replaceAll("\"", ""));
				data.put(lines[0], map);
			} else{
				Map<String, String> map = new TreeMap<String, String>();
				for(int c=1; c<lines.length; c++){
					String[] chunk = lines[c].split(":");
					if(chunk.length >= 2){
						String key = chunk[0];
						String content = "";
						for(int c2=1; c2<chunk.length; c2++){
							content += chunk[c2];
						}
						content = content.trim();
						map.put(key, content);
					} 
				}
				data.put(lines[0], map);
			}
		}
	}

}
