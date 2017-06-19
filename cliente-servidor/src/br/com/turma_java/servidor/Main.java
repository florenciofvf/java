package br.com.turma_java.servidor;

public class Main {

	public static void main(String[] args) throws Exception {
		Servidor servidor = new Servidor(10, true);
		servidor.iniciar(4567);
	}

}