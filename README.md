# Proyecto_Individual 
#PetCare

Mi proyecto consiste en una aplicación móvil pensada para facilitar la comunicación entre las personas y su veterinaria de confianza.  
En la app, los usuarios podrán **crear una cuenta e iniciar sesión** para tener su propio perfil, donde podrán **registrar a sus mascotas** con información básica como nombre, raza, edad y una fotografia.

Una vez registradas las mascotas, el usuario podrá **agendar citas para revisiones o consultas**.  
Además, la aplicación permitirá **ver las citas agendadas**, así como **consultar el historial médico de cada mascota**, de manera que los dueños puedan llevar un control fácil y rápido del cuidado de sus animales.

![Imagen Mockup](https://github.com/TamaraAlpizar/Proyecto_Individual/blob/main/Mockup/Mockup.png)

Base URL de la API
https://petcare-rg-hjcydddmfpg2eud2.canadacentral-01.azurewebsites.net

1. ENDPOINT DE PRUEBA – Comprobar que la API funciona
✔ GET /
Descripción: "🐶 Veterinaria API - ¡TODO FUNCIONA PERFECTO!"
Ejemplo:
GET https://petcare-rg-hjcydddmfpg2eud2.canadacentral-01.azurewebsites.net/

2. ENDPOINTS DE DUEÑOS (Owners)
➤ Crear dueño
POST /owners
Body (JSON):
{
  "name": "Fabiana",
  "fLastName": "Castro",
  "sLastName": "Jimenez",
  "email": "fabiana@email.com",
  "password": "12345",
  "phone": "8888-8888",
  "address": "Costa Rica"
}
➤ Obtener todos los dueños
GET /owners
➤ Editar dueño
PUT /owners/:id
➤ Eliminar dueño
DELETE /owners/:id

3. ENDPOINTS DE MASCOTAS (Pets)
➤ Crear mascota
POST /pets
Body (JSON):
{
  "petName": "Firulais",
  "breed": "Labrador",
  "gender": "Macho",
  "age": 3,
  "weight": 12.4,
  "ownerId": "ID_DEL_DUEÑO",
  "notes": "Muy juguetón"
}
➤ Obtener todas las mascotas
GET /pets
➤ Editar mascota
PUT /pets/:id
➤ Eliminar mascota
DELETE /pets/:id
ENDPOINTS DE CITAS (Appointments)
➤ Crear cita
POST /appointments
Body (JSON):
{
  "petId": "ID_MASCOTA",
  "serviceType": "Vacunación",
  "date": "2025-01-20",
  "hour": "14:00",
  "notes": "Revisión general"
}
➤ Obtener todas las citas
GET /appointments
(ordenadas descendentemente por fecha)
➤ Editar estado de la cita
PUT /appointments/:id
➤ Eliminar cita
DELETE /appointments/:id

