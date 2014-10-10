package be.trojkasoftware.android.touch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TouchData {
	public TouchData() {
		this(2);
	}
	
	public TouchData(int historySize) {
		historyPosXMove = new ArrayList();
		historyPosYMove = new ArrayList();
		mHistorySize = historySize;
	}
	
	public static int INVALID = Integer.MIN_VALUE;
	
	public int posXDown;
	public int posYDown;
	
	public void pushMove(int X, int Y) {
		historyPosXMove.add(0, X);
		historyPosYMove.add(0, Y);

		if(historyPosXMove.size() > mHistorySize) {
			historyPosXMove.remove(historyPosXMove.size() - 1);
		}
		if(historyPosYMove.size() > mHistorySize) {
			historyPosYMove.remove(historyPosYMove.size() - 1);
		}
	}
	
	public int getMoveX(int index) {
		if (historyPosXMove.size() < index + 1) {
			return historyPosXMove.get(historyPosXMove.size() - 1);
		}
		return historyPosXMove.get(index);
	}
	
	public int getMoveY(int index) {
		if (historyPosYMove.size() < index + 1) {
			return historyPosYMove.get(historyPosYMove.size() - 1);
		}
		return historyPosYMove.get(index);
	}
	
	public boolean isValid() {
		return ((posXUp != INVALID) || (posYUp != INVALID));
	}
	
	public int posXUp = INVALID;
	public int posYUp = INVALID;
	public Map<Integer, Integer> posXOnPointerDown = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> posYOnPointerDown = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> posXOnPointerUp = new HashMap<Integer, Integer>();
	public Map<Integer, Integer> posYOnPointerUp = new HashMap<Integer, Integer>();

	private int mHistorySize;
	//public int lastPosXMove;
	private List<Integer> historyPosXMove;
	//public int lastPosYMove;
	private List<Integer> historyPosYMove;
}
