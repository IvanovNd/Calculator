package Package01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Calculator extends JFrame implements ActionListener {

    Calculator() {
        create_calculator();
    }

    private JTextField textField_1 = new JTextField();
    private JTextField textField_2 = new JTextField();
    private Parser parser = new Parser();

    private void create_calculator() {
        JFrame frame = new JFrame("Это калькулятор!");
        frame.setSize(300, 170);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        textField_2.setEditable(false);
        JLabel label_1 = new JLabel("Введите выражение:");
        JLabel label_2 = new JLabel("Результат:");
        JButton result_button = new JButton("Вычислить");

        panel.add(label_1);
        panel.add(textField_1);
        panel.add(result_button);
        panel.add(label_2);
        panel.add(textField_2);

        label_1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        textField_1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        result_button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        result_button.setMaximumSize(new Dimension(200, 100));
        label_2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        textField_2.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        result_button.addActionListener(this);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private double round_3 (double d) {
        d = d * 1000;
        int i = (int) Math.round(d);
        d = (double) i / 1000;
        return d;
    }

    public void actionPerformed (ActionEvent actionEvent) {
        double result = 0.0;
        String get_text = textField_1.getText();

        try {
            if (get_text.length() != 0) result = parser.evaluate(get_text);
            textField_2.setText(Double.toString(round_3(result)));
        } catch (ParserException exception) {
            textField_2.setText(exception.toString());
        }
    }
}
