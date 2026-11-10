package com.cursokotlin.examen_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.FabPosition
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

// ───────────────────────── Colores ─────────────────────────
private val Morado = Color(0xFF5B21B6)
private val MoradoTexto = Color(0xFF6D28D9)
private val Lila = Color(0xFFF0EAFB)
private val Fondo = Color(0xFFFBFAFF)
private val TextoOscuro = Color(0xFF1A1033)
private val TextoGris = Color(0xFF4B4B5A)

// Coordenadas aproximadas de la UPP (ajústalas si quieres el pin más exacto)
private val UPP = LatLng(19.9798144, -98.6846703)

// ───────────────────────── Modelo ─────────────────────────
data class Lenguaje(val nombre: String, val icono: Int)

// Agrega estos íconos en res/drawable (png o vector)
private val lenguajes = listOf(
    Lenguaje("Python", R.drawable.ic_python),
    Lenguaje("Java", R.drawable.ic_java),
    Lenguaje("Kotlin", R.drawable.ic_kotlin),
    Lenguaje("C#", R.drawable.ic_csharp),
    Lenguaje("C++", R.drawable.ic_cpp)
)

private val OlaShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height * 0.93f)
    cubicTo(
        size.width * 0.70f, size.height * 1.02f,
        size.width * 0.35f, size.height * 0.80f,
        0f, size.height * 0.97f
    )
    close()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewContainer()
        }
    }
}

// ───────────────────────── Scaffold (Reporte 5) ─────────────────────────
@Composable
fun ViewContainer() {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = { FAB() },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = Fondo
    ) { innerPadding ->
        Content(innerPadding)
    }
}

@Composable
fun FAB() {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = { Toast.makeText(context, "Hola", Toast.LENGTH_SHORT).show() },
        containerColor = Morado,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Agregar", modifier = Modifier.size(32.dp))
    }
}

// ───────────────────────── Contenido (LazyColumn, Reporte 1-2) ─────────────────────────
@Composable
fun Content(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo),
        contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding() + 96.dp)
    ) {
        item { Encabezado() }
        item { InfoPerfil() }
        item { SeccionLenguajes() }
        item { MapaUPP() }
    }
}

@Composable
fun Encabezado() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bakugo),
            contentDescription = "Campus UPP",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(OlaShape)
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 12.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_upp),
                contentDescription = "Logo UPP",
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.padding(end = 12.dp)) {
                Text("Universidad Politécnica", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text("de Pachuca", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 16.dp, top = 12.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Share, contentDescription = "Compartir", tint = Color.White, modifier = Modifier.size(20.dp))
        }

        // Foto de perfil + insignia de cámara
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp)
                .size(92.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.foto_perfil),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(6.dp, CircleShape)
                    .border(3.dp, Color.White, CircleShape)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Morado)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }

        // Etiqueta "UPP Software"
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 6.dp)
                .clip(RoundedCornerShape(50))
                .background(Lila)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.School, contentDescription = null, tint = Morado, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("UPP TIID", color = Morado, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun InfoPerfil() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Karina Lazcano Báez",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextoOscuro
            )
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Filled.Verified, contentDescription = "Verificado", tint = Morado, modifier = Modifier.size(24.dp))
        }
        Text(
            text = "Ingeniero en Software | Desarrollador Mobile & Web",
            color = MoradoTexto,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(
            text = "Apasionada por el desarrollo de aplicaciones móviles, arquitectura de software e inteligencia artificial en la UPP.",
            color = TextoGris,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
fun SeccionLenguajes() {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        // Row con SpaceBetween + fillMaxWidth + padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Code, contentDescription = null, tint = MoradoTexto, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "LENGUAJES DE PROGRAMACIÓN",
                    color = TextoOscuro,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Desliza", color = TextoGris.copy(alpha = 0.8f), fontSize = 13.sp)
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextoGris.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // LazyRow: lista horizontal desplazable
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lenguajes) { lenguaje ->
                ChipLenguaje(lenguaje)
            }
        }
    }
}

@Composable
fun ChipLenguaje(lenguaje: Lenguaje) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Lila)
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = lenguaje.icono),
            contentDescription = lenguaje.nombre,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(lenguaje.nombre, color = TextoOscuro, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MapaUPP() {
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(UPP, 16f)
    }
    var tipoMapa by remember { mutableStateOf(MapType.NORMAL) }
    val properties = MapProperties(mapType = tipoMapa)

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                mapToolbarEnabled = false,
                compassEnabled = false
            )
        )
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            Marker(
                state = rememberMarkerState(position = UPP),
                title = "Universidad Politécnica de Pachuca (UPP)",
                snippet = "Zempoala, Hidalgo"
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotonMapa(Icons.Filled.Layers) {
                tipoMapa = if (tipoMapa == MapType.NORMAL) MapType.HYBRID else MapType.NORMAL
            }
            BotonMapa(Icons.Filled.MyLocation) {
                scope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(UPP, 16f))
                }
            }
        }
    }
}

@Composable
fun BotonMapa(icono: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icono, contentDescription = null, tint = Color(0xFF444444), modifier = Modifier.size(26.dp))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PerfilPreview() {
    ViewContainer()
}
