// Main Application Class
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

// Custom Exception Classes (Unit II - Exception Handling)
class EmptyTextException extends Exception {
    public EmptyTextException(String message) {
        super(message);
    }
}

class InvalidFileException extends Exception {
    public InvalidFileException(String message) {
        super(message);
    }
}

// Text Analysis Result Class (Unit I - OOP Principles)
class TextAnalysisResult {
    private int wordCount;
    private int characterCount;
    private int characterCountNoSpaces;
    private int sentenceCount;
    private int paragraphCount;
    private Map<String, Integer> wordFrequency;
    
    // Constructor
    public TextAnalysisResult() {
        this.wordFrequency = new HashMap<>();
    }
    
    // Getter and Setter methods (Unit I - Encapsulation)
    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }
    
    public int getCharacterCount() { return characterCount; }
    public void setCharacterCount(int characterCount) { this.characterCount = characterCount; }
    
    public int getCharacterCountNoSpaces() { return characterCountNoSpaces; }
    public void setCharacterCountNoSpaces(int characterCountNoSpaces) { 
        this.characterCountNoSpaces = characterCountNoSpaces; 
    }
    
    public int getSentenceCount() { return sentenceCount; }
    public void setSentenceCount(int sentenceCount) { this.sentenceCount = sentenceCount; }
    
    public int getParagraphCount() { return paragraphCount; }
    public void setParagraphCount(int paragraphCount) { this.paragraphCount = paragraphCount; }
    
    public Map<String, Integer> getWordFrequency() { return wordFrequency; }
    public void setWordFrequency(Map<String, Integer> wordFrequency) { 
        this.wordFrequency = wordFrequency; 
    }
}

// Text Analyzer Class (Unit I - OOP, Unit III - Collections)
class TextAnalyzer {
    
    // Method to analyze text and return results
    public TextAnalysisResult analyzeText(String text) throws EmptyTextException {
        if (text == null || text.trim().isEmpty()) {
            throw new EmptyTextException("Text cannot be empty!");
        }
        
        TextAnalysisResult result = new TextAnalysisResult();
        
        // Count characters
        result.setCharacterCount(text.length());
        result.setCharacterCountNoSpaces(text.replaceAll("\\s", "").length());
        
        // Count words using StringTokenizer (Unit III - Utility Classes)
        StringTokenizer tokenizer = new StringTokenizer(text);
        result.setWordCount(tokenizer.countTokens());
        
        // Count sentences
        result.setSentenceCount(countSentences(text));
        
        // Count paragraphs
        result.setParagraphCount(countParagraphs(text));
        
        // Word frequency analysis using Collections
        result.setWordFrequency(calculateWordFrequency(text));
        
        return result;
    }
    
    private int countSentences(String text) {
        String[] sentences = text.split("[.!?]+");
        return sentences.length > 0 && !sentences[0].trim().isEmpty() ? sentences.length : 0;
    }
    
    private int countParagraphs(String text) {
        String[] paragraphs = text.split("\n\n+");
        return paragraphs.length > 0 && !paragraphs[0].trim().isEmpty() ? paragraphs.length : 0;
    }
    
    // Using HashMap for word frequency (Unit III - Collections)
    private Map<String, Integer> calculateWordFrequency(String text) {
        Map<String, Integer> frequency = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(text.toLowerCase(), " \t\n\r\f.,;:!?\"'()[]{}");
        
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }
        
        return frequency;
    }
}

// File Handler Class (Unit II - Files and I/O Streams)
class FileHandler {
    
    public String readFile(File file) throws InvalidFileException, IOException {
        if (!file.exists()) {
            throw new InvalidFileException("File does not exist: " + file.getName());
        }
        
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            throw new InvalidFileException("Only .txt files are supported!");
        }
        
        StringBuilder content = new StringBuilder();
        
        // Using BufferedReader for efficient file reading
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    public void saveResults(File file, String results) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(results);
        }
    }
}

// Main GUI Class (Unit V - GUI Programming with Swing)
public class WordCounterGUI extends JFrame implements ActionListener {
    
    // GUI Components
    private JTextArea textArea;
    private JTextArea resultsArea;
    private JButton analyzeButton;
    private JButton openFileButton;
    private JButton saveResultsButton;
    private JButton clearButton;
    private JLabel statusLabel;
    
    // Business Logic Classes
    private TextAnalyzer analyzer;
    private FileHandler fileHandler;
    private TextAnalysisResult currentResult;
    
    // Constructor
    public WordCounterGUI() {
        // Initialize business logic objects
        analyzer = new TextAnalyzer();
        fileHandler = new FileHandler();
        
        initializeGUI();
        setupEventHandlers();
    }
    
