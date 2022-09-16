package asgeltaren.model;

import asgeltaren.math.Tensor;

public class Pin {

	// Data
	private Tensor data;

	// Gradient
	private Tensor gradient;

	// Reference to the associated model object
	private ModelObject obj;

	// Specify whether this pin works as an input
	private boolean isInput;

	// Link
	private Link link;

	public Pin(ModelObject obj, boolean isInput) {
		this.obj = obj;
		this.isInput = isInput;
	}

	public void setData(Tensor data) {
		this.data = data;
	}

	public Tensor getData() {
		return data;
	}

	public void setGradient(Tensor gradient) {
		this.gradient = gradient;
	}

	public Tensor getGradient() {
		return gradient;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Link getLink() {
		return link;
	}

	public boolean isInput() {
		return isInput;
	}

	public ModelObject getModelObject() {
		return obj;
	}

	@Override
	public String toString() {
		return obj + "|" + (isInput ? "OutputPin" : "InputPin");
	}
}
