package br.com.florencio.qb.main;

import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Visao;

public class Main {

	public static void main(String[] args) {
		Visao visao = new Visao();
		visao.setSize(Constantes.LARGURA_JANELA, Constantes.ALTURA_JANELA);
		visao.setLocationRelativeTo(null);
		visao.setVisible(true);
	}

}