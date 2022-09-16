package asgeltaren.model;

public class Link {

	private Pin start, end;

	private Link(Pin start, Pin end) {
		this.start = start;
		this.end = end;
	}

	public static Link create(Pin a, Pin b) {
		if (a.isInput() != b.isInput()) {
			return a.isInput() ? new Link(b, a) : new Link(a, b);
		}
		return null;
	}

	public static Link createAndApply(Pin a, Pin b) {
		if (a.isInput() != b.isInput() && a.getLink() == null && b.getLink() == null) {
			Link res = a.isInput() ? new Link(b, a) : new Link(a, b);
			a.setLink(res);
			b.setLink(res);
			return res;
		}
		return null;
	}

	public void transmitData() {
		end.setData(start.getData());
	}

	public void transmitGradient() {
		start.setGradient(end.getGradient());
	}

	public Pin getStart() {
		return start;
	}

	public Pin getEnd() {
		return end;
	}

	public ModelObject getStartObj() {
		return start.getModelObject();
	}

	public ModelObject getEndObj() {
		return end.getModelObject();
	}

	@Override
	public String toString() {
		return start + " -> " + end;
	}

	public String toString(boolean dir) {
		return start + (dir ? "->" : "<-") + end;
	}

}
