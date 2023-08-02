const express = require('express');
const sql = require('mssql');
const app = express();
const port = 3000; // Puedes cambiar el puerto si lo deseas

// Configurar la conexión a la base de datos SQL Server
const dbConfig = {
  user: 'usersql',
  password: 'root2',
  server: 'localhost',
  database: 'dboPrueba',
  options: {
    encrypt: false // Configura en "true" si tu servidor SQL Server requiere una conexión segura
  }
};

// Conectar a la base de datos
sql.connect(dbConfig).then(() => {
  console.log('Conexión a SQL Server exitosa!');
}).catch(err => {
  console.error('Error al conectar a SQL Server:', err);
});

// Ruta de prueba
app.get('/', (req, res) => {
  res.send('¡Hola desde tu API REST en Node.js con Express y SQL Server!');
});

// Obtener todas las calificaciones
app.get('/calificaciones', (req, res) => {
  const query = 'SELECT * FROM calificaciones';
  sql.query(query).then(result => {
    res.json(result.recordset);
  }).catch(err => {
    console.error('Error al obtener las calificaciones:', err);
    res.status(500).send('Error al obtener las calificaciones');
  });
});

// Obtener calificaciones por correo electrónico del usuario
app.get('/calificaciones/:email', (req, res) => {
    const email = req.params.email;
    const query = `SELECT * FROM calificaciones WHERE nControl IN (SELECT ncontrol FROM alumnos WHERE email = '${email}')`;
    sql.query(query).then(result => {
      res.json(result.recordset);
    }).catch(err => {
      console.error('Error al obtener las calificaciones:', err);
      res.status(500).send('Error al obtener las calificaciones');
    });
  });

// Obtener todas las calificaciones
app.get('/alumnos', (req, res) => {
    const query = 'SELECT * FROM alumnos';
    sql.query(query).then(result => {
      res.json(result.recordset);
    }).catch(err => {
      console.error('Error al obtener los alumnos:', err);
      res.status(500).send('Error al obtener los alumnos');
    });
  });

// Iniciar el servidor
app.listen(port, () => {
  console.log(`Servidor iniciado en http://localhost:${port}`);
});