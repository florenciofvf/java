package br.com.florencio.fluxo;

public class Local {
	int yTop = 10;
	int x;
	int y;

	public Local() {
	}

	public Local(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}