package com.example.cmtech.activity

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.cmtech.R
import com.example.cmtech.model.Bateria
import com.example.cmtech.repository.BateriaRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BateriaActivity : Activity() {

    private lateinit var bateriaRepository: BateriaRepository
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.bateria)

        val listViewBaterias = findViewById<ListView>(R.id.listViewBateria)

        sharedPreferences = getSharedPreferences("BateriaPrefs", Context.MODE_PRIVATE)

        bateriaRepository = BateriaRepository()

        atualizarListaBaterias(listViewBaterias)
    }

    private fun atualizarListaBaterias(listView: ListView) {
        bateriaRepository.buscarBaterias() { baterias, erro ->
            runOnUiThread {
                if (erro != null) {
                    Toast.makeText(this, "Erro ao buscar baterias: $erro", Toast.LENGTH_SHORT).show()
                    val bateriasSalvas = recuperarBateriasDoSharedPreferences()
                    atualizarListView(listView, bateriasSalvas)
                } else {
                    if (baterias != null) {
                        salvarBateriasNoSharedPreferences(baterias)
                        atualizarListView(listView, baterias.map {
                            "Status: ${it.status}\nNível de Energia: ${it.nivelEnergia}"
                        })
                    }
                }
            }
        }
    }

    private fun salvarBateriasNoSharedPreferences(baterias: List<Bateria>) {
        val editor = sharedPreferences.edit()
        val jsonBaterias = gson.toJson(baterias)
        editor.putString("baterias", jsonBaterias)
        editor.apply()
    }

    private fun recuperarBateriasDoSharedPreferences(): List<String> {
        val jsonBaterias = sharedPreferences.getString("baterias", null)
        return if (jsonBaterias != null) {
            val type = object : TypeToken<List<Bateria>>() {}.type
            val baterias: List<Bateria> = gson.fromJson(jsonBaterias, type)
            baterias.map { "Status: ${it.status}\nNível de Energia: ${it.nivelEnergia}" }
        } else {
            emptyList()
        }
    }

    private fun atualizarListView(listView: ListView, listaBateriasStr: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaBateriasStr)
        listView.adapter = adapter
    }
}