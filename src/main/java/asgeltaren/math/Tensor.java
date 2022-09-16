package asgeltaren.math;

import java.util.function.Function;

public class Tensor {

	private float[][][] data;

	private Tensor() {

	}

	// Constructors

	public static Tensor create(float[][][] data) {
		Tensor res = new Tensor();
		res.data = data;
		return res;
	}

	public static Tensor zeros(int depth, int rows, int cols) {
		Tensor res = new Tensor();
		res.data = new float[depth][rows][cols];
		return res;
	}

	public static Tensor copyShape(Tensor t) {
		return zeros(t.getDepth(), t.getRows(), t.getCols());
	}

	public static Tensor create(Matrix[] mats) {
		float[][][] data = new float[mats.length][][];
		for (int i = 0; i < mats.length; i++) {
			data[i] = mats[i].getData();
		}
		return create(data);
	}

	// Methods

	public Tensor add(Tensor t) {
		Tensor result = copyShape(this);
		for (int i = 0; i < getDepth(); i++) {
			for (int j = 0; j < getRows(); j++) {
				for (int k = 0; k < getCols(); k++) {
					result.data[i][j][k] = this.data[i][j][k] + t.data[i][j][k];
				}
			}
		}
		return result;
	}

	public Tensor sub(Tensor t) {
		Tensor result = copyShape(this);
		for (int i = 0; i < getDepth(); i++) {
			for (int j = 0; j < getRows(); j++) {
				for (int k = 0; k < getCols(); k++) {
					result.data[i][j][k] = this.data[i][j][k] - t.data[i][j][k];
				}
			}
		}
		return result;
	}

	public Tensor scale(float s) {
		Tensor result = copyShape(this);
		for (int i = 0; i < getDepth(); i++) {
			for (int j = 0; j < getRows(); j++) {
				for (int k = 0; k < getCols(); k++) {
					result.data[i][j][k] = this.data[i][j][k] * s;
				}
			}
		}
		return result;
	}

	public Tensor schurProduct(Tensor t) {
		Tensor result = copyShape(this);
		for (int i = 0; i < getDepth(); i++) {
			for (int j = 0; j < getRows(); j++) {
				for (int k = 0; k < getCols(); k++) {
					result.data[i][j][k] = this.data[i][j][k] * t.data[i][j][k];
				}
			}
		}
		return result;
	}

	public Tensor map(Function<Float, Float> func) {
		Tensor result = copyShape(this);
		for (int i = 0; i < getDepth(); i++) {
			for (int j = 0; j < getRows(); j++) {
				for (int k = 0; k < getCols(); k++) {
					result.data[i][j][k] = func.apply(this.data[i][j][k]);
				}
			}
		}
		return result;
	}

	public Matrix[] toMatrixes() {
		Matrix[] res = new Matrix[getDepth()];
		for (int i = 0; i < getDepth(); i++) {
			res[i] = getSub(i);
		}
		return res;
	}

	// Getters
	public int getDepth() {
		return data.length;
	}

	public int getRows() {
		return data[0].length;
	}

	public int getCols() {
		return data[0][0].length;
	}

	public float[][][] getData() {
		return data;
	}

	public Matrix getSub(int i) {
		return Matrix.create(data[i]);
	}
}