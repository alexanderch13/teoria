
package vista;

import controlador.ControladorInventario;
import modelo.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VistaInventario extends JFrame {

private ControladorInventario controlador;

private JTextArea areaLista;
private JTextArea areaLog;

private JComboBox<String> comboOrden;
private JComboBox<String> comboPresentacion;
private JCheckBox chkMostrarEventos;

private EstrategiaListado presentador = null;

private JPanel panelFormulario; // Panel dinámico para formularios

public VistaInventario(ControladorInventario controlador) {
    this.controlador = controlador;
    setTitle("Gestión de Inventario y Mantenimientos");
    setSize(960, 620);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    initComponentes();

    controlador.getRepositorioArchivo().agregarListener(evento -> {
        if (chkMostrarEventos.isSelected()) appendLog("[EVENT] " + evento);
        SwingUtilities.invokeLater(this::mostrarPares);
    });

    comboPresentacion.setSelectedItem("Detallada");
    comboOrden.setSelectedItem("Normal");

    mostrarPares();
    appendLog("[i] Interfaz lista.");
}

private void initComponentes() {
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout(8, 8));

    // --------- LEFT: botones de acciones ----------
    JPanel left = new JPanel();
    left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JButton btnRegistrar = new JButton("Registrar par");
    JButton btnEliminar = new JButton("Eliminar equipo");
    JButton btnGuardar = new JButton("Guardar archivo");
    JButton btnCargar = new JButton("Cargar archivo");
    JButton btnGuardarBD = new JButton("Guardar BD y salir");
    JButton btnLimpiar = new JButton("Limpiar inventario");

    Dimension btnDim = new Dimension(200, 32);
    for (JButton b : new JButton[]{btnRegistrar, btnEliminar, btnGuardar, btnCargar, btnGuardarBD, btnLimpiar}) {
        b.setMaximumSize(btnDim);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(b);
        left.add(Box.createVerticalStrut(8));
    }

    chkMostrarEventos = new JCheckBox("Mostrar eventos", true);
    chkMostrarEventos.setMaximumSize(btnDim);
    chkMostrarEventos.setAlignmentX(Component.CENTER_ALIGNMENT);
    left.add(chkMostrarEventos);
    left.add(Box.createVerticalStrut(8));

    left.add(new JLabel("Orden:"));
    comboOrden = new JComboBox<>(new String[]{"Normal", "Por Fecha", "Por Costo", "Por Técnico"});
    comboOrden.setMaximumSize(btnDim);
    comboOrden.setAlignmentX(Component.CENTER_ALIGNMENT);
    left.add(comboOrden);
    left.add(Box.createVerticalStrut(8));

    left.add(new JLabel("Presentación:"));
    comboPresentacion = new JComboBox<>(new String[]{"Detallada", "Compacta", "Técnica"});
    comboPresentacion.setMaximumSize(btnDim);
    comboPresentacion.setAlignmentX(Component.CENTER_ALIGNMENT);
    left.add(comboPresentacion);
    left.add(Box.createVerticalStrut(8));

    cp.add(left, BorderLayout.WEST);

    // --------- CENTER: lista + log ----------
    areaLista = new JTextArea(); areaLista.setEditable(false); areaLista.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    JScrollPane scrollLista = new JScrollPane(areaLista);

    areaLog = new JTextArea(); areaLog.setEditable(false); areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    areaLog.setForeground(new Color(30, 120, 30));
    JScrollPane scrollLog = new JScrollPane(areaLog);

    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollLista, scrollLog);
    split.setResizeWeight(0.7);
    cp.add(split, BorderLayout.CENTER);

    // --------- RIGHT: panel dinámico ----------
    panelFormulario = new JPanel();
    panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
    panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario"));
    panelFormulario.setPreferredSize(new Dimension(320, 0));
    cp.add(panelFormulario, BorderLayout.EAST);

    // ---------- listeners botones ----------
    btnRegistrar.addActionListener(a -> mostrarFormularioRegistrar());
    btnEliminar.addActionListener(a -> mostrarFormularioEliminar());

    btnGuardar.addActionListener(a -> {
        if (controlador.guardarArchivo()) appendLog("[+] Archivo guardado.");
        else appendLog("[-] Error al guardar.");
    });
    btnCargar.addActionListener(a -> cargarArchivo());
    btnGuardarBD.addActionListener(a -> {
        if (controlador.guardarEnBD()) { appendLog("[+] Guardado en BD. Saliendo..."); System.exit(0);}
        else appendLog("[-] Error al guardar en BD.");
    });
    btnLimpiar.addActionListener(a -> {controlador.limpiarInventario(); mostrarPares(); appendLog("[+] Inventario limpiado.");});

    comboOrden.addActionListener(e -> mostrarPares());
    comboPresentacion.addActionListener(e -> actualizarPresentador());
}

