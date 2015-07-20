package com.greg.util;

import javax.faces.model.DataModel;

public abstract class PaginationHelper {

	private int pageSize;
	private int page;

	public PaginationHelper() {
	}
	public PaginationHelper(int pageSize) {
        this.pageSize = pageSize;
	}

	public abstract int getItemsCount();
	public abstract DataModel createPageDataModel();
	public int getPageFirstItem() {
        return page * pageSize; 
	}


}
