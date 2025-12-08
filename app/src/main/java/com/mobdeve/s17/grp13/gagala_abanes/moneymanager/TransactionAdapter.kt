package com.mobdeve.s17.grp13.gagala_abanes.moneymanager

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter (private var transactions: List<Transaction>, private val tagMap: Map<String, String>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagLogo: ImageView = itemView.findViewById(R.id.catLogo)
        val thCategory: TextView = itemView.findViewById(R.id.thCategory)
        val thAmount: TextView = itemView.findViewById(R.id.thAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_transaction_history, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        Log.d("MoneyManagerDB", "Binding transaction: ${transaction.tag} ${transaction.amount}")
        holder.thCategory.text = tagMap[transaction.tag] ?: transaction.tag
        holder.thAmount.text = "PHP ${String.format("%.2f", transaction.amount)}"
    }

    fun updateData(newList: List<Transaction>) {
        this.transactions = newList
        notifyDataSetChanged()
    }
}