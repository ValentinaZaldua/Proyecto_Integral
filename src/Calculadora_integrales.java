import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

public class Calculadora_integrales extends JFrame {
    private JTextField funcionField;
    private JTextField aField;
    private JTextField bField;
    private JTextField nField;
    private JLabel resultadoLabel;

    public Calculadora_integrales() {
        setTitle("Calculadora de Longitud de Arco");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        funcionField = new JTextField();
        aField = new JTextField();
        bField = new JTextField();
        nField = new JTextField();
        resultadoLabel = new JLabel("");

        JButton ingresarFuncionButton = new JButton("Ingresar Función");
        ingresarFuncionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String funcion = JOptionPane.showInputDialog(null, "Ingrese la función a integrar (por ejemplo, x^2, sin(x), cos(x), tan(x), exp(x)):");
                funcionField.setText(funcion);
            }
        });

        JButton calcularButton = new JButton("Calcular Longitud de Arco");
        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String funcion = funcionField.getText();
                double a = Double.parseDouble(aField.getText());
                double b = Double.parseDouble(bField.getText());
                if (a >= b) {
                    JOptionPane.showMessageDialog(null, "El límite inferior (a) debe ser menor que el límite superior (b).");
                } else {
                    int n = Integer.parseInt(nField.getText());
                    if (n % 2 != 0) {
                        JOptionPane.showMessageDialog(null, "El número de intervalos (n) debe ser par.");
                        return;
                    }
                    DoubleUnaryOperator f = x -> evaluarFuncion(funcion, x);
                    double resultado = longitudArco(a, b, n, f);
                    resultadoLabel.setText("La longitud de arco es: " + resultado);
                }
            }
        });

        JButton limpiarButton = new JButton("Limpiar Datos");
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                funcionField.setText("");
                aField.setText("");
                bField.setText("");
                nField.setText("");
                resultadoLabel.setText("");
            }
        });

        panel.add(new JLabel("Función a integrar:"));
        panel.add(funcionField);
        panel.add(ingresarFuncionButton);
        panel.add(new JLabel("Límite inferior (a):"));
        panel.add(aField);
        panel.add(new JLabel(""));
        panel.add(new JLabel("Límite superior (b):"));
        panel.add(bField);
        panel.add(new JLabel(""));
        panel.add(new JLabel("Número de intervalos (n):"));
        panel.add(nField);
        panel.add(new JLabel(""));
        panel.add(calcularButton);
        panel.add(limpiarButton);
        panel.add(resultadoLabel);

        add(panel);
        setVisible(true);
    }

    // Método para calcular la longitud de arco por el método de Simpson
    public static double longitudArco(double a, double b, int n, DoubleUnaryOperator funcion) {
        double h = (b - a) / n;
        double sum = 0;

        for (int i = 0; i < n; i++) {
            double x0 = a + i * h;
            double x1 = x0 + h;
            double y0 = funcion.applyAsDouble(x0);
            double y1 = funcion.applyAsDouble(x1);
            double dx = x1 - x0;
            double dy = y1 - y0;
            sum += Math.sqrt(dx * dx + dy * dy);
        }

        return sum;
    }

    // Método para evaluar una función en un punto
    public static double evaluarFuncion(String funcion, double x) {
        switch (funcion) {
            case "sin(x)":
                return Math.sin(x);
            case "cos(x)":
                return Math.cos(x);
            case "tan(x)":
                return Math.tan(x);
            case "exp(x)":
                return Math.exp(x);
            default:
                // Si la función no coincide con ninguna de las funciones conocidas, se considera una función algebraica
                return evaluarFuncionAlgebraica(funcion, x);
        }
    }

    // Método para evaluar una función algebraica en un punto
    public static double evaluarFuncionAlgebraica(String funcion, double x) {
        javax.script.ScriptEngineManager mgr = new javax.script.ScriptEngineManager();
        javax.script.ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            Object result = engine.eval(funcion.replaceAll("x", String.valueOf(x)));
            if (result instanceof Integer) {
                return (double) ((Integer) result);
            } else {
                return (double) result;
            }
        } catch (javax.script.ScriptException e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    public static void main(String[] args) {
        new Calculadora_integrales();
    }
}

