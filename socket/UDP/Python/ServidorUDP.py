import socket
import tkinter as tk
from tkinter import scrolledtext, messagebox
import threading

class UDPServerApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Servidor UDP")

        # Crear widgets de la interfaz
        self.create_widgets()

        # Inicializar variables del socket
        self.server_socket = None
        self.client_addr = None
        self.server_running = False

    def create_widgets(self):
        # Configuración de IP y Puerto
        frame = tk.Frame(self.root)
        frame.pack(pady=10)

        tk.Label(frame, text="IP:").grid(row=0, column=0, padx=5)
        self.ip_entry = tk.Entry(frame, width=20)
        self.ip_entry.grid(row=0, column=1)
        self.ip_entry.insert(0, '127.0.0.1')  # Valor predeterminado

        tk.Label(frame, text="Puerto:").grid(row=1, column=0, padx=5)
        self.port_entry = tk.Entry(frame, width=20)
        self.port_entry.grid(row=1, column=1)
        self.port_entry.insert(0, '8080')  # Valor predeterminado

        # Botones para iniciar y detener servidor
        self.update_button = tk.Button(self.root, text="Activar Servidor", command=self.update_socket)
        self.update_button.pack(pady=5)

        self.stop_button = tk.Button(self.root, text="Detener Servidor", command=self.stop_server, state='disabled')
        self.stop_button.pack(pady=5)

        # Campo de entrada de mensaje
        tk.Label(self.root, text="Mensaje:").pack(pady=5)
        self.message_entry = tk.Entry(self.root, width=50)
        self.message_entry.pack(padx=10, pady=5)

        # Botón para enviar mensaje
        self.send_button = tk.Button(self.root, text="Enviar", command=self.send_message)
        self.send_button.pack(padx=10, pady=5)

        # Etiqueta de estado
        self.status_label = tk.Label(self.root, text="Estado: Inactivo")
        self.status_label.pack(padx=10, pady=5)

        # Área de texto para mostrar mensajes
        self.text_area = scrolledtext.ScrolledText(self.root, state='disabled', width=60, height=10)
        self.text_area.pack(padx=10, pady=10)

    def update_socket(self):
        ip = self.ip_entry.get()
        port_str = self.port_entry.get()

        # Validar IP y Puerto
        if not self.validate_ip(ip):
            messagebox.showerror("Error", "Dirección IP no válida")
            return

        if not port_str.isdigit() or not (1024 <= int(port_str) <= 65535):
            messagebox.showerror("Error", "Número de puerto no válido (debe ser entre 1024 y 65535)")
            return

        port = int(port_str)

        # Cerrar el socket existente si está abierto
        self.stop_server()

        # Crear y vincular el nuevo socket UDP
        try:
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.server_socket.bind((ip, port))
            self.server_running = True
            self.status_label.config(text=f"Estado: Activo en {ip}:{port}")
            self.display_message(f"Servidor UDP escuchando en {ip}:{port}")

            # Habilitar el botón de detener y deshabilitar el de activar
            self.update_button.config(state='disabled')
            self.stop_button.config(state='normal')

            # Iniciar la recepción de mensajes en un hilo separado
            threading.Thread(target=self.receive_messages, daemon=True).start()
        except Exception as e:
            self.display_message(f"Error al iniciar el servidor: {e}")
            self.status_label.config(text="Estado: Inactivo")

    def validate_ip(self, ip):
        # Validar dirección IP
        try:
            socket.inet_pton(socket.AF_INET, ip)
            return True
        except socket.error:
            return False

    def receive_messages(self):
        while self.server_running:
            try:
                data, addr = self.server_socket.recvfrom(1024)
                self.client_addr = addr  # Guardar la dirección del cliente
                self.display_message(f"Mensaje recibido de {self.client_addr}: {data.decode()}")
                
                # Responder al cliente
                response = f"Servidor: Recibí tu mensaje '{data.decode()}'"
                self.server_socket.sendto(response.encode(), self.client_addr)

                if data.decode().lower() == 'salir':
                    self.display_message("Servidor: Cliente terminó la conexión.")
                    break
            except Exception as e:
                if self.server_running:
                    self.display_message(f"Error en la recepción de mensajes: {e}")
                break

    def send_message(self):
        if self.client_addr:
            server_message = self.message_entry.get()
            if server_message:
                self.server_socket.sendto(server_message.encode(), self.client_addr)
                self.display_message(f"Servidor: {server_message}")
                self.message_entry.delete(0, tk.END)
        else:
            messagebox.showwarning("Advertencia", "No hay cliente conectado para enviar el mensaje")

    def stop_server(self):
        if self.server_socket:
            self.server_running = False
            self.server_socket.close()
            self.server_socket = None
            self.status_label.config(text="Estado: Inactivo")
            self.display_message("Servidor detenido.")
            self.update_button.config(state='normal')
            self.stop_button.config(state='disabled')

    def display_message(self, message):
        self.text_area.config(state='normal')
        self.text_area.insert(tk.END, message + '\n')
        self.text_area.config(state='disabled')
        self.text_area.yview(tk.END)

# Configuración de la ventana principal
root = tk.Tk()
app = UDPServerApp(root)
root.mainloop()
