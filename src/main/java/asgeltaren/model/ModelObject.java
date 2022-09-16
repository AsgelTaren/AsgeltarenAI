package asgeltaren.model;

public abstract class ModelObject {

	// Input and output pins
	protected Pin[] pins;

	protected ModelObject(int pinsNb) {
		this.pins = new Pin[pinsNb];
	}

	public abstract void feed();

	public abstract void backProp();

	public abstract int paramsAmount();

	public abstract Pin defaultInput();

	public abstract Pin defaultOutput();

	// Pins management

	public Pin getPinAt(int index) {
		return pins[index];
	}

	public int indexOfPin(Pin p) {
		for (int i = 0; i < pins.length; i++) {
			if (pins[i] == p) {
				return i;
			}
		}
		return -1;
	}

	public Pin[] getPins() {
		return pins;
	}

}
