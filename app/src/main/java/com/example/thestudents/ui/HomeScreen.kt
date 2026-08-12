package com.example.thestudents.ui


import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.thestudents.R
import com.example.thestudents.ui.theme.TheStudentsTheme
import androidx.compose.material3.Button

//composables: son funciones que tienen un componente gráfico de mi pantalla como piezas de rompecabezas
@Composable
fun MensajeBienvenida(modifier: Modifier = Modifier) {
    Text(
        text = "Bienvenido a The Students",
        modifier = modifier,
        fontSize = 20.sp, //para que este acorde a la configuración del usario,
        fontWeight = FontWeight.Bold,
        color = Color.Blue
    )
}

@Preview(showBackground = true)//forma de ir visualizando los cambios de forma rapida sin tener que instalarlo en el cel
@Composable
fun MensajeBienvenidaPreview() {
    TheStudentsTheme {
        MensajeBienvenida()
    }
}

@Composable
fun LogoApp(){
    Image(
        painterResource(R.drawable.logosinfondo),
        contentDescription = "Logo The Students"
    )
}


@Preview(showBackground = true)//forma de ir visualizando los cambios de forma rapida sin tener que instalarlo en el cel
@Composable
fun LogoAppPreview() {
    LogoApp()
}


@Composable
fun AppButton(){
    Button(onClick = {/*TODO*/}
    ){
    }
}




// row, colum, box
@Composable
fun BodyHomeScreen(){
    Column{
        LogoApp()
        MensajeBienvenida()

    }
}

@Composable
@Preview(showBackground = true)
fun BodyHomeScreenPreview(){
    BodyHomeScreen()
}