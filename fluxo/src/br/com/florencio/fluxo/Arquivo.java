package br.com.florencio.fluxo;

import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Arquivo {
	public static final String SUFIXO = ".fvf";

	public static void salvarArquivo(Instancia raiz, File file) throws Exception {
		PrintWriter pw = new PrintWriter(file);
		gravarPrologo(pw);
		raiz.imprimir(pw);
		pw.close();
	}

	public static String semSufixo(String s) {
		if (s == null) {
			return "";
		}

		if (s.endsWith(Arquivo.SUFIXO)) {
			int pos = s.lastIndexOf(Arquivo.SUFIXO);
			s = s.substring(0, pos);
		}

		return s;
	}

	public static Instancia lerArquivo(File file) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		Manipulador m = new Manipulador();
		parser.parse(file, m);
		return m.raiz;
	}

	public static String citar(String s) {
		return "\"" + s + "\"";
	}

	private static void gravarPrologo(PrintWriter pw) {
		pw.println("<?xml version=" + citar("1.0") + " encoding=" + citar("iso-8859-1") + "?>");
		pw.println();
	}

	public static void inicioTag(String tab, Instancia i, PrintWriter pw) {
		pw.print(
				tab + "<instancia nome=" + citar(i.getDescricao()) + " margemInferior=" + citar("" + i.margemInferior));

		if (i.getCor() != null) {
			pw.print(" cor=" + citar("" + i.getCor().getRGB()));
		}

		pw.print(" minimizado=" + citar("" + i.isMinimizado()));

		pw.print(" comentario=" + citar(i.getComentario()));

		if (i.isVazio()) {
			pw.println("/>");
		} else {
			pw.println(">");
		}
	}

	public static void finalTag(String tab, Instancia i, PrintWriter pw) {
		if (!i.isVazio()) {
			pw.println(tab + "</instancia>");
		}
	}

	private static class Manipulador extends DefaultHandler {
		Instancia raiz;
		Instancia sel;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			Instancia instancia = new Instancia(attributes.getValue("nome"));

			String minimizado = attributes.getValue("minimizado");
			instancia.setMinimizado(Boolean.parseBoolean(minimizado));

			String margemInferior = attributes.getValue("margemInferior");
			if (margemInferior != null && margemInferior.trim().length() > 0) {
				instancia.margemInferior = Integer.parseInt(margemInferior);
			}

			String cor = attributes.getValue("cor");
			if (cor != null && cor.trim().length() > 0) {
				instancia.setCor(new Color(Integer.parseInt(cor)));
			}

			String comentario = attributes.getValue("comentario");
			instancia.setComentario(comentario);

			if (raiz == null) {
				raiz = instancia;
				sel = raiz;
				return;
			}

			sel.adicionar(instancia);
			sel = instancia;

			raiz.controlarMargemInferior();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			sel = sel.getPai();
		}
	}
}