package br.com.turma_java.cliente;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.turma_java.Mensagem;

public class Form extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel panel = new JPanel();
	private final List<Cliente> clientes;
	private Tarefa tarefa;
	boolean vencedor;
	boolean proximo;
	boolean abatido;
	String cliente;

	public Form() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.clientes = new ArrayList<>();
		add(BorderLayout.CENTER, panel);
		montarMenu();
		setSize(500, 500);
	}

	private void montarMenu() {
		JMenuBar barra = new JMenuBar();
		setJMenuBar(barra);

		JMenu menu = new JMenu("Config");
		JMenuItem item = new JMenuItem("Reiniciar");
		barra.add(menu);
		menu.add(item);

		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!vencedor) {
					JOptionPane.showMessageDialog(Form.this, "Somente o vencedor pode reiniciar.");
				} else {
					enviar(new Mensagem(Mensagem.REINICIAR));
				}
			}
		});
	}

	public void conectar(String servidor, int porta, String apelido) throws Exception {
		Socket socket = new Socket(servidor, porta);
		ObjectOutput out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(apelido);
		tarefa = new Tarefa(this, socket);
		tarefa.start();
	}

	public void enviar(Mensagem m) {
		try {
			tarefa.enviar(m);
		} catch (Exception e) {
			Tarefa.excecao("Form.enviar: FECHANDO O FORMULÁRIO: " + cliente, e);
			System.exit(0);
		}
	}

	public void receber(Mensagem m) {
		if (m.isConectado()) {
			cliente = m.getCliente();
			Objects.requireNonNull(cliente, "Cliente nulo.");
			setTitle(cliente);
		} else if (m.isMontarTela()) {
			panel.removeAll();
			clientes.clear();
			vencedor = false;
			abatido = false;
			proximo = false;

			String[] metaInfos = m.getMetaInfo().split(",");
			int lado = (int) Math.sqrt(m.getTotalClientes());
			panel.setLayout(new GridLayout(lado, lado));

			for (int i = 0; i < m.getTotalClientes(); i++) {
				String[] nomeEspecial = metaInfos[i].split("-");
				int especial = Integer.valueOf(nomeEspecial[1]);
				String nome = nomeEspecial[0];

				Cliente c = new Cliente(this, nome, m.getCelulas(), especial);
				clientes.add(c);
				panel.add(c);
			}

			SwingUtilities.updateComponentTreeUI(this);
		} else if (m.isExcluido()) {
			List<Cliente> excluidos = getExcluidos(m.getMetaInfo());
			clientes.removeAll(excluidos);
			for (Cliente c : excluidos) {
				panel.remove(c);
			}
			SwingUtilities.updateComponentTreeUI(this);
		} else if (m.isClick()) {
			for (Cliente cliente : clientes) {
				cliente.click(m);
			}
		} else if (m.isAbatido()) {
			for (Cliente cliente : clientes) {
				cliente.abatido(m);
			}
		} else if (m.isProximo()) {
			for (Cliente cliente : clientes) {
				cliente.proximo(m);
			}
		} else if (m.isVencedor()) {
			vencedor = cliente.equals(m.getCliente());
			for (Cliente cliente : clientes) {
				cliente.vencedor(m);
			}
		} else if (m.isAlvo()) {
			for (Cliente cliente : clientes) {
				cliente.mostrar();
			}
		}
	}

	private List<Cliente> getExcluidos(String metaInfo) {
		List<Cliente> excluidos = new ArrayList<>();
		for (Cliente c : clientes) {
			String info = "[" + c.getNome() + "]";
			if (metaInfo.indexOf(info) != -1) {
				excluidos.add(c);
			}
		}
		return excluidos;
	}
}