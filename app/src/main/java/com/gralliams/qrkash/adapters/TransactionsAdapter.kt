package com.gralliams.qrkash.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.gralliams.qrkash.databinding.ListItemTransactionBinding
import com.gralliams.qrkash.db.TransactionItem

class TransactionsAdapter(context: Context, transactions: List<TransactionItem>) :
    ArrayAdapter<TransactionItem>(context, 0, transactions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ListItemTransactionBinding

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = ListItemTransactionBinding.inflate(inflater, parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ListItemTransactionBinding
        }

        val transaction = getItem(position)

        // Set data using ViewBinding
        binding.textViewDescription.text = transaction?.description
        binding.textViewAmount.text = "₦${transaction?.amount}"

        return binding.root
    }
}
