package combineGUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.util.*;

public class combineGUI {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JFrame frame = new PanelFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setResizable(false);
		});
	}
}

class PanelFrame extends JFrame {
	private DefaultTreeModel model;

	private JTree tree;
	private JList<String> list = new JList<>();
	private JTable tabelData;
	private int index;

	private String[] columnNames = { "Dummy" };
	private Object[][] cells = { { null } };

	DefaultListModel<String> modelList = new DefaultListModel<>();

	private boolean removeNull = true;
	DefaultMutableTreeNode selectedElement;

	public PanelFrame() {
		setTitle("Combine Panel");

		TableModel modelTable = new DefaultTableModel(cells, columnNames);

		tabelData = new JTable(modelTable);
		JScrollPane scrollPaneTable = new JScrollPane(tabelData);

		DefaultMutableTreeNode dummy = new DefaultMutableTreeNode("Dummy");
		TreeNode root = dummy;
		model = new DefaultTreeModel(root);
		tree = new JTree(model);
		tree.setEditable(true);
		JScrollPane scrollPaneTree = new JScrollPane(tree);

		list.setModel(modelList);
		JScrollPane scrollPaneList = new JScrollPane(list);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		JTextField textField = new JTextField(20);
		inputPanel.add(textField);

		JButton button = new JButton("Add");
		inputPanel.add(button);
		button.addActionListener(event -> textField.setText(""));
		button.addActionListener(event -> {
			DefaultMutableTreeNode inputRoot = (DefaultMutableTreeNode) model.getRoot();

			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(textField.getText());
			model.insertNodeInto(newNode, inputRoot, inputRoot.getChildCount());

			TreeNode[] nodes = model.getPathToRoot(newNode);
			TreePath path = new TreePath(nodes);
			tree.scrollPathToVisible(path);
		});

		button.addActionListener(event -> {
			modelList.addElement(textField.getText());
		});

		button.addActionListener(event -> {
			DefaultTableModel dataModel = (DefaultTableModel) tabelData.getModel();
			ArrayList<String> inputList = new ArrayList<>();
			tabelData.setAutoCreateColumnsFromModel(true);
			inputList.add(textField.getText());
			dataModel.addRow(inputList.toArray());

			if (removeNull) {
				dataModel.removeRow(0);
				removeNull = false;
			}
		});

		JButton button2 = new JButton("Delete");
		inputPanel.add(button2);

		button2.addActionListener(event -> {
			try {
				DefaultTableModel dataModel = (DefaultTableModel) tabelData.getModel();
				index = list.getSelectedIndex();
				int row = tabelData.getSelectedRow();

				if (row != -1)
					index = row;

				try {
					DefaultMutableTreeNode selectedElement = (DefaultMutableTreeNode) tree.getSelectionPath()
							.getLastPathComponent();

					for (int i = 0; i < dataModel.getRowCount(); i++) {
						if (((String) dataModel.getValueAt(i, 0)).equals(selectedElement.getUserObject())) {
							index = i;
						}
					}
				}

				catch (Exception e) {

				}

				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				TreePath[] paths = tree.getSelectionPaths();
				if (paths != null) {
					for (TreePath path : paths) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (node.getParent() != null) {
							model.removeNodeFromParent(node);
						}
					}
				}

				modelList.remove(index);
				dataModel.removeRow(index);
			}

			catch (Exception e) {

			}
		});

		JPanel contentPanel = new JPanel();
		contentPanel.add(scrollPaneTree);
		contentPanel.add(scrollPaneTable);
		contentPanel.add(scrollPaneList);

		add(contentPanel, BorderLayout.NORTH);
		add(inputPanel, BorderLayout.SOUTH);
		pack();
	}
}