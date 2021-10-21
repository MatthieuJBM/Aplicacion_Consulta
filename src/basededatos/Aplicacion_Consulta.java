package basededatos;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;

public class Aplicacion_Consulta {
    public static void main(String[] args) {
        JFrame mimarco=new Marco_Aplicacion();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mimarco.setVisible(true);
    }
}
class Marco_Aplicacion extends JFrame{
    public Marco_Aplicacion(){
        setTitle("Consulta BBDD");
        setBounds(100,100,400,400);
        setLayout(new BorderLayout());
        JPanel menus = new JPanel();
        menus.setLayout(new FlowLayout());
        secciones = new JComboBox();
        secciones.setEditable(false);
        secciones.addItem("Todos");
        paises=new JComboBox();
        paises.setEditable(false);
        paises.addItem("Todos");
        resultado=new JTextArea(4,50);
        resultado.setEditable(false);
        add(resultado);
        menus.add(secciones);
        menus.add(paises);
        add(menus, BorderLayout.NORTH);
        add(resultado, BorderLayout.CENTER);
        JButton botonConsulta = new JButton("Consulta");
        botonConsulta.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutaConsulta();
            }
        });
        add(botonConsulta, BorderLayout.SOUTH);

        //-----------------------CONEXIÓN CON BBDD---------------------------------
        try {

            //1. CREAR CONEXIÓN.
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/curso_sql", "root", "");
            //2. PREPARANDO CONSULTA
            Statement sentencia=miConexion.createStatement();
            //3. Extraemos las sentencias de la tabla a través de las instrucciones SQL.
            String consulta="SELECT DISTINCTROW SECCIÓN FROM PRODUCTOS";
            //4. Construimos un ResultSet para almacenar lo que nos devuelve esta consulta para después recorrerlo
            //   y ir rellenando pues el primer JComboBox.
            ResultSet rs=sentencia.executeQuery(consulta);
            //5. Ese ResultSet que tenemos ya cargado con las secciones, lo tenemos que recorrer registro a registro
            //   para que cada registro lo incluya en el JComboBox.
            while(rs.next()){
                secciones.addItem(rs.getString(1)); //Nuestro ResultSet tiene solamente una columna, pero hay que agregarla.

            }
            rs.close(); // cerramos el ResultSet para liberar la memoria.

            //---------CARGA JCOMBOBOX PAISES--------------
            consulta="SELECT DISTINCTROW PAÍSDEORIGEN FROM PRODUCTOS";
            rs=sentencia.executeQuery(consulta);
            while(rs.next()){
                paises.addItem(rs.getString(1));
            }
            rs.close();
        }catch(Exception e){

        }
        }
    private void ejecutaConsulta(){
        ResultSet rs=null;
        try{
            //Cada vez que nosotros pulsamos el botón de consulta el flujo de ejecución tiene que pasar por esta línea forzosamente.
            resultado.setText("");
            //Guardar dentro de la variable "seccion" el elemento seleccionado del desplegable de JComboBox
            String seccion = (String)secciones.getSelectedItem(); //Utilizamos este método para extraer los elementos del JComboBox.
            String pais=(String)paises.getSelectedItem();
            //Creamos el if
            if(!seccion.equals("Todos") && pais.equals("Todos")) {
                //Creamos una consulta preparada y la almacenamos
                enviaConsultaSeccion = miConexion.prepareStatement(consultaSeccion);
                enviaConsultaSeccion.setString(1, seccion);
                rs = enviaConsultaSeccion.executeQuery();
            }else if(seccion.equals("Todos") && !pais.equals("Todos")){
                enviaConsultaPais = miConexion.prepareStatement(consultaPais);
                enviaConsultaPais.setString(1, pais);
                rs = enviaConsultaPais.executeQuery();
            }else if(!seccion.equals("Todos") && !pais.equals("Todos")){
                enviaConsultaTodos = miConexion.prepareStatement(consultaTodos);
                enviaConsultaTodos.setString(1, seccion);
                enviaConsultaTodos.setString(2, pais);
                rs = enviaConsultaTodos.executeQuery();
            }
            while(rs.next()){
                resultado.append(rs.getString(1));
                resultado.append(", ");
                resultado.append(rs.getString(2));
                resultado.append(", ");
                resultado.append(rs.getString(3));
                resultado.append(", ");
                resultado.append(rs.getString(4));
                resultado.append("\n");
            }
        }catch(Exception e){

        }
    }
    //Variables
    private JComboBox secciones;
    private JComboBox paises;
    private JTextArea resultado;
    private PreparedStatement enviaConsultaSeccion;
    private PreparedStatement enviaConsultaPais;
    private PreparedStatement enviaConsultaTodos;
    private final String consultaSeccion="SELECT NOMBREARTÍCULO, SECCIÓN, PRECIO, PAÍSDEORIGEN FROM PRODUCTOS WHERE SECCIÓN=?";
    private final String consultaPais="SELECT NOMBREARTÍCULO, SECCIÓN, PRECIO, PAÍSDEORIGEN FROM PRODUCTOS WHERE PAÍSDEORIGEN=?";
    private final String consultaTodos="SELECT NOMBREARTÍCULO, SECCIÓN, PRECIO, PAÍSDEORIGEN FROM PRODUCTOS WHERE SECCIÓN=? AND PAÍSDEORIGEN=?";
    private Connection miConexion;
    //Variables
}