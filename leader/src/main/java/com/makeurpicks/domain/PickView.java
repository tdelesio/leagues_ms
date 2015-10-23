package com.makeurpicks.domain;

public class PickView extends AbstractModel {

	private PickStatus pickOutcome;
	
	public enum PickStatus {WINNER, LOSER, UNPLAYED}

	public PickStatus getPickOutcome() {
		return pickOutcome;
	}

	public void setPickOutcome(PickStatus pickOutcome) {
		this.pickOutcome = pickOutcome;
	};
	
	
}
