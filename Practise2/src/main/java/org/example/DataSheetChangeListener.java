package org.example;
import java.util.EventListener;
public interface DataSheetChangeListener extends EventListener {
    public void dataChanged(DataSheetChangeEvent e);
}
