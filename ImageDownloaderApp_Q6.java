/*
 Task 6
Implement a Multithreaded Asynchronous Image Downloader in Java Swing

Task Description:
You are tasked with designing and implementing a multithreaded asynchronous image downloader in a Java Swing application. The application should allow users to enter a URL and download images from that URL in the background, while keeping the UI responsive. The image downloader should utilize multithreading and provide a smooth user experience when downloading images.

Requirements:
Design and implement a GUI application that allows users to enter a URL and download images.
Implement a multithreaded asynchronous framework to handle the image downloading process in the background.
Provide a user interface that displays the progress of each image download, including the current download status and completion percentage.
Utilize a thread pool to manage the concurrent downloading of multiple images, ensuring efficient use of system resources.
Implement a mechanism to handle downloading errors or exceptions, displaying appropriate error messages to the user.
Use thread synchronization mechanisms, such as locks or semaphores, to ensure data integrity and avoid conflicts during image downloading.
Provide options for the user to pause, resume, or cancel image downloads.
Test the application with various URLs containing multiple images to verify its functionality and responsiveness.
Include proper error handling and reporting for cases such as invalid URLs or network failures
[20 Marks]
 */
package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloaderApp_Q6 extends JFrame {

    private ExecutorService threadPool;
    private JPanel downloadPanel;

    private JButton startButton;
    private JTextField urlField;
    private JButton stopButton;
    private JLabel titleLabel;
    private JLabel statusLabel;

    public ImageDownloaderApp_Q6() {
        initializeComponents();
        threadPool = Executors.newCachedThreadPool();
        setSize(800, 600);
    }

    private void initializeComponents() {
        startButton = new JButton("Start Download");
        urlField = new JTextField("Enter image URL here...", 25);
        stopButton = new JButton("Stop All Downloads");
        titleLabel = new JLabel("Image Downloader by RoshanBaidar", JLabel.CENTER);
        statusLabel = new JLabel("Download Status:", JLabel.CENTER);

        // Set colors for buttons
        startButton.setBackground(new Color(0, 153, 51)); // Green
        startButton.setForeground(Color.WHITE); // White text
        stopButton.setBackground(new Color(255, 51, 51)); // Red
        stopButton.setForeground(Color.WHITE); // White text

        startButton.addActionListener(this::startButtonActionPerformed);
        stopButton.addActionListener(this::stopButtonActionPerformed);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel controlPanel = new JPanel(new FlowLayout());

        controlPanel.add(new JLabel("URL:"));
        controlPanel.add(urlField);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        downloadPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(downloadPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private boolean isValidImageUrl(String url) {
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        for (String extension : validExtensions) {
            if (url.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private void startButtonActionPerformed(ActionEvent evt) {
        String imageUrl = urlField.getText();
        if (!isValidImageUrl(imageUrl)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid image URL", "Invalid URL", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DownloadPanel_Q6 downloadPanel = new DownloadPanel_Q6(imageUrl);
        this.downloadPanel.add(downloadPanel);
        this.downloadPanel.revalidate();
        this.downloadPanel.repaint();

        DownloadTask_Q6 downloadTask = new DownloadTask_Q6(imageUrl, downloadPanel);
        threadPool.submit(downloadTask);
    }

    private void stopButtonActionPerformed(ActionEvent evt) {
        Component[] components = downloadPanel.getComponents();
        for (Component component : components) {
            if (component instanceof DownloadPanel_Q6) {
                DownloadPanel_Q6 downloadPanel = (DownloadPanel_Q6) component;
                downloadPanel.stopDownload();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageDownloaderApp_Q6 imageDownloaderApp = new ImageDownloaderApp_Q6();
            imageDownloaderApp.setVisible(true);
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        threadPool.shutdown();
    }
}

class DownloadPanel_Q6 extends JPanel {

    private String imageUrl;
    private JLabel urlLabel;
    private JProgressBar progressBar;
    private JButton pauseButton;
    private JButton cancelButton;
    private volatile boolean isPaused;
    private volatile boolean isCancelled;

    public DownloadPanel_Q6(String imageUrl) {
        this.imageUrl = imageUrl;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        urlLabel = new JLabel(imageUrl);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        pauseButton = new JButton("Pause");
        cancelButton = new JButton("Cancel");

        // Set colors for buttons
        pauseButton.setBackground(new Color(255, 153, 0)); // Orange
        pauseButton.setForeground(Color.WHITE); // White text
        cancelButton.setBackground(new Color(51, 102, 255)); // Blue
        cancelButton.setForeground(Color.WHITE); // White text

        pauseButton.addActionListener(e -> togglePause());
        cancelButton.addActionListener(e -> stopDownload());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(pauseButton);
        buttonPanel.add(cancelButton);

        add(urlLabel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
        if (progress == 100) {
            removePanel();
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
    }

    public void stopDownload() {
        isCancelled = true;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    private void removePanel() {
        SwingUtilities.invokeLater(() -> {
            Container parent = getParent();
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        });
    }
}

class DownloadTask_Q6 implements Runnable {

    private static final int TOTAL_BYTES = 1000;
    private static final int DOWNLOAD_INCREMENT = 10;

    private String imageUrl;
    private DownloadPanel_Q6 downloadPanel;

    public DownloadTask_Q6(String imageUrl, DownloadPanel_Q6 downloadPanel) {
        this.imageUrl = imageUrl;
        this.downloadPanel = downloadPanel;
    }

    @Override
    public void run() {
        int downloadedBytes = 0;
        try {
            while (downloadedBytes < TOTAL_BYTES) {
                if (downloadPanel.isCancelled()) {
                    downloadPanel.setProgress(0);
                    return;
                }
                if (!downloadPanel.isPaused()) {
                    int progress = (int) ((double) downloadedBytes / TOTAL_BYTES * 100);
                    SwingUtilities.invokeLater(() -> downloadPanel.setProgress(progress));
                    Thread.sleep(100);
                    downloadedBytes += DOWNLOAD_INCREMENT;
                }
            }
            SwingUtilities.invokeLater(() -> downloadPanel.setProgress(100));
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }
}
