package br.com.florencio.fluxo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class Painel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JMenuItem menuItemSalvarArquivo = new JMenuItem("Salvar arquivo");
	private JMenuItem menuItemAbrirArquivo = new JMenuItem("Abrir arquivo");
	private JMenuItem menuItemExcluir = new JMenuItem("Excluir");
	private JMenuItem menuItemCopiar = new JMenuItem("Copiar");
	private JMenuItem menuItemColar = new JMenuItem("Colar");
	private JMenuItem menuItemNovo = new JMenuItem("Novo");
	private JPopupMenu popup = new JPopupMenu();
	private Instancia copiado;
	private Instancia raiz;
	private String arquivo;
	private Local local;

	public Painel(Instancia raiz) {
		this.raiz = raiz;
		registrarEventos();

		popup.add(menuItemSalvarArquivo);
		popup.add(menuItemAbrirArquivo);
		popup.addSeparator();
		popup.add(menuItemNovo);
		popup.add(menuItemExcluir);
		popup.addSeparator();
		popup.add(menuItemCopiar);
		popup.add(menuItemColar);
	}

	private void tamanhoPainel() {
		Dimension d = new Dimension(Dimensao.larguraTotal, Dimensao.alturaTotal);
		setPreferredSize(d);
		setMinimumSize(d);
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

					String descricao = JOptionPane.showInputDialog(Painel.this, objeto.getDescricao(), objeto.getDescricao());

					if (descricao == null || descricao.trim().length() == 0) {
						return;
					}

					objeto.setDescricao(descricao);
					raiz.organizar();
					tamanhoPainel();
					repaint();
				}
			}
		});

		menuItemAbrirArquivo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arquivo = JOptionPane.showInputDialog(Painel.this, arquivo, arquivo);

				if (arquivo != null && arquivo.trim().length() > 0) {
					arquivo += Arquivo.SUFIXO;
					abrirArquivo();
				} else {
					arquivo = null;
				}
			}
		});

		menuItemSalvarArquivo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				salvarArquivo();
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
				raiz.organizar();
				tamanhoPainel();
				repaint();
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

				raiz.organizar();
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
				raiz.organizar();
				tamanhoPainel();
				repaint();
			}
		});
	}

	private void abrirArquivo() {
		try {
			File file = new File(arquivo);

			if (!file.exists()) {
				Arquivo.salvarArquivo(new Instancia(file.getName()), file);
			}

			raiz = Arquivo.lerArquivo(file);
			raiz.organizar();
			tamanhoPainel();
			repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private void salvarArquivo() {
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
}