package com.gecon.core.ml

import javax.inject.Inject

data class GestureInfo(
    val id: Int,
    val name: String,
    val command: String,
    val description: String,
    val iconResName: String
)

class GestureMapper @Inject constructor() {

    private val gestureMap = mapOf(
        0 to GestureInfo(
            id = 1,
            name = "Sonrisa",
            command = "Aceptar comando",
            description = "Sonríe para aceptar el comando actual",
            iconResName = "ic_smile"
        ),
        1 to GestureInfo(
            id = 2,
            name = "Fruncir el ceño",
            command = "Cancelar acción",
            description = "Frunce el ceño para cancelar la acción actual",
            iconResName = "ic_frown"
        ),
        2 to GestureInfo(
            id = 3,
            name = "Ojo derecho cerrado",
            command = "Avanzar",
            description = "Cierra el ojo derecho para avanzar al siguiente elemento",
            iconResName = "ic_wink_right"
        ),
        3 to GestureInfo(
            id = 4,
            name = "Ojo izquierdo cerrado",
            command = "Retroceder",
            description = "Cierra el ojo izquierdo para retroceder al elemento anterior",
            iconResName = "ic_wink_left"
        ),
        4 to GestureInfo(
            id = 5,
            name = "Movimiento de cejas",
            command = "Confirmar",
            description = "Levanta las cejas para confirmar la acción",
            iconResName = "ic_eyebrows"
        ),
        5 to GestureInfo(
            id = 6,
            name = "Mano abierta",
            command = "Detener",
            description = "Muestra la mano abierta para detener la acción actual",
            iconResName = "ic_hand_open"
        ),
        6 to GestureInfo(
            id = 7,
            name = "Mano cerrada",
            command = "Ejecutar",
            description = "Cierra la mano para ejecutar la acción seleccionada",
            iconResName = "ic_hand_open"
        ),
        7 to GestureInfo(
            id = 8,
            name = "Pulgar arriba",
            command = "Aceptar",
            description = "Muestra el pulgar hacia arriba para aceptar",
            iconResName = "ic_thumb_up"
        ),
        8 to GestureInfo(
            id = 9,
            name = "Pulgar abajo",
            command = "Rechazar",
            description = "Muestra el pulgar hacia abajo para rechazar",
            iconResName = "ic_thumb_down"
        ),
        9 to GestureInfo(
            id = 10,
            name = "Señal con dos dedos",
            command = "Pausa/Reproducir",
            description = "Muestra dos dedos para pausar o reproducir",
            iconResName = "ic_hand_open"
        ),
        10 to GestureInfo(
            id = 11,
            name = "Palma hacia la cámara",
            command = "Encender",
            description = "Muestra la palma hacia la cámara para encender",
            iconResName = "ic_hand_open"
        ),
        11 to GestureInfo(
            id = 12,
            name = "Gesto de OK con la mano",
            command = "Finalizar acción",
            description = "Muestra el gesto de OK para finalizar la acción",
            iconResName = "ic_hand_open"
        ),
        12 to GestureInfo(
            id = 13,
            name = "Gesto de círculo con la mano",
            command = "Reiniciar",
            description = "Haz un círculo con la mano para reiniciar",
            iconResName = "ic_hand_open"
        )
    )

    fun mapIndexToGesture(index: Int): GestureInfo {
        return gestureMap[index] ?: throw IllegalArgumentException("Índice de gesto no válido: $index")
    }

    fun getAllGestures(): List<GestureInfo> {
        return gestureMap.values.toList()
    }

    fun getGestureById(id: Int): GestureInfo? {
        return gestureMap.values.find { it.id == id }
    }
}