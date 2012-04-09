package br.livro.android.cap15.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import br.livro.android.cap14.banco.Carro;
import br.livro.android.cap14.banco.RepositorioCarro;
import br.livro.android.cap14.banco.RepositorioCarroScript;
import br.livro.android.cap14.banco.Carro.Carros;

/**
 * ---------------------------------------------------- URI:
 * content://br.livro.android.provider.Carro/carros
 * content://br.livro.android.provider.Carro/carros/1
 * ----------------------------------------------------
 * 
 * Implementa um Content Provider para armazenar Carros
 * 
 * Para inserir/recuperar os Carros do banco é utilizada a classe
 * 
 * RepositorioCarro criada anteriormente, a qual encapsula o código do SQLite
 * 
 * @author rlecheta
 * 
 */
public class CarroProvider extends ContentProvider {
	// Delegar as operaçoes CRUD para o repositório
	private RepositorioCarroScript rep;

	// Colunas para selecionar
	private static HashMap<String, String> colunas;

	private static final int CARROS = 1;
	private static final int CARROS_ID = 2;

	// Utilizada para validar as expressões regulares sobre a Uri
	private static final UriMatcher uriCarro;

	static {
		// Uri Matcher para fazer as expressões regulares
		uriCarro = new UriMatcher(UriMatcher.NO_MATCH);
		// content://br.livro.android.provider.carro/carros
		uriCarro.addURI(Carro.AUTHORITY, "carros", CARROS);
		// content://br.livro.android.provider.carro/carros/id
		uriCarro.addURI(Carro.AUTHORITY, "carros/#", CARROS_ID);

		// Colunas para selecionar
		colunas = new HashMap<String, String>();
		colunas.put(Carros._ID, Carros._ID);
		colunas.put(Carros.NOME, Carros.NOME);
		colunas.put(Carros.PLACA, Carros.PLACA);
		colunas.put(Carros.ANO, Carros.ANO);
	}

	@Override
	public boolean onCreate() {
		rep = new RepositorioCarroScript(getContext());
		return true;
	}

	@Override
	// Retorna o MIME type correto
	public String getType(Uri uri) {
		switch (uriCarro.match(uri)) {
		case CARROS:
			return Carros.CONTENT_TYPE;

		case CARROS_ID:
			return Carros.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("URI desconhecida: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Valida se a Uri é de /carros
		if (uriCarro.match(uri) != CARROS) {
			throw new IllegalArgumentException("URI desconhecida: " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Valida se tudo está preenchido
		if (values.containsKey(Carros.NOME) == false) {
			values.put(Carros.NOME, "");
		}
		if (values.containsKey(Carros.PLACA) == false) {
			values.put(Carros.PLACA, "");
		}
		if (values.containsKey(Carros.ANO) == false) {
			values.put(Carros.ANO, "");
		}

		long id = rep.inserir(values);
		if (id > 0) {
			// Inseriu
			Uri uriCarro = Carros.getUriId(id);

			getContext().getContentResolver().notifyChange(uriCarro, null);

			// Retorna a Uri com o id do carro inserido
			return uriCarro;
		}

		throw new SQLException("Falhou ao inserir " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// Classe utilitária para criar queries no SQLite
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();

		// Configura a tabela para busca e a projeção
		switch (uriCarro.match(uri)) {
		case CARROS:
			query.setTables(RepositorioCarro.NOME_TABELA);
			query.setProjectionMap(colunas);
			break;
		case CARROS_ID:
			query.setTables(RepositorioCarro.NOME_TABELA);
			query.setProjectionMap(colunas);
			// where _id=?
			query.appendWhere(Carros._ID + "=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("URI desconhecida: " + uri);
		}

		// Ordenação
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Carros.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		// Query
		Cursor c = rep.query(query, projection, selection, selectionArgs, null, null, orderBy);

		// Notifica o content provider
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		int count;
		switch (uriCarro.match(uri)) {
		case CARROS:
			count = rep.atualizar(values, where, whereArgs);
			break;
		case CARROS_ID:
			// id do carro
			String id = uri.getPathSegments().get(1);
			// Atualiza o where se informado com o "_id=?" para atualizar
			String whereFinal = Carros._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			count = rep.atualizar(values, whereFinal, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("URI desconhecida: " + uri);
		}

		// Notifica o content provider
		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;
		switch (uriCarro.match(uri)) {
		case CARROS:
			count = rep.deletar(where, whereArgs);
			break;
		case CARROS_ID:
			// id do carro
			String id = uri.getPathSegments().get(1);
			// Atualiza o where se informado com o "_id=?" para atualizar
			String whereFinal = Carros._ID + "=" + id + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
			count = rep.deletar(whereFinal, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("URI desconhecida: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}
}
