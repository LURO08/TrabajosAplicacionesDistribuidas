import socket
import threading
import tkinter as tk
from tkinter import messagebox

class TCPServerApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Servidor TCP")
        self.clients = []
        self.lock = threading.Lock()  # Para manejo seguro de la lista de clientes
        self.create_widgets()

    def create_widgets(self):
        # Entradas para host y puerto
        tk.Label(self.root, text="Host:").grid(row=0, column=0, padx=5, pady=5)
        self.host_entry = tk.Entry(self.root)
        self.host_entry.insert(0, '0.0.0.0')  # Cambiado a 0.0.0.0 para escuchar en todas las interfaces
        self.host_entry.grid(row=0, column=1, padx=5, pady=5)

        tk.Label(self.root, text="Puerto:").grid(row=1, column=0, padx=5, pady=5)
        self.port_entry = tk.Entry(self.root)
        self.port_entry.insert(0, '8080')
        self.port_entry.grid(row=1, column=1, padx=5, pady=5)

        # Botón para iniciar el servidor
        self.start_button = tk.Button(self.root, text="Iniciar Servidor", command=self.start_server_thread)
        self.start_button.grid(row=2, column=0, columnspan=2, padx=5, pady=5)

        # Área de texto para los logs
        self.log_text = tk.Text(self.root, height=10, width=50)
        self.log_text.grid(row=3, column=0, columnspan=2, padx=5, pady=5)

        # Entrada y botón para enviar mensajes a los clientes
        tk.Label(self.root, text="Mensaje a enviar:").grid(row=4, column=0, padx=5, pady=5)
        self.message_entry = tk.Entry(self.root)
        self.message_entry.grid(row=4, column=1, padx=5, pady=5)
        self.send_button = tk.Button(self.root, text="Enviar a todos", command=self.broadcast_message)
        self.send_button.grid(row=5, column=0, columnspan=2, padx=5, pady=5)

    def start_server(self):
        host = self.host_entry.get()
        port = int(self.port_entry.get())
        
        try:
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server_socket.bind((host, port))
            self.server_socket.listen(5)
            self.log(f"Servidor escuchando en {host}:{port}")

            def handle_connections():
                while True:
                    client_socket, addr = self.server_socket.accept()
                    self.log(f"Conexión desde {addr}")
                    with self.lock:
                        self.clients.append(client_socket)
                    threading.Thread(target=self.handle_client, args=(client_socket, addr)).start()

            threading.Thread(target=handle_connections, daemon=True).start()

        except Exception as e:
            messagebox.showerror("Error", f"No se pudo iniciar el servidor: {e}")
            self.log(f"Error: {e}")

    def handle_client(self, client_socket, addr):
        """Manejar un cliente TCP"""
        try:
            while True:
                data = client_socket.recv(1024)
                if not data:
                    break  # Desconexión del cliente
                self.log(f"Recibido de {addr}: {data.decode()}")
                client_socket.sendall(data)  # Echo de vuelta al cliente
        except Exception as e:
            self.log(f"Error con cliente {addr}: {e}")
        finally:
            with self.lock:
                self.clients.remove(client_socket)
            client_socket.close()
            self.log(f"Cliente {addr} desconectado.")

    def start_server_thread(self):
        threading.Thread(target=self.start_server, daemon=True).start()

    def broadcast_message(self):
        """Enviar un mensaje a todos los clientes TCP conectados"""
        message = self.message_entry.get()
        if not message:
            messagebox.showwarning("Advertencia", "No hay mensaje para enviar.")
            return

        with self.lock:
            for client_socket in self.clients:
                try:
                    client_socket.sendall(message.encode())
                except Exception as e:
                    self.log(f"Error al enviar mensaje a un cliente: {e}")

        self.message_entry.delete(0, tk.END)
        self.log(f"Mensaje enviado a todos: {message}")

    def log(self, message):
        """Agregar un mensaje al área de texto de la interfaz"""
        self.log_text.insert(tk.END, f"{message}\n")
        self.log_text.yview(tk.END)

if __name__ == "__main__":
    root = tk.Tk()
    app = TCPServerApp(root)
    root.mainloop()
