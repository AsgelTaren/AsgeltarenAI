package asgeltaren.model.datasets;

public abstract class Dataset {

	protected Dataset() {

	}

	public abstract Object[] provide(int id, int type,int index);

	public abstract int length();
	
	/** 
	 * Is supposed to return the number of available elements for this category
	 * **/
	public abstract int lengthOf(int i);

}