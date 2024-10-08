import express from 'express';
import logger from 'morgan';

import { Server } from 'socket.io';
import { createServer } from 'node:http';

import path from 'path';
import { fileURLToPath } from 'url';

// Obtener la ruta del archivo actual
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

import DBConnector from './dbconnector.js';

DBConnector.query('CREATE TABLE IF NOT EXISTS messages (id INT AUTO_INCREMENT PRIMARY KEY, content TEXT, user TEXT)');


const port = process.env.PORT ?? 3000;

const app = express();
const server = createServer(app);
const io = new Server(server, {connectionStateRecovery: {}});

io.on('connection', async (socket) => {
    console.log('Usuario Conectado:' , socket.handshake.auth.username );

    socket.on('disconnect', () => {
        console.log('Usuario desconectado: ', socket.handshake.auth.username);
    });

    socket.on('chat message', (msg) => {
        let result
        const user = socket.handshake.auth.username ?? 'Anonimo';

        try{
            result = DBConnector.query(`INSERT INTO messages (content,user) VALUES ('${msg}', '${user}')`);
        } catch (e){
            console.log(e);
            return;
        }
        console.log('Mensaje: ' + msg + ' de: ' + socket.handshake.auth.username);

        // Seleccion de ultimo mensaje insertado en la base de datos
        const q = DBConnector.query(`SELECT * FROM messages ORDER BY id DESC LIMIT 1`);

        io.emit('chat message', msg, q.id, user);
    });

    if (!socket.recovered){
        try{
            const results = await DBConnector.query(`SELECT id,content,user FROM messages WHERE id > ${socket.handshake.auth.serverOffset}`);
            results.forEach(result => {
                socket.emit('chat message', result.content, result.id, result.user);
            });

        }catch(e){
            console.log(e);
        }

    } 

});

app.use(logger('dev'));

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, '../client', 'index.html'));
});


server.listen(port, () => {
    console.log(`Servidor Corriendo en http://localhost:${port}`);
});
