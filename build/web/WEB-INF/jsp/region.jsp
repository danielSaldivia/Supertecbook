<html>
    <head>
    </head>
    <body>
        <h3>Registro de Region</h3>
        <form method="POST" action="/region" modelAttribute="region">
             <table>
                <tr>
                    <td><label path="nombre">Nombre</label></td>
                    <td><input path="nombre"/></td>
                </tr>
                <tr>
                    <td><label path="codigo">Codigo Region</label></td>
                    <td><input path="codigo"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>