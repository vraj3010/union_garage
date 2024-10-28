package com.example.spring_boot.repository;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class tut {
    public static void main(String[] args) {
        
        int carId = 12;  // Example car ID

        byte[] proofDocument = CarsDAO.getProofDocumentByCarId(carId);

        if (proofDocument != null) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(proofDocument);
                BufferedImage image = ImageIO.read(bais);

                // Displaying the image in a JFrame (Swing)
                JFrame frame = new JFrame("Proof Document Preview");
                frame.setSize(500, 500);
                JLabel label = new JLabel(new ImageIcon(image));
                frame.add(label);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to read proof document as an image.");
            }
        } else {
            System.out.println("No proof document found for the specified car ID.");
        }
    }
}