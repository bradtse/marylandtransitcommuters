package dataobjects;

import org.json.JSONArray;

public class Direction {
	
	private JSONArray rawData;
	private JSONArray prettyData;
	
	/**
	 * Constructor
	 * @param data The direction data returned by the server
	 */
	public Direction(JSONArray data) {
		this.rawData = data;
	}
	
}
