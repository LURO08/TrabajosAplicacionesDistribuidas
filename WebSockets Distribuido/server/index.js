import express from 'express';
import logger from 'morgan';
import { Server } from 'socket.io';
import { createServer } from 'node:http';
import cors from 'cors';
import DBConnector from './dbconnector.js';

// Configuración del puerto, permite elegir entre una variable de entorno o usar 8080 por defecto
const port = process.env.PORT || 8080;

// Configuración del servidor Express
const app = express();
const server = createServer(app);
const io = new Server(server, {
  cors: {
    origin: ["http://localhost:5500", "http://127.0.0.1:5500"], // Permitir solicitudes desde estos orígenes
    methods: ["GET", "POST"],
    credentials: true, // Permitir el envío de credenciales si es necesario
  },
});

// Middleware
app.use(logger('dev')); // Logging de solicitudes
app.use(express.json()); // Para procesar JSON en las solicitudes
app.use(cors()); // Permitir solicitudes desde el cliente

// Crear tabla de mensajes si no existe
DBConnector.query(`
  CREATE TABLE IF NOT EXISTS messages (
    id INT AUTO_INCREMENT PRIMARY KEY, 
    content TEXT, 
    user TEXT
  )
`).then(() => {
  console.log('Tabla de mensajes asegurada');
}).catch((err) => {
  console.error('Error al crear tabla:', err);
});

// Socket.IO para manejo de chat en tiempo real
io.on('connection', async (socket) => {
  const username = socket.handshake.auth.username || 'Anónimo';
  console.log('Usuario Conectado:', username);

  // Desconexión del usuario
  socket.on('disconnect', () => {
    console.log('Usuario desconectado:', username);
  });

  // Recepción de mensajes del chat
  socket.on('chat message', async (msg) => {
    try {
      // Inserta el mensaje en la base de datos
      const result = await DBConnector.query(
        `INSERT INTO messages (content, user) VALUES (?, ?)`,
        [msg, username]
      );

      // Obtener el ID del mensaje insertado
      const messageId = Number(result.insertId) || null;

      console.log(`Mensaje recibido: ${msg} de: ${username}`);

      // Emitir el mensaje a todos los usuarios conectados
      if (messageId) {
        io.emit('chat message', { id: messageId, content: msg, user: username });

        // Seleccion de ultimo mensaje insertado en la base de datos
      
      } else {
        console.error('Error: No se obtuvo un ID de mensaje válido.');
      }
    } catch (error) {
      console.error('Error al insertar mensaje:', error);
    }
  });

  if (!socket.recovered) {

    try {
      const results = await DBConnector.query(`SELECT * FROM messages WHERE id > ${socket.handshake.auth.serverOffset}`);

      results.forEach(element => {
        socket.emit('previous messages', {
          id: element.id,
          content: element.content,
          user: element.user,
        });
      });

    } catch (error) {
      console.error('Error al recuperar mensajes:', error);
    }

  }

});


// Ruta base para confirmar que la API está funcionando
app.get('/api', (req, res) => {
  res.json({ message: 'API funcionando correctamente' });
});

// Iniciar el servidor
server.listen(port, () => {
  console.log(`Servidor corriendo en http://localhost:${port}`);
});
