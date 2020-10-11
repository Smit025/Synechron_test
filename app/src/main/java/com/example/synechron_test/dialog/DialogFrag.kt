package com.example.synechron_test.dialog


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class DialogFrag : DialogFragment() {

    companion object {

        val ARG_MSG: String = "ARG_MSG"
        fun newInstance(msg: String?): DialogFrag? {
            val fragment = DialogFrag()
            val args = Bundle()
            args.putString("ARG_MSG", msg)
            fragment.setArguments(args)
            return fragment
        }
    }

    var message: String? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (getArguments() != null) {
            message = getArguments()?.getString(DialogFrag.ARG_MSG);

        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Attention!")
            .setMessage(message)
            .setNegativeButton("Cancel",
                { dialogInterface, i -> dialog?.dismiss() })
            .setPositiveButton("Ok",
                { dialogInterface, i -> dialog?.dismiss() })
        return builder.create()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}