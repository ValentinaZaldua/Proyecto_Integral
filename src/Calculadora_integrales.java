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
        setTitle("Calculadora de Integrales y Longitud de Arco");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

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

        JButton calcularIntegralButton = new JButton("Calcular Integral");
        calcularIntegralButton.addActionListener(new ActionListener() {
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
                    double resultado = simpson(a, b, n, funcion, false);
                    resultadoLabel.setText("El resultado de la integral es: " + resultado);
                }
            }
        });

        JButton calcularLongitudArcoButton = new JButton("Calcular Longitud de Arco");
        calcularLongitudArcoButton.addActionListener(new ActionListener() {
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
                    double resultado = simpson(a, b, n, funcion, true);
                    resultadoLabel.setText("La longitud del arco es: " + resultado);
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
        panel.add(calcularIntegralButton);
        panel.add(calcularLongitudArcoButton);
        panel.add(limpiarButton);
        panel.add(resultadoLabel);

        add(panel);
        setVisible(true);
    }

    // Método para calcular la integral o la longitud de arco por el método de Simpson
    public static double simpson(double a, double b, int n, String funcion, boolean longitudArco) {
        double h = (b - a) / n;
        double sum = longitudArco ? longitudArcoIntegrando(a, funcion) + longitudArcoIntegrando(b, funcion) : funcion(a, funcion) + funcion(b, funcion);

        for (int i = 0; i < n-1; i++) {
            double x = a + (i+1) * h;
            double fx = longitudArco ? longitudArcoIntegrando(x, funcion) : funcion(x, funcion);
            sum += ((i+1) % 2 == 0 ? 2 : 4) * fx;
        }

        return sum * h / 3;
    }

    // Método para evaluar la función
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
                return evaluarFuncionAlgebraica(funcion, x);
        }
    }

    // Método para evaluar la longitud de arco en un punto
    public static double longitudArcoIntegrando(double x, String funcion) {
        double dfdx = derivada(x, funcion);
        return Math.sqrt(1 + dfdx * dfdx);
    }

    // Método para evaluar la derivada de una función en un punto
    public static double derivada(double x, String funcion) {
        double h = 1e-5;
        return (funcion(x + h, funcion) - funcion(x - h, funcion)) / (2 * h);
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