// ---------- Formulario Registrar Par ----------
private void mostrarFormularioRegistrar() {
    panelFormulario.removeAll();

    JTextField idE = new JTextField(), nombre = new JTextField(), tipo = new JTextField();
    JTextField idM = new JTextField(), desc = new JTextField(), tec = new JTextField(), fecha = new JTextField("YYYY-MM-DD"), costo = new JTextField();

    panelFormulario.add(new JLabel("Registrar Par - Equipo"));
    panelFormulario.add(new JLabel("ID:")); panelFormulario.add(idE);
    panelFormulario.add(new JLabel("Nombre:")); panelFormulario.add(nombre);
    panelFormulario.add(new JLabel("Tipo:")); panelFormulario.add(tipo);

    panelFormulario.add(Box.createVerticalStrut(10));
    panelFormulario.add(new JLabel("Mantenimiento"));
    panelFormulario.add(new JLabel("ID:")); panelFormulario.add(idM);
    panelFormulario.add(new JLabel("Descripción:")); panelFormulario.add(desc);
    panelFormulario.add(new JLabel("Técnico:")); panelFormulario.add(tec);
    panelFormulario.add(new JLabel("Fecha (YYYY-MM-DD):")); panelFormulario.add(fecha);
    panelFormulario.add(new JLabel("Costo:")); panelFormulario.add(costo);

    JButton btnConfirmar = new JButton("Registrar");
    panelFormulario.add(Box.createVerticalStrut(10));
    panelFormulario.add(btnConfirmar);

    btnConfirmar.addActionListener(ev -> {
        try {
            Equipo e = new Equipo(Integer.parseInt(idE.getText().trim()), nombre.getText().trim(), tipo.getText().trim());
            Mantenimiento m = new Mantenimiento(Integer.parseInt(idM.getText().trim()), desc.getText().trim(), tec.getText().trim(), LocalDate.parse(fecha.getText().trim()), Double.parseDouble(costo.getText().trim()));
            controlador.registrarAsociacion(e, m);
            appendLog("[+] Par registrado: " + e.getNombre());
            mostrarPares();
        } catch (Exception ex) {
            appendLog("[-] Error al registrar: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    });

    panelFormulario.revalidate();
    panelFormulario.repaint();
}

// ---------- Formulario Eliminar Equipo ----------
private void mostrarFormularioEliminar() {
    panelFormulario.removeAll();

    JTextField idE = new JTextField();
    idE.setMaximumSize(new Dimension(150, 24)); 
    panelFormulario.add(new JLabel("Eliminar Equipo"));
    panelFormulario.add(new JLabel("ID Equipo:")); panelFormulario.add(idE);

    JButton btnConfirmar = new JButton("Eliminar");
    panelFormulario.add(Box.createVerticalStrut(10));
    panelFormulario.add(btnConfirmar);

    btnConfirmar.addActionListener(ev -> {
        try {
            int id = Integer.parseInt(idE.getText().trim());
            ParAsociativo<Equipo, Mantenimiento> toRemove = null;
            for (ParAsociativo<Equipo, Mantenimiento> par : controlador.listarAsociaciones()) {
                if (par.getPrimero().getId() == id) { toRemove = par; break; }
            }
            if (toRemove != null) {
                controlador.eliminarAsociacion(toRemove);
                appendLog("[+] Equipo eliminado: " + toRemove.getPrimero().getNombre());
                mostrarPares();
            } else appendLog("[-] No se encontró equipo con ID " + id);
        } catch (Exception ex) {
            appendLog("[-] Error al eliminar: " + ex.getMessage());
        }
    });

    panelFormulario.revalidate();
    panelFormulario.repaint();
}

// ---------- Resto de métodos ----------
private void mostrarPares() {
    List<ParAsociativo<Equipo, Mantenimiento>> lista = controlador.listarAsociaciones();
    areaLista.setText("");
    if (lista == null || lista.isEmpty()) { areaLista.setText("Sin registros.\n"); return; }

    String orden = (String) comboOrden.getSelectedItem();
    if ("Por Fecha".equals(orden)) Collections.sort(lista, Comparator.comparing(p -> p.getSegundo().getFecha()));
    else if ("Por Costo".equals(orden)) Collections.sort(lista, Comparator.comparingDouble(p -> p.getSegundo().getCosto()));
    else if ("Por Técnico".equals(orden)) Collections.sort(lista, Comparator.comparing(p -> p.getSegundo().getTecnico().toLowerCase()));

    if (presentador == null) actualizarPresentador();

    for (ParAsociativo<Equipo, Mantenimiento> par : lista) areaLista.append(presentador.representar(par) + "\n");
}

private void actualizarPresentador() {
    String sel = (String) comboPresentacion.getSelectedItem();
    switch (sel) {
        case "Compacta":
            presentador = par -> par.getPrimero().getNombre();
            break;
        case "Técnica":
            presentador = par -> {
                Equipo e = par.getPrimero();
                Mantenimiento m = par.getSegundo();
                return String.format("ID:%d | %s | Tipo:%s | Mant:%s | Tec:%s | %s | %.2f", e.getId(), e.getNombre(), e.getTipo(), m.getDescripcion(), m.getTecnico(), m.getFecha(), m.getCosto());
            };
            break;
        default: // Detallada
            presentador = ParAsociativo::toString;
            break;
    }
    mostrarPares();
}

private void appendLog(String s) { areaLog.append(s + "\n"); areaLog.setCaretPosition(areaLog.getDocument().getLength()); }

private void cargarArchivo() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Seleccionar archivo TXT");
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto", "txt"));
    int r = chooser.showOpenDialog(this);
    if (r == JFileChooser.APPROVE_OPTION) {
        java.io.File f = chooser.getSelectedFile();
        try {
            RepositorioArchivo temp = new RepositorioArchivo(f.getAbsolutePath());
            temp.cargarDesdeArchivo();
            controlador.getRepositorioArchivo().reemplazarTodo(temp.listar());
            appendLog("[+] Inventario cargado desde: " + f.getName());
            mostrarPares();
        } catch (Exception ex) { appendLog("[-] Error al cargar archivo: " + ex.getMessage()); }
    } else appendLog("[i] Carga cancelada.");
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new VistaInventario(new ControladorInventario("asociaciones.txt")).setVisible(true));
}

}

