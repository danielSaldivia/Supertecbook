<html>
    <head>
    </head>
    <body>
        <h3>Registro de Comuna</h3>
        <form method="POST" action="/addcomuna" modelAttribute="comuna">
             <table>
                <tr>
                    <td><label path="nombre">Nombre</label></td>
                    <td><input path="nombre"/></td>
                </tr>
                <tr>
                    <td><label path="codigo">Codigo Comuna</label></td>
                    <td><input path="codigo"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>