package br.livro.android.cap15;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * Activity que demonstra o cadastro de carros:
 * 
 * - ListActivity: para listar os carros
 * - RepositorioCarro para salvar os dados no banco
 * - Carro
 * 
 * @author rlecheta
 *
 */
public class CadastroCarros extends br.livro.android.cap14.banco.CadastroCarros {
	private static final String CATEGORIA = "livro";
	private Cursor cursor;
	
	@Override
	protected void atualizarLista() {
		cursor = managedQuery(Carros.CONTENT_URI, null, null, null, null);
		//Gerencia o cursor automaticamente. Ao fechar a activity, o cursor é fechado também.
		startManagingCursor(cursor);

		String[] campos = new String[] { Carros.NOME, Carros.PLACA,Carros.ANO };
		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.carro_linha_tabela, cursor, campos, new int[] {R.id.nome, R.id.placa, R.id.ano });
		setListAdapter(adapter);
	}

	@Override
	//Sobrescreve para informar a classe correta de EditarCarro e BuscarCarro
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//Clicou no menu
		switch (item.getItemId()) {
			case INSERIR_EDITAR:
				//Abre a tela com o formulário para adicinoar
				startActivityForResult(new Intent(this, EditarCarro.class), INSERIR_EDITAR);
				break;
			case BUSCAR:
				//Abre a tela para buscar o carro pelo nome
				startActivity(new Intent(this, BuscarCarro.class));
			break;
		}
		return true;
	}

	@Override
	protected void editarCarro(int posicao) {
		//Cria a intent para abrir a tela de editar
		Intent it = new Intent(this, EditarCarro.class);

		//Descobre o id do carro selecionado, o cursor já fica posicionado corretamente
		long id = cursor.getLong(cursor.getColumnIndexOrThrow(Carros._ID));

		Log.i(CATEGORIA, "Id do carro: " + id);

		//Passa o id do carro como parâmetro
		it.putExtra(Carros._ID, id);

		//Abre a tela de edição
		startActivityForResult(it, INSERIR_EDITAR);
	}
}