package com.example.actividad_8componentelistview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText tfNumber1, tfNumber2;
    private Button buttonCalculate;
    private ListView listView;
    private ArrayList<String> resultsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Evitar que el teclado oculte las vistas
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tfNumber1 = findViewById(R.id.tfNumber1);
        tfNumber2 = findViewById(R.id.tfNumber2);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        listView = findViewById(R.id.listView);

        resultsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultsList);
        listView.setAdapter(adapter);

        // Llama al método para gestionar la visibilidad del teclado
        setupKeyboardVisibilityListener(this, new OnKeyboardVisibilityListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean keyboardVisible) {
                if (keyboardVisible) {
                    // El teclado está visible, ajusta las vistas si es necesario
                    adjustViewsForKeyboard();
                } else {
                    // El teclado se oculta, restaura las vistas a su posición original
                    restoreViews();
                }
            }
        });

        // Agregar un clic de elemento a la lista para mostrar Toast con el registro
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                showToast(selectedItem);
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void adjustViewsForKeyboard() {
        // Ajusta las vistas (puede mover hacia arriba las cajas de texto o el botón)
    }

    private void restoreViews() {
        // Restaura las vistas a su posición original
    }

    private void calculate() {
        String num1Str = tfNumber1.getText().toString();
        String num2Str = tfNumber2.getText().toString();

        if (!num1Str.isEmpty() && !num2Str.isEmpty()) {
            double num1 = Double.parseDouble(num1Str);
            double num2 = Double.parseDouble(num2Str);
            double result = num1 + num2;

            String calculation = num1Str + " + " + num2Str + " = " + result;
            resultsList.add(calculation);
            adapter.notifyDataSetChanged();

            tfNumber1.setText("");
            tfNumber2.setText("");

            // Oculta el teclado después de realizar el cálculo
            hideKeyboard();
        }
    }


    // Utilidad para gestionar la visibilidad del teclado
    public static void setupKeyboardVisibilityListener(Activity activity, OnKeyboardVisibilityListener listener) {
        final View contentView = activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect rect = new Rect();
            private boolean wasOpened = false;

            @Override
            public void onGlobalLayout() {
                contentView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = contentView.getHeight();
                int keypadHeight = screenHeight - rect.bottom;

                if (keypadHeight > screenHeight * 0.15) { // Si el teclado ocupa más del 15% de la pantalla
                    if (!wasOpened) {
                        wasOpened = true;
                        listener.onKeyboardVisibilityChanged(true);
                    }
                } else {
                    if (wasOpened) {
                        wasOpened = false;
                        listener.onKeyboardVisibilityChanged(false);
                    }
                }
            }
        });
    }

    public interface OnKeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean keyboardVisible);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
