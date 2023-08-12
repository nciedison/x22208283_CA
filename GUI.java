package service.project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.grpc.ConnectivityState;
import io.grpc.stub.StreamObserver;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private static final Logger logger = Logger.getLogger(GUI.class.getName());
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private Service2Client client;
    public Service3Client service3Client;
    
    
    
    public void setClient(Service2Client client) {
        this.client = client;
    }
    
    public void updateTextField(String notification) {
        textField.setText(notification);
    }
    
    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 449, 379);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Smart Dublin Bus");
        lblNewLabel.setBounds(65, 17, 330, 32);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(189, 99, 29, 29);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Search my current location");
        btnNewButton.setBounds(52, 61, 350, 29);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = RandomNum(1, 4);
                String notification = " "  + i ;
                textField.setText(notification);
            }
        });
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_1 = new JLabel("Route notification: Bus");
        lblNewLabel_1.setBounds(35, 102, 153, 21);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("route is altered due to an event.");
        lblNewLabel_2.setBounds(230, 104, 214, 16);
        contentPane.add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("The ID of the bus:");
        lblNewLabel_3.setBounds(35, 137, 118, 26);
        contentPane.add(lblNewLabel_3);
        
        textField_1 = new JTextField();
        textField_1.setBounds(216, 137, 81, 29);
        contentPane.add(textField_1);
        textField_1.setColumns(10);
        
        JLabel lblNewLabel_4 = new JLabel("Available seats of the bus:");
        lblNewLabel_4.setBounds(35, 175, 176, 21);
        contentPane.add(lblNewLabel_4);
        
        textField_2 = new JTextField();
        textField_2.setBounds(216, 172, 179, 29);
        contentPane.add(textField_2);
        textField_2.setColumns(10);
        
        
        JButton btnNewButton_1 = new JButton("Search bus location");
        btnNewButton_1.setBounds(52, 214, 350, 29);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Generate numbers
                double randomLatitude = service3Client.getRandomLatitude();
                double randomLongitude = service3Client.getRandomLongitude();

                // Display numbers
                textField_3.setText("Latitude: " + randomLatitude + ", Longitude: " + randomLongitude);

                
                if (service3Client != null) {
                    MsgRequest request = MsgRequest.newBuilder()
                            .setLatitude(randomLatitude)
                            .setLongitude(randomLongitude)
                            .build();
                    	StreamObserver<MsgReply> responseObserver = new StreamObserver<MsgReply>() {
                        @Override
                        public void onNext(MsgReply msgReply) {
                            
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            System.err.println("Error in sending location: " + throwable.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("Location sent to server");
                        }
                    };

                    
                    service3Client.sendLocation(request, responseObserver);
                    String currentLocation = textField_3.getText();
                    if (!currentLocation.isEmpty()) {
                        String randomAirCode = generateRandomAirCode();
                        textField_4.setText(randomAirCode);
                    } else {
                        textField_4.setText("Please update location first.");
                    }
                   
                    
                }
            }
        });
        contentPane.add(btnNewButton_1);
        
        JLabel lblNewLabel_5 = new JLabel("The bus location:");
        lblNewLabel_5.setBounds(35, 247, 118, 21);
        contentPane.add(lblNewLabel_5);
        
        textField_3 = new JTextField();
        textField_3.setBounds(151, 247, 244, 29);
        contentPane.add(textField_3);
        textField_3.setColumns(10);
        
        JButton btnNewButton_2 = new JButton("Confirm");
        btnNewButton_2.setBounds(308, 137, 117, 29);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String vehicleId = textField_1.getText();
                if (!vehicleId.isEmpty()) {
                    String availableSeats = client.checkSeats(vehicleId);
                    textField_2.setText(availableSeats);
                }
            }
        });
        contentPane.add(btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton("Refresh");
        btnNewButton_3.setBounds(52, 316, 350, 29);
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // reset GUI for refresh
                resetGUI();
            }
        });
        contentPane.add(btnNewButton_3);
        
        JLabel lblNewLabel_6 = new JLabel("The eircode:");
        lblNewLabel_6.setBounds(35, 290, 87, 16);
        contentPane.add(lblNewLabel_6);
        
        textField_4 = new JTextField();
        textField_4.setBounds(151, 281, 125, 29);
        contentPane.add(textField_4);
        textField_4.setColumns(10);
    }
    
    private void resetGUI() {
        textField.setText(""); 
        textField_1.setText(""); 
        textField_2.setText("");
        textField_3.setText("");
        textField_4.setText("");
    }

    public static int RandomNum(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }

        Random random = new Random();
        
        int randomNum = random.nextInt(max - min + 1) + min;
        return randomNum;
    }
    
    
    private String generateRandomAirCode() {
        int num = RandomNum(1, 24);
        String airCode = String.format("D%02d", num);
        
        
        Random random = new Random();
        char randomChar1 = (char) (random.nextInt(26) + 'A');
        char randomChar2 = (char) (random.nextInt(26) + 'A');
        
        
        int randomNum1 = random.nextInt(10);
        int randomNum2 = random.nextInt(10);
        
        airCode += String.format("%c%c%d%d", randomChar1, randomChar2, randomNum1, randomNum2);
        return airCode;
    }
    
    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
            	try {
                    GUI frame = new GUI();
                    frame.setVisible(true);
                    Service2Client client = new Service2Client("localhost", 50052, frame);
                    frame.setClient(client);
                    
                    Service3Client service3Client = new Service3Client("localhost", 50053, frame);
                    frame.service3Client = service3Client;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}