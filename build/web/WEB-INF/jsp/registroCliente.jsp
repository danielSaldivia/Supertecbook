<%-- 
    Document   : Cliente
    Created on : 01-dic-2018, 12:57:11
    Author     : yo
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import = "Model.Cliente" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="../../../../favicon.ico">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
        <title>Login SuperTec</title>

        <!-- Bootstrap core CSS -->
        <link href="../../dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="floating-labels.css" rel="stylesheet">
    </head>

    <body>

        <div class="container"> 
            <form:form modelAttribute="cliente"  >
                <div class="text-center mb-4">
                    <h1 class="h3 mb-3 font-weight-normal">Crear Cuenta Cliente</h1>

                </div>
                <div class="form-label-group">
                    <input path="correo"  class="form-control" placeholder="asd@asd.cl" required />
                    <label >Email</label>
                </div>
                    <div class="form-label-group">
                    <input path="usuario"  class="form-control" placeholder="Ej: CristianxD" required />
                    <label for="inputEmail">Email</label>
                </div>

                <div class="form-label-group">
                    <input path="contrasenia" type="password" id="inputPassword" class="form-control" placeholder="Password" required/>
                    <label for="inputPassword">Password</label>
                </div>
                <div class="form-label-group">
                    <input path="rut" type="text" id="inputEmail" class="form-control" placeholder="11.111.111-k" required />
                    <label for="rut">Rut</label>
                </div>
                <div class="form-label-group">
                    <input path="nombre"type="text"  class="form-control" placeholder="Nombre & Apellidos" required autofocus/>
                    <label for="name">Nombre Completo</label>
                </div>
                <div class="form-label-group">
                    <input path="fechaNacimiento" type="date" id="inputEmail" class="form-control">
                    <label >Fecha de Nacimiento</label>
                </div>
                <div class="form-label-group">
                    <input path="telefono" type="text" id="inputEmail" class="form-control" placeholder="+5699999999" required autofocus>
                    <label >Telefono</label>
                </div>
                <div class="container">
                    <button class="btn btn-lg btn-outline-primary btn-block" type="submit">Registrarse</button>
                </div>
                <p class="mt-5 mb-3 text-muted text-center">&copy; SuperTec-2018</p>
            </form:form>
        </div>
    </body>
</html>