    private void initializeGUI() {
        setTitle("Word Counter Tool - Java Semester Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create main panel with GridLayout (Unit V - Layout Managers)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Left panel for input
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Input Text"));
        
        textArea = new JTextArea(20, 30);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(textArea);
        leftPanel.add(inputScrollPane, BorderLayout.CENTER);
        
        // Right panel for results
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Analysis Results"));
        
        resultsArea = new JTextArea(20, 30);
        resultsArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        resultsArea.setEditable(false);
        resultsArea.setBackground(new Color(245, 245, 245));
        JScrollPane resultsScrollPane = new JScrollPane(resultsArea);
        rightPanel.add(resultsScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        // Button panel using FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        openFileButton = new JButton("Open File");
        analyzeButton = new JButton("Analyze Text");
        saveResultsButton = new JButton("Save Results");
        clearButton = new JButton("Clear All");
        
        // Set button properties
        openFileButton.setBackground(new Color(70, 130, 180));
        openFileButton.setForeground(Color.WHITE);
        analyzeButton.setBackground(new Color(34, 139, 34));
        analyzeButton.setForeground(Color.WHITE);
        saveResultsButton.setBackground(new Color(255, 140, 0));
        saveResultsButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        
        buttonPanel.add(openFileButton);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(saveResultsButton);
        buttonPanel.add(clearButton);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusPanel.add(new JLabel("Status: "));
        statusPanel.add(statusLabel);
        
        // Add panels to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.NORTH);
        
        pack();
        setLocationRelativeTo(null); // Center the window
    }
    
    private void setupEventHandlers() {
        // Add action listeners (Unit V - Event Handling)
        openFileButton.addActionListener(this);
        analyzeButton.addActionListener(this);
        saveResultsButton.addActionListener(this);
        clearButton.addActionListener(this);
        
        // Add mouse listener for text area (Unit V - Mouse Events)
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                statusLabel.setText("Text area clicked - Ready for input");
            }
        });
    }
    
    // Event handling method (Unit V - Event Handling)
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == openFileButton) {
                openFile();
            } else if (e.getSource() == analyzeButton) {
                analyzeText();
            } else if (e.getSource() == saveResultsButton) {
                saveResults();
            } else if (e.getSource() == clearButton) {
                clearAll();
            }
        } catch (Exception ex) {
            showErrorDialog("Error: " + ex.getMessage());
            statusLabel.setText("Error occurred");
        }
    }
    
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            
            @Override
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                String content = fileHandler.readFile(selectedFile);
                textArea.setText(content);
                statusLabel.setText("File loaded: " + selectedFile.getName());
            } catch (InvalidFileException | IOException ex) {
                showErrorDialog("Failed to open file: " + ex.getMessage());
            }
        }
    }
    
    private void analyzeText() {
        try {
            String text = textArea.getText();
            currentResult = analyzer.analyzeText(text);
            displayResults();
            statusLabel.setText("Analysis completed successfully");
        } catch (EmptyTextException ex) {
            showErrorDialog(ex.getMessage());
            statusLabel.setText("Analysis failed - empty text");
        }
    }
    
    private void displayResults() {
        if (currentResult == null) return;
        
        StringBuilder results = new StringBuilder();
        results.append("=== TEXT ANALYSIS RESULTS ===\n\n");
        results.append(String.format("Words: %d\n", currentResult.getWordCount()));
        results.append(String.format("Characters (with spaces): %d\n", currentResult.getCharacterCount()));
        results.append(String.format("Characters (without spaces): %d\n", currentResult.getCharacterCountNoSpaces()));
        results.append(String.format("Sentences: %d\n", currentResult.getSentenceCount()));
        results.append(String.format("Paragraphs: %d\n\n", currentResult.getParagraphCount()));
        
        // Word frequency analysis (showing top 10 most frequent words)
        results.append("=== TOP 10 MOST FREQUENT WORDS ===\n");
        
        // Convert HashMap to List and sort (Unit III - Collections, Comparable interface)
        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(currentResult.getWordFrequency().entrySet());
        sortedWords.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedWords) {
            if (count >= 10) break;
            results.append(String.format("%d. %s: %d times\n", 
                          ++count, entry.getKey(), entry.getValue()));
        }
        
        resultsArea.setText(results.toString());
    }
    
    private void saveResults() {
        if (currentResult == null) {
            showErrorDialog("No analysis results to save. Please analyze text first.");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("analysis_results.txt"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File saveFile = fileChooser.getSelectedFile();
                fileHandler.saveResults(saveFile, resultsArea.getText());
                statusLabel.setText("Results saved to: " + saveFile.getName());
                JOptionPane.showMessageDialog(this, "Results saved successfully!", 
                                            "Save Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showErrorDialog("Failed to save results: " + ex.getMessage());
            }
        }
    }
    
    private void clearAll() {
        textArea.setText("");
        resultsArea.setText("");
        currentResult = null;
        statusLabel.setText("All cleared - Ready for new input");
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Main method
public static void main(String[] args) {
    // Set Look and Feel
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        // Use default look and feel if system L&F is not available
        e.printStackTrace(); // Optional: helpful for debugging
    }

    // Create and show GUI on Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
        new WordCounterGUI().setVisible(true);
    });
}

}
