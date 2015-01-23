package com.oceanbrasil.agendaormlite.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.oceanbrasil.agendaormlite.R;
import com.oceanbrasil.agendaormlite.helper.FormularioHelper;
import com.oceanbrasil.agendaormlite.model.bean.Contato;
import com.oceanbrasil.agendaormlite.model.dao.ContatoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ListagemActivity extends ActionBarActivity {

    // Coleção de strings que serão exibidas
    private List<Contato> listaDeContatos = new ArrayList<>();

    // Tag usada para controle no LogCat
    private final String TAG = ActionBarActivity.class.getSimpleName();

    // Adapter usado para exibir as Strings na ListView
    private ArrayAdapter<Contato> adapter = null;

    // Objeto Helper do Formulário
    private FormularioHelper formularioHelper = null;

    // Layout de exibição da listview
    int adapterLayout = android.R.layout.simple_list_item_1;

    // Contato selecionado no click
    private Contato contatoSelecionado = null;

    // ListView que exibirá os dados do Contato
    private ListView lvListagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagemlayout);

        lvListagem = (ListView) findViewById(R.id.lvListagem);

        // Informa a listView que tem menu de contexto
        registerForContextMenu(lvListagem);

        carregarLista();


        formularioHelper = new FormularioHelper(this);

        Button btSalvar = (Button) findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContatoDAO dao = new ContatoDAO(ListagemActivity.this);
                try {
                    dao.cadastrar(formularioHelper.getContato());
                    carregarLista();
                    adapter.notifyDataSetChanged();
                    formularioHelper.setContato(new Contato());
                } catch (SQLException e) {
                    Log.e(TAG, "falha ao salvar Contato");
                } finally {
                    dao.close();
                }
            }
        });

        // Click simples
       /* lvListagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent form = new Intent(ListagemActivity.this, new FormularioHelper().setContato((Contato)lvListagem.getItemAtPosition(position)));

                contatoSelecionado = (Contato) lvListagem.getItemAtPosition(position);

                Log.i(TAG, "Contato selecionado Click Simples" + contatoSelecionado.getNome());
            }
        });*/

        lvListagem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                // Marca o aluno selecionado na ListView
                contatoSelecionado = (Contato) adapter.getItemAtPosition(position);

                return false;
            }
        });
    }

    /**
     * Método que solicita serviço da camada de modelo
     * e atualiza a lista de contatos exibida
     */
    private void carregarLista() {
        // Limpa a coleção de contatos exibidos
        listaDeContatos.clear();

        // Criação do objeto de persistência
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> lista = null;
        try{
            // Solicitação de serviço da camada model
            lista = dao.listar();
        }catch (SQLException e){
            // Tratamento da exceção lançada pela model
            Log.e(TAG, "falha ao carregar lista");
        }
        for(Contato contato: lista){
            listaDeContatos.add(contato);
        }
        // Encerramento da conexão
        dao.close();
        adapter = new ArrayAdapter<Contato>(this, adapterLayout, listaDeContatos);
        lvListagem.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listagemmenu, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuDeletar:
                excluirAluno();
                break;
            case R.id.menuAlterar:
                Toast.makeText(ListagemActivity.this, "Não Implementado!!! Apague e cadastre de novo :D", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void excluirAluno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirma a exclusão de: " + contatoSelecionado.getNome());

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContatoDAO dao = new ContatoDAO(ListagemActivity.this);
                try {
                    dao.excluir(contatoSelecionado);
                    dao.close();
                    carregarLista();
                    contatoSelecionado = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.setTitle("Confirmação de operação");
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
