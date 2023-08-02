package com.spawndev.firebaseExample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spawndev.firebaseExample.model.Calificaciones

class CalificacionesAdapter : RecyclerView.Adapter<CalificacionesAdapter.ViewHolder>() {

    private var calificacionesList: List<Calificaciones> = emptyList()

    fun setCalificacionesList(calificacionesList: List<Calificaciones>) {
        this.calificacionesList = calificacionesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calificaciones, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calificaciones = calificacionesList[position]
        holder.bind(calificaciones)
    }

    override fun getItemCount(): Int {
        return calificacionesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val nControlTextView: TextView = itemView.findViewById(R.id.nControlTextView)
        private val grupoTextView: TextView = itemView.findViewById(R.id.grupoTextView)
        private val calificacionTextView: TextView = itemView.findViewById(R.id.calificacionTextView)
        private val nControlAsignaturaTextView: TextView = itemView.findViewById(R.id.nControlAsignaturaTextView)

        fun bind(calificaciones: Calificaciones) {
            //nControlTextView.text = calificaciones.nControl.toString()
            grupoTextView.text = calificaciones.grupo
            calificacionTextView.text = calificaciones.calificacion.toString()
            nControlAsignaturaTextView.text = calificaciones.nControlAsignatura
        }
    }
}