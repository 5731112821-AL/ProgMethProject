package osuUtilities;

import java.io.File;
import java.io.IOException;

import osuUtilities.Tools.BinaryFileInputStream;

public class OsuReplay {

	public static void main(String[] args) {

		File replayFile = new File(
				"res/BobbyL2k - APG550 - NEVERLAND [Advanced] (2015-12-11) Osu.osr");
		if (replayFile.exists()) {
			try {
				OsuReplay replay = new OsuReplay(replayFile);
				System.out.println(replay);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class GameMode {
		public static final int STANDARD = 0;
		public static final int TAIKO = 1;
		public static final int CATCH_THE_BEAT = 2;
		public static final int MANIA = 3;
		public static final String[] names = { "Standard", "Taiko",
				"Catch the Beat", "Mania" };
	}

	static class HitData {
		public static final int S300 = 0, S100 = 1, S50 = 2, SGekis = 3,
				SKatus = 4, SMiss = 5;
		public static final String[][] names = {
			{"300s","100s","50s","Gekis"	,"Katus","misses"}, // Standard
			{"300s","150s",""	,""			,""		,"misses"},	// Taiko
			{"300s","100s","50s",""			,""		,"misses"},	// Catch
			{"300s","200s","50s","Max 300s"	,"100s"	,"misses"}	// Mania
		};
	}

	public int gameMode = -1;
	public int gameVersion = -1;
	public String beatmapMD5Hash;
	public String playerName;
	public short[] hitData = new short[6];
	private String replayHash;
	private int totalScore;
	private short maxCombo;
	private boolean fullCombo;
	private int modsUsed;
	private String lifeBar;
	private long timestamp;
	private int replayDataSize;

	public OsuReplay(File replayFile) throws IOException {
		BinaryFileInputStream stream = new BinaryFileInputStream(replayFile, false);
		
		gameMode = stream.readByte();
		gameVersion = stream.readInteger();
		beatmapMD5Hash = stream.readString();
		playerName = stream.readString();
		replayHash = stream.readString();
		for (int c = 0; c < hitData.length; c++)
			hitData[c] = stream.readShort();
		totalScore = stream.readInteger();
		maxCombo = stream.readShort();
		fullCombo = (stream.readByte()) != 0x00;
		modsUsed = stream.readInteger();
		lifeBar = stream.readString();
		timestamp = stream.readLong();
		replayDataSize = stream.readInteger();
		
		stream.close();
	}

	private String genLine(String name, String value){
		return name + " : " + value + "\n";
	}
	private String genLine(String name, long value){
		return name + " : " + value + "\n";
	}
	private String genLine(String name, boolean value){
		return name + " : " + (value?"Yes":"No") + "\n";
	}
	
	@Override
	public String toString() {
		String result = "\n";

		if (gameMode > -1 && gameMode < GameMode.names.length)
			result += genLine("GameMode", GameMode.names[gameMode]);
		else
			result += genLine("GameMode", "Unknown");

		result += genLine("Game Version", gameVersion);

		result += genLine("Beatmap MD5 hash", beatmapMD5Hash);

		result += genLine("Player", playerName);
		
		result += genLine("Replay Hash", replayHash);
		
		for(int c=0; c<hitData.length; c++){
			result += genLine(HitData.names[gameMode][c], hitData[c]);
		}

		result += genLine("totalScore",totalScore);
		
		result += genLine("maxCombo",maxCombo);
		
		result += genLine("fullCombo",fullCombo);
		
		result += genLine("modsUsed",modsUsed);
		
		result += genLine("lifeBar",lifeBar);
		
		result += genLine("timestamp",timestamp);
		
		result += genLine("replayDataSize",replayDataSize);

		return super.toString() + result;
	}

}
