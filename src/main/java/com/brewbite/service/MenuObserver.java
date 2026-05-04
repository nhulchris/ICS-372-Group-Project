package com.brewbite.service;

import com.brewbite.model.MenuItem;
import java.util.List;

public interface MenuObserver {
    void updateMenu(List<MenuItem> menu);
}