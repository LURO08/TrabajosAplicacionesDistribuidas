import socket
import threading
import signal
import sys
import mysql.connector  # Conector MySQL
import tkinter as tk
from tkinter import scrolledtext, messagebox
from datetime import datetime


class MessageMiddleware:
    def __init__(self, tcp_port, udp_port, tcp_host='0.0.0.0', udp_host='0.0.0.0'):
        self.tcp_host = tcp_host
        self.udp_host = udp_host
        self.tcp_port = tcp_port
        self.udp_port = udp_port
        
        self.tcp_server = None
        self.udp_server = None
        self.client_addr = None
        
        self.current_tcp_clients = []  # Para almacenar los clientes TCP conectados
        
        # Conexión a MySQL
        self.db_connection = mysql.connector.connect(
            host="localhost",      # Cambia esto según tu host
            user="root",     # Cambia por tu usuario de MySQL
            password="",# Cambia por tu contraseña de MySQL
            database="middlewere"   # Cambia por el nombre de tu base de datos
        )
        self.db_cursor = self.db_connection.cursor()
        self.create_tables()  # Crear la tabla de mensajes

        # Configuración de la interfaz
        self.root = tk.Tk()
        self.root.title("Middleware de Mensajes")

        # Entrada para host TCP
        self.tcp_host_entry = tk.Entry(self.root)
        self.tcp_host_entry.insert(0, self.tcp_host)
        self.tcp_host_entry.pack()

        # Entrada para puerto TCP
        self.tcp_port_entry = tk.Entry(self.root)
        self.tcp_port_entry.insert(0, str(self.tcp_port))
        self.tcp_port_entry.pack()

        # Entrada para host UDP
        self.udp_host_entry = tk.Entry(self.root)
        self.udp_host_entry.insert(0, self.udp_host)
        self.udp_host_entry.pack()

        # Entrada para puerto UDP
        self.udp_port_entry = tk.Entry(self.root)
        self.udp_port_entry.insert(0, str(self.udp_port))
        self.udp_port_entry.pack()

        # Botón de inicio
        self.start_button = tk.Button(self.root, text="Iniciar", command=self.start_server)
        self.start_button.pack()

        # Área de texto para logs
        self.text_area = scrolledtext.ScrolledText(self.root, wrap=tk.WORD, state='disabled')
        self.text_area.pack(expand=True, fill='both')

        # Señal para cerrar el servidor con Ctrl+C
        signal.signal(signal.SIGINT, self.shutdown)

        self.server_running = False

    def create_tables(self):
        # Crear la tabla para almacenar los mensajes si no existe
        self.db_cursor.execute('''
            CREATE TABLE IF NOT EXISTS mensajes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                tipo VARCHAR(10) NOT NULL,
                contenido TEXT NOT NULL,
                fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ''')
        self.db_connection.commit()

    def log(self, message):
        self.text_area.configure(state='normal')
        self.text_area.insert(tk.END, f"[LOG] {message}\n")
        self.text_area.configure(state='disabled')
        self.text_area.see(tk.END)

    def store_message(self, tipo, contenido):
        # Insertar un mensaje en la base de datos
        sql = "INSERT INTO mensajes (tipo, contenido) VALUES (%s, %s)"
        val = (tipo, contenido)
        self.db_cursor.execute(sql, val)
        self.db_connection.commit()

    def handle_tcp_client(self, client_socket):
        self.current_tcp_clients.append(client_socket)
        try:
            while True:
                # Recibir datos del cliente
                data = client_socket.recv(1024).decode('utf-8')
                if not data:
                    # Si no se recibe más datos, cerramos la conexión
                    self.log("Conexión TCP cerrada por el cliente")
                    break

                # Registrar el mensaje recibido
                self.log(f"Mensaje TCP recibido: {data}")

                # Guardar el mensaje en la base de datos
                self.store_message('TCP', data)

                # Preparar para enviar el mensaje a través de UDP
                try:
                    port = int(self.udp_port)
                    self.udp_server.sendto(data.encode('utf-8'), self.client_addr)
                    self.log(f"Mensaje enviado a UDP: {data}")
                except ValueError:
                    # Si `udp_port` no es un número válido, lanzamos una advertencia
                    self.log("Error: El puerto UDP no es válido.")
                except Exception as udp_error:
                    self.log(f"Error enviando mensaje UDP: {udp_error}")

        except Exception as e:
            self.log(f"Error en la conexión TCP: {e}")
        finally:
            # Cerrar el socket y eliminarlo de la lista de clientes actuales
            client_socket.close()
            if client_socket in self.current_tcp_clients:
                self.current_tcp_clients.remove(client_socket)
            self.log("Conexión TCP finalizada y cliente eliminado.")


    def handle_tcp_connections(self):
        while self.server_running:
            try:
                client_socket, _ = self.tcp_server.accept()
                threading.Thread(target=self.handle_tcp_client, args=(client_socket,), daemon=True).start()
            except Exception as e:
                self.log(f"Error al aceptar conexión TCP: {e}")

    def handle_udp_messages(self):
        while self.server_running:
            try:
                udp_data, addr = self.udp_server.recvfrom(1024)
                self.client_addr = addr
                if udp_data:
                    message = udp_data.decode('utf-8')
                    self.log(f"Mensaje UDP recibido: {message} de {addr}")
                    self.store_message('UDP', message)  # Guardar en la base de datos
                    self.send_to_tcp(message)
            except Exception as e:
                self.log(f"Error en UDP: {e}")

    def send_to_tcp(self, message):
        if self.current_tcp_clients:
            try:
                for client_socket in self.current_tcp_clients:
                    client_socket.sendall(message.encode('utf-8'))
                self.log(f"Mensaje enviado a TCP: {message}")
            except Exception as e:
                self.log(f"Error al enviar a TCP: {e}")
        else:
            self.log("No hay clientes TCP conectados para enviar el mensaje.")

    def send_to_udp(self, message):
        try:
            # client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            # client_socket.bind((self.tcp_host, self.tcp_port))
            # client_socket.listen(5)
            port = int(self.udp_port)
            self.udp_server.sendto(message, self.client_addr)
            self.log(f"Mensaje enviado a UDP: {message}")
        except Exception as e:
            self.log(f"Error al enviar a UDP: {e}")

            
    def start_server(self):
        if self.server_running:
            messagebox.showinfo("Info", "El servidor ya está en funcionamiento.")
            return

        try:
            self.tcp_host = self.tcp_host_entry.get()
            self.tcp_port = int(self.tcp_port_entry.get())
            self.udp_host = self.udp_host_entry.get()
            self.udp_port = int(self.udp_port_entry.get())

            # Cerrar el servidor existente si está corriendo
            if self.tcp_server:
                self.tcp_server.close()
            if self.udp_server:
                self.udp_server.close()

            # Reconfigurar y reiniciar los sockets
            self.tcp_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.tcp_server.bind((self.tcp_host, self.tcp_port))
            self.tcp_server.listen(5)

            self.udp_server = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.udp_server.bind((self.udp_host, self.udp_port))
            self.tcp_server.listen(5)

            self.server_running = True
            # Crear hilos separados para manejar TCP y UDP simultáneamente
            threading.Thread(target=self.handle_tcp_connections, daemon=True).start()
            threading.Thread(target=self.handle_udp_messages, daemon=True).start()

            self.log("Servidor iniciado.")
        except Exception as e:
            messagebox.showerror("Error", f"No se pudo iniciar el servidor: {e}")

    def shutdown(self, signum, frame):
        self.log("Cerrando el middleware...")
        self.server_running = False
        if self.tcp_server:
            self.tcp_server.close()
        if self.udp_server:
            self.udp_server.close()
        self.db_connection.close()  # Cerrar la conexión de la base de datos
        self.root.quit()

if __name__ == "__main__":
    middleware = MessageMiddleware(9000, 9001)
    middleware.root.mainloop()
