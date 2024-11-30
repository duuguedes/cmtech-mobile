package com.example.cmtech.activity

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.cmtech.R
import com.example.cmtech.model.EstacaoCarregamento
import com.example.cmtech.repository.EstacaoCarregamentoRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EstacaoCarregamentoActivity : Activity() {

    private lateinit var estacaoCarregamentoRepository: EstacaoCarregamentoRepository
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.estacao_carregamento)

        val listViewEstacoes = findViewById<ListView>(R.id.listViewEstacaoCarregamento)

        sharedPreferences = getSharedPreferences("EstacaoCarregamentoPrefs", Context.MODE_PRIVATE)

        estacaoCarregamentoRepository = EstacaoCarregamentoRepository()

        atualizarListaEstacoes(listViewEstacoes)
    }

    private fun atualizarListaEstacoes(listView: ListView) {
        estacaoCarregamentoRepository.buscarEstacoesCarregamento() { estacoes, erro ->
            runOnUiThread {
                if (erro != null) {
                    Toast.makeText(this, "Erro ao buscar estações de carregamento: $erro", Toast.LENGTH_SHORT).show()
                    val estacoesSalvas = recuperarEstacoesDoSharedPreferences()
                    atualizarListView(listView, estacoesSalvas)
                } else {
                    if (estacoes != null) {
                        salvarEstacoesNoSharedPreferences(estacoes)
                        atualizarListView(listView, estacoes.map {
                            "Localização: ${it.localizacao}\nCapacidade Máxima: ${it.capacidadeMaxima} kW"
                        })
                    }
                }
            }
        }
    }

    private fun salvarEstacoesNoSharedPreferences(estacoes: List<EstacaoCarregamento>) {
        val editor = sharedPreferences.edit()
        val jsonEstacoes = gson.toJson(estacoes)
        editor.putString("estacoes", jsonEstacoes)
        editor.apply()
    }

    private fun recuperarEstacoesDoSharedPreferences(): List<String> {
        val jsonEstacoes = sharedPreferences.getString("estacoes", null)
        return if (jsonEstacoes != null) {
            val type = object : TypeToken<List<EstacaoCarregamento>>() {}.type
            val estacoes: List<EstacaoCarregamento> = gson.fromJson(jsonEstacoes, type)
            estacoes.map { "Localização: ${it.localizacao}\nCapacidade Máxima: ${it.capacidadeMaxima} kW" }
        } else {
            emptyList()
        }
    }

    private fun atualizarListView(listView: ListView, listaEstacoesStr: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaEstacoesStr)
        listView.adapter = adapter
    }
}