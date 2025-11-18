package br.com.vinidiefen.pong.infrastructure.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import br.com.vinidiefen.pong.application.services.GameStateService;
import br.com.vinidiefen.pong.constants.UIConstants;
import br.com.vinidiefen.pong.infrastructure.persistence.models.MatchModel;
import br.com.vinidiefen.pong.infrastructure.ui.factories.ButtonFactory;
import br.com.vinidiefen.pong.infrastructure.ui.utils.FontUtils;

/**
 * Dialog for selecting a saved game to load
 */
public class SaveGameDialog {

    private SaveGameDialog() {
    }

    /**
     * Shows dialog and calls callback with selected match ID
     * 
     * @param parent           Parent component for positioning
     * @param matches          List of available matches
     * @param gameStateService Service to get score information
     * @param onLoadCallback   Callback to execute with selected match ID
     */
    public static void show(Component parent, List<MatchModel> matches,
            GameStateService gameStateService, Consumer<UUID> onLoadCallback) {
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "Nenhum jogo salvo encontrado!",
                    "Carregar Jogo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = createDialog(parent);
        JList<String> saveList = createSaveList(matches, gameStateService);
        setupDialog(dialog, saveList, matches, onLoadCallback);
        dialog.setVisible(true);
    }

    private static JDialog createDialog(Component parent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Selecione um Jogo Salvo");
        dialog.setModal(true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
        return dialog;
    }

    private static JList<String> createSaveList(List<MatchModel> matches, GameStateService gameStateService) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (int i = 0; i < matches.size(); i++) {
            MatchModel match = matches.get(i);
            String saveInfo = formatSaveInfo(i + 1, match, gameStateService);
            listModel.addElement(saveInfo);
        }

        JList<String> saveList = new JList<>(listModel);
        saveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        saveList.setBackground(UIConstants.LIST_BACKGROUND);
        saveList.setForeground(UIConstants.TEXT_COLOR);
        saveList.setFont(FontUtils.getDefaultFont());
        saveList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return saveList;
    }

    private static String formatSaveInfo(int index, MatchModel match, GameStateService gameStateService) {
        try {
            var scoreManager = gameStateService.getScoreForMatch(match.getScoreManagerId());
            if (scoreManager != null) {
                return String.format("Save #%d - Placar: %d x %d",
                        index, scoreManager.getLeftScore(), scoreManager.getRightScore());
            }
        } catch (Exception e) {
            // If fails, just show basic info
        }
        return String.format("Save #%d", index);
    }

    private static void setupDialog(JDialog dialog, JList<String> saveList,
            List<MatchModel> matches, Consumer<UUID> onLoadCallback) {
        // Title
        JLabel titleLabel = new JLabel("Jogos Salvos");
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(FontUtils.getDefaultFont(UIConstants.SUBTITLE_FONT_SIZE));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(saveList);
        scrollPane.setBorder(new LineBorder(Color.WHITE, 1));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton loadButton = ButtonFactory.createMenuButton("CARREGAR");
        JButton cancelButton = ButtonFactory.createMenuButton("CANCELAR");

        loadButton.addActionListener(e -> {
            int selectedIndex = saveList.getSelectedIndex();
            if (selectedIndex >= 0) {
                UUID selectedMatchId = matches.get(selectedIndex).getId();
                dialog.dispose();
                onLoadCallback.accept(selectedMatchId);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Selecione um jogo para carregar!",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);

        // Add components
        dialog.add(titleLabel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }
}
