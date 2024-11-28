package com.example.shoppingchecklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.shoppingchecklist.ui.theme.ShoppingCheckListTheme
import androidx.compose.ui.Alignment

data class ShoppingItem(val name: String, var isChecked: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingCheckListTheme {
                ShoppingCheckListApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCheckListApp() {
    var shoppingList by remember { mutableStateOf<List<ShoppingItem>>(emptyList()) }
    var newItemName by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Надо купить!") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Поле для добавления новой покупки
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Новая покупка") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box (
                    modifier = Modifier.align(Alignment.CenterVertically) // Выравнивание кнопки
                ) {
                Button(
                    onClick = {
                        if (newItemName.text.isNotEmpty()) {
                            shoppingList = shoppingList + ShoppingItem(newItemName.text, false)
                            newItemName = TextFieldValue("") // Очистка поля
                        }
                    }
                ) {
                    Text("Добавить")
                }
            }
                }

            Spacer(modifier = Modifier.height(16.dp))

            // Список покупок
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(shoppingList) { item ->
                    val index = shoppingList.indexOf(item) // Получаем индекс элемента в списке
                    ShoppingListItem(
                        item = item,
                        onCheckedChange = { isChecked ->
                            // Обновляем элемент по индексу
                            shoppingList = shoppingList.toMutableList().apply {
                                this[index] = this[index].copy(isChecked = isChecked)
                            }
                        }
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка удаления всех отмеченных элементов
            Button(
                onClick = {
                    shoppingList = shoppingList.filterNot { it.isChecked }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Удалить")
            }
        }
    }
}

@Composable
fun ShoppingListItem(item: ShoppingItem, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically // Центрирование всех элементов по вертикали
    ) {
        Text(item.name, modifier = Modifier.weight(1f))
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
