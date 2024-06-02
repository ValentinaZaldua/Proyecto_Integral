import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

public class Calculadora_integrales extends JFrame {
    private JTextField functionInput;
    private JTextField lowerLimitInput;
    private JTextField upperLimitInput;
    private JTextField intervalsInput;
    private JLabel resultLabel;

    public Calculadora_integrales() {
        setTitle("Calculadora de Integrales");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // Crear componentes de la interfaz gráfica
        functionInput = new JTextField();
        lowerLimitInput = new JTextField();
        upperLimitInput = new JTextField();
        intervalsInput = new JTextField();
        resultLabel = new JLabel("Resultado:");

        JButton calculateButton = new JButton("Calcular");

        // Añadir componentes a la ventana
        add(new JLabel("Función:"));
        add(functionInput);
        add(new JLabel("Límite inferior:"));
        add(lowerLimitInput);
        add(new JLabel("Límite superior:"));
        add(upperLimitInput);
        add(new JLabel("Número de intervalos:"));
        add(intervalsInput);
        add(calculateButton);
        add(resultLabel);

        // Acción al presionar el botón
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateIntegral();
            }
        });
    }

    private void calculateIntegral() {
        try {
            String functionStr = functionInput.getText();
            double lowerLimit = Double.parseDouble(lowerLimitInput.getText());
            double upperLimit = Double.parseDouble(upperLimitInput.getText());
            int intervals = Integer.parseInt(intervalsInput.getText());

            Function<Double, Double> function = (Function<Double, Double>) eval(functionStr);

            double result = integrateSimpson(function, lowerLimit, upperLimit, intervals);

            resultLabel.setText("Resultado: " + result);
        } catch (Exception e) {
            resultLabel.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculadora_integrales calculator = new Calculadora_integrales();
            calculator.setVisible(true);
        });
    }

    public static Object eval(final String str) {
        class Parser {
            int pos = -1, c;

            void nextChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (c == ' ') nextChar();
                if (c == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) c);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((c >= '0' && c <= '9') || c == '.') {
                    while ((c >= '0' && c <= '9') || c == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (c >= 'a' && c <= 'z') {
                    while (c >= 'a' && c <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) c);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());

                return x;
            }
        }
        return new Function<Double, Double>() {
            @Override
            public Double apply(Double x) {
                return new Parser().parse();
            }
        };
    }

    public static double integrateSimpson(Function<Double, Double> f, double a, double b, int n) {
        if (n % 2 != 0) {
            throw new IllegalArgumentException("El número de intervalos debe ser par.");
        }

        double h = (b - a) / n;
        double sum = f.apply(a) + f.apply(b);

        for (int i = 1; i < n; i += 2) {
            sum += 4 * f.apply(a + i * h);
        }

        for (int i = 2; i < n - 1; i += 2) {
            sum += 2 * f.apply(a + i * h);
        }

        return (h / 3) * sum;
    }
}
