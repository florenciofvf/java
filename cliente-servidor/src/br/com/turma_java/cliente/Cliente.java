package br.com.turma_java.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.turma_java.Mensagem;

public class Cliente extends JPanel {
	public static final boolean PRINT_STACK_TRACE = Boolean.FALSE;
	private static final long serialVersionUID = 1L;
	private final List<Celula> celulas;
	private boolean clienteAbatido;
	private final JLabel mensagem;
	private final boolean nativo;
	private final JLabel titulo;
	private final JPanel panel;
	private final String nome;
	private final Form form;

	public Cliente(Form form, String nome, int celulas, int celulaEspecial, boolean comBorda) {
		if (nome == null || nome.trim().length() == 0) {
			throw new IllegalArgumentException("Cliente sem nome.");
		}

		this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		this.panel = new JPanel(new GridLayout(celulas, celulas));
		this.nativo = form.cliente.equals(nome);
		this.mensagem = new JLabel("Ativo");
		this.setLayout(new BorderLayout());
		this.celulas = new ArrayList<>();
		this.titulo = new JLabel(nome);
		int total = celulas * celulas;
		this.nome = nome;
		this.form = form;

		for (int i = 0; i < total; i++) {
			Celula c = new Celula(this, i, celulaEspecial == i, comBorda);
			this.celulas.add(c);
		}

		montarLayout();
	}

	private void montarLayout() {
		mensagem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		titulo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mensagem.setOpaque(true);
		titulo.setOpaque(true);

		for (Celula c : celulas) {
			panel.add(c);
		}

		add(BorderLayout.NORTH, titulo);
		add(BorderLayout.CENTER, panel);
		add(BorderLayout.SOUTH, mensagem);
	}

	public void click(Mensagem m) {
		if (nome.equals(m.getClienteAlvo())) {
			for (Celula c : celulas) {
				c.marcar(m.getCelulaAlvo());
			}
		}
	}

	public void proximo(Mensagem m) {
		form.proximo = form.cliente.equals(m.getCliente());
		titulo.setBackground(getBackground());
		if (nome.equals(m.getCliente())) {
			titulo.setBackground(Color.CYAN);
		}
	}

	public void abatido(Mensagem m) {
		if (nome.equals(m.getCliente())) {
			mensagem.setBackground(Color.RED);
			mensagem.setText("ABATIDO");
			if (nativo) {
				form.abatido = true;
			}
			clienteAbatido = true;
		}
	}

	public void vencedor(Mensagem m) {
		if (nome.equals(m.getCliente())) {
			mensagem.setBackground(Color.GREEN);
			mensagem.setText("VENCEDOR");
			if (nativo) {
				form.enviar(new Mensagem(Mensagem.ALVO));
			}
		}
	}

	public void mostrar() {
		for (Celula c : celulas) {
			c.mostrar();
		}
	}

	public String getNome() {
		return nome;
	}

	class Celula extends JPanel {
		private static final long serialVersionUID = 1L;
		private final boolean especial;
		private final Cliente cliente;
		private final int numero;

		public Celula(Cliente cliente, int numero, boolean especial, boolean comBorda) {
			if (comBorda) {
				this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}

			this.addMouseListener(new Ouvinte());
			this.especial = especial;
			this.cliente = cliente;
			this.numero = numero;

			if (especial && cliente.nativo) {
				setBackground(Color.BLUE);
			}
		}

		public void mostrar() {
			if (especial) {
				setBackground(Color.BLUE);
			}
		}

		public void marcar(int numero) {
			if (this.numero == numero) {
				setBackground(Color.RED);

				if (especial && cliente.nativo) {
					form.enviar(new Mensagem(Mensagem.ABATIDO, cliente.nome));
				}
			}
		}

		private class Ouvinte extends MouseAdapter {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clienteAbatido || form.abatido || form.vencedor) {
					JOptionPane.showMessageDialog(form, mensagem.getText());
					return;
				}

				if (!form.proximo) {
					JOptionPane.showMessageDialog(form, "Espere sua vez.");
					return;
				}

				if (especial && cliente.nativo) {
					int i = JOptionPane.showConfirmDialog(form, "Deseja se auto destruir?", "Atenção",
							JOptionPane.YES_NO_OPTION);

					if (i != JOptionPane.OK_OPTION) {
						return;
					}
				}

				Mensagem m = new Mensagem(Mensagem.CLICK);
				m.setClienteAlvo(cliente.nome);
				m.setCliente(form.cliente);
				m.setCelulaAlvo(numero);
				form.enviar(m);
			}
		}
	}
}