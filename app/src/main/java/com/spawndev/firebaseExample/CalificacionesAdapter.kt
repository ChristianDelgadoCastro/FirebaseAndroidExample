package com.spawndev.firebaseExample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.spawndev.firebaseExample.model.Calificaciones

class CalificacionesAdapter : RecyclerView.Adapter<CalificacionesAdapter.ViewHolder>() {
    private var calificacionesList: List<Calificaciones> = emptyList()

    private val backgroundColors = listOf(
        R.color.color1,
        R.color.color2,
        R.color.color3,
        R.color.color4,
        R.color.color5,
        R.color.color6,
        R.color.color7
    )

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

        // Obtener el ConstraintLayout de cada elemento
        val itemLayout = holder.itemView.findViewById<ConstraintLayout>(R.id.itemLayout)

        // Cambiar el color de fondo en función de la posición
        val colorRes = backgroundColors[position % backgroundColors.size]
        val backgroundColor = ContextCompat.getColor(holder.itemView.context, colorRes)
        itemLayout.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount(): Int {
        return calificacionesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val nControlTextView: TextView = itemView.findViewById(R.id.nControlTextView)
        private val grupoTextView: TextView = itemView.findViewById(R.id.grupoTextView)
        private val calificacionTextView: TextView = itemView.findViewById(R.id.calificacionTextView)
        private val asignaturaTextView: TextView = itemView.findViewById(R.id.asignaturaTextView)

        fun bind(calificaciones: Calificaciones) {
            //nControlTextView.text = calificaciones.nControl.toString()
            grupoTextView.text = calificaciones.grupo
            calificacionTextView.text = calificaciones.calificacion.toString()
            // Mostrar el nombre de la asignatura en lugar del nControlAsignatura
            asignaturaTextView.text = calificaciones.asignatura?.asignatura ?: ""
        }
    }

}