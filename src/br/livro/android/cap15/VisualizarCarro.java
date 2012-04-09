package br.livro.android.cap15;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * Activity que pode ser iniciada com uma Intent ACTION_VIEW:
 * 
 * Exemplo:
 * 
 * --------------------------------------------------- int id = 6; Uri uri =
 * Carros.getUriId(id); startActivity(new Intent(Intent.ACTION_VIEW,uri));
 * ---------------------------------------------------
 * 
 * Utiliza o TableLayout para visualizar o carro
 * 
 * @author rlecheta
 * 
 */
public class VisualizarCarro extends Activity {
	private static final String CATEGORIA = "livro";

	// Campos texto
	private TextView campoNome;
	private TextView campoPlaca;
	private TextView campoAno;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.visualizar_carro);

		String nome = null;
		String placa = null;
		int ano = 0;

		campoNome = (TextView) findViewById(R.id.cnome);
		campoPlaca = (TextView) findViewById(R.id.cplaca);
		campoAno = (TextView) findViewById(R.id.cano);

		// Uri que contêm o endereço do carro
		// Ex: content://br.livro.android.provider.carro/carros/1
		Uri uri = getIntent().getData();

		if (uri != null) {
			Log.i(CATEGORIA, "Uri para visualizar: " + uri);

			// Busca no ContentProvider.
			Cursor c = getContentResolver().query(uri, null, null, null, null);

			boolean encontrou = c.moveToFirst();

			if (encontrou) {
				nome = c.getString(c.getColumnIndexOrThrow(Carros.NOME));
				placa = c.getString(c.getColumnIndexOrThrow(Carros.PLACA));
				ano = c.getInt(c.getColumnIndexOrThrow(Carros.ANO));
			}
		}

		if (nome != null) {
			campoNome.setText(nome);
		}
		if (placa != null) {
			campoPlaca.setText(placa);
		}
		if (ano != 0) {
			campoAno.setText(String.valueOf(ano));
		}
	}
}