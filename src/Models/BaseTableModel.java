package Models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTableModel<T> extends AbstractTableModel {
    protected List<T> items = new ArrayList<>();
    protected final String[] columnNames;

    public BaseTableModel(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setItems(List<T> items) {
        this.items = items != null ? items : new ArrayList<>();
        onItemsChanged();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public T getItemAt(int rowIndex) {
        return (rowIndex >= 0 && rowIndex < items.size()) ? items.get(rowIndex) : null;
    }

    protected void onItemsChanged() {
    }

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);
}