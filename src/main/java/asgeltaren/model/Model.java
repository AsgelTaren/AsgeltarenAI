package asgeltaren.model;

import java.util.ArrayList;
import java.util.function.Consumer;

import asgeltaren.math.Tensor;

public class Model {

	// Model types
	public static final int COM = 0, INPUT = 1, OUTPUT = 2;

	// Model's objects
	private ArrayList<ModelObject> objects;

	// Model's links
	private ArrayList<Link> links;

	// Input objects
	private ArrayList<ModelObject> inputs;

	// Output objects
	private ArrayList<ModelObject> outputs;

	public Model() {
		this.objects = new ArrayList<ModelObject>();
		this.links = new ArrayList<Link>();
		this.inputs = new ArrayList<ModelObject>();
		this.outputs = new ArrayList<ModelObject>();
	}

	public ArrayList<ModelObject> getObjects() {
		return objects;
	}

	public ArrayList<Link> getLinks() {
		return links;
	}

	public ArrayList<ModelObject> getInputs() {
		return inputs;
	}

	public ArrayList<ModelObject> getOutputs() {
		return outputs;
	}

	public ModelObject quickAdd(ModelObject obj, int type) {
		if (objects.contains(obj)) {
			return null;
		}
		if (objects.size() > 0) {
			Link l = Link.createAndApply(objects.get(objects.size() - 1).defaultOutput(), obj.defaultInput());
			if (l == null) {
				return null;
			}

			// Link list
			links.add(l);
		}

		// Object list
		objects.add(obj);
		switch (type) {
		case INPUT: {
			if (!inputs.contains(obj)) {
				inputs.add(obj);
			}
			break;
		}
		case OUTPUT: {
			if (!outputs.contains(obj)) {
				outputs.add(obj);
			}
			break;
		}
		}

		return obj;
	}

	public ModelObject quickAdd(ModelObject obj) {
		return quickAdd(obj, COM);
	}

	public void mapForward(Consumer<ModelObject> mapper, Consumer<Link> mapperLinks) {
		ArrayList<ModelObject> next = new ArrayList<ModelObject>();

		for (ModelObject input : inputs) {
			next.add(input);
		}
		while (!next.isEmpty()) {
			ArrayList<ModelObject> temp = new ArrayList<ModelObject>();
			for (ModelObject obj : next) {
				mapper.accept(obj);
				for (Pin p : obj.getPins()) {
					if (!p.isInput() && p.getLink() != null && !temp.contains(p.getLink().getEndObj())) {
						temp.add(p.getLink().getEndObj());
						if (mapperLinks != null) {
							mapperLinks.accept(p.getLink());
						}
					}
				}
			}
			next = temp;
		}
	}

	public void mapBackward(Consumer<ModelObject> mapper, Consumer<Link> mapperLinks) {
		ArrayList<ModelObject> next = new ArrayList<ModelObject>();

		for (ModelObject output : outputs) {
			next.add(output);
		}
		while (!next.isEmpty()) {
			ArrayList<ModelObject> temp = new ArrayList<ModelObject>();
			for (ModelObject obj : next) {
				mapper.accept(obj);
				for (Pin p : obj.getPins()) {
					if (p.isInput() && p.getLink() != null && !temp.contains(p.getLink().getStartObj())) {
						temp.add(p.getLink().getStartObj());
						if (mapperLinks != null) {
							mapperLinks.accept(p.getLink());
						}
					}
				}
			}
			next = temp;
		}
	}

	public void feed(Tensor data) {
		mapForward(ModelObject::feed, l -> l.transmitData());
	}

	public void backProp() {
		mapBackward(ModelObject::backProp, l -> l.transmitGradient());
	}

	public void printForward() {
		mapForward(e -> System.out.println(e), l -> System.out.println(l));
	}

	public void printBackward() {
		mapBackward(e -> System.out.println(e), l -> System.out.println(l.toString(false)));
	}
}