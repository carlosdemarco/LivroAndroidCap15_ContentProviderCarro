package br.livro.android.cap15;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import br.livro.android.cap14.banco.Carro;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * Buscar o Carro.
 * 
 * @author rlecheta
 * 
 */
public class BuscarCarro extends br.livro.android.cap14.banco.BuscarCarro {
	private static final String CATEGORIA = "livro";

	// Sobrescreve para buscar com um provedor de conteúdo
	protected Carro buscarCarro(String nome) {

		Log.i(CATEGORIA, "Buscando carro: " + nome);

		Uri uri = Carros.CONTENT_URI;

		String where = Carros.NOME + "=?";
		String[] whereArgs = new String[] { nome };
		Cursor c = getContentResolver().query(uri, null, where, whereArgs, null);

		boolean encontrou = c.moveToFirst();

		if (encontrou) {

			long id = c.getLong(c.getColumnIndexOrThrow(Carros._ID));
			nome = c.getString(c.getColumnIndexOrThrow(Carros.NOME));
			String placa = c.getString(c.getColumnIndexOrThrow(Carros.PLACA));
			int ano = c.getInt(c.getColumnIndexOrThrow(Carros.ANO));

			Log.i(CATEGORIA, "Carro encontrado com id: " + id);

			Carro carro = new Carro(nome, placa, ano);

			return carro;
		}

		return null;
	}
}
