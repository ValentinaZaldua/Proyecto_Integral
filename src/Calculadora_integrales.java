import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora_integrales extends JFrame {
    private JTextField funcionField;
    private JTextField aField;
    private JTextField bField;
    private JTextField nField;
    private JLabel resultadoLabel;

    public Calculadora_integrales() {
        setTitle("Calculadora de Integrales");
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
        JButton calcularButton = new JButton("Calcular Integral");
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
                    double resultado = simpson(a, b, n, funcion);
                    resultadoLabel.setText("El resultado de la integral es: " + resultado);
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

    // Método para calcular la integral por el método de Simpson
    public static double simpson(double a, double b, int n, String funcion) {
        double h = (b - a) / n;
        double sum = funcion(a, funcion) + funcion(b, funcion);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += 2 * funcion(x, funcion) * (i % 2 == 0 ? 2 : 4);
        }

        return sum * h / 3;
    }

    // Método para evaluar una función en un punto
    public static double funcion(double x, String funcion) {
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
                return evaluarFuncionAlgebraica(x, funcion);
        }
    }

    // Método para evaluar una función algebraica en un punto
    public static double evaluarFuncionAlgebraica(double x, String funcion) {
        // Aquí puedes implementar la evaluación de la función algebraica
        // En este ejemplo, simplemente se evalúa la expresión algebraica utilizando la clase ScriptEngine
        javax.script.ScriptEngineManager mgr = new javax.script.ScriptEngineManager();
        javax.script.ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            return (double) engine.eval(funcion.replaceAll("x", String.valueOf(x)));
        } catch (javax.script.ScriptException e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }

    public static void main(String[] args) {
        new Calculadora_integrales();
    }
}
