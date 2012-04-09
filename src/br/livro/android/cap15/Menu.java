package br.livro.android.cap15;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * Exemplos de ContentProvider
 * 
 * @author rlecheta
 * 
 */
public class Menu extends ListActivity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		String[] mStrings = new String[] { 
				"ListActivity - simples",
				"Cadastro Carros",
				"Intent:VIEW_ACTION",
				"Intent:VIEW_ACTION, id=1"};

		this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mStrings));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long itemId) {

		switch (position) {
			case 0:
				//Carros
				startActivity(new Intent(this,ListarCarros.class));
				break;
			case 1:
				//Carros
				startActivity(new Intent(this,CadastroCarros.class));
				break;
			case 2:
				//Vai abrir a Activity ListActivityCursorCarros. Veja o AndroidManifest
				startActivity(new Intent(Intent.ACTION_VIEW,Carros.CONTENT_URI));
				break;
			case 3:
				//Edite para algum id que exista
				int id = 1;
				Uri uri = Carros.getUriId(id);
				startActivity(new Intent(Intent.ACTION_VIEW,uri));
				
				//Vai abrir a Activity VisualizarCarro. Veja o AndroidManifest

				break;
			default:
				finish();
		}
	}
}