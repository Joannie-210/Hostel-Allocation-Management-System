package org.example.utils;

import java.util.ArrayList;
import java.util.List;

public class DataChangeNotifier {

    private static final DataChangeNotifier instance = new DataChangeNotifier();
    private final List<Runnable> listeners = new ArrayList<>();

    private DataChangeNotifier() {}

    public static DataChangeNotifier getInstance() {
        return instance;
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    public void notifyDataChanged() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
}
