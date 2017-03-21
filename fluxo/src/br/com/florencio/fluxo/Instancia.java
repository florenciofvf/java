package br.com.florencio.fluxo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Instancia {
	private List<Instancia> filhos;
	private boolean selecionado;
	private List<Linha> linhas;
	private Dimensao dimensao;
	private String descricao;
	private Instancia pai;
	private Local local;

	public Instancia(String descricao) {
		this.descricao = Arquivo.semSufixo(descricao);
		filhos = new ArrayList<>();
		linhas = new ArrayList<>();
		dimensao = new Dimensao();
		local = new Local();
	}

	public void adicionar(Instancia i) {
		if (i.pai != null) {
			i.pai.excluir(i);
		}

		Instancia cmp = this;

		while (cmp != null) {
			if (i == cmp) {
				throw new IllegalStateException();
			}
			cmp = cmp.pai;
		}

		i.pai = this;
		filhos.add(i);
	}

	public void excluir(Instancia i) {
		if (i.pai != this) {
			return;
		}

		i.pai = null;
		filhos.remove(i);
	}

	public List<Instancia> getFilhos() {
		return filhos;
	}

	public Instancia getFilho(int index) {
		if (index < 0 || index >= getTamanho()) {
			throw new IllegalStateException();
		}

		return filhos.get(index);
	}

	public int getTamanho() {
		return filhos.size();
	}

	public boolean isVazio() {
		return filhos.isEmpty();
	}

	public Instancia getPai() {
		return pai;
	}

	public List<Linha> getLinhas() {
		return linhas;
	}

	public Dimensao getDimensao() {
		return dimensao;
	}

	public Local getLocal() {
		return local;
	}

	public void desenhar(Graphics2D g2) {
		g2.setColor(selecionado ? Color.RED : Color.BLACK);

		g2.drawRoundRect(local.x, local.y, dimensao.largura, dimensao.altura, 8, 8);
		g2.drawString(descricao, local.x + 1, local.y + 15);

		for (Instancia i : filhos) {
			i.desenhar(g2);
		}

		for (Linha l : linhas) {
			l.desenhar(g2);
		}
	}

	public void organizar() {
		Dimensao.larguraTotal = 0;
		Dimensao.alturaTotal = 0;
		inicializar();
		calcularAltura();
		Dimensao.alturaTotal = dimensao.altura;
		calcularX();
		calcularY(local.y);
		centralizarY();
		afastar(0);
		calcularLarturaTotal();
		criarLinhas();
	}

	private void inicializar() {
		linhas = new ArrayList<>();
		dimensao = new Dimensao();
		local = new Local();

		for (Instancia i : filhos) {
			i.inicializar();
		}
	}

	public int calcularAltura() {
		int total = 0;

		for (Instancia i : filhos) {
			total += i.calcularAltura();
		}

		dimensao.altura = total;

		if (dimensao.altura == 0) {
			dimensao.altura = Dimensao.ALTURA_PADRAO;
		}

		return dimensao.altura;
	}

	public void calcularX() {
		local.x = pai == null ? 0 : pai.local.x + dimensao.largura;

		for (Instancia i : filhos) {
			i.calcularX();
		}
	}

	public void calcularY(int yPai) {
		for (Instancia i : filhos) {
			i.local.y = yPai;
			yPai += i.dimensao.altura;
			i.calcularY(i.local.y);
		}
	}

	public void centralizarY() {
		local.y += (dimensao.altura - Dimensao.ALTURA_PADRAO) / 2;
		dimensao.altura = Dimensao.ALTURA_PADRAO;

		for (Instancia i : filhos) {
			i.centralizarY();
		}
	}

	private void afastar(int acumulado) {
		int totalAcumulado = acumulado + Dimensao.MARGEM_DIREITA;
		local.x += totalAcumulado;

		for (Instancia i : filhos) {
			i.afastar(totalAcumulado);
		}
	}

	private void calcularLarturaTotal() {
		if (local.x + dimensao.largura > Dimensao.larguraTotal) {
			Dimensao.larguraTotal = local.x + dimensao.largura;
		}

		for (Instancia i : filhos) {
			i.calcularLarturaTotal();
		}
	}

	private void criarLinhas() {
		int x1 = local.x + dimensao.largura;
		int y1 = local.y + dimensao.altura / 2;

		for (Instancia i : filhos) {
			int x2 = i.local.x;
			int y2 = i.local.y + i.dimensao.altura / 2;

			linhas.add(new Linha(x1, y1, x2, y2));
			i.criarLinhas();
		}
	}

	public Instancia procurar(int x, int y) {
		if (x >= local.x && y >= local.y && x <= local.x + dimensao.largura && y <= local.y + dimensao.altura) {
			return this;
		}

		for (Instancia i : filhos) {
			Instancia c = i.procurar(x, y);

			if (c != null) {
				return c;
			}
		}

		return null;
	}

	public Instancia clonar() {
		Instancia obj = new Instancia(descricao);

		for (Instancia i : this.filhos) {
			Instancia o = i.clonar();
			obj.adicionar(o);
		}

		return obj;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void imprimir(PrintWriter pw) {
		imprimir("", pw);
	}

	public void imprimir(String tab, PrintWriter pw) {
		tab += pai != null ? "\t" : "";

		Arquivo.inicioTag(tab, this, pw);

		for (Instancia i : filhos) {
			i.imprimir(tab, pw);
		}

		Arquivo.finalTag(tab, this, pw);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}