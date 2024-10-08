import socket
import threading
import tkinter as tk
from tkinter import messagebox

class TCPClientApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Cliente TCP")
        self.client_socket = None
        self.connected = False
        self.create_widgets()

    def create_widgets(self):
        # Entradas para host y puerto
        tk.Label(self.root, text="Host:").grid(row=0, column=0, padx=5, pady=5)
        self.host_entry = tk.Entry(self.root)
        self.host_entry.insert(0, 'localhost')
        self.host_entry.grid(row=0, column=1, padx=5, pady=5)

        tk.Label(self.root, text="Puerto:").grid(row=1, column=0, padx=5, pady=5)
        self.port_entry = tk.Entry(self.root)
        self.port_entry.insert(0, '8080')
        self.port_entry.grid(row=1, column=1, padx=5, pady=5)

        # Botones
        self.start_button = tk.Button(self.root, text="Conectar", command=self.start_client)
        self.start_button.grid(row=2, column=0, columnspan=2, padx=5, pady=5)

        tk.Label(self.root, text="Mensaje:").grid(row=3, column=0, padx=5, pady=5)
        self.mens_entry = tk.Entry(self.root)
        self.mens_entry.grid(row=3, column=1, padx=5, pady=5)

        self.send_button = tk.Button(self.root, text="Enviar", command=self.send_message, state=tk.DISABLED)
        self.send_button.grid(row=4, column=0, columnspan=2, padx=5, pady=5)

        self.disconnect_button = tk.Button(self.root, text="Desconectar", command=self.disconnect, state=tk.DISABLED)
        self.disconnect_button.grid(row=5, column=0, columnspan=2, padx=5, pady=5)

        # Etiqueta de estado
        self.status_label = tk.Label(self.root, text="Desconectado", fg="red")
        self.status_label.grid(row=6, column=0, columnspan=2, padx=5, pady=5)

        # Área de texto para los logs
        self.log_text = tk.Text(self.root, height=10, width=50, state=tk.DISABLED)
        self.log_text.grid(row=7, column=0, columnspan=2, padx=5, pady=7)

    def start_client(self):
        host = self.host_entry.get()
        port = self.port_entry.get()

        # Validar el puerto
        if not port.isdigit() or not (1024 <= int(port) <= 65535):
            messagebox.showerror("Error", "Por favor, ingrese un puerto válido (1024-65535).")
            return

        self.thread = threading.Thread(target=self.connect_to_server, args=(host, int(port)))
        self.thread.start()

    def connect_to_server(self, host, port):
        try:
            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.client_socket.connect((host, port))
            self.connected = True
            self.update_status("Conectado", "green")
            self.log("Conectado al servidor.")
            self.update_buttons_state(True)
            self.receive_messages()
        except socket.error as e:
            self.log(f"Error de conexión: {e}")
            self.disconnect()
        except Exception as e:
            self.log(f"Error inesperado: {e}")
            self.disconnect()

    def receive_messages(self):
        while self.connected:
            try:
                response = self.client_socket.recv(1024)
                if not response:
                    self.log("El servidor cerró la conexión.")
                    self.disconnect()
                    break
                self.log(f"Mensaje: {response.decode()}")
            except socket.error as e:
                self.log(f"Error al recibir mensaje: {e}")
                self.disconnect()
            except Exception as e:
                self.log(f"Error inesperado: {e}")
                self.disconnect()

    def send_message(self):
        if not self.connected:
            self.log("No estás conectado al servidor.")
            return

        message = self.mens_entry.get()
        if message:
            try:
                self.client_socket.sendall(message.encode())
                self.mens_entry.delete(0, tk.END)
            except socket.error as e:
                self.log(f"Error al enviar mensaje: {e}")
                self.disconnect()
            except Exception as e:
                self.log(f"Error inesperado: {e}")
                self.disconnect()

    def disconnect(self):
        if self.connected:
            try:
                self.client_socket.close()
            except socket.error as e:
                self.log(f"Error al cerrar la conexión: {e}")
            self.connected = False
            self.update_buttons_state(False)
            self.update_status("Desconectado", "red")
            self.log("Desconectado del servidor.")

    def update_buttons_state(self, connected):
        self.send_button.config(state=tk.NORMAL if connected else tk.DISABLED)
        self.disconnect_button.config(state=tk.NORMAL if connected else tk.DISABLED)
        self.start_button.config(state=tk.DISABLED if connected else tk.NORMAL)

    def update_status(self, text, color):
        self.status_label.config(text=text, fg=color)

    def log(self, message):
        self.log_text.config(state=tk.NORMAL)  # Habilitar el área de texto para escritura
        self.log_text.insert(tk.END, f"{message}\n")
        self.log_text.yview(tk.END)
        self.log_text.config(state=tk.DISABLED)  # Volver a deshabilitar el área de texto

if __name__ == "__main__":
    root = tk.Tk()
    app = TCPClientApp(root)
    root.mainloop()
