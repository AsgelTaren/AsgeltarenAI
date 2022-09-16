package asgeltaren.math;

import java.util.function.Function;

/**
 * @author Asgeltaren
 **/
public class Matrix {

	private float[][] data;

	private Matrix() {

	}

	// Constructors
	public static Matrix create(float[][] data) {
		Matrix res = new Matrix();
		res.data = data;
		return res;
	}

	public static Matrix zeros(int rows, int cols) {
		Matrix res = new Matrix();
		res.data = new float[rows][cols];
		return res;
	}

	public static Matrix copyShape(Matrix mat) {
		return zeros(mat.getRows(), mat.getCols());
	}

	public static Matrix identity(int rows, int cols) {
		int min = rows > cols ? cols : rows;
		Matrix res = zeros(rows, cols);
		for (int i = 0; i < min; i++) {
			res.data[i][i] = 1;
		}
		return res;
	}

	public static Matrix identity(int n) {
		return Matrix.identity(n, n);
	}

	public static Matrix copy(Matrix mat) {
		Matrix res = zeros(mat.getRows(), mat.getCols());
		for (int i = 0; i < res.getRows(); i++) {
			for (int j = 0; j < res.getCols(); j++) {
				res.data[i][j] = mat.data[i][j];
			}
		}
		return res;
	}

	//// Methods (NBE)

	public Matrix add(Matrix mat) {
		Matrix res = copyShape(this);
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				res.data[i][j] = this.data[i][j] + mat.data[i][j];
			}
		}
		return res;
	}

	public Matrix sub(Matrix mat) {
		Matrix res = copyShape(this);
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				res.data[i][j] = this.data[i][j] - mat.data[i][j];
			}
		}
		return res;
	}

	public Matrix scale(float s) {
		Matrix res = copyShape(this);
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				res.data[i][j] = this.data[i][j] * s;
			}
		}
		return res;
	}

	public Matrix dotProduct(Matrix mat) {
		Matrix res = zeros(getRows(), mat.getCols());
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < mat.getCols(); j++) {
				float temp = 0f;
				for (int k = 0; k < getCols(); k++) {
					temp += this.data[i][k] * mat.data[k][j];
				}
				res.data[i][j] = temp;
			}
		}
		return res;
	}

	public Matrix schurProduct(Matrix mat) {
		Matrix res = copyShape(this);
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				res.data[i][j] = this.data[i][j] * mat.data[i][j];
			}
		}
		return res;
	}

	public Matrix map(Function<Float, Float> func) {
		Matrix res = copyShape(this);
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				res.data[i][j] = func.apply(this.data[i][j]);
			}
		}
		return res;
	}

	public Matrix tranpose() {
		Matrix res = zeros(getCols(), getRows());
		for (int i = 0; i < getCols(); i++) {
			for (int j = 0; j < getRows(); j++) {
				res.data[i][j] = this.data[j][i];
			}
		}
		return res;
	}

	//// Getters

	public int getRows() {
		return data.length;
	}

	public int getCols() {
		return data[0].length;
	}

	public float[][] getData() {
		return data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				builder.append(this.data[i][j] + ((j == getCols() - 1) ? "\n" : ""));
			}
		}
		return builder.toString();
	}

}