import socket
import tkinter as tk
from tkinter import scrolledtext, messagebox
import threading

class MiddlewareApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Middleware TCP <-> UDP")

        # Crear widgets de la interfaz
        self.create_widgets()

        # Inicializar los sockets TCP y UDP
        self.tcp_socket = None
        self.udp_socket = None
        self.server_ip = None
        self.server_port_tcp = None
        self.server_port_udp = None

    def create_widgets(self):
        # Campos para IP y puerto del servidor TCP
        tk.Label(self.root, text="IP del Servidor:").pack(padx=10, pady=5)
        self.ip_entry = tk.Entry(self.root, width=20)
        self.ip_entry.pack(padx=10, pady=5)
        self.ip_entry.insert(0, '127.0.0.1')  # Valor predeterminado

        tk.Label(self.root, text="Puerto TCP:").pack(padx=10, pady=5)
        self.port_entry_tcp = tk.Entry(self.root, width=20)
        self.port_entry_tcp.pack(padx=10, pady=5)
        self.port_entry_tcp.insert(0, '8080')  # Valor predeterminado

        tk.Label(self.root, text="Puerto UDP:").pack(padx=10, pady=5)
        self.port_entry_udp = tk.Entry(self.root, width=20)
        self.port_entry_udp.pack(padx=10, pady=5)
        self.port_entry_udp.insert(0, '9090')  # Valor predeterminado

        # Botones para conectar y desconectar
        self.connect_button = tk.Button(self.root, text="Conectar", command=self.connect_to_servers)
        self.connect_button.pack(padx=10, pady=5)

        self.disconnect_button = tk.Button(self.root, text="Desconectar", command=self.disconnect_from_servers, state='disabled')
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

    def connect_to_servers(self):
        self.server_ip = self.ip_entry.get()
        self.server_port_tcp = self.port_entry_tcp.get()
        self.server_port_udp = self.port_entry_udp.get()

        # Validar IP y puertos
        if not self.validate_ip(self.server_ip):
            messagebox.showerror("Error", "Dirección IP no válida")
            return

        if not self.server_port_tcp.isdigit() or not (1024 <= int(self.server_port_tcp) <= 65535):
            messagebox.showerror("Error", "Puerto TCP no válido")
            return

        if not self.server_port_udp.isdigit() or not (1024 <= int(self.server_port_udp) <= 65535):
            messagebox.showerror("Error", "Puerto UDP no válido")
            return

        port_tcp = int(self.server_port_tcp)
        port_udp = int(self.server_port_udp)

        try:
            # Conectar al servidor TCP
            self.tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.tcp_socket.connect((self.server_ip, port_tcp))
            self.display_message(f"Conectado al servidor TCP {self.server_ip}:{port_tcp}")

            # Crear el socket UDP
            self.udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            
            self.display_message(f"Escuchando en el puerto UDP {port_udp}")
            
            self.send_button.config(state='normal')
            self.disconnect_button.config(state='normal')
            self.connect_button.config(state='disabled')

            # Iniciar la recepción de mensajes en hilos separados
            threading.Thread(target=self.receive_tcp_messages, daemon=True).start()
            threading.Thread(target=self.receive_udp_messages, daemon=True).start()

        except Exception as e:
            self.display_message(f"Error al conectar: {e}")

    def disconnect_from_servers(self):
        if self.tcp_socket:
            self.tcp_socket.close()
            self.tcp_socket = None
            self.display_message("Desconectado del servidor TCP")

        if self.udp_socket:
            self.udp_socket.close()
            self.udp_socket = None
            self.display_message("Desconectado del servidor UDP")

        self.send_button.config(state='disabled')
        self.disconnect_button.config(state='disabled')
        self.connect_button.config(state='normal')

    def send_message(self):
        message = self.message_entry.get()

        if not message:
            messagebox.showwarning("Advertencia", "No hay mensaje para enviar")
            return

        try:
            # Enviar mensaje a través de TCP
            if self.tcp_socket:
                self.tcp_socket.sendall(message.encode())
                self.display_message(f"Mensaje TCP enviado: {message}")

            # Enviar mensaje a través de UDP
            if self.udp_socket:
                self.udp_socket.sendto(message.encode(), (self.server_ip, int(self.server_port_udp))) # type: ignore
                self.display_message(f"Mensaje UDP enviado: {message}")

            self.message_entry.delete(0, tk.END)

        except Exception as e:
            self.display_message(f"Error al enviar el mensaje: {e}")

    def receive_tcp_messages(self):
        while self.tcp_socket:
            try:
                data = self.tcp_socket.recv(1024)
                if data:
                    message = data.decode()
                    self.display_message(f"Mensaje TCP recibido: {message}")
                    # Reenviar a través de UDP
                    self.udp_socket.sendto(message.encode(), (self.server_ip, int(self.server_port_udp))) # type: ignore
            except Exception as e:
                self.display_message(f"Error en TCP: {e}")
                break

    def receive_udp_messages(self):
        while self.udp_socket:
            try:
                data, _ = self.udp_socket.recvfrom(1024)
                if data:
                    message = data.decode()
                    self.display_message(f"Mensaje UDP recibido: {message}")
                    # Reenviar a través de TCP
                    self.tcp_socket.sendall(message.encode()) # type: ignore
            except Exception as e:
                self.display_message(f"Error en UDP: {e}")
                break

    def validate_ip(self, ip):
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
app = MiddlewareApp(root)
root.mainloop()
