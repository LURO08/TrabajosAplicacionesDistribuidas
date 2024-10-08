import socket
import threading
import tkinter as tk
from tkinter import scrolledtext

# Crear la ventana principal de la interfaz gráfica
root = tk.Tk()
root.title("MIDDLEWERE TCP/UDP")

# Crear una caja de texto para mostrar la comunicación
log_text = scrolledtext.ScrolledText(root, width=50, height=20)
log_text.grid(row=0, column=0, columnspan=2, padx=10, pady=10)

def log_message(message):
    """Función para mostrar los mensajes en la interfaz"""
    log_text.config(state=tk.NORMAL)
    log_text.insert(tk.END, message + '\n')
    log_text.yview(tk.END)
    log_text.config(state=tk.DISABLED)

def handle_receive_TCP(client_socketTCP, client_socketUDP, host, portUDP):
    """Función para recibir mensajes del servidor TCP y reenviar al servidor UDP"""
    while True:
        try:
            # Recibir mensaje del servidor TCP
            responseTCP = client_socketTCP.recv(1024).decode()

            if not responseTCP:
                log_message("No se recibió respuesta del servidor TCP, cerrando conexión.")
                break

            log_message(f"Respuesta del servidor TCP: {responseTCP}")

            # Reenviar mensaje al servidor UDP
            client_socketUDP.sendto(responseTCP.encode(), (host, portUDP))
            log_message(f"Mensaje reenviado al servidor UDP en {host}:{portUDP}")
        
        except Exception as e:
            log_message(f"Error al recibir mensaje TCP: {e}")
            break

def handle_receive_UDP(client_socketUDP, client_socketTCP,server_ipTCP, server_portTCP):
    """Función para recibir mensajes del servidor UDP y reenviar al servidor TCP"""
    while client_socketUDP:
            try:
                data, _ = client_socketUDP.recvfrom(1024)
                udp_response = data.decode()
                log_message(f"Respuesta del servidor UDP: {udp_response}")
                
                  
                client_socketTCP.sendall(udp_response.encode())
                log_message(f"Mensaje '{udp_response}' enviado al servidor TCP")

            except socket.timeout:
                # Manejo del timeout sin errores críticos
                continue
            except Exception as e:
                log_message(f"Error al recibir mensaje UDP: {e}")
                break
          
        
def start_client():
    try:
        global server_ip, server_portUDP, server_ipTCP, server_portTCP

        # Obtener las IPs y puertos de los campos de entrada
        server_ip = udp_ip_entry.get()
        server_portUDP = int(udp_port_entry.get())
        server_ipTCP = tcp_ip_entry.get()
        server_portTCP = int(tcp_port_entry.get())

        # Crear sockets TCP y UDP
        client_socketTCP = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socketUDP = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

        # Enlazar el socket UDP a un puerto local para recibir respuestas
        client_socketUDP.bind(('', 0))  # Bind a cualquier puerto disponible en el cliente

        # Conectar al servidor TCP
        client_socketTCP.connect((server_ipTCP, server_portTCP))
        client_socketUDP.bind((server_ip, server_portUDP))
        log_message(f"Conectado al servidor udp en {server_ip}:{server_portUDP}")
        log_message(f"Conectado al servidor TCP en {server_ipTCP}:{server_portTCP}")

        # Enviar mensaje inicial al servidor UDP
        initial_message = "Servidor en línea"
        client_socketUDP.sendto(initial_message.encode(), (server_ip, server_portUDP))
        log_message(f"Mensaje inicial enviado al servidor UDP en {server_ip}:{server_portUDP}")

        # Crear y lanzar hilos para manejar el envío y recepción simultánea
        tcp_receive_thread = threading.Thread(target=handle_receive_TCP, args=(client_socketTCP, client_socketUDP, server_ip, server_portUDP))
        udp_receive_thread = threading.Thread(target=handle_receive_UDP, args=(client_socketUDP, client_socketTCP,server_ipTCP, server_portTCP))

        tcp_receive_thread.start()
        udp_receive_thread.start()

        tcp_receive_thread.join()
        udp_receive_thread.join()

    except Exception as e:
        log_message(f"Ocurrió un error al iniciar el cliente: {e}")

def start_client_thread():
    """Función para iniciar el cliente en un hilo separado"""
    client_thread = threading.Thread(target=start_client)
    client_thread.start()

# Entradas para IP y Puerto del servidor UDP
tk.Label(root, text="IP Servidor UDP:").grid(row=1, column=0, padx=5, pady=5, sticky="e")
udp_ip_entry = tk.Entry(root)
udp_ip_entry.grid(row=1, column=1, padx=5, pady=5)
udp_ip_entry.insert(0, "192.168.211.187")  # Valor predeterminado

tk.Label(root, text="Puerto UDP:").grid(row=2, column=0, padx=5, pady=5, sticky="e")
udp_port_entry = tk.Entry(root)
udp_port_entry.grid(row=2, column=1, padx=5, pady=5)
udp_port_entry.insert(0, "8081")  # Valor predeterminado

# Entradas para IP y Puerto del servidor TCP
tk.Label(root, text="IP Servidor TCP:").grid(row=3, column=0, padx=5, pady=5, sticky="e")
tcp_ip_entry = tk.Entry(root)
tcp_ip_entry.grid(row=3, column=1, padx=5, pady=5)
tcp_ip_entry.insert(0, "192.168.212.2")  # Valor predeterminado

tk.Label(root, text="Puerto TCP:").grid(row=4, column=0, padx=5, pady=5, sticky="e")
tcp_port_entry = tk.Entry(root)
tcp_port_entry.grid(row=4, column=1, padx=5, pady=5)
tcp_port_entry.insert(0, "8080")  # Valor predeterminado

# Botón para iniciar la conexión
start_button = tk.Button(root, text="Iniciar Cliente", command=start_client_thread)
start_button.grid(row=5, column=0, columnspan=2, pady=10)

# Iniciar la interfaz gráfica
root.mainloop()
