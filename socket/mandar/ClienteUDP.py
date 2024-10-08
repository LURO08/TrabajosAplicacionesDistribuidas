import socket
import tkinter as tk
from tkinter import scrolledtext, messagebox
import threading

class UDPClientApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Cliente UDP")

        # Crear widgets de la interfaz
        self.create_widgets()

        # Inicializar el socket UDP
        self.client_socket = None
        self.server_ip = None
        self.server_port = None

    def create_widgets(self):
        # Campos para IP y puerto del servidor
        tk.Label(self.root, text="IP del Servidor:").pack(padx=10, pady=5)
        self.ip_entry = tk.Entry(self.root, width=20)
        self.ip_entry.pack(padx=10, pady=5)
        self.ip_entry.insert(0, '127.0.0.1')  # Valor predeterminado

        tk.Label(self.root, text="Puerto del Servidor:").pack(padx=10, pady=5)
        self.port_entry = tk.Entry(self.root, width=20)
        self.port_entry.pack(padx=10, pady=5)
        self.port_entry.insert(0, '8080')  # Valor predeterminado

        # Botones para conectar y desconectar
        self.connect_button = tk.Button(self.root, text="Conectar", command=self.connect_to_server)
        self.connect_button.pack(padx=10, pady=5)

        self.disconnect_button = tk.Button(self.root, text="Desconectar", command=self.disconnect_from_server, state='disabled')
        self.disconnect_button.pack(padx=10, pady=5)

        # Campo de entrada para mensajes del cliente
        self.message_entry = tk.Entry(self.root, width=50)
        self.message_entry.pack(padx=10, pady=5)

        # Botón para enviar mensajes
        self.send_button = tk.Button(self.root, text="Enviar", command=self.send_message, state='disabled')
        self.send_button.pack(padx=10, pady=5)
        
        # Área de texto para mostrar mensajes
        self.text_area = scrolledtext.ScrolledText(self.root, state='disabled', width=60, height=10)
        self.text_area.pack(padx=10, pady=10)

    def connect_to_server(self):
        self.server_ip = self.ip_entry.get()
        self.server_port = self.port_entry.get()

        # Validar IP y puerto
        if not self.validate_ip(self.server_ip):
            messagebox.showerror("Error", "Dirección IP no válida")
            return

        if not self.server_port.isdigit() or not (1024 <= int(self.server_port) <= 65535):
            messagebox.showerror("Error", "Número de puerto no válido (debe ser entre 1024 y 65535)")
            return

        port = int(self.server_port)

        try:
            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.client_socket.settimeout(5)  # Establecer un tiempo de espera
            self.display_message(f"Conectado al servidor {self.server_ip}:{port}")
            self.send_button.config(state='normal')
            self.disconnect_button.config(state='normal')
            self.connect_button.config(state='disabled')

            # Iniciar la recepción de mensajes en un hilo separado
            threading.Thread(target=self.receive_messages, daemon=True).start()
        except Exception as e:
            self.display_message(f"Error al conectar: {e}")

    def disconnect_from_server(self):
        if self.client_socket:
            self.client_socket.close()
            self.client_socket = None
            self.display_message("Desconectado del servidor")
            self.send_button.config(state='disabled')
            self.disconnect_button.config(state='disabled')
            self.connect_button.config(state='normal')

    def send_message(self):
        if not self.client_socket:
            messagebox.showwarning("Advertencia", "No estás conectado al servidor")
            return

        message = self.message_entry.get()

        if not message:
            messagebox.showwarning("Advertencia", "No hay mensaje para enviar")
            return

        try:
            port = int(self.server_port)  # type: ignore # Asegurarse de que el puerto sea un entero
            self.client_socket.sendto(message.encode(), (self.server_ip, port))
            self.display_message(f"Enviado: {message}")

            # Limpiar el campo de mensaje después de enviar
            self.message_entry.delete(0, tk.END)
        except Exception as e:
            self.display_message(f"Error al enviar el mensaje: {e}")

    def receive_messages(self):
        while self.client_socket:
            try:
                data, _ = self.client_socket.recvfrom(1024)
                self.display_message(f"Mensaje recibido: {data.decode()}")
            except socket.timeout:
                # Manejo del timeout sin errores críticos
                continue
            except Exception as e:
                self.display_message(f"Error en la recepción de mensajes: {e}")
                break

    def validate_ip(self, ip):
        # Validar dirección IP
        try:
            socket.inet_pton(socket.AF_INET, ip)
            return True
        except socket.error:
            return False

    def display_message(self, message):
        self.text_area.config(state='normal')
        self.text_area.insert(tk.END, message + '\n')
        self.text_area.config(state='disabled')
        self.text_area.yview(tk.END)

# Configuración de la ventana principal
root = tk.Tk()
app = UDPClientApp(root)
root.mainloop()
