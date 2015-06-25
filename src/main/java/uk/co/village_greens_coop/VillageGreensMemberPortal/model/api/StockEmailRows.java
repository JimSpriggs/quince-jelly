package uk.co.village_greens_coop.VillageGreensMemberPortal.model.api;

import java.util.List;

public class StockEmailRows {
	
	private List<StockEmailRow> data;

	public StockEmailRows(List<StockEmailRow> stockEmailRows) {
		this.data = stockEmailRows;
	}
	
	public List<StockEmailRow> getStockEmailRows() {
		return data;
	}

	public void setStockEmailRows(List<StockEmailRow> stockEmailRows) {
		this.data = stockEmailRows;
	}
	
}
