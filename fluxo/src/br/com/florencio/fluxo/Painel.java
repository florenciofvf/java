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
	private JMenuItem menuItemSairHierarquia = new JMenuItem("Sair da Hierarquia");
	private JMenuItem menuItemMargemInferior = new JMenuItem("Margem Inferior");
	private JMenuItem menuItemComentario = new JMenuItem("Comentário");
	private JMenuItem menuItemPrimeiro = new JMenuItem("Primeiro");
	private JMenuItem menuItemVermelho = new JMenuItem("Vermelho");
	private JMenuItem menuItemRecortar = new JMenuItem("Recortar");
	private JMenuItem menuItemAmarelo = new JMenuItem("Amarelo");
	private JMenuItem menuItemLaranja = new JMenuItem("Laranja");
	private JMenuItem menuItemExcluir = new JMenuItem("Excluir");
	private JMenuItem menuItemPadrao = new JMenuItem("Padrão");
	private JMenuItem menuItemCopiar = new JMenuItem("Copiar");
	private JMenuItem menuItemUltimo = new JMenuItem("Último");
	private JMenuItem menuItemDescer = new JMenuItem("Descer");
	private JMenuItem menuItemSubir = new JMenuItem("Subir");
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
		popup.add(menuItemSairHierarquia);
		popup.addSeparator();
		popup.add(menuItemCopiar);
		popup.add(menuItemRecortar);
		popup.add(menuItemColar);
		popup.addSeparator();
		popup.add(menuItemMargemInferior);
		popup.addSeparator();
		popup.add(menuItemPrimeiro);
		popup.add(menuItemSubir);
		popup.add(menuItemDescer);
		popup.add(menuItemUltimo);
		popup.addSeparator();
		popup.add(menuItemVermelho);
		popup.add(menuItemLaranja);
		popup.add(menuItemAmarelo);
		popup.add(menuItemVerde);
		popup.add(menuItemAzul);
		popup.add(menuItemPadrao);
		popup.addSeparator();
		popup.add(menuItemComentario);
	}

	private void tamanhoPainel() {
		Dimension d = new Dimension(Dimensao.larguraTotal + 139, Dimensao.alturaTotal + 139);
		setPreferredSize(d);
		setMinimumSize(d);
		SwingUtilities.updateComponentTreeUI(getParent());
	}

	private void organizar() {
		raiz.controlarMargemInferior();
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

					if (!objeto.duploClickValido(e.getX())) {
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
				} else {

					Instancia objeto = raiz.procurar(e.getX(), e.getY());

					if (objeto == null) {
						return;
					}

					if (objeto.clicadoNoIcone(e.getX(), e.getY())) {
						objeto.inverterIcone();
						organizar();
						tamanhoPainel();
						repaint();
					}
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

		menuItemComentario.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				String comentario = JOptionPane.showInputDialog(Painel.this, objeto.getDescricao(),
						objeto.getComentario());

				if (comentario == null || comentario.trim().length() == 0) {
					return;
				}

				objeto.setComentario(comentario);
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

				int resp = JOptionPane.showConfirmDialog(Painel.this, "Confirma?", "Atenção",
						JOptionPane.YES_NO_OPTION);

				if (JOptionPane.OK_OPTION == resp) {
					if (objeto.getPai() != null) {
						objeto.getPai().excluir(objeto);
					}

					organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemSairHierarquia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				int resp = JOptionPane.showConfirmDialog(Painel.this, "Confirma?", "Atenção",
						JOptionPane.YES_NO_OPTION);

				if (JOptionPane.OK_OPTION == resp) {
					objeto.sairDaHierarquia();
					organizar();
					tamanhoPainel();
					repaint();
				}
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

		menuItemRecortar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					copiado = null;
					return;
				}

				copiado = objeto.clonar();

				if (objeto.getPai() != null) {
					objeto.getPai().excluir(objeto);
					organizar();
					tamanhoPainel();
					repaint();
				}
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

		menuItemPrimeiro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				if (objeto.getPai() != null) {
					objeto.getPai().primeiro(objeto);
					organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemSubir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				if (objeto.getPai() != null) {
					objeto.getPai().subir(objeto);
					organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemDescer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				if (objeto.getPai() != null) {
					objeto.getPai().descer(objeto);
					organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemUltimo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto == null) {
					return;
				}

				if (objeto.getPai() != null) {
					objeto.getPai().ultimo(objeto);
					organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemVermelho.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(new Color(255, 50, 50));
					repaint();
				}
			}
		});

		menuItemVerde.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(new Color(0, 150, 0));
					repaint();
				}
			}
		});

		menuItemLaranja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(new Color(255, 170, 70));
					repaint();
				}
			}
		});

		menuItemAmarelo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(new Color(240, 210, 100));
					repaint();
				}
			}
		});

		menuItemAzul.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Instancia objeto = procurar();

				if (objeto != null) {
					objeto.setCor(new Color(80, 80, 255));
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