package br.com.florencio.fluxo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class Painel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JMenuItem menuItemMargemInferior = new JMenuItem("Margem Inferior");
	private JMenuItem menuItemVermelho = new JMenuItem("Vermelho");
	private JMenuItem menuItemLaranja = new JMenuItem("Laranja");
	private JMenuItem menuItemExcluir = new JMenuItem("Excluir");
	private JMenuItem menuItemPadrao = new JMenuItem("Padrão");
	private JMenuItem menuItemCopiar = new JMenuItem("Copiar");
	private JMenuItem menuItemVerde = new JMenuItem("Verde");
	private JMenuItem menuItemColar = new JMenuItem("Colar");
	private JMenuItem menuItemNovo = new JMenuItem("Novo");
	private JMenuItem menuItemAzul = new JMenuItem("Azul");
	private JPopupMenu popup = new JPopupMenu();
	private Instancia copiado;
	private Instancia raiz;
	private String arquivo;
	private Local local;

	public Painel(Instancia raiz) {
		this.raiz = raiz;
		registrarEventos();
		popup.add(menuItemNovo);
		popup.add(menuItemExcluir);
		popup.addSeparator();
		popup.add(menuItemCopiar);
		popup.add(menuItemColar);
		popup.addSeparator();
		popup.add(menuItemMargemInferior);
		popup.addSeparator();
		popup.add(menuItemAzul);
		popup.add(menuItemVermelho);
		popup.add(menuItemLaranja);
		popup.add(menuItemVerde);
		popup.add(menuItemPadrao);
	}

	private void tamanhoPainel() {
		Dimension d = new Dimension(Dimensao.larguraTotal, Dimensao.alturaTotal);
		setPreferredSize(d);
		setMinimumSize(d);
		SwingUtilities.updateComponentTreeUI(getParent());
	}

	private void organizar() {
		raiz.organizar(getFontMetrics(getFont()));
	}

	private void registrarEventos() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					local = new Local(e.getX(), e.getY());
					popup.show(Painel.this, e.getX(), e.getY());
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && raiz != null) {
					Instancia objeto = raiz.procurar(e.getX(), e.getY());

					if (objeto == null) {
						return;
					}

					String descricao = JOptionPane.showInputDialog(Painel.this, objeto.getDescricao(),
							objeto.getDescricao());

					if (descricao == null || descricao.trim().length() == 0) {
						return;
					}

					objeto.setDescricao(descricao);
					organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemNovo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				String descricao = JOptionPane.showInputDialog(Painel.this, objeto.getDescricao());

				if (descricao == null || descricao.trim().length() == 0) {
					return;
				}

				objeto.adicionar(new Instancia(descricao));
				organizar();
				tamanhoPainel();
				repaint();
			}
		});

		menuItemMargemInferior.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				String valor = JOptionPane.showInputDialog(Painel.this, objeto.getDescricao(), objeto.margemInferior);

				if (valor == null || valor.trim().length() == 0) {
					return;
				}

				try {
					objeto.margemInferior = Integer.parseInt(valor);
					organizar();
					tamanhoPainel();
					repaint();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(Painel.this, ex.getMessage());
				}
			}
		});

		menuItemExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				if (objeto.getPai() != null) {
					objeto.getPai().excluir(objeto);
				}

				organizar();
				tamanhoPainel();
				repaint();
			}
		});

		menuItemCopiar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					copiado = null;
					return;
				}

				copiado = objeto.clonar();
			}
		});

		menuItemColar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null || copiado == null) {
					return;
				}

				objeto.adicionar(copiado.clonar());
				organizar();
				tamanhoPainel();
				repaint();
			}
		});

		menuItemVermelho.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(Color.RED);
					repaint();
				}
			}
		});

		menuItemVerde.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(Color.GREEN);
					repaint();
				}
			}
		});

		menuItemLaranja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(Color.ORANGE);
					repaint();
				}
			}
		});

		menuItemAzul.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(Color.BLUE);
					repaint();
				}
			}
		});

		menuItemPadrao.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(null);
					repaint();
				}
			}
		});
	}

	public void setArquivo(String arq) {
		arquivo = Arquivo.semSufixo(arq);

		if (arquivo != null && arquivo.trim().length() > 0) {
			arquivo += Arquivo.SUFIXO;
			abrirArquivo();
		} else {
			arquivo = null;
		}
	}

	private void abrirArquivo() {
		try {
			File file = new File(arquivo);

			if (!file.exists()) {
				Arquivo.salvarArquivo(new Instancia(file.getName()), file);
			}

			raiz = Arquivo.lerArquivo(file);
			organizar();
			tamanhoPainel();
			repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void salvarArquivo(String alternativo) {
		if (alternativo != null && alternativo.trim().length() > 0) {
			arquivo = Arquivo.semSufixo(alternativo);
			arquivo += Arquivo.SUFIXO;
		}

		if (arquivo == null || raiz == null) {
			return;
		}

		try {
			Arquivo.salvarArquivo(raiz, new File(arquivo));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private Instancia procurar() {
		return raiz.procurar(local.getX(), local.getY());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		raiz.desenhar((Graphics2D) g);
	}

	public String getArquivo() {
		return arquivo;
	}

	public void processarSQL(String arquivo) {
		File file = new File(arquivo);

		if (!file.exists()) {
			return;
		}

		try {
			StringBuilder sb = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String linha = br.readLine();

			while (linha != null) {
				sb.append(linha);
				linha = br.readLine();
			}

			br.close();
			raiz.limpar();
			processar(sb.toString());
			organizar();
			tamanhoPainel();
			repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private void processar(String sql) throws Exception {
		if (sql.trim().length() == 0) {
			return;
		}

		Class.forName(Config.get("driver"));
		Connection conn = DriverManager.getConnection(Config.get("url"), Config.get("usuario"), Config.get("senha"));
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();

		int colunas = meta.getColumnCount();

		while (rs.next()) {
			String[] grafo = new String[colunas];

			for (int i = 0; i < colunas; i++) {
				grafo[i] = rs.getString(i + 1);
			}

			raiz.importar(grafo);
		}

		rs.close();
		ps.close();
		conn.close();
	}
}