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

    def create_widgets(self):
        # Campos para IP y puerto
        tk.Label(self.root, text="IP:").pack(pady=5)
        self.ip_entry = tk.Entry(self.root, width=20)
        self.ip_entry.pack(padx=10, pady=5)
        self.ip_entry.insert(0, '127.0.0.1')  # Valor predeterminado

        tk.Label(self.root, text="Puerto:").pack(pady=5)
        self.port_entry = tk.Entry(self.root, width=20)
        self.port_entry.pack(padx=10, pady=5)
        self.port_entry.insert(0, '8080')  # Valor predeterminado

        # Botón para actualizar configuración
        self.update_button = tk.Button(self.root, text="Activar Servidor", command=self.update_socket)
        self.update_button.pack(padx=10, pady=5)

        # Campo de entrada para mensajes del servidor
        tk.Label(self.root, text="Mensaje:").pack(padx=20, pady=5)
        self.message_entry = tk.Entry(self.root, width=50)
        self.message_entry.pack(padx=10, pady=5)

        # Botón para enviar mensajes
        self.send_button = tk.Button(self.root, text="Enviar", command=self.send_message)
        self.send_button.pack(padx=10, pady=5)

        # Etiqueta para mostrar el estado del servidor
        self.status_label = tk.Label(self.root, text="Estado: Inactivo")
        self.status_label.pack(padx=10, pady=5) 
        
        # Área de texto para mostrar mensajes
        self.text_area = scrolledtext.ScrolledText(self.root, state='disabled', width=60, height=10)
        self.text_area.pack(padx=10, pady=10)

    def update_socket(self):
        ip = self.ip_entry.get()
        port_str = self.port_entry.get()

        # Validar IP
        if not self.validate_ip(ip):
            messagebox.showerror("Error", "Dirección IP no válida")
            return

        # Validar Puerto
        if not port_str.isdigit() or not (1024 <= int(port_str) <= 65535):
            messagebox.showerror("Error", "Número de puerto no válido (debe ser entre 1024 y 65535)")
            return

        port = int(port_str)

        # Cerrar el socket existente si está abierto
        if self.server_socket:
            self.server_socket.close()

        # Crear y vincular el nuevo socket UDP
        try:
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.server_socket.bind((ip, port))
            self.status_label.config(text=f"Estado: Activo en {ip}:{port}")
            self.display_message(f"Servidor UDP escuchando en {ip}:{port}")

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
        while True:
            try:
                data, addr = self.server_socket.recvfrom(1024)  # type: ignore # buffer de 1024 bytes
                self.client_addr = addr  # Guardar la dirección del cliente para responder
                self.display_message(f"Mensaje recibido de {self.client_addr}: {data.decode()}")

                # Responder al cliente
                response = f"Servidor: Recibí tu mensaje '{data.decode()}'"
                self.server_socket.sendto(response.encode(), self.client_addr) # type: ignore

                if data.decode().lower() == 'salir':
                    self.display_message("Servidor: Cliente terminó la conexión.")
                    break
            except Exception as e:
                self.display_message(f"Error en la recepción de mensajes: {e}")
                break

    def send_message(self):
        if self.client_addr:
            server_message = self.message_entry.get()
            self.server_socket.sendto(server_message.encode(), self.client_addr) # type: ignore
            self.display_message(f"Servidor: {server_message}")
            self.message_entry.delete(0, tk.END)
        else:
            messagebox.showwarning("Advertencia", "No hay cliente conectado para enviar el mensaje")

    def display_message(self, message):
        self.text_area.config(state='normal')
        self.text_area.insert(tk.END, message + '\n')
        self.text_area.config(state='disabled')
        self.text_area.yview(tk.END)

# Configuración de la ventana principal
root = tk.Tk()
app = UDPServerApp(root)
root.mainloop()
