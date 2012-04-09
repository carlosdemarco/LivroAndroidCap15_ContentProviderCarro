package br.livro.android.cap15;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * ListActivity que utiliza o Cursor recuperado do Content Provider 'CarroProvider"
 * 
 * Exemplo de ListActivity que utiliza o SimpleCursorAdapter
 * para enviar os dados do Cursor de carros para uma view 'nativa' do Android
 * 
 * @author ricardo
 *
 */
public class ListarCarros extends ListActivity {
	private ListAdapter mAdapter;

	private static final String TAG = "ID";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Log.i(TAG,"Uri carros: " + Carros.CONTENT_URI);

        //Recupera o cursor dos contatos
        Cursor c = getContentResolver().query(Carros.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);

        //Listar o nome e telefone do contato
        String[] columns = new String[]{Carros.NOME,Carros.PLACA};

        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

        //layout nativo para simplificar
        int layoutNativo = android.R.layout.simple_list_item_2;

        //Informa o adapter para ligar os valores ao XML da View
		mAdapter = new SimpleCursorAdapter(this, layoutNativo, c, columns, to);

        setListAdapter(mAdapter);
    }

    /**
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //recupera o cursor na posição selecionada
        Cursor c = (Cursor) mAdapter.getItem(position);

        //recupera o Nome e Telefone
        String nome 	= c.getString(c.getColumnIndexOrThrow(Carros.NOME));
        String placa 	= c.getString(c.getColumnIndexOrThrow(Carros.PLACA));
        int ano  		= c.getInt(c.getColumnIndexOrThrow(Carros.ANO));

        Toast.makeText(this, "Carro selecionado: Nome: " + nome + ", Placa: " + placa+ ", Ano: " + ano, Toast.LENGTH_SHORT).show();
    }
}
