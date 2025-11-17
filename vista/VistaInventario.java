package examen;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class VistaInventario extends JFrame {

    private ControladorInventario controlador;
    private JTextArea areaTexto;
    
    private static final String ARCHIVO_TEMP = "temp_asociaciones.txt";

    
    public VistaInventario(ControladorInventario controlador) {
        this.controlador = controlador;
        setTitle("Gestión de Inventario y Mantenimientos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentes();
    }

    private void initComponentes() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 5, 5, 5));

        JButton btnRegistrar = new JButton("Registrar Par");
        JButton btnVer = new JButton("Ver Pares");
        JButton btnGuardarArchivo = new JButton("Guardar Archivo");
        JButton btnCargarArchivo = new JButton("Cargar Archivo");
        JButton btnGuardarBD = new JButton("Guardar BD y Salir");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVer);
        panelBotones.add(btnGuardarArchivo);
        panelBotones.add(btnCargarArchivo);
        panelBotones.add(btnGuardarBD);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaTexto);

        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Listeners de botones
        btnRegistrar.addActionListener(e -> registrarPar());
        btnVer.addActionListener(e -> mostrarPares());
        btnGuardarArchivo.addActionListener(e -> guardarArchivo());
        btnCargarArchivo.addActionListener(e -> cargarArchivo());
        btnGuardarBD.addActionListener(e -> guardarBDySalir());
    }

    private void registrarPar() {
        try {
            // Panel de ingreso de Equipo
            JTextField txtIdE = new JTextField();
            JTextField txtNombre = new JTextField();
            JTextField txtTipo = new JTextField();

            Object[] equipoCampos = {
                    "ID Equipo:", txtIdE,
                    "Nombre:", txtNombre,
                    "Tipo:", txtTipo
            };
            int resp = JOptionPane.showConfirmDialog(this, equipoCampos, "Registrar Equipo", JOptionPane.OK_CANCEL_OPTION);
            if (resp != JOptionPane.OK_OPTION) return;

            Equipo equipo = new Equipo(
                    Integer.parseInt(txtIdE.getText()),
                    txtNombre.getText(),
                    txtTipo.getText()
            );

            // Panel de ingreso de Mantenimiento
            JTextField txtIdM = new JTextField();
            JTextField txtDesc = new JTextField();
            JTextField txtTec = new JTextField();
            JTextField txtFecha = new JTextField(); // Formato yyyy-mm-dd
            JTextField txtCosto = new JTextField();

            Object[] manCampos = {
                    "ID Mantenimiento:", txtIdM,
                    "Descripción:", txtDesc,
                    "Técnico:", txtTec,
                    "Fecha (YYYY-MM-DD):", txtFecha,
                    "Costo:", txtCosto
            };
            resp = JOptionPane.showConfirmDialog(this, manCampos, "Registrar Mantenimiento", JOptionPane.OK_CANCEL_OPTION);
            if (resp != JOptionPane.OK_OPTION) return;

            Mantenimiento man = new Mantenimiento(
                    Integer.parseInt(txtIdM.getText()),
                    txtDesc.getText(),
                    txtTec.getText(),
                    LocalDate.parse(txtFecha.getText()),
                    Double.parseDouble(txtCosto.getText())
            );

            controlador.registrarAsociacion(equipo, man);
            JOptionPane.showMessageDialog(this, "Par registrado correctamente!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar par: " + ex.getMessage());
        }
    }

    private void mostrarPares() {
        List<ParAsociativo<Equipo, Mantenimiento>> lista = controlador.listarAsociaciones();
        areaTexto.setText("");
        for (ParAsociativo<Equipo, Mantenimiento> par : lista) {
            areaTexto.append(par.toString() + "\n");
        }
    }

    private void guardarArchivo() {
        try {
            // Crear un controlador temporal que use el archivo temp
            ControladorInventario ctrlTemp = new ControladorInventario(ARCHIVO_TEMP);

            // Guardar todos los pares actuales en ese archivo
            for (ParAsociativo<Equipo, Mantenimiento> par : controlador.listarAsociaciones()) {
                ctrlTemp.registrarAsociacion(par.getPrimero(), par.getSegundo());
            }

            if (ctrlTemp.guardarArchivo()) {
                JOptionPane.showMessageDialog(this, "Archivo temporal guardado correctamente!");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo temporal.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }


    private void cargarArchivo() {
        // Abrir un JFileChooser para seleccionar archivo
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo TXT");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto", "txt"));

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File archivoSeleccionado = fileChooser.getSelectedFile();
            // Crear un nuevo repositorio temporal con el archivo seleccionado
            controlador = new ControladorInventario(archivoSeleccionado.getAbsolutePath());

            // Intentar cargar desde ese archivo
            if (controlador.cargarArchivo()) {
                JOptionPane.showMessageDialog(this, "Archivo cargado correctamente!");
                // Actualizar la vista con los pares cargados
                mostrarPares();
            } else {
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo.");
            }
        }
    }

    private void guardarBDySalir() {
        if (controlador.guardarEnBD()) {
            JOptionPane.showMessageDialog(this, "Datos guardados en BD. Saliendo...");

            // Vaciar el archivo temporal
            try (java.io.FileWriter fw = new java.io.FileWriter(ARCHIVO_TEMP)) {
                fw.write(""); // sobrescribe con contenido vacío
            } catch (Exception e) {
                System.err.println("Error al vaciar el archivo temporal: " + e.getMessage());
            }

            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar en BD. Saliendo...");
            System.exit(1);
        }
    }


    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorInventario ctrl = new ControladorInventario("asociaciones.txt");
            new VistaInventario(ctrl).setVisible(true);
        });
    }
}
