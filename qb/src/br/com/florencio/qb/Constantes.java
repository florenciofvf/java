package br.com.florencio.qb;

import java.awt.Color;

public class Constantes {

	private Constantes() {
	}

	public static final short DESLOCAMENTO_X_TERRITORIO = 50;

	public static final short DESLOCAMENTO_Y_TERRITORIO = 30;

	public static final byte HORIZONTAL = 0;

	public static final byte VERTICAL = 1;

	public static final int TOTAL_PECAS = 250;

	public static final byte TOTAL_CAMADAS = 30;

	public static final byte TOTAL_COLUNAS = 20;

	public static final byte LADO_QUADRADO = 20;

	public static final byte BORDA_QUADRADO = 3;

	public static final short LARGURA_JANELA = 800;

	public static final short ALTURA_JANELA = 600;

	public static final byte DIRECAO_NORTE = 0;

	public static final byte DIRECAO_SUL = 1;

	public static final byte DIRECAO_LESTE = 2;

	public static final byte DIRECAO_OESTE = 3;

	public static final byte SENTIDO_HORARIO = 0;

	public static final byte SENTIDO_ANTIHORARIO = 1;

	public static final Color[] CORES = { Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA,
			Color.ORANGE };
}