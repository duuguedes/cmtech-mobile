package com.example.cmtech.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.example.cmtech.R
import com.example.cmtech.model.Veiculo
import com.example.cmtech.repository.VeiculoRepository

class VeiculoActivity : Activity() {
    private lateinit var veiculoRepository: VeiculoRepository
    private lateinit var veiculos: List<Veiculo>
    private lateinit var listViewVeiculo: ListView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.veiculo)

        listViewVeiculo = findViewById(R.id.listViewVeiculo)
        sharedPreferences = getSharedPreferences("VeiculoPrefs", Context.MODE_PRIVATE)
        veiculoRepository = VeiculoRepository()

        atualizarListaVeiculos(listViewVeiculo)

        listViewVeiculo.setOnItemLongClickListener { _, _, position, _ ->
            val veiculoSelecionado = veiculos[position]
            abrirTelaDeEdicao(veiculoSelecionado)
            true
        }

        listViewVeiculo.setOnItemClickListener { _, _, position, _ ->
            val veiculoSelecionado = veiculos[position]
            confirmarExclusao(veiculoSelecionado)
        }

        carregarUltimosDados()
    }

    private fun carregarUltimosDados() {
        val modelo = sharedPreferences.getString("modelo", "")
        val ano = sharedPreferences.getInt("ano", 0)

        if (!modelo.isNullOrEmpty() || ano != 0) {
            Toast.makeText(this, "Dados do último veículo editado carregados.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun atualizarListaVeiculos(listView: ListView) {
        veiculoRepository.buscarVeiculos { veiculos, erro ->
            if (erro != null) {
                runOnUiThread {
                    Toast.makeText(this, "Erro ao buscar veículos: $erro", Toast.LENGTH_SHORT).show()
                }
            } else {
                this.veiculos = veiculos ?: emptyList()

                val listaVeiculosStr = this.veiculos.map {
                    "Modelo: ${it.modelo}\nAno: ${it.ano}"
                }

                runOnUiThread {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVeiculosStr)
                    listView.adapter = adapter
                }
            }
        }
    }

    private fun abrirTelaDeEdicao(veiculo: Veiculo) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_editar_veiculo, null)

        val editModelo = dialogView.findViewById<EditText>(R.id.editModelo)
        val editAno = dialogView.findViewById<EditText>(R.id.editAno)

        editModelo.setText(veiculo.modelo)
        editAno.setText(veiculo.ano.toString())

        AlertDialog.Builder(this)
            .setTitle("Editar Veículo")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                veiculo.modelo = editModelo.text.toString()
                veiculo.ano = editAno.text.toString().toInt()

                salvarDadosNoSharedPreferences(veiculo)

                veiculoRepository.editarVeiculo(veiculo) { sucesso, erro ->
                    if (sucesso) {
                        atualizarListaVeiculos(listViewVeiculo)
                        Toast.makeText(this, "Veículo editado com sucesso", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Erro ao editar veículo: $erro", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun salvarDadosNoSharedPreferences(veiculo: Veiculo) {
        val editor = sharedPreferences.edit()
        editor.putString("modelo", veiculo.modelo)
        editor.putInt("ano", veiculo.ano)
        editor.apply()
    }

    private fun confirmarExclusao(veiculo: Veiculo) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Você realmente deseja excluir este veículo?")
            .setPositiveButton("Sim") { _, _ ->
                veiculo.id?.let { id ->
                    veiculoRepository.excluirVeiculo(id) { sucesso, erro ->
                        if (sucesso) {
                            atualizarListaVeiculos(listViewVeiculo)
                            Toast.makeText(this, "Veículo excluído com sucesso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Erro ao excluir veículo: $erro", Toast.LENGTH_SHORT).show()
                        }
                    }
                } ?: run {
                    Toast.makeText(this, "ID do veículo não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Não", null)
            .show()
    }
}