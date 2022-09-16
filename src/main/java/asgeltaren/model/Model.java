package asgeltaren.model;

import java.util.ArrayList;
import java.util.function.Consumer;

import asgeltaren.math.Tensor;
import asgeltaren.model.datasets.Dataset;

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

	// Dataset links
	private ArrayList<DatasetLink> dataLinks;

	public Model() {
		this.objects = new ArrayList<ModelObject>();
		this.links = new ArrayList<Link>();
		this.inputs = new ArrayList<ModelObject>();
		this.outputs = new ArrayList<ModelObject>();
		this.dataLinks = new ArrayList<DatasetLink>();
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

	public ArrayList<DatasetLink> getDataLinks() {
		return dataLinks;
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

	public void addDataLink(Dataset set, ModelObject obj, int index, int type) {
		DatasetLink link = new DatasetLink(set, obj, index, type);
		if (!dataLinks.contains(link)) {
			dataLinks.add(link);
		}
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

	public void provideForAll(int index) {
		dataLinks.forEach(e -> e.provide(index));
	}

	public void provideFor(int type, int index) {
		for (DatasetLink link : dataLinks) {
			if (link.getType() == type) {
				link.provide(index);
			}
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

	/**
	 * Represents a link between a model object and a data set. This is how data is
	 * being transmitted to the model
	 **/
	public class DatasetLink {

		private Dataset set;
		private ModelObject obj;
		private int id, type;

		public DatasetLink(Dataset set, ModelObject obj, int id, int type) {
			this.set = set;
			this.obj = obj;
			this.id = id;
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public int getID() {
			return id;
		}

		public ModelObject getObj() {
			return obj;
		}

		public Dataset getSet() {
			return set;
		}

		public void provide(int index) {
			obj.provide(set.provide(id, type, index));
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof DatasetLink l) {
				return l.set == this.set && l.obj == this.obj && l.id == this.id && l.type == this.type;
			}
			return false;
		}
	}
}