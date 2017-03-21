package br.com.florencio.fluxo;

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;

public class Linha {
	private int x1;
	private int y1;
	private int x2;
	private int y2;

	public Linha(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void desenhar(Graphics2D g2) {
		CubicCurve2D curva = new CubicCurve2D.Float(x1, y1, x1 + 10, y1, x2 - 10, y2, x2, y2);
		g2.draw(curva);
	}
}