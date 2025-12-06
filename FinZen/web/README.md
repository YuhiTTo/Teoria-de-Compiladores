
# Interfaz Web para FinZen (Streamlit)

Esta carpeta contiene una interfaz web en Streamlit para tu compilador **FinZen**.

## Estructura recomendada del proyecto

Coloca esta carpeta `web/` dentro de tu proyecto `FinZen`:

FinZen/
├── src/
├── build/
├── run.sh
└── web/
    ├── app.py
    └── requirements.txt

El script `run.sh` debe encargarse de:
1. Compilar el código FinZen (si es necesario).
2. Ejecutar el compilador `FinZen` sobre el archivo `.finzen` que se le pasa como argumento.
3. Ejecutar el programa resultante (si aplica) y mostrar la salida en consola.

## Instalación de dependencias

Desde la raíz de tu proyecto (FinZen_v2), ejecuta:

```bash
pip install -r web/requirements.txt
```

## Ejecución de la interfaz

Desde la raíz `FinZen_v2`:

```bash
streamlit run web/app.py
```

Esto abrirá (o indicará) una URL local como `http://localhost:8501` donde podrás usar la interfaz.

## Notas

- Asegúrate de que `run.sh` existe en la raíz del proyecto y tenga permisos de ejecución:

  ```bash
  chmod +x run.sh
  ```

- El archivo `app.py` asume que `run.sh` recibe como argumento el path de un archivo `.finzen`
  e imprime en consola tanto los mensajes del compilador como la salida del programa generado.
