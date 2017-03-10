package br.com.florencio.pattern;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Composite implements Serializable {
	private static final long serialVersionUID = 1214102220657826946L;
	public static final int LARGURA_PADRAO = 40;
	public static final int ALTURA_PADRAO = 40;
	private final List<Composite> childs;
	private int altura = ALTURA_PADRAO;
	private final List<Linha> lines;
	private Composite parent;
	private int largura;
	private int x;
	private int y;

	public Composite() {
		childs = new ArrayList<>();
		lines = new ArrayList<>();
	}

	public void add(Composite c) {
		if (c.parent != null) {
			c.parent.remove(c);
		}
		Composite cmp = this;
		while (cmp != null) {
			if (c == cmp) {
				throw new IllegalStateException();
			}
			cmp = cmp.parent;
		}
		c.parent = this;
		childs.add(c);
		totalGeral++;
	}

	public void remove(Composite c) {
		if (c.parent != this) {
			return;
		}
		c.parent = null;
		childs.remove(c);
	}

	public List<Composite> getChilds() {
		return Collections.unmodifiableList(childs);
	}

	public Composite getChild(int index) {
		if (index < 0 || index >= getSize()) {
			throw new IllegalStateException();
		}
		return childs.get(index);
	}

	public int getSize() {
		return childs.size();
	}

	public boolean isEmpty() {
		return childs.isEmpty();
	}

	public Composite getParent() {
		return parent;
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

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public int getLargura() {
		return largura;
	}

	public void desenhar(Graphics2D g2) {
		g2.drawRect(x, y, largura, altura);

		for (Composite c : getChilds()) {
			c.desenhar(g2);
		}

		for (Linha c : lines) {
			c.desenhar(g2);
		}
	}

	public void organizar() {
		x = 0;
		calcularLargura();
		calcularAltura();
		calcularX(x);
		minimizar();
	}
	
	public int calcularLargura() {
		int total = 0;

		for (Composite c : getChilds()) {
			total += c.calcularLargura();
		}

		largura = total;
		
		if(largura == 0) {
			largura = LARGURA_PADRAO;
		}
		
		return largura;
	}
	
	public void calcularAltura() {
		y = parent == null ? altura : parent.y + altura;
		
		for (Composite c : getChilds()) {
			c.calcularAltura();
		}
	}

	public void minimizar() {
		final int TAMANHO = 20;
		final int novoX = (largura - TAMANHO) / 2;
		
		y += 10;
		altura -= 20;
		
		x += novoX;
		largura = TAMANHO;
		
		int x1 = x + largura / 2;
		int y1 = y + altura;
		
		for (Composite c : getChilds()) {
			c.minimizar();
			
			int x2 = c.x + c.largura / 2;
			int y2 = c.y;
			
			lines.add(new Linha(x1, y1, x2, y2));
		}
	}
	
	public void calcularX(int xPai) {
		int posX = xPai;
		
		for (Composite c : getChilds()) {
			c.x = posX;
			posX += c.largura;
			c.calcularX(c.x);
		}
	}
	
	private class Linha {
		int x1;
		int y1;
		int x2;
		int y2;
		
		public Linha(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		void desenhar(Graphics2D g2) {
			g2.drawLine(x1, y1, x2, y2);
		}
	}
	
	private static Random random = new Random();
	private static int totalGeral;
	
	public void gerarFilhos() {
		int total = random.nextInt(5);
		
		for(int i=0; i<total; i++) {
			if(totalGeral < 30) {
				add(new Composite());
			}
		}

		for (Composite c : getChilds()) {
			c.gerarFilhos();
		}
	}	
}