package br.livro.android.cap15;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import br.livro.android.cap14.banco.Carro;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * Activity que utiliza o TableLayout para editar o carro
 * 
 * @author rlecheta
 * 
 */
public class EditarCarro extends br.livro.android.cap14.banco.EditarCarro {
	private static final String CATEGORIA = "livro";

	@Override
	protected Carro buscarCarro(long id) {
		// content://br.livro.android.provider.carro/carros/id
		Uri uri = Carro.Carros.getUriId(id);

		Cursor c = getContentResolver().query(uri, null, null, null, null);

		boolean encontrou = c.moveToFirst();

		Carro carro = null;

		if (encontrou) {
			String nome = c.getString(c.getColumnIndexOrThrow(Carros.NOME));
			String placa = c.getString(c.getColumnIndexOrThrow(Carros.PLACA));
			int ano = c.getInt(c.getColumnIndexOrThrow(Carros.ANO));

			carro = new Carro(id, nome, placa, ano);
		}

		return carro;
	}

	@Override
	protected void salvarCarro(Carro carro) {

		Log.i(CATEGORIA, "Inserindo novo carro, nome: " + carro.nome);

		ContentValues values = new ContentValues();
		values.put(Carros.NOME, carro.nome);
		values.put(Carros.PLACA, carro.placa);
		values.put(Carros.ANO, carro.ano);

		long id = carro.id;

		if (id == 0) {
			// Inserir
			Uri uri = getContentResolver().insert(Carros.CONTENT_URI, values);
			Log.i(CATEGORIA, "Novo carro salvo com sucesso, Uri: " + uri);
		} else {
			// Atualizar

			Uri uri = Carros.getUriId(id);
			Log.i(CATEGORIA, "Atualizando carro Uri: " + uri);

			int count = getContentResolver().update(uri, values, null, null);
			Log.i(CATEGORIA, "Carro atualizado com sucesso: " + count);
		}
	}

	@Override
	protected void excluirCarro(long id) {
		Uri uriCarro = Carros.getUriId(id);

		Log.i(CATEGORIA, "Deletando carro Uri: " + uriCarro);

		int count = getContentResolver().delete(uriCarro, null, null);

		Log.i(CATEGORIA, "Carro excluido com sucesso: " + count);
	}
}
